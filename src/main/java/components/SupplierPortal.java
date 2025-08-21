package components;

import static utils.Methods.*;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.XmlDateUpdater;
import model.IntegrationDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Methods;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SupplierPortal {

	private static final int TIMEOUT = 10;
	private static final String USERNAME_ID = "username_id";
	private static final String PASSWORD_ID = "passwordID";
	private static final String LOGIN_BTN_ID = "submitbutton";
	private static final String MESSAGE_BTN_ID = "messageButtonID";
	private static final String NAVIGATE_XML = "/nav?cmd=DocumentSubmission";
	private static final String NAVIGATE_HOME = "/nav?cmd=Home";
	private static final String NAVIGATE_INTEGRATIONLOG = "/nav?cmd=IntegrationLog";
	private WebDriver driver;

	public SupplierPortal(WebDriver driver) {
		this.driver = driver;
		setDriver(driver);
	}

	public void login(String email, String password) {
		sline("logging in to supplier portal as "+email);
		sendKeys("id", USERNAME_ID, TIMEOUT, email);
		sendKeys("id", PASSWORD_ID, TIMEOUT, password);
		click("id", LOGIN_BTN_ID, TIMEOUT);
	}

	public boolean isLoginSuccessful() {
		waitForPageLoad(TIMEOUT);
		return true;
	}

	public void navigateToLocalXML(String URL) {
		sline("navigating to Local XML upload");
		driver.get(URL + NAVIGATE_XML);
	}

	public void navigateToHome(String URL) {
		sline("navigating to Home Screen");
		driver.get(URL + NAVIGATE_HOME);
	}

	public void navigateToIntegrationLog(String URL) {
		sline("navigating to Integration Log");
		driver.get(URL + NAVIGATE_INTEGRATIONLOG);
	}

	public void uploadXML(String fileName) throws InterruptedException {
	    sline("uploading XML using local xml upload");

	    String updatedFilePath = XmlDateUpdater.updateForecastDates(fileName);
	    String absolutePath = getAbsolutePath(updatedFilePath); 

	    getDriver().switchTo().frame("mainFrame");
	    sendKeys("id", "fileData", absolutePath); 
	    click("cssSelector", "form#uploadForm input[type='submit'][value='Upload File']");
	    getDriver().switchTo().defaultContent();
	}

	public void filterIntegrationLog(String shipToCompany, String shipFromCompany, String messageType, String status,
			String direction, String searchText, String acked) throws InterruptedException {
		getDriver().switchTo().frame("mainFrame");
		if (shipToCompany != null && !shipToCompany.isEmpty()) {
			selectDropdownOption("name", "buyerID", "text", shipToCompany, TIMEOUT);
		}
		if (shipFromCompany != null && !shipFromCompany.isEmpty()) {
			selectDropdownOption("name", "supplierID", "text", shipFromCompany, TIMEOUT);
		}
		if (messageType != null && !messageType.isEmpty()) {
			selectDropdownOption("name", "messageType", "text", messageType, TIMEOUT);
		}
		if (status != null && !status.isEmpty()) {
			selectDropdownOption("name", "status", "text", status, TIMEOUT);
		}
		if (direction != null && !direction.isEmpty()) {
			selectDropdownOption("name", "direction", "text", direction, TIMEOUT);
		}
		if (acked != null && !acked.isEmpty()) {
			selectDropdownOption("name", "acked", "text", acked, TIMEOUT);
		}
		if (searchText != null && !searchText.isEmpty()) {
			sendKeys("id", "documentIdentifier_id", TIMEOUT, searchText);
		}
		click("id", "view_id", TIMEOUT);
	}
	public void validateIntegrationLogTable() {
	    By tableLocator = By.id("logTableRow");
	    List<Map<String, String>> tableRows = parseTableRows(tableLocator);

	    for (Map<String, String> row : tableRows) {
	        System.out.println("Row:");
	        row.forEach((header, value) -> System.out.println(header + ": " + value));
	    }
	}
	
	// ---------- Integration Log: navigate, click Details, read & parse ----------

	/** Navigate directly to the Integration Log page. */
	public void navigateIntegrationLog() {
	    Methods.sline("Navigating to Integration Log");
	    // If you already have a base URL/openUrl helper, replace this with that
	    driver.get(driver.getCurrentUrl().split("/nav")[0] + "/nav?cmd=IntegrationLog");
	}

	/** Click the Details icon for a specific Document Identifier in the Integration Log table. */
	public void clickIntegrationLogDetails(String tableId, String documentId) {
	    WebElement idCell = findDocIdCellInTable(tableId, documentId);
	    Methods.scrollIntoView(driver, idCell);

	    WebElement row = findRowForDocId(tableId, documentId);
	    WebElement detailsImg = findDetailsIconInRow(row);

	    try {
	        new WebDriverWait(driver, Duration.ofSeconds(10))
	                .until(ExpectedConditions.elementToBeClickable(detailsImg)).click();
	    } catch (Exception e) {
	        Methods.jsClick(driver, detailsImg);
	    }

	    new WebDriverWait(driver, Duration.ofSeconds(30))
	            .until(ExpectedConditions.visibilityOfElementLocated(By.id("showIntegrationDetails")));
	    Methods.sleep(500); // allow content to settle
	}

	/** Read the Integration Details popup (#showIntegrationDetails) into a typed model. */
	public IntegrationDetails readIntegrationLogDetails(String documentId) {
	    List<WebElement> rows = getPopupRows("showIntegrationDetails");
	    return parsePopupToModel(documentId, rows);
	}

	// ---------- Private helpers (Integration Log–specific) ----------

	private WebElement findDocIdCellInTable(String tableId, String documentId) {
	    By docCell = By.xpath("//table[@id='" + tableId + "']//td[contains(@class,'noBreaks') and normalize-space(text())='" + documentId + "']");
	    return new WebDriverWait(driver, Duration.ofSeconds(30))
	            .until(ExpectedConditions.visibilityOfElementLocated(docCell));
	}

	private WebElement findRowForDocId(String tableId, String documentId) {
	    By row = By.xpath("//table[@id='" + tableId + "']//tr[td[contains(@class,'noBreaks') and normalize-space(text())='" + documentId + "']]");
	    return new WebDriverWait(driver, Duration.ofSeconds(30))
	            .until(ExpectedConditions.visibilityOfElementLocated(row));
	}

	private WebElement findDetailsIconInRow(WebElement row) {
	    return row.findElement(By.cssSelector("img.imageLink"));
	}

	private List<WebElement> getPopupRows(String popupId) {
	    new WebDriverWait(driver, Duration.ofSeconds(30))
	            .until(ExpectedConditions.visibilityOfElementLocated(By.id(popupId)));
	    return driver.findElements(By.cssSelector("#" + popupId + " tbody tr"));
	}

	// ---------- Parsing (kept here per your request) ----------

	// Regex to parse "Key: Value;" pairs inside the "Details:" cell
	private static final Pattern DETAILS_PAIR = Pattern.compile("([A-Za-z ]+):\\s*([^;]*);");

	private IntegrationDetails parsePopupToModel(String documentId, List<WebElement> popupRows) {
	    IntegrationDetails d = new IntegrationDetails();
	    d.documentIdentifier = documentId;

	    for (WebElement tr : popupRows) {
	        List<WebElement> tds = tr.findElements(By.tagName("td"));
	        if (tds.size() < 2) continue;

	        String key = normalizeKey(tds.get(0).getText());
	        String val = normalizeCellText(tds.get(1).getText());

	        switch (key) {
	            case "Process Date":
	                d.processDate = val;
	                break;
	            case "Direction":
	                d.direction = val;
	                break;
	            case "Doc Type":
	                d.docType = val;
	                break;
	            case "Status":
	                d.status = val;
	                break;
	            case "Ship To Company":
	                d.shipToCompany = val;
	                break;
	            case "Ship From Company":
	                d.shipFromCompany = val;
	                break;
	            case "File":
	                // Prefer anchor text if present
	                try {
	                    String a = tds.get(1).findElement(By.tagName("a")).getText();
	                    d.fileName = normalizeCellText(a);
	                } catch (NoSuchElementException ignored) {
	                    d.fileName = val;
	                }
	                break;
	            case "Details":
	                // Use innerText to keep breaks
	                String detailsText = getInnerText(tds.get(1));
	                fillDetailsBlock(detailsText, d);
	                break;
	            default:
	                // ignore System ID / Internal ID / Group ID, etc.
	        }
	    }
	    return d;
	}

	private void fillDetailsBlock(String detailsText, IntegrationDetails d) {
	    if (detailsText == null) return;
	    String text = detailsText.replace("\r", " ").replace("\n", " ").trim();
	    Matcher m = DETAILS_PAIR.matcher(text);
	    while (m.find()) {
	        String k = m.group(1).trim();
	        String v = m.group(2).trim();
	        switch (k) {
	            case "Planning Item Name":
	                d.planningItemName = v;
	                break;
	            case "Total Lines":
	                d.totalLines = Methods.safeInt(v);
	                break;
	            case "Orders Cancelled":
	                d.ordersCancelled = Methods.safeInt(v);
	                break;
	            case "Cancelled Orders List":
	                d.cancelledOrdersList = Methods.parseColonSeparatedList(v);
	                break;
	            default:
	                // ignore new/unknown keys
	        }
	    }
	}

	// ---------- Micro-utils local to this page (delegating to Methods where generic) ----------

	private String normalizeKey(String s) { return normalizeCellText(s).replace(":", ""); }

	private String normalizeCellText(String s) {
	    if (s == null) return "";
	    return s.replace('\u00A0', ' ').replaceAll("\\s+", " ").trim();
	}

	private String getInnerText(WebElement el) {
	    Object txt = ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;", el);
	    return txt == null ? "" : txt.toString();
	}

	


}