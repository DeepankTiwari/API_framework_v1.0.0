package com.Deepank.api.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("Test Started: {}", result.getName());
        LOG.info(
                "Running test: {} on thread: {}",
                result.getName(),
                Thread.currentThread().getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("Test Passed: {}", result.getName());
        LOG.info(
                "Test passed: {} on thread: {}",
                result.getName(),
                Thread.currentThread().getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("Test Failed: {}", result.getName());
        if (result.getThrowable() != null) {
            LOG.error("Failure reason", result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("Test skipped: {}", result.getName());
    }
}
