package com.maha.apitest.validator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonElement;
import com.jayway.jsonpath.JsonPath;
import com.maha.apitest.model.RestAPIResponse;
import com.maha.apitest.util.ConfigUtil;
import com.maha.apitest.util.ResponseTrackerUtil;

public class Main {
	

	static String  JSON = "{\"status\":404,\"isOK\":true, \"flt\":12.501,  \"a\": { \"name\" : \"Jill\", \"b\":[1,2,3], \"c\":{ \"name\" : \"Jack\", \"intv\":12}}, \"arr\":[{\"c\":1}, \"text\", [1]]}";
	static String str = ConfigUtil.getProperty("server.url");
	
	public static void validate() {
		JsonElement obj1 = JsonPath.read(JSON, "$");
		JsonElement status = JsonPath.read(JSON, "$.status");
		JsonElement isOk = JsonPath.read(JSON, "$.isOK");
		JsonElement name = JsonPath.read(JSON, "$.a.name");
		JsonElement b = JsonPath.read(JSON, "$.arr");
		
		JSONObject actualObj = new JSONObject(obj1.toString());
		JSONObject expectedObj = new JSONObject(JSON);
		JSONAssert.assertEquals(actualObj, expectedObj, JSONCompareMode.NON_EXTENSIBLE);
		
		JSONArray actArr =  new JSONArray(b.getAsJsonArray().toString());
		JSONAssert.assertEquals(actArr, expectedObj.getJSONArray("b"), JSONCompareMode.NON_EXTENSIBLE);
		
		Assert.assertEquals(status.getAsString(), expectedObj.get("status").toString());
		Assert.assertEquals(isOk.getAsString(), expectedObj.get("isOK").toString());
		
	}
	
	public static void replaceTest(){
		RestAPIResponse response = new RestAPIResponse();
		response.setResponse(JSON);
		ResponseTrackerUtil.trackedResponse = response;
		
		String text = "{\"name\":\"$[trackedResp.a]\", \"myArr\":$[invalidName.dd], \"unknown\":$[trackedResp.xx],   \"myArr\":['a', '1'],  \"bool\":$[trackedResp.isOK], \"f\":$[trackedResp.flt], \"arr\":$[trackedResp.arr], \"b\":{ \"anothername\":\"$[trackedResp.a.c.name]\", \"intv\":\"$[trackedResp.a.c.intv]\"}}";
		System.out.println(ResponseTrackerUtil.replacePlaceHolders(text));
		
	}
	
	public static void main(String[] args) {
		replaceTest();
	}
	
}
