package com.cdoframework.cdolib.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
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
import com.cdoframework.transaction.Propagation;
import com.cdoframework.transaction.TransactionImpl;
import com.cdoframework.transaction.TransactionThreadLocal;
import com.cdoframework.transaction.exception.TransactionException;

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
		}
		return 0;
	}
	
	private class TransactionStatus{
		Connection connection;
		boolean isNewConn;
		boolean isTransaction;
		boolean isSavePoint;
		public TransactionStatus(Connection connection,boolean isNewConn,boolean isSavePoint) throws SQLException{
			this.connection=connection;
			this.isNewConn=isNewConn;
			this.isTransaction=!connection.getAutoCommit();
			this.isSavePoint=isSavePoint;
		}
		public Connection getConnection() {
			return this.connection;
		}
	};
	
	private TransactionStatus newTransactionStatus(Propagation propagation,String strDataGroupId,IDataEngine dataEngine) throws SQLException{
		Connection conn=null;
		if(propagation==Propagation.MANDATORY){
			TransactionThreadLocal transaction=new TransactionThreadLocal();
			conn=transaction.getConnection(strDataGroupId);
			if(conn==null){
				//========抛出异常,由TransService.processTrans处理后续操作=======//
				throw new SQLException("propagation MANDATORY can't get connnection");
			}
			return new TransactionStatus(conn,false,false);
		}else if(propagation==Propagation.REQUIRES_NEW){
			conn=dataEngine.getConnection();
			conn.setAutoCommit(false);
			return new TransactionStatus(conn,true,false);
		}else if(propagation==Propagation.NOT_SUPPORTED){
			conn=dataEngine.getConnection();
			conn.setAutoCommit(true);
			return new TransactionStatus(conn,true,false);
		}else if(propagation==Propagation.NESTED){
			TransactionThreadLocal transaction=new TransactionThreadLocal();
			conn=transaction.getConnection(strDataGroupId);
			if(conn==null){
				//=====conn is null,表示在Transname方法上设置了denyAutoStartTransaction=true//
				//=====因此需在此处理ThreadLocal的value,防止内存泄露==========================//
				ThreadLocal<TransactionImpl> tranManager=transaction.getThreadLocal();
				if(tranManager.get().isEmpty()){
					tranManager.remove();
				}
				conn=dataEngine.getConnection();
				conn.setAutoCommit(false);
				return new TransactionStatus(conn,true,false);
			}				
			return new TransactionStatus(conn,false,true);
		}else{
			throw new SQLException("unsupport propagation:"+propagation);
		}
	}
	
	protected Return executeTrans(HashMap<String,IDataEngine> hmDataEngine,SQLTrans trans,CDO cdoRequest,CDO cdoResponse){
   	//==========处理事务,加入事务传播==========//   	
	Return ret=new Return();
   	String strDataGroupId=trans.getDataGroupId();
   	IDataEngine dataEngine=hmDataEngine.get(strDataGroupId);
   	Propagation propagation=Propagation.getPropagation(trans.getPropagation().value());
   	//========根据传播属性,获取对应的connection======//
	Connection connection=null;
	//=========增加SelectTable处理,方便复用SQL条件过滤======//
	Map<String,String> selTblMap=null;
	TransactionStatus transactionStatus=null;
	Savepoint savepoint=null;
	try{
			if(dataEngine==null){//DataGroupId错误
				throw new SQLException("Invalid datagroup id: "+strDataGroupId);
			}			
			transactionStatus=newTransactionStatus(propagation, strDataGroupId, dataEngine);
			//获取Connection	
			connection=transactionStatus.getConnection();
			if(connection==null){
				throw new SQLException("datagroup id: "+strDataGroupId+",Invalid Connection,Connection is null");
			}
			if(transactionStatus.isTransaction && transactionStatus.isSavePoint){
				savepoint = connection.setSavepoint("savepoint"); 
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
			
			if(transactionStatus.isNewConn && transactionStatus.isTransaction){
				connection.commit();
			}			
		}catch(Throwable e){
			String strTransName=cdoRequest.getStringValue("strTransName");
			logger.error("executeTrans Exception: "+strTransName,e);
		   	if(transactionStatus!=null && transactionStatus.isTransaction){
		   		//==========Propagation.REQUIRES_NEW || Propagation.NESTED====// 
		   		if(transactionStatus.isNewConn){
		   			try{connection.rollback();}catch(Exception ex){};
		   			OnException onException=trans.getOnException();
	   				return Return.valueOf(onException.getReturn().getCode(),onException.getReturn().getText(),onException.getReturn().getInfo());		   			
		   		}
		   		//==========Propagation.NESTED=====//
		   		if(transactionStatus.isSavePoint){
		   			if(savepoint!=null){
		   				try{connection.rollback(savepoint);}catch(Exception ex){};
		   				OnException onException=trans.getOnException();
		   				return Return.valueOf(onException.getReturn().getCode(),onException.getReturn().getText(),onException.getReturn().getInfo());
		   			}		   				
		   		}
		   		//=========Propagation.MANDATORY===========//
		   		TransactionThreadLocal transaction=new TransactionThreadLocal();
		   		try{transaction.rollback(strDataGroupId);}catch(SQLException ex){};
		   	}
		   	//========其它情况处理========//
			OnException onException=trans.getOnException();
			ret=Return.valueOf(onException.getReturn().getCode(),onException.getReturn().getText(),onException.getReturn().getInfo());	
			throw new TransactionException(ret,e.getMessage(),e);
		}finally{
			//关闭连接
			if(transactionStatus!=null && transactionStatus.isNewConn){
				SQLUtil.closeConnection(connection);
			}
			if(selTblMap!=null){
				selTblMap.clear();
				selTblMap=null;
			}
		}		
		return ret;
	}

	//公共方法,所有可提供外部使用的函数在此定义为public方法------------------------------------------------------
	
	public Return handleTrans(HashMap<String,IDataEngine> hmDataEngine,SQLTrans sqlTrans, CDO cdoRequest,CDO cdoResponse)
	{
		return this.executeTrans(hmDataEngine,sqlTrans,cdoRequest,cdoResponse);
	}

	//构造函数,所有构造函数在此定义------------------------------------------------------------------------------

	public DataServiceParse(){
		
	}
	
}