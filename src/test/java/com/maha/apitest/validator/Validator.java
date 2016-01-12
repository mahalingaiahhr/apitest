package com.maha.apitest.validator;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.maha.apitest.model.Expression;
import com.maha.apitest.model.Input;
import com.maha.apitest.model.RestAPIResponse;
import com.maha.apitest.util.ResponseTrackerUtil;


public class Validator {
	
	public static void validate(Input input, RestAPIResponse response) {
		Assert.assertEquals("Status code do not match", input.getStatusCode(), response.getStatusCode());
		if (input.getResponseMatchers() != null && !input.getResponseMatchers().isEmpty()) {
			for (Expression exp : input.getResponseMatchers()) {
				validateExpression(exp, response);
			}
		}
	}
	
	private static void validateExpression(Expression exp, RestAPIResponse response) {
		String operator = exp.getOperator();
		String message = String.format("For %s|%s|%.20s -", exp.getKey(), exp.getOperator(), exp.getExpexted());
		JsonElement actual = ResponseTrackerUtil.findInJSON(response, exp.getKey());
		
		if (operator.equalsIgnoreCase("NULL")) {
			Assert.assertNull(message, actual);
			return;
		}
		Assert.assertNotNull(message+" Expected valid value but was NULL", actual);
		if (operator.equalsIgnoreCase("NB")) {
			if (actual.isJsonPrimitive()) {
				Assert.assertEquals("Expected key "+exp.getKey()+" to be not blank but was blank", true, StringUtils.isNotBlank(actual.getAsString()));
			}
		} else if (operator.equalsIgnoreCase("EQ")) {
			if (actual instanceof JsonObject) {
				JSONAssert.assertEquals(new JSONObject(exp.getExpexted()), new JSONObject(actual.toString()), JSONCompareMode.LENIENT);
			} else if (actual instanceof JsonArray)  {
				JSONAssert.assertEquals(new JSONArray(exp.getExpexted()), new JSONArray(actual.toString()), JSONCompareMode.LENIENT);
			} else {
				Assert.assertEquals(message, exp.getExpexted(), actual.getAsString());
			}
		} else if (operator.equalsIgnoreCase("NE")) {
			Assert.assertNotEquals(message, exp.getExpexted(), actual.getAsString());
		} else {
			Assert.assertEquals("Either no or invalid comaprison for test case", true, false); //Fail it for invalid or no comparison case
		}
	}

}
