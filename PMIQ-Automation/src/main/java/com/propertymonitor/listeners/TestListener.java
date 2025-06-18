package com.propertymonitor.listeners;

import com.propertymonitor.utils.EmailUtility;
import org.testng.*;

public class TestListener implements ITestListener, ISuiteListener {

    private int totalTests = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;

    @Override
    public void onStart(ISuite suite) {
        System.out.println("=== Test Suite Execution Started: " + suite.getName() + " ===");
        // Reset counters at suite start
        totalTests = 0;
        passedTests = 0;
        failedTests = 0;
        skippedTests = 0;
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("=== Test Suite Execution Finished: " + suite.getName() + " ===");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        System.out.println("Skipped: " + skippedTests);

        // Send email report with dynamic summary
        try {
            System.out.println("📧 Sending summary email...");
            EmailUtility.sendReportEmail(totalTests, passedTests, failedTests, skippedTests);
            System.out.println("✅ Summary email sent.");
        } catch (Exception e) {
            System.err.println("❌ Failed to send summary email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        totalTests++;
        System.out.println("▶️ Test started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests++;
        System.out.println("✅ Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        System.out.println("❌ Test failed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests++;
        System.out.println("⚠️ Test skipped: " + result.getMethod().getMethodName());
    }

    // Optional interface methods with empty bodies
    @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}

}