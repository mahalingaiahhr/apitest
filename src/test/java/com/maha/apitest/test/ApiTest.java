package com.maha.apitest.test;

import org.databene.benerator.anno.Source;
import org.databene.feed4junit.Feeder;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.maha.apitest.model.Input;

@RunWith(Feeder.class)
public class ApiTest extends BaseTest{

	@Test
	public void dotest(@Source("testdata.xlsx") Input input){
		execute(input);
	}
	
}
