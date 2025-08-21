package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.Methods;
import webdriverfactory.WebDriverFactory;

public class DriverManagerBase {
    protected WebDriver driver;

    @BeforeClass(alwaysRun = true)
    @Parameters({"browser", "sessionScope"})
    public void setUpClass(@Optional("chrome") String browser,
                           @Optional("method") String sessionScope) {
        if ("class".equalsIgnoreCase(sessionScope)) {
            WebDriverFactory.initDriver(browser);
            driver = WebDriverFactory.getDriver();
            driver.manage().window().maximize();
            Methods.setDriver(driver);
        }
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "sessionScope"})
    public void setUpMethod(@Optional("chrome") String browser,
                            @Optional("method") String sessionScope) {
        if (!"class".equalsIgnoreCase(sessionScope)) { // default: per-method
            WebDriverFactory.initDriver(browser);
            driver = WebDriverFactory.getDriver();
            driver.manage().window().maximize();
            Methods.setDriver(driver);
        }
    }

    @AfterMethod(alwaysRun = true)
    @Parameters({"sessionScope"})
    public void tearDownMethod(@Optional("method") String sessionScope) {
        if (!"class".equalsIgnoreCase(sessionScope)) {
            WebDriverFactory.quitDriver();
        }
    }

    @AfterClass(alwaysRun = true)
    @Parameters({"sessionScope"})
    public void tearDownClass(@Optional("method") String sessionScope) {
        if ("class".equalsIgnoreCase(sessionScope)) {
            WebDriverFactory.quitDriver();
        }
    }
}
