package com.cdoframework.cdolib.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdo.field.Field;
import com.cdo.util.sql.SQLUtil;
import com.cdoframework.cdolib.base.Return;
import com.cdoframework.cdolib.data.cdo.CDO;
import com.cdoframework.cdolib.data.cdo.CDOArrayField;
import com.cdoframework.cdolib.database.xsd.BlockType;
import com.cdoframework.cdolib.database.xsd.BlockTypeItem;
import com.cdoframework.cdolib.database.xsd.Delete;
import com.cdoframework.cdolib.database.xsd.Else;
import com.cdoframework.cdolib.database.xsd.For;
import com.cdoframework.cdolib.database.xsd.If;
import com.cdoframework.cdolib.database.xsd.Insert;
import com.cdoframework.cdolib.database.xsd.OnError;
import com.cdoframework.cdolib.database.xsd.OnException;
import com.cdoframework.cdolib.database.xsd.SQLBlockType;
import com.cdoframework.cdolib.database.xsd.SQLBlockTypeItem;
import com.cdoframework.cdolib.database.xsd.SQLElse;
import com.cdoframework.cdolib.database.xsd.SQLFor;
import com.cdoframework.cdolib.database.xsd.SQLIf;
import com.cdoframework.cdolib.database.xsd.SQLThen;
import com.cdoframework.cdolib.database.xsd.SQLTrans;
import com.cdoframework.cdolib.database.xsd.SQLTransChoiceItem;
import com.cdoframework.cdolib.database.xsd.SelectField;
import com.cdoframework.cdolib.database.xsd.SelectRecord;
import com.cdoframework.cdolib.database.xsd.SelectRecordSet;
import com.cdoframework.cdolib.database.xsd.SelectTable;
import com.cdoframework.cdolib.database.xsd.SetVar;
import com.cdoframework.cdolib.database.xsd.Then;
import com.cdoframework.cdolib.database.xsd.Update;
import com.cdoframework.cdolib.database.xsd.types.IfTypeType;
import com.cdoframework.cdolib.database.xsd.types.SQLIfTypeType;

/**
* @author KenelLiu
*/
public class DataServiceParse
{

	//内部类,所有内部类在此声明----------------------------------------------------------------------------------

	//静态对象,所有static在此声明并初始化------------------------------------------------------------------------
	Logger logger  = Logger.getLogger(DataServiceParse.class);

