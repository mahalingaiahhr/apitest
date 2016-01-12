package com.maha.apitest.util;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.maha.apitest.model.RestAPIRequest;
import com.maha.apitest.model.RestAPIResponse;

public class RestClientUtils {
	
	private static Logger logger = LogManager.getLogger(RestClientUtils.class);
	
	public static RestAPIResponse execute(RestAPIRequest request) {
		RestAPIResponse response = null;
		if (request.getMethod().equalsIgnoreCase("GET")) {
			response = executeGet(request);
		} else if (request.getMethod().equalsIgnoreCase("POST")) {
			response = executePost(request);
		} else if (request.getMethod().equalsIgnoreCase("PUT")) {
			response = executePut(request);
		} else if (request.getMethod().equalsIgnoreCase("DELETE")) {
			response = executeDelete(request);
		}
		return response;
	}
	
	public static RestAPIResponse executeGet(RestAPIRequest request) {
		HttpGet httpGet = new HttpGet(request.getUrl());
		return executeRequest(httpGet, request);
	}

	public static RestAPIResponse executePost(RestAPIRequest request) {
		HttpPost httpPost = new HttpPost(request.getUrl());
		if(StringUtils.isNotBlank(request.getBody())){
			StringEntity entuty = new StringEntity(request.getBody(), CharEncoding.UTF_8);					
			httpPost.setEntity(entuty);
		}
		return executeRequest(httpPost, request);
	}
	
	public static RestAPIResponse executePut(RestAPIRequest request) {
		HttpPut httpPut = new HttpPut(request.getUrl());
		if(StringUtils.isNotBlank(request.getBody())){
			StringEntity entuty = new StringEntity(request.getBody(), CharEncoding.UTF_8);					
			httpPut.setEntity(entuty);
		}
		return executeRequest(httpPut, request);
	}
	
	public static RestAPIResponse executeDelete(RestAPIRequest request) {
		HttpDelete httpDelete = new HttpDelete(request.getUrl());
		return executeRequest(httpDelete, request);
	}
	
	private static RestAPIResponse executeRequest(HttpRequestBase base, RestAPIRequest request) {
		addHeaders(base, request);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(base);
			RestAPIResponse response = createRestResponse(httpResponse);
			logger.debug("\n---------------------------------------------------------------------------------------\n "
					+ base.getMethod()+" "+base.getURI() + getBody(base)
					+ "\n\n\n Response: \n"+response.getResponse()
					+"\n---------------------------------------------------------------------------------------");
			return response;
		} catch (Exception e) {
		} finally{
			try {
				if (httpResponse != null) {
					httpResponse.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	private static String getBody(HttpRequestBase base) throws Exception {
		if (base instanceof HttpEntityEnclosingRequestBase) {
			HttpEntityEnclosingRequestBase req = (HttpEntityEnclosingRequestBase) base;
			if (req.getEntity() != null) {
				return "\n Body \n"+EntityUtils.toString(req.getEntity());
			}
		}
		
		return "";
	}

	private static void addHeaders(HttpRequestBase base, RestAPIRequest request) {
		if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
			for (Entry<String, String> entry : request.getHeaders().entrySet()) {
				base.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private static RestAPIResponse createRestResponse(CloseableHttpResponse httpResponse) throws Exception{
		RestAPIResponse response = new RestAPIResponse();
		String responseStr = EntityUtils.toString(httpResponse.getEntity());
		response.setResponse(responseStr);
		response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		return response;
	}
}
