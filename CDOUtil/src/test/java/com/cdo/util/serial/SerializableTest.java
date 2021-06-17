package com.cdo.util.serial;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
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
		cdoOut.setStringValue("abc", "2020-10-01 00:00:00");
		byte[] array=Serializable.protoCDO2Byte(cdoOut);
		CDO cdo2=Serializable.byte2ProtoCDO(array);
		
		System.out.println(cdo2.toXMLWithIndent());
		//cdo2.getDateTimeValue("abc").toString();
		//BinaryOperator<String> conn=(c,d)->c+","+d;
		//BinaryOperator<Long> add=(a,b)->a+b;
		String t1=Stream.of("a","b","c").reduce((a,b)->a+","+b).get();
		System.out.println(t1);
		//Stream.of(1).collect(Collectors.groupingBy(classifier, downstream));
		List<CDO> cdoList=new ArrayList<CDO>();
//		Map<> map=cdoList.stream().filter(x->x.exists("field"))
//				.collect(Collectors.toMap(param->param.));
	} 	 

}