	private void handleReturn(com.cdoframework.cdolib.database.xsd.Return returnObject,CDO cdoRequest,CDO cdoResponse,Return ret) throws SQLException{
		int nReturnItemCount=returnObject.getReturnItemCount();
		for(int j=0;j<nReturnItemCount;j++)
		{
			String strFieldId=returnObject.getReturnItem(j).getFieldId();
			String strValueId=returnObject.getReturnItem(j).getValueId();
			strFieldId=strFieldId.substring(1,strFieldId.length()-1);
			strValueId=strValueId.substring(1,strValueId.length()-1);
			Field object=null;
			try
			{
				if(cdoRequest.exists(strValueId))
					object=cdoRequest.getObject(strValueId);
			}
			catch(Exception e)
			{
				continue;
			}

			// 输出
			if(object==null)
			{
				continue;
			}
		
			Object objValue=object.getObjectValue();
			//=======设置返回数据=======//
			DataEngineHelp.setFieldValue(cdoResponse, object.getFieldType(), strFieldId, objValue);			
		}
		ret.setCode(returnObject.getCode());
		ret.setInfo(returnObject.getInfo());
		ret.setText(returnObject.getText());
	}
	/**
	 * 检查If的条件
	 * @param strValue1
	 * @param strOperator
	 * @param strValue2
	 * @param strType
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	protected boolean checkCondition(String strValue1,String strOperator,String strValue2,IfTypeType ifType,String strType,CDO cdoRequest)
	{
		return DataEngineHelp.checkCondition(strValue1, strOperator, strValue2, ifType, strType, cdoRequest);
	}

	/**
	 * 检查If的条件
	 * 
	 * @param strValue1
	 * @param strOperator
	 * @param strValue2
	 * @param strType
	 * @param cdoRequest
	 * @return
	 * @throws Exception
	 */
	protected boolean checkCondition(String strValue1,String strOperator,String strValue2,SQLIfTypeType sqlIfType,String strType,CDO cdoRequest)
	{
		return DataEngineHelp.checkCondition(strValue1, strOperator, strValue2, sqlIfType, strType, cdoRequest);
	}
	/**
	 * 处理SQL语句中的If语句
	 * 
	 * @param sqlIf
	 * @param cdoRequest
	 * @return 0-自然执行完毕，1-碰到Break退出，2-碰到Return退出
	 * @throws Exception
	 */
	private int handleSQLIf(SQLIf sqlIf,CDO cdoRequest,StringBuilder strbSQL,Map<String,String> selTblMap)
	{
		// 检查执行条件
		boolean bCondition=checkCondition(sqlIf.getValue1(),sqlIf.getOperator().toString(),sqlIf.getValue2(),sqlIf
						.getType(),sqlIf.getType().toString(),cdoRequest);
		if(bCondition==true)
		{// Handle Then
			SQLThen sqlThen=sqlIf.getSQLThen();
			return handleSQLBlock(sqlThen,cdoRequest,strbSQL,selTblMap);
		}
		else
		{// handle Else
			SQLElse sqlElse=sqlIf.getSQLElse();
			if(sqlElse==null)
			{// 自然完成
				return 0;
			}
			return handleSQLBlock(sqlElse,cdoRequest,strbSQL,selTblMap);
		}
	}
	/**
	 * 处理SQL语句中的For语句
	 * 
	 * @param sqlFor
	 * @param cdoRequest
	 * @param strbSQL
	 * @return 0-自然执行完毕，1-碰到Break退出，2-碰到Return退出
	 * @throws Exception
	 */
	protected int handleSQLFor(SQLFor sqlFor,CDO cdoRequest,StringBuilder strbSQL,Map<String,String> selTblMap)
	{
		// 获取循环数据
		int nFromIndex=0;
		int nStep=1;
		int nCount=DataEngineHelp.getArrayLength(sqlFor.getArrKey(), cdoRequest);
		if(sqlFor.getFromIndex()!=null)
			nFromIndex=DataEngineHelp.getIntegerValue(sqlFor.getFromIndex(),cdoRequest);
		if(sqlFor.getStep()!=null)
			nStep=DataEngineHelp.getIntegerValue(sqlFor.getStep(),cdoRequest);
		if(sqlFor.getCount()!=null)
			nCount=DataEngineHelp.getIntegerValue(sqlFor.getCount(),cdoRequest);	
					
		String strIndexId=sqlFor.getIndexId();
		strIndexId=strIndexId.substring(1,strIndexId.length()-1);

		// 执行循环
		for(int i=nFromIndex;i<nFromIndex+nCount;i=nStep+i)
		{
			// 设置IndexId
			cdoRequest.setIntegerValue(strIndexId,i);

			// 执行Block
			int nResult=handleSQLBlock(sqlFor,cdoRequest,strbSQL,selTblMap);
			if(nResult==0)
			{// 自然执行完毕
				continue;
			}
			else if(nResult==1)
			{// 碰到Break
				break;
			}
			else
			{// 碰到Return
				return nResult;
			}
		}

		return 0;
	}

