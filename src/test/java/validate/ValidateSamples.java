package validate;

import base.DriverManagerBase;
import components.SupplierPortal;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.login.SupplierLoginFlow;

public class ValidateSamples extends DriverManagerBase {

    private SupplierPortal portal;
    private static final String XML_DIR = "resources/xml";

    @BeforeClass
    public void loginAndNavigateOnce() {
        // Login exactly like UploadSample
        SupplierLoginFlow login = new SupplierLoginFlow(getDriver());
        login.login();

        // Navigate to Integration Log (single action lives in components.SupplierPortal)
        portal = new SupplierPortal(getDriver());
        portal.navigateIntegrationLog();
    }

    // Keep only the files you want to validate "for now"
    @DataProvider(name = "samples")
    public Object[][] samples() {
        return new Object[][]{
            {"orderCreate01_updated.xml"},
            {"orderCancel01_updated.xml"},
            {"orderChange01_updated.xml"},
            {"orderMix01_updated.xml"},
            {"case07_updated.xml"},
            {"case08_updated.xml"},
            {"case09_updated.xml"},
        };
    }

    @Test(dataProvider = "samples")
    public void validateOneSample(String xmlFile) {
        IntegrationLogVerification.validateAndPersist(
                getDriver(),
                portal,
                XML_DIR,
                xmlFile
        );
    }
}
