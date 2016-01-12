package com.maha.apitest.model;

import java.io.Serializable;

public class RestAPIResponse implements Serializable {

	private static final long serialVersionUID = -1552614849700944415L;

	private int statusCode;
	private String response;

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

}