	/**
	 * 处理SQLBlock对象，得到输出的SQL语句
	 * 
	 * @param sqlBlock
	 * @return 0-自然执行完毕，1-碰到Break退出，2-碰到Return退出
	 */
	protected int handleSQLBlock(SQLBlockType sqlBlock,CDO cdoRequest,StringBuilder strbSQL,Map<String,String> selTblMap)
	{
		// 依次处理各个Item
		int nItemCount=sqlBlock.getSQLBlockTypeItemCount();
		for(int i=0;i<nItemCount;i++)
		{
			// 处理当前的Item
			SQLBlockTypeItem item=sqlBlock.getSQLBlockTypeItem(i);
			if(item.getOutputSQL()!=null)
			{// OutputSQL,直接输出源文本
				strbSQL.append(item.getOutputSQL());
			}
			else if(item.getOutputField()!=null)
			{// OutputField，输出文本代表的字段值
				String strOutputFieldId=item.getOutputField();
				strOutputFieldId=strOutputFieldId.substring(1,strOutputFieldId.length()-1);
				strbSQL.append(cdoRequest.getStringValue(strOutputFieldId));
			}
			else if(item.getOutputTable()!=null){
				// OutputTable，输出文本代表的字段值
				String outTblId=item.getOutputTable();
				outTblId=outTblId.substring(1,outTblId.length()-1);
				if(selTblMap==null || selTblMap.get(outTblId)==null){					
					throw new RuntimeException("在使用OutputTable前,未定义SelectTable.当前未找到["+outTblId+"]对应的SelectTable");
				}else{
					strbSQL.append(selTblMap.get(outTblId));
				}
			}
			else if(item.getSQLIf()!=null)
			{// SQLIf
				int nResult=handleSQLIf(item.getSQLIf(),cdoRequest,strbSQL,selTblMap);
				if(nResult==0)
				{// 自然执行完毕
					continue;
				}
				else
				{// 碰到Break或Return
					return nResult;
				}
			}
			else if(item.getSQLFor()!=null)
			{// SQLFor
				int nResult=handleSQLFor(item.getSQLFor(),cdoRequest,strbSQL,selTblMap);
				if(nResult==0)
				{// 自然执行完毕
					continue;
				}
				else
				{// 碰到Break或Return
					return nResult;
				}
			}
			else
			{
				continue;
			}
		}

		// 自然执行完毕
		return 0;
	}

	/**
	 * 处理If语句
	 * 
	 * @param ifItem
	 * @param cdoRequest
	 * @return 0-自然执行完毕，1-碰到Break退出，2-碰到Return退出
	 * @throws Exception
	 */
	private int handleIf(IDataEngine dataEngine ,Connection connection,If ifItem,CDO cdoRequest,CDO cdoResponse,Return ret,Map<String,String> selTblMap) throws SQLException,IOException
	{
		// 检查执行条件
		boolean bCondition=checkCondition(ifItem.getValue1(),ifItem.getOperator().toString(),ifItem.getValue2(),ifItem.getType(),ifItem.getType().toString(),cdoRequest);
		if(bCondition==true)
		{// Handle Then
			Then thenItem=ifItem.getThen();
			return handleBlock(dataEngine,connection,thenItem,cdoRequest,cdoResponse,ret,selTblMap);
		}
		else
		{// handle Else
			Else elseItem=ifItem.getElse();
			if(elseItem==null)
			{// 没有else模块，当作自然执行完毕处理???
				return 0;
			}
			return handleBlock(dataEngine,connection,elseItem,cdoRequest,cdoResponse,ret,selTblMap);
		}
	}
	/**
	 * 处理For语句
	 * 
	 * @param sqlFor
	 * @param cdoRequest
	 * @param strbSQL
	 * @return 0-自然执行完毕，1-碰到Break退出，2-碰到Return退出
	 * @throws Exception
	 */
	private int handleFor(IDataEngine dataEngine ,Connection connection,For forItem,CDO cdoRequest,CDO cdoResponse,Return ret,Map<String,String> selTblMap) throws SQLException,IOException
	{
		// 获取循环数据
		int nFromIndex=0;
		int nStep=1;
		int nCount=DataEngineHelp.getArrayLength(forItem.getArrKey(), cdoRequest);
		if(forItem.getFromIndex()!=null)
			nFromIndex=DataEngineHelp.getIntegerValue(forItem.getFromIndex(),cdoRequest);
		if(forItem.getStep()!=null)
			nStep=DataEngineHelp.getIntegerValue(forItem.getStep(),cdoRequest);
		if(forItem.getCount()!=null)
			nCount=DataEngineHelp.getIntegerValue(forItem.getCount(),cdoRequest);	
		
		String strIndexId=forItem.getIndexId();
		strIndexId=strIndexId.substring(1,strIndexId.length()-1);
		
		// 执行循环
		for(int i=nFromIndex;i<nFromIndex+nCount;i=i+nStep)
		{
			// 设置IndexId
			cdoRequest.setIntegerValue(strIndexId,i);

			// 执行Block
			int nResult=handleBlock(dataEngine,connection,forItem,cdoRequest,cdoResponse,ret,selTblMap);
			if(nResult==0)
			{// 自然执行完毕
				continue;
			}
			else if(nResult==1)
			{// 碰到Break
				break;
			}
			else
			{// 碰到Return
				return nResult;
			}
		}

		return 0;
	}
	/**
	 * 执行commit语句
	 * 
	 * @throws Exception
	 */
	protected void executeCommit(Connection conn) throws SQLException,IOException
	{
		//提交事务
		if(conn.getAutoCommit()==false){//尚未提交
			conn.commit();
		}
	}
	
