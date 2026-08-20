package com.deepank.api.tests;

import com.Deepank.api.reporting.AllureEnvironmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @BeforeSuite
    public void beforeSuite() {
        LOG.info("Test suite execution started");
        AllureEnvironmentUtil.writeEnvironmentUtil();
    }

    @AfterSuite
    public void afterSuite() {
        LOG.info("Test suite execution completed");
    }
}
