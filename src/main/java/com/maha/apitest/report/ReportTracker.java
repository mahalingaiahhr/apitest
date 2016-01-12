package com.maha.apitest.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ReportTracker {
	
	private static Logger logger = LogManager.getLogger(ReportTracker.class);

	private static List<TestReport> reports = new ArrayList<TestReport>();
	
	private static Map<TestStatus, Integer> reportCount = new HashMap<TestStatus, Integer>();

	static {
		for (TestStatus status : TestStatus.values()) {
			reportCount.put(status, 0);
		}
	}
	
	public static void add(TestReport report) {
		reports.add(report);
		reportCount.put(report.getStatus(), reportCount.get(report.getStatus())+1);
	}

	public static void generateReport() {
		for (TestReport report : reports) {
			if (TestStatus.FAILED.equals(report.getStatus())) {
				logger.info(report.getTestId()+" Failed : "+report.getReason());	
			}
		}
		
		logger.info(String.format("\n==========================================================================================\n"
				+ "Test Report - Run: %d,  Success: %d, Skipped: %d, Failed: %d \n"
				+ "==========================================================================================",
				reports.size(),
				reportCount.get(TestStatus.SUCCESS).intValue(), 
				reportCount.get(TestStatus.SKIPPED).intValue(), 
				reportCount.get(TestStatus.FAILED).intValue()));
	}

}
