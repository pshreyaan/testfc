package components;

import org.openqa.selenium.WebDriver;

import static utils.Methods.*;
import utils.XmlDateUpdater;

public class Network {

    private static final int TIMEOUT = 100; 

    private static final String EMAIL_ID = "input[name='username'][type='email']";
    private static final String PASSWORD_ID = "password";
    private static final String CONTINUE_BTN_ID = ":r1:";
    private static final String DASHBOARD = "nav-dashboard";
    private static final String SUBMITBUTTON = "upload-message";
    private static final String CLICKLIVE = "label[for='live']";
    private static final String PASTEMESSAGE = "paste-message";
    private static final String SUBMITMESSAGE = "uploadFileBtn";
    private static final String Check = "lift__noticesContainer___notice";
    private WebDriver driver;
    
    public Network(WebDriver driver) {
    	this.driver = driver;
    	setDriver(driver);
    }

    public void login(String email, String password) {
        sendKeys("cssSelector", EMAIL_ID, TIMEOUT, email);
        click("id", CONTINUE_BTN_ID, TIMEOUT);
        waitForPageLoad(TIMEOUT);
        sendKeys("id", PASSWORD_ID, TIMEOUT, password);
        click("id", CONTINUE_BTN_ID, TIMEOUT);
    }
    
    public void navigateDashboard() {
    	click("class",DASHBOARD);
    }
    public void clickSubmitMessage() {
    	click("class",SUBMITBUTTON);
    }
    public void clickLiveProcessing() {
    	click("cssSelector", CLICKLIVE);
    }
    public void inboundConfiguration() {
        typeAndSelectSuggestion(
            "cssSelector",
            "input[placeholder='e.g. Lubrizol']",
            "smart",
            "suggestions",
            2
        );
    }

    public void pasteMessage(String FileName) {
        try {
            if (FileName.endsWith(".xml")) {
                FileName = FileName.replace(".xml", "");
            }

            XmlDateUpdater.updateForecastDates("resources/xml/" + FileName + ".xml");

            String updatedXml = new String(java.nio.file.Files.readAllBytes(
                    java.nio.file.Paths.get("resources/testdata/" + FileName + "_updated.xml")));

            setValue("id", PASTEMESSAGE, updatedXml);
            System.out.println("Message Pasted Successfully !!");
        } catch (Exception e) {
            System.out.println("data change issue");
            e.printStackTrace();
        }
    }

    
    public void submitMessage() {
    	click("id", SUBMITMESSAGE);;
    }

    public boolean isLoginSuccessful() {
        waitForPageLoad(TIMEOUT);
        return true;
    }
}
