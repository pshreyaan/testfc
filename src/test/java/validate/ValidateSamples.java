package validate;

import base.DriverManagerBase;
import components.SupplierPortal;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.login.NetworkLoginFlow;
import pages.login.SupplierLoginFlow;
import utils.ConfigReader;

public class ValidateSamples extends DriverManagerBase {

    private static final String XML_DIR = "resources/xml";
	private SupplierLoginFlow portal;
	private NetworkLoginFlow network;
    private String url;
    private String userName;
    private String password;
    
    @BeforeClass
    public void loginAndNavigateOnce() {
	    // Suppress Selenium CDP logging
	    java.util.logging.Logger.getLogger("org.openqa.selenium.devtools.CdpVersionFinder").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.openqa.selenium.chromium.ChromiumDriver").setLevel(java.util.logging.Level.OFF);
	    
	    this.portal = new SupplierLoginFlow();
	    this.network = new NetworkLoginFlow();
	    
	    url = ConfigReader.get("supplier_portal");
        userName = ConfigReader.get("supplier_portal_UserName");
        password = ConfigReader.get("supplier_portal_Password");
        
        System.out.println("Java Runtime version: " + System.getProperty("java.version"));
        Package seleniumPackage = RemoteWebDriver.class.getPackage();
        System.out.println("Selenium version: " + seleniumPackage.getImplementationVersion());
        Package wdmPackage = WebDriverManager.class.getPackage();
        System.out.println("WebDriverManager version: " + wdmPackage.getImplementationVersion());
        
        portal.loginFlow(driver, userName, password, url);
    }

    // Keep only the files you want to validate "for now"
    @DataProvider(name = "samples")
    public Object[][] samples() {
        return new Object[][]{
            {"orderCreate01_updated.xml"},
            {"orderCancel01_updated.xml"},
            {"orderChange01_updated.xml"},
            {"orderMix01_updated.xml"},
        };
    }

	/*    
	 * UC1_UploadOrderCreate validation
	*/
    @Test
    public void UC1_UploadOrderCreateValidation() throws InterruptedException{
    	System.out.println("UseCase 1 : Upload Order Create Validation");
    	
    	
    }

}