	/**
	 * 执行rollback语句
	 * 
	 * @throws Exception
	 */
	protected void executeRollback(Connection conn) throws SQLException,IOException
	{
		//提交事务
		if(conn!=null && conn.getAutoCommit()==false){//尚未提交
			conn.rollback();
		}
	}

	/*
	 * 处理每个block内容
	 * 
	 * @return 0-自然执行完毕，1-碰到Break退出，2-碰到Return退出
	 */
	private int handleBlock(IDataEngine dataEngine,Connection connection,BlockType block,
							CDO cdoRequest,CDO cdoResponse,Return ret,Map<String,String> selTblMap) throws SQLException,IOException
	{
		int nItemCount=block.getBlockTypeItemCount();
		for(int i=0;i<nItemCount;i++)
		{
			BlockTypeItem blockItem=block.getBlockTypeItem(i);
			if(blockItem.getSelectTable()!=null){
				// 获得将要执行的SQL
				SelectTable selectTable=(SelectTable)blockItem.getSelectTable();
				StringBuilder strbSQL=new StringBuilder();
				handleSQLBlock(selectTable,cdoRequest,strbSQL,selTblMap);
				String strSQL=strbSQL.toString();
				if(selTblMap==null)
					selTblMap=new HashMap<String,String>(5);
				
				String strOutputId=selectTable.getOutputId();
				strOutputId=strOutputId.substring(1,strOutputId.length()-1);
				selTblMap.put(strOutputId, strSQL);
			}else if(blockItem.getInsert()!=null)
			{ 
				//Insert  获得将要执行的SQL
				Insert insert=(Insert)blockItem.getInsert();
				StringBuilder strbSQL=new StringBuilder();
				handleSQLBlock(insert,cdoRequest,strbSQL,selTblMap);
				String strSQL=strbSQL.toString();
				// 执行SQL				
				dataEngine.executeUpdate(connection,strSQL,cdoRequest);	
			}
			else if(blockItem.getSelectRecord()!=null)
			{
				// 获得将要执行的SQL
				SelectRecord selectRecord=(SelectRecord)blockItem.getSelectRecord();
				StringBuilder strbSQL=new StringBuilder();
				handleSQLBlock(selectRecord,cdoRequest,strbSQL,selTblMap);
				String strSQL=strbSQL.toString();

				// 执行SQL
				CDO cdoRecord=new CDO();
				String strRecordCountId=selectRecord.getRecordCountId();				
				int nRecordCount=dataEngine.executeQueryRecord(connection,strSQL,cdoRequest,cdoRecord);
				if(strRecordCountId.length()>0)
				{// 输出受影响的记录数
					strRecordCountId=strRecordCountId.substring(1,strRecordCountId.length()-1);
					cdoRequest.setIntegerValue(strRecordCountId,nRecordCount);
				}

				String strOutputId=selectRecord.getOutputId();
				strOutputId=strOutputId.substring(1,strOutputId.length()-1);
				if(nRecordCount>0)
				{
					cdoRequest.setCDOValue(strOutputId,cdoRecord);
				}
			}
			else if(blockItem.getUpdate()!=null)
			{// Update
				// 获得将要执行的SQL
				Update update=(Update)blockItem.getUpdate();
				StringBuilder strbSQL=new StringBuilder();
				handleSQLBlock(update,cdoRequest,strbSQL,selTblMap);
				String strSQL=strbSQL.toString();

				// 执行SQL				
				int nRecordCount=dataEngine.executeUpdate(connection,strSQL,cdoRequest);	
				String strRecordCountId=update.getRecordCountId();
				if(strRecordCountId.length()>0)
				{// 输出受影响的记录数
					strRecordCountId=strRecordCountId.substring(1,strRecordCountId.length()-1);
					cdoRequest.setIntegerValue(strRecordCountId,nRecordCount);
				}
			}
			else if(blockItem.getDelete()!=null)
			{// Delete
				// 获得将要执行的SQL
				Delete delete=(Delete)blockItem.getDelete();
				StringBuilder strbSQL=new StringBuilder();
				handleSQLBlock(delete,cdoRequest,strbSQL,selTblMap);
				String strSQL=strbSQL.toString();

				// 执行SQL				
				int nRecordCount=dataEngine.executeUpdate(connection,strSQL,cdoRequest);
				String strRecordCountId=delete.getRecordCountId();
				if(strRecordCountId.length()>0)
				{// 输出受影响的记录数
					strRecordCountId=strRecordCountId.substring(1,strRecordCountId.length()-1);
					cdoRequest.setIntegerValue(strRecordCountId,nRecordCount);
				}
			}
			else if(blockItem.getSelectField()!=null)
			{
				// 获得将要执行的SQL
				SelectField selectField=(SelectField)blockItem.getSelectField();
				StringBuilder strbSQL=new StringBuilder();
				handleSQLBlock(selectField,cdoRequest,strbSQL,selTblMap);
				String strSQL=strbSQL.toString();

				// 执行SQL				
				Field objFieldValue=dataEngine.executeQueryFieldExt(connection,strSQL,cdoRequest);
				if(objFieldValue==null)
				{
					continue;
				}
				Object objValue=objFieldValue.getObjectValue();

				String strOutputId=selectField.getOutputId();
				strOutputId=strOutputId.substring(1,strOutputId.length()-1);
				//=====设置Field数据=====//
				DataEngineHelp.setFieldValue(cdoRequest, objFieldValue.getFieldType(), strOutputId, objValue);
				
			}
			else if(blockItem.getSelectRecordSet()!=null)
			{
				// SelectRecordSet 获得将要执行的SQL
				SelectRecordSet selectRecordSet=(SelectRecordSet)blockItem.getSelectRecordSet();
				StringBuilder strbSQL=new StringBuilder();
				handleSQLBlock(selectRecordSet,cdoRequest,strbSQL,selTblMap);
				String strSQL=strbSQL.toString();

				// 执行SQL
				CDOArrayField cdoArrayField=new CDOArrayField("");
				String strRecordCountId=selectRecordSet.getRecordCountId();
				int nRecordCount=dataEngine.executeQueryRecordSet(connection,strSQL,cdoRequest,cdoArrayField);
				if(strRecordCountId.length()>0)
				{// 输出受影响的记录数
					strRecordCountId=strRecordCountId.substring(1,strRecordCountId.length()-1);
					cdoRequest.setIntegerValue(strRecordCountId,nRecordCount);
				}
				
				String strOutputId=selectRecordSet.getOutputId();
				strOutputId=strOutputId.substring(1,strOutputId.length()-1);
				// RecordSet输出到数组
				cdoRequest.setCDOListValue(strOutputId, cdoArrayField.getValue());
			}
			else if(blockItem.getSetVar()!=null)
			{
				SetVar sv=blockItem.getSetVar();				
				DataEngineHelp.setVar(sv, cdoRequest);
			}
			else if(blockItem.getIf()!=null)
			{
				int nResult=this.handleIf(dataEngine,connection,(If)blockItem.getIf(),cdoRequest,cdoResponse,ret,selTblMap);
				if(nResult==0)
				{// 自然执行完毕
					continue;
				}
				else
				{// 碰到Break或Return
					return nResult;
				}
			}
			else if(blockItem.getFor()!=null)
			{
				int nResult=this.handleFor(dataEngine,connection,(For)blockItem.getFor(),cdoRequest,cdoResponse,ret,selTblMap);
				if(nResult==0)
				{// 自然执行完毕
					continue;
				}
				else
				{// 碰到Break或Return
					return nResult;
				}
			}
			else if(blockItem.getReturn()!=null)
			{
				com.cdoframework.cdolib.database.xsd.Return returnObject=(com.cdoframework.cdolib.database.xsd.Return)blockItem.getReturn();
				this.handleReturn(returnObject,cdoRequest,cdoResponse,ret);

				return 2;
			}
			else if(blockItem.getBreak()!=null)
			{// Break退出
				return 1;
			}
			else if(blockItem.getCommit()!=null)
			{
				this.executeCommit(connection);
			}
			else if(blockItem.getRollback()!=null)
			{
				this.executeRollback(connection);
			}
		}

		return 0;
	}
	
