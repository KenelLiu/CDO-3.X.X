package com.cdo.business.rpc.server.proto;

import java.io.File;
//import java.net.InetSocketAddress;
import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
//import java.util.concurrent.SynchronousQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cdo.business.BusinessService;
import com.cdo.business.rpc.RPCFile;
import com.cdo.google.GoogleCDO;
import com.cdo.google.Header;
import com.cdo.google.parse.ParseProtoCDO;
import com.cdo.protocol.google.fileHandle.CDOFileMessage;
import com.cdo.protocol.google.handle.ProtoProtocol;
import com.cdo.util.constants.Constants;
import com.cdoframework.cdolib.base.Return;
import com.cdoframework.cdolib.data.cdo.CDO;
import com.cdoframework.cdolib.servicebus.ITransService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.SystemPropertyUtil;
/**
 * 不考虑  处理CDO 文件流
 * @author KenelLiu
 *
 */
public class RPCFileServerHandler extends SimpleChannelInboundHandler<CDOFileMessage> {

	private static Logger logger=Logger.getLogger(RPCFileServerHandler.class);
	private int consumerCount=Math.max(1,SystemPropertyUtil.getInt(Constants.Consumer.COUNT,Runtime.getRuntime().availableProcessors()));
	private ExecutorService exService=null;
	
	private Channel channel;
	private LinkedTransferQueue<HandleFileData> lnkTransQueue;
	private  FileConsumer[] consumer;
	public RPCFileServerHandler(){

	}
    /**
     * 防止   客户机与服务器之间的长连接   发生阻塞,业务数据采用线程池处理,长连接channel仅用于数据读取io数据
     * 多个action通过长连接 发送请求到service端 
     */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CDOFileMessage msg)
			throws Exception {	
		  Header header=msg.getHeader();
		  switch(header.getType()){
		  	  case ProtoProtocol.TYPE_CDO:
					GoogleCDO.CDOProto proto=(GoogleCDO.CDOProto)(msg.getBody());
					List<File> listFile=msg.getFiles();
					HandleFileData handleData=new HandleFileData();
					handleData.setProto(proto);
					handleData.setListFile(listFile);
					try{
						lnkTransQueue.transfer(handleData);
					}catch(InterruptedException ex){
						logger.warn("add element to TransferQueue occured InterruptedException,retry add... ...");
						tryAddElement(handleData);
					}
		  		 break;
		  	 default:
		  		ctx.fireChannelRead(msg); 
		  }		  	
	}
	
	private void tryAddElement(HandleFileData handleData){
		int retry=1;
		while(retry<=3){
			try{
				lnkTransQueue.transfer(handleData);
				break;
			}catch(InterruptedException ex){
				retry++;
				try{Thread.sleep(500);}catch(Exception e){};
			}
		}
	}
	
	public void writeAndFlush(Object msg){
		channel.writeAndFlush(msg);	
	}
	
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();                  
		lnkTransQueue = new LinkedTransferQueue<HandleFileData>();
		exService=Executors.newCachedThreadPool();
		consumer=new FileConsumer[consumerCount]; 
		for(int i=0;i<consumerCount;i++){
			consumer[i]=new FileConsumer(channel+",Consumer"+i,lnkTransQueue,this);
			exService.submit(consumer[i]);
		}		
		if(logger.isInfoEnabled())
			logger.info("channel="+channel+" is channelRegistered, lnkTransQueue  create consumer count="+consumerCount);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();        
        logger.warn("channel="+channel+" is channelUnregistered,lnkTransQueue executor will be shutdown");
        lnkTransQueue.clear();
		for(int i=0;i<consumer.length;i++){
			consumer[i].setStop(true);	
		    consumer[i]=null;   
		}	               
        lnkTransQueue=null;            
        exService.shutdownNow();
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {        
        super.channelInactive(ctx);     
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	super.channelActive(ctx);	
    }

}
