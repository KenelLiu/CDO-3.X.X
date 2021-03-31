package com.cdo.util.serial;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONException;
import org.junit.Test;

import com.cdo.example.ExampleCDO;
import com.cdoframework.cdolib.data.cdo.CDO;
import com.cdoframework.cdolib.util.JsonUtil;
import com.google.protobuf.InvalidProtocolBufferException;

public class SerializableTest {
	
	@Test
	public void testProtoCDO2Byte() throws IOException {
		CDO cdo=ExampleCDO.getCDO();
		cdo.setNullValue("NullKey");
		cdo.setStringArrayValue("NullKeyArr", new String[]{ "数组元素1", null, "数组元素3",null});
		CDO cdoOut=new CDO();
		cdoOut.copyFrom(cdo);
		cdoOut.setCDOValue("cdo", cdo.clone());
		cdoOut.setCDOArrayValue("cdoArr", new CDO[]{cdo.clone(),cdo.clone()});
		cdoOut.setStringValue("abc", null);
		byte[] array=Serializable.protoCDO2Byte(cdoOut);
		CDO x=Serializable.byte2ProtoCDO(array);
		
		System.out.println(x.toXMLWithIndent());
		x.getDateTimeValue("abc").toString();

	} 	 

}