	private void callOnException(String strText,Exception e)
	{
		try
		{
			onException(strText,e);
		}
		catch(Exception ex)
		{
		}
	}

	protected Return executeTrans(HashMap<String,IDataEngine> hmDataGroup,SQLTrans trans,CDO cdoRequest,CDO cdoResponse){

	//==========TODO 加入事务传播========//
		
   	//处理事务   	
	Return ret=new Return();
   	String strDataGroupId=trans.getDataGroupId();
   	IDataEngine dataEngine=hmDataGroup.get(strDataGroupId);
	Connection connection=null;
	//=========增加SelectTable处理,方便复用SQL,创建唯一个======//
	Map<String,String> selTblMap=null;
	try{
			if(dataEngine==null){//DataGroupId错误
				throw new SQLException("Invalid datagroup id: "+strDataGroupId);
			}
			//创建Connection	
			connection=dataEngine.getConnection();
			if(connection==null){
				throw new SQLException("datagroup id: "+strDataGroupId+",Invalid Connection,Connection is null");
			}
			if(!trans.getTransFlag().value().equals(0)){
				connection.setAutoCommit(false);
			}

			//=========生成Block对象==========//
			BlockType block=new BlockType();
			int nTransItemCount=trans.getSQLTransChoice().getSQLTransChoiceItemCount();
			for(int i=0;i<nTransItemCount;i++)
			{
				SQLTransChoiceItem transItem=trans.getSQLTransChoice().getSQLTransChoiceItem(i);
				BlockTypeItem blockItem=null;
				if(transItem.getSelectTable()!=null){
					
					blockItem=new BlockTypeItem();
					blockItem.setSelectTable(transItem.getSelectTable());
					if(selTblMap==null)
						selTblMap=new HashMap<String,String>(5);
				}
				else if(transItem.getInsert()!=null)
				{
					blockItem=new BlockTypeItem();
					blockItem.setInsert(transItem.getInsert());
				}
				else if(transItem.getUpdate()!=null)
				{
					blockItem=new BlockTypeItem();
					blockItem.setUpdate(transItem.getUpdate());
				}
				else if(transItem.getDelete()!=null)
				{
					blockItem=new BlockTypeItem();
					blockItem.setDelete(transItem.getDelete());
				}
				else if(transItem.getSelectRecordSet()!=null)
				{
					blockItem=new BlockTypeItem();
					blockItem.setSelectRecordSet(transItem.getSelectRecordSet());
				}
				else if(transItem.getSelectRecord()!=null)
				{
					blockItem=new BlockTypeItem();
					blockItem.setSelectRecord(transItem.getSelectRecord());
				}
				else if(transItem.getSelectField()!=null)
				{
					blockItem=new BlockTypeItem();
					blockItem.setSelectField(transItem.getSelectField());
				}
				else if(transItem.getIf()!=null)
				{
					blockItem=new BlockTypeItem();
					blockItem.setIf(transItem.getIf());
				}
				else if(transItem.getFor()!=null)
				{
					blockItem=new BlockTypeItem();
					blockItem.setFor(transItem.getFor());
				}
				else if(transItem.getSetVar()!=null){
					blockItem=new BlockTypeItem();
					blockItem.setSetVar(transItem.getSetVar());
				}
				if(blockItem!=null)
				{
					block.addBlockTypeItem(blockItem);
				}
			}

			// 处理事务
			int nResult=handleBlock(dataEngine,connection,block,cdoRequest,cdoResponse,ret,selTblMap);
			if(nResult!=2){
				// Break或自然执行完毕退出
				com.cdoframework.cdolib.database.xsd.Return returnObject=trans.getReturn();
				this.handleReturn(returnObject,cdoRequest,cdoResponse,ret);
			}
			
			if(!trans.getTransFlag().value().equals(0)){
				connection.commit();
			}
		}catch(SQLException e){
		   	String strTransName=cdoRequest.getStringValue("strTransName");
			try{this.executeRollback(connection);}catch(Exception ex){};
			callOnException("executeTrans Exception: "+strTransName,e);
			ret=null;
			OnException onException=trans.getOnException();
			int nErrorCount=onException.getOnErrorCount();
			for(int i=0;i<nErrorCount;i++){
				OnError onError=onException.getOnError(i);
				if(onError.getCode()==e.getErrorCode()){
					ret=Return.valueOf(onError.getReturn().getCode(),onError.getReturn().getText(),onError.getReturn().getInfo(),e);
					break;
				}
			}
			if(ret==null){
				// 没有定义OnError
				String strText = onException.getReturn().getText();
				if("OK".equalsIgnoreCase(strText)){
					strText="系统服务器故障";
				}
				ret=Return.valueOf(onException.getReturn().getCode(),strText,onException.getReturn().getInfo(),e);
			}
			return ret;
		}catch(Exception e){
		   	String strTransName=cdoRequest.getStringValue("strTransName");
			try{this.executeRollback(connection);}catch(Exception ex){};
			callOnException("executeTrans Exception: "+strTransName,e);
			OnException onException=trans.getOnException();
			ret=Return.valueOf(onException.getReturn().getCode(),onException.getReturn().getText(),onException.getReturn().getInfo());
			
			return ret;
		}finally{
			//关闭连接
			SQLUtil.closeConnection(connection);
			if(selTblMap!=null){
				selTblMap.clear();
				selTblMap=null;
			}
		}		
		return ret;
	}


	public void onException(String strText,Exception e)
	{
		logger.error(strText, e); 
	}

	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	
	public Return handleTrans(HashMap<String,IDataEngine> hmDataGroup,SQLTrans sqlTrans, CDO cdoRequest,CDO cdoResponse)
	{
		return this.executeTrans(hmDataGroup,sqlTrans,cdoRequest,cdoResponse);
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------

	public DataServiceParse(){
		
	}
	
}