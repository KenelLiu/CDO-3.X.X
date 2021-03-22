package com.cdo.business.rpc.client.proto;

import java.io.File;
import java.util.List;

import com.cdo.business.rpc.client.IRPCResponse;
import com.cdo.google.GoogleCDO;

public class RPCResponse implements IRPCResponse{

	private GoogleCDO.CDOProto cdoProto;
	private List<File> listFile;
	
	public RPCResponse(){}
	public RPCResponse(GoogleCDO.CDOProto cdoProto){
		this.cdoProto=cdoProto;
	}
	
	public RPCResponse(GoogleCDO.CDOProto cdoProto,List<File> listFile){
		this.cdoProto=cdoProto;
		this.listFile=listFile;
	}
	
	public GoogleCDO.CDOProto getCdoProto() {
		return cdoProto;
	}
	public void setCdoProto(GoogleCDO.CDOProto cdoProto) {
		this.cdoProto = cdoProto;
	}
	public List<File> getListFile() {
		return listFile;
	}
	public void setListFile(List<File> listFile) {
		this.listFile = listFile;
	}

}
