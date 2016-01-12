package com.maha.apitest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.maha.apitest.model.RestAPIResponse;

public class ResponseTrackerUtil {
	
	public static RestAPIResponse trackedResponse = null; 
	private static Pattern pattern = Pattern.compile("\\$\\[(.+?)\\]");
	
	public static String replacePlaceHolders(String text){
		if (ResponseTrackerUtil.trackedResponse == null || StringUtils.isBlank(text)) {
			return text;
		}
		Matcher matcher = pattern.matcher(text);
		StringBuilder builder = new StringBuilder();
		int i = 0;
		while (matcher.find()) {
		    String replacement = getReplacementText(matcher.group(1));
		    builder.append(text.substring(i, matcher.start()));
		    if (replacement == null)
		        builder.append(matcher.group(0));
		    else
		        builder.append(replacement);
		    i = matcher.end();
		}
		builder.append(text.substring(i, text.length()));
		return builder.toString();
	}

	private static String getReplacementText(String group) {
		String key = group.replace("trackedResp", "$");
		JsonElement matched =  findInJSON(trackedResponse, key);
		if (matched != null) {
			if (matched instanceof JsonObject || matched instanceof JsonArray) {
				return matched.toString();
			}
			return  matched.getAsString();
		}
		return null;
	}

	public static JsonElement findInJSON(RestAPIResponse response, String key) {
		if (response == null || response.getResponse() == null) {
			return null;
		}
		try {
			return JsonPath.read(response.getResponse(), key);
		}catch (Exception e) {
			return null;
		}
	}
}
