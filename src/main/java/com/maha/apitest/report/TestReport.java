package com.maha.apitest.report;

import java.util.HashMap;
import java.util.Map;

public class TestReport {

	private String testId;
	private TestStatus status;
	private String reason;

	public TestReport(String testId, TestStatus status, String reason) {
		this.testId = testId;
		this.status = status;
		this.reason = reason;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public TestStatus getStatus() {
		return status;
	}

	public void setStatus(TestStatus status) {
		this.status = status;
	}
	
}
