package com.maha.apitest.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.maha.apitest.util.ResponseTrackerUtil;

public class Input {

	private String url;
	private String headers;
	private String params;
	private String method;
	private String body;
	private String response;
	private int statusCode;
	private String skip;
	private String testCaseNo;
	private List<Expression> responseMatchers = null;
	private String trackResponse;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = stripQuotes(headers);
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = stripQuotes(params);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = stripQuotes(body);
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getSkip() {
		return skip;
	}

	public void setSkip(String skip) {
		this.skip = skip;
	}

	public List<Expression> getResponseMatchers() {
		return responseMatchers;
	}

	public String getTestCaseNo() {
		return testCaseNo;
	}

	public void setTestCaseNo(String testCaseNo) {
		this.testCaseNo = testCaseNo;
	}

	
	public String getTrackResponse() {
		return trackResponse;
	}

	public void setTrackResponse(String trackResponse) {
		this.trackResponse = trackResponse;
	}

	public Boolean isTracked(){
		return new Boolean(this.trackResponse);
	}
	
	public void processBeforeExecution(){
		this.body = replacePlaceholders(body);
		this.headers = replacePlaceholders(headers);
		this.params = replacePlaceholders(params);
		this.url = replacePlaceholders(this.url);
		this.response = replacePlaceholders(response);
		createMatchers();
	}
	
	private String replacePlaceholders(String text){
		if (StringUtils.isNotBlank(text)) {
			text = ResponseTrackerUtil.replacePlaceHolders(text);
		}
		return text;
	}
	
	private String stripQuotes(String str) {
		if (StringUtils.isNotBlank(str)) {
			str = str.trim().substring(1, str.length() - 1); // Remove starting
		}
		return str;
	}
	
	private void createMatchers() {
		if (StringUtils.isNotBlank(response)) {
			String[] conditions = response.split("\\\n");
			responseMatchers = new ArrayList<Expression>();
			for (String cond : conditions) {
				cond = cond.trim();
				if (StringUtils.isNotBlank(cond)) {
					String[] equations = cond.split("\\|");
					Expression exp = new Expression();
					exp.setOperator(equations[1].trim());
					exp.setKey(equations[0].trim());
					if (equations.length > 2) {
						exp.setExpexted(equations[2].trim());
					}
					responseMatchers.add(exp);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "url: " + url + "\nheaders: " + headers + "\nparams: " + params
				+ "\nmethod: " + method + "\nbody: " + body + "\nstatusCode: "
				+ statusCode + "\nresponse: " + response + "\n";
	}

}
