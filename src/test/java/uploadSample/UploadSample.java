package uploadSample;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import base.DriverManagerBase;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.login.NetworkLoginFlow;
import pages.login.SupplierLoginFlow;
import utils.ConfigReader;
import utils.Methods;

public class UploadSample extends DriverManagerBase {
	
	private SupplierLoginFlow portal;
	private NetworkLoginFlow network;
    private String url;
    private String userName;
    private String password;
	
    @BeforeClass(alwaysRun = true)
	public void initializePages() throws InterruptedException {
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

//    @AfterClass(alwaysRun = true)
//    public void teardown() {
//        try { portal.logoutIfPresent(); } catch (Exception ignored) {}
//        if (driver != null) driver.quit();
//    }
	
//  uploading sample from supplier portal
//    @Test 
//    public void testSupplierPortalUploadXML() throws InterruptedException {
//
//        //order created for 10 line item with all forecast type as firm. UC1
//        String orderChange = "orderChange.xml";
//        String orderCancel = "orderCancel.xml";
//        
//        portal.uploadFlow(url, orderChange);
//        portal.uploadFlow(url, orderCancel);
//
//    }
    
	/*
	 * Order Create
	 * 10 line items are create.
	 * forecast type = 10 orders.
	 * UOM = KGM 
	 * Between FCgoodyear and Orion Engineering Carbons
	 */
    
    @Test 
    public void UC1_UploadOrderCreate() throws InterruptedException{
    	
    	System.out.println("UseCase 1 : Upload Order Create");
        
        String orderCreate = "orderCreate01.xml";
        portal.uploadFlow(url, orderCreate);
    	
    }
    
	/*
	 * Order Change
	 * line number 1 and line number 2 have quantity change
	 * forecast type = 10 orders.
	 * Between FCgoodyear and Orion Engineering Carbons
	 */
    @Test 
    public void UC1_UploadOrderChange() throws InterruptedException{

    	System.out.println("UseCase 1 : Upload Order Change");

        String orderCreate = "orderChange01.xml";
        portal.uploadFlow(url, orderCreate);
    	
    }
    
	/*
	 * Order Cancel
	 * line number 10 and 8 are completely removed
	 * line number 9 has 0 quantity
	 * should have 3 order cancel
	 * Between FCgoodyear and Orion Engineering Carbons
	 */
    
    @Test 
    public void UC1_UploadOrderCancel() throws InterruptedException{

    	System.out.println("UseCase 1 : Upload Order Change");
        
        String orderCreate = "orderCancel01.xml";
        portal.uploadFlow(url, orderCreate);
    	
    }
    
	/*
	 * Order Create and cancel
	 * line number 11 and 12 are added
	 * line number 9 and 7 are removed
	 * line number 6 is zero
	 * should have 3 order cancel and 2 order create
	 * Between FCgoodyear and Orion Engineering Carbons
	 */
    
    @Test 
    public void UC1_UploadMixCase() throws InterruptedException{

    	System.out.println("UseCase 1 : Upload Order Change");
        
        String orderCreate = "orderMix01.xml";
        portal.uploadFlow(url, orderCreate);
    	
    }
    
//  uploading sample from network
//    @Test
//    public void testUploadNetwork() throws InterruptedException {
//    	String url = ConfigReader.get("network_dev");
//    	String email = ConfigReader.get("network_email");
//    	String password = ConfigReader.get("network_password");
//    	String locatorId = "lift__noticesContainer___notice";
//        String expectedMessage = "Your message is being saved to the Elemica Backbone. Stay on this page for status updates.";
//        String FileName = "case07";
//        
//        System.out.println("........Test Starting : testUploadNetwork........");
//    	network.loginFlow(driver, email, password, url);
//    	Thread.sleep(10000);    	
//    	System.out.println(".......Flow Completed.......");
//    	Assertions using methods
//        Methods.assertElementPresent("id", locatorId, 10, "Notice container not found!");
//        Methods.assertTextContains("id", locatorId, 10, expectedMessage);
//    	
//    }
}
