package com.maha.apitest.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.maha.apitest.model.Input;
import com.maha.apitest.model.RestAPIRequest;

public class RequestFactory {

	public static RestAPIRequest createRequest(Input input){
		RestAPIRequest request = new RestAPIRequest();
		request.setUrl(formUrl(input));
		request.setHeaders(convertToJSON(input.getHeaders()));
		request.setBody(input.getBody());
		request.setMethod(input.getMethod());
		return request;
	}
	
	private static Map<String, String> convertToJSON(String json){
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotBlank(json)) {
			JSONObject obj = new JSONObject(json);
			for (Object key : obj.keySet()) {
				String k = key.toString();
				map.put(k, obj.getString(k));
			}
		}
		return map;
	}
	
	private static String formUrl(Input input){
		StringBuilder urlBuilder = new StringBuilder(ConfigUtil.getProperty("server.url"));
		urlBuilder.append(input.getUrl().trim());
		if (StringUtils.isNotBlank(input.getParams())) {
			JSONObject json = new JSONObject(input.getParams());
			StringBuilder params = new StringBuilder();
			for (Iterator iterator = json.keys(); iterator.hasNext();) {
				String key = (String) iterator.next();
				params.append(key+"="+json.get(key).toString());
				if(iterator.hasNext()){
					params.append("&");
				}
			}
			urlBuilder.append("?");
			urlBuilder.append(params);
		}
		return urlBuilder.toString();
	}
}
