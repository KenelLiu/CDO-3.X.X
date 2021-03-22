package com.cdo.google;

import com.cdo.protocol.google.handle.ProtoProtocol;

public final class Header {
	private final short crcCode=ProtoProtocol.MAGIC_NUMBER;//cdo协议魔数
	private byte type; //消息类型
	
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public short getCrcCode() {
		return crcCode;
	}
	
	public String toString(){
		
		return "Header [ crcCode="+crcCode+",type="+type+"]";
	}
}
