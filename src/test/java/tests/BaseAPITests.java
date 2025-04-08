package tests;

import helpers.EntityHelper;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

public class BaseAPITests {

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        EntityHelper.cleanup();
    }

    @AfterSuite
    public void suiteCleanup() {
        EntityHelper.releaseResources();
    }
}
