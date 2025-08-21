package pages.login;

import static utils.Methods.*;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import components.SupplierPortal;

public class SupplierLoginFlow {

	private SupplierPortal supplier;

	public void loginFlow(WebDriver driver, String userName, String password, String url) {
		driver.get(url);
		this.supplier = new SupplierPortal(driver);
		supplier.login(userName, password);
		waitForPageLoad();

		if (!supplier.isLoginSuccessful()) {
			throw new RuntimeException("login Failed !!");
		}
		sline("Logged in Successfully");
	}

	public void uploadFlow(String URL, String fileName) throws InterruptedException {
		//flow -> navigated to local xml
		//		>update the date from today
		//		> get the absolute path and upload it.
		supplier.navigateToLocalXML(URL);
		supplier.uploadXML(fileName);
	}

	public void validateIntegrationLog(String ShipToCompany, String ShipFromCompany, String MessageType, String Status,
			String Direction, String SearchText, String Acked, String url, String fileName)
			throws InterruptedException {
		supplier.navigateToIntegrationLog(url);
		supplier.filterIntegrationLog(ShipToCompany, ShipFromCompany, MessageType, Status, Direction, SearchText,
				Acked);
		supplier.validateIntegrationLogTable();

	}
	



}
