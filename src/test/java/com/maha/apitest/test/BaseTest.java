package com.maha.apitest.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.AssumptionViolatedException;

import com.maha.apitest.model.Input;
import com.maha.apitest.model.RestAPIRequest;
import com.maha.apitest.model.RestAPIResponse;
import com.maha.apitest.report.ReportTracker;
import com.maha.apitest.report.TestReport;
import com.maha.apitest.report.TestStatus;
import com.maha.apitest.util.RequestFactory;
import com.maha.apitest.util.ResponseTrackerUtil;
import com.maha.apitest.util.RestClientUtils;
import com.maha.apitest.validator.Validator;

public class BaseTest {
	
	protected void execute(Input input)  {
		try {
			Boolean isSkip = new Boolean(input.getSkip());
			Assume.assumeFalse(isSkip);
			
			RestAPIResponse response = executeHttp(input);
			if (input.isTracked()) {
				ResponseTrackerUtil.trackedResponse=response;
			}
			validateResponse(input, response);
			ReportTracker.add(new TestReport(input.getTestCaseNo(), TestStatus.SUCCESS, null));
		}catch (Throwable e){
			String reason = null;
			TestStatus status = null;
			if (e instanceof AssumptionViolatedException) {
				status = TestStatus.SKIPPED;
			} else {
				status =TestStatus.FAILED;
				StringWriter writer = new StringWriter();
				PrintWriter pw = new PrintWriter(writer);
				e.printStackTrace( pw);
				reason = writer.toString().substring(0, 100);
			}
			ReportTracker.add(new TestReport(input.getTestCaseNo(), status, reason));
		}
	}
	
	@AfterClass
	public static void generateReport(){
		ReportTracker.generateReport();
	}

	protected RestAPIResponse executeHttp(Input input) {
		input.processBeforeExecution();
		RestAPIRequest request = RequestFactory.createRequest(input);
		RestAPIResponse response = RestClientUtils.execute(request);
		return response;
	}
	
	protected void validateResponse(Input input, RestAPIResponse response) {
		Validator.validate(input, response);
	}
	
}
