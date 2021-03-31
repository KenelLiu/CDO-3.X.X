package com.cdo.example;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cdoframework.cdolib.data.cdo.CDO;

public class CDOFieldTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		CDO cdo=ExampleCDO.getCDO();
		cdo.setNullValue("NullKey");
		cdo.setStringArrayValue("NullKeyArr", new String[]{ "数组元素1", null, "数组元素3",null});
		CDO cdoOut=new CDO();
		cdoOut.copyFrom(cdo);
		cdoOut.setCDOValue("cdo", cdo.clone());
		cdoOut.setCDOArrayValue("cdoArr", new CDO[]{cdo.clone(),cdo.clone()});
		CDO x=new CDO();
		x.copyFrom(cdoOut.toXML());
		System.out.println(x.toXMLWithIndent());
	}

}
