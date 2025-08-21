package utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for WebDriver interactions.
 * - Pass driver once via setDriver().
 * - Use by element id or classname.
 * - Timeout always in seconds.
 */
public class Methods {

    // ------ DRIVER MANAGEMENT ------

    private static WebDriver driver;
    private static final int DEFAULT_TIMEOUT = 10;

    /**
     * Set the WebDriver instance ONCE per test run.
     */
    public static void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    /**
     * Optionally, get the driver (for custom checks).
     */
    public static WebDriver getDriver() {
        return driver;
    }

    public static WebDriverWait getWait(int timeoutSeconds) {
        return new WebDriverWait(driver, java.time.Duration.ofSeconds(timeoutSeconds));
    }

    // ---------------------- CLICK METHODS ----------------------

    /**
     * Click an element by type ("id" or "classname") and value (actual id/class).
     * Waits until clickable.
     */
    public static void click(String type, String value, int timeoutSeconds) {
        try {
            By locator = buildLocator(type, value);
            getWait(timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (Exception e) {
            System.err.println("[ERROR] Methods.click(type=" + type + ", value=" + value + ", timeout=" + timeoutSeconds + ")");
            e.printStackTrace();
            throw new RuntimeException("Failed to click element: type=" + type + ", value=" + value, e);
        }
    }


    /**
     * Click an element by type ("id" or "classname") and value, with default timeout.
     */
    public static void click(String type, String value) {
        click(type, value, DEFAULT_TIMEOUT);
    }
    
    /**
     * Clicks an element matching the class and tag based on index.
     *
     * @param tag           HTML tag to match (e.g., "img")
     * @param className     Class name to filter by (e.g., "imageLink")
     * @param index         0-based index of the element to click
     * @param timeout       Max time to wait for elements to be visible
     */
    public static void clickElementByClassIndex(String tag, String className, int index, int timeout) {
        By locator = By.cssSelector(tag + "." + className);
        List<WebElement> elements = getWait(timeout).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

        if (index >= 0 && index < elements.size()) {
            elements.get(index).click();
        } else {
            throw new IllegalArgumentException("Index " + index + " is out of bounds for elements with class '" + className + "'");
        }
    }

    // ---------------------- SEND KEYS METHODS ----------------------

    /**
     * Send keys to an element by type ("id" or "classname") and value, with timeout.
     * Clears first.
     */
    public static void sendKeys(String type, String value, int timeoutSeconds, String text) {
        try {
            By locator = buildLocator(type, value);
            WebElement element = getWait(timeoutSeconds)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            System.err.println("[ERROR] Methods.sendKeys(type=" + type + ", value=" + value + ", timeout=" + timeoutSeconds + ", text=" + text + ")");
            e.printStackTrace();
            throw new RuntimeException("Failed to send keys: type=" + type + ", value=" + value, e);
        }
    }


    /**
     * Send keys with default timeout.
     */
    public static void sendKeys(String type, String value, String text) {
        sendKeys(type, value, DEFAULT_TIMEOUT, text);
    }

    /**
     * Sends text input to the specified element and presses Enter.
     *
     * @param type           Locator strategy (e.g., "id", "cssSelector")
     * @param value          The locator value
     * @param timeoutSeconds Max wait time for the element to be visible
     * @param text           Text to be entered before pressing Enter
     */
    public static void sendKeysAndEnter(String type, String value, int timeoutSeconds, String text) {
        By locator = buildLocator(type, value);
        WebElement element = getWait(timeoutSeconds)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.sendKeys(text + Keys.RETURN);
    }
    
    /**
     * Sends text input and presses Enter with default timeout.
     */
    public static void sendKeysAndEnter(String type, String value, String text) {
        sendKeysAndEnter(type, value, DEFAULT_TIMEOUT, text);
    }
    
    // ---------------------- GET TEXT METHODS ----------------------

    /**
     * Get visible text from element by type and value, with timeout.
     */
    public static String getText(String type, String value, int timeoutSeconds) {
        try {
            By locator = buildLocator(type, value);
            WebElement element = getWait(timeoutSeconds)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.getText();
        } catch (Exception e) {
            System.err.println("[ERROR] Methods.getText(type=" + type + ", value=" + value + ", timeout=" + timeoutSeconds + ")");
            e.printStackTrace();
            throw new RuntimeException("Failed to get text: type=" + type + ", value=" + value, e);
        }
    }


    /**
     * Get text with default timeout.
     */
    public static String getText(String type, String value) {
        return getText(type, value, DEFAULT_TIMEOUT);
    }

    // ---------------------- WAIT METHODS ----------------------

    /**
     * Wait for the page to be fully loaded.
     */
    public static void waitForPageLoad(int timeoutSeconds) {
        try {
            getWait(timeoutSeconds).until(webDriver ->
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            System.err.println("[ERROR] Methods.waitForPageLoad(timeout=" + timeoutSeconds + ")");
            e.printStackTrace();
            throw new RuntimeException("Failed to wait for page load", e);
        }
    }


    /**
     * Wait for the page to load with default timeout.
     */
    public static void waitForPageLoad() {
        waitForPageLoad(DEFAULT_TIMEOUT);
    }
    /**
     * Wait for element to disappear by type and value.
     */
    public static void waitForElementToDisappear(String type, String value, int timeoutSeconds) {
        By locator = buildLocator(type, value);
        getWait(timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to disappear with default timeout.
     */
    public static void waitForElementToDisappear(String type, String value) {
        waitForElementToDisappear(type, value, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for specific text to appear in element.
     */
    public static void waitForTextPresent(String type, String value, int timeoutSeconds, String text) {
        By locator = buildLocator(type, value);
        getWait(timeoutSeconds).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Hard wait - pauses execution for given seconds.
     * Use only when necessary; prefer WebDriver waits.
     */
    public static void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during wait", e);
        }
    }

    // ---------------------- UTILITIES ----------------------

    /**
     * Builds a Selenium By locator from a given type and value.
     *
     * Supported types: id, name, class/classname, tagname, cssSelector/css, xpath, linkText, partialLinkText
     */
    private static By buildLocator(String type, String value) {
        switch (type.toLowerCase()) {
            case "id":
                return By.id(value);
            case "name":
                return By.name(value);
            case "class":
            case "classname":
                return By.className(value);
            case "tagname":
                return By.tagName(value);
            case "css":
            case "cssselector":
                return By.cssSelector(value);
            case "xpath":
                return By.xpath(value);
            case "linktext":
                return By.linkText(value);
            case "partiallinktext":
                return By.partialLinkText(value);
            default:
                throw new IllegalArgumentException("Unsupported locator type: " + type);
        }
    }

    
    /**
     * Checks if an element is present and visible within the given timeout.
     * @param type "id" or "classname"
     * @param value actual id/class string
     * @param timeoutSeconds how long to wait (seconds)
     * @return true if element is present and visible, false otherwise
     */
    public static boolean isElementPresent(String type, String value, int timeoutSeconds) {
        try {
            By locator = buildLocator(type, value);
            getWait(timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException e) {
            System.err.println("[WARN] Methods.isElementPresent(type=" + type + ", value=" + value + ", timeout=" + timeoutSeconds + ") - not found");
            // Optionally print stack for debug
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("[ERROR] Methods.isElementPresent(type=" + type + ", value=" + value + ", timeout=" + timeoutSeconds + ")");
            e.printStackTrace();
            throw new RuntimeException("Failed checking element presence: type=" + type + ", value=" + value, e);
        }
    }

    
	/*
	 * read xml content
	 */
    public static String readXmlContent(String path) {
        try {
            return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(path)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read XML content", e);
        }
    }
    
    /**
     * Set the value of an element using JavaScript.
     * Supports type = id, class, cssSelector, name, tag.
     * Dispatches an 'input' event for framework compatibility (React, Angular, etc).
     */
    public static void setValue(String type, String selector, String value) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String script;

            switch (type.toLowerCase()) {
                case "id":
                    script = "let el = document.getElementById(arguments[0]);" +
                             "if(el){ el.value = arguments[1]; el.dispatchEvent(new Event('input', { bubbles: true })); }";
                    js.executeScript(script, selector, value);
                    break;

                case "class":
                case "classname":
                    script = "let el = document.getElementsByClassName(arguments[0])[0];" +
                             "if(el){ el.value = arguments[1]; el.dispatchEvent(new Event('input', { bubbles: true })); }";
                    js.executeScript(script, selector, value);
                    break;

                case "name":
                    script = "let el = document.getElementsByName(arguments[0])[0];" +
                             "if(el){ el.value = arguments[1]; el.dispatchEvent(new Event('input', { bubbles: true })); }";
                    js.executeScript(script, selector, value);
                    break;

                case "tag":
                    script = "let el = document.getElementsByTagName(arguments[0])[0];" +
                             "if(el){ el.value = arguments[1]; el.dispatchEvent(new Event('input', { bubbles: true })); }";
                    js.executeScript(script, selector, value);
                    break;

                case "css":
                case "cssselector":
                    script = "let el = document.querySelector(arguments[0]);" +
                             "if(el){ el.value = arguments[1]; el.dispatchEvent(new Event('input', { bubbles: true })); }";
                    js.executeScript(script, selector, value);
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported selector type: " + type);
            }

        } catch (Exception e) {
            ExceptionHandler.logAndThrow("setValue(" + type + "=" + selector + ")", e);
        }
    }
    /**
     * Asserts that the element is present on the page within the specified timeout.
     *
     * @param type             Locator strategy (e.g., "id", "cssSelector")
     * @param value            The locator value
     * @param timeoutInSeconds Maximum time to wait for the element
     * @param messageIfMissing Custom assertion failure message
     */
    public static void assertElementPresent(String type, String value, int timeoutInSeconds, String messageIfMissing) {
        boolean isPresent = isElementPresent(type, value, timeoutInSeconds);
        try {
            Assert.assertTrue(isPresent, messageIfMissing);
            System.out.println("Success");
        } catch (AssertionError e) {
            System.err.println("[ASSERTION FAILED] assertElementPresent");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Asserts that the element's text is exactly equal to the expected value.
     *
     * @param type             Locator strategy (e.g., "id", "cssSelector")
     * @param value            The locator value
     * @param timeoutInSeconds Maximum time to wait for the element
     * @param expectedText     The expected exact text content
     */
    public static void assertTextEquals(String type, String value, int timeoutInSeconds, String expectedText) {
        String actualText = getText(type, value, timeoutInSeconds).trim();
        try {
            Assert.assertEquals(actualText, expectedText,
                "Text assertion failed. Expected: '" + expectedText + "' but found: '" + actualText + "'");
            System.out.println("Success");
        } catch (AssertionError e) {
            System.err.println("[ASSERTION FAILED] assertTextEquals");
            e.printStackTrace(); 
            throw e; 
        }
    }

    /**
     * Asserts that the element's text contains the expected substring.
     *
     * @param type                Locator strategy (e.g., "id", "cssSelector")
     * @param value               The locator value
     * @param timeoutInSeconds    Maximum time to wait for the element
     * @param expectedSubstring   Text expected to be present in the element
     */
    public static void assertTextContains(String type, String value, int timeoutInSeconds, String expectedSubstring) {
        String actualText = getText(type, value, timeoutInSeconds).trim();
        try {
            Assert.assertTrue(actualText.contains(expectedSubstring),
                "Text assertion failed. Expected to find: '" + expectedSubstring + "' in '" + actualText + "'");
            System.out.println("Success");
        } catch (AssertionError e) {
            System.err.println("[ASSERTION FAILED] assertTextContains");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Types a value into an input field and selects the nth suggestion
     * from a dropdown list (<ul>) with the specified class.
     *
     * @param inputType       Locator strategy for input (e.g., "id", "cssSelector")
     * @param inputValue      Locator value for the input element
     * @param textToType      The text to type into the input
     * @param dropdownClass   Class name of the suggestion <ul>
     * @param suggestionIndex Index (1-based) of the <li> to select from dropdown
     * @param timeoutSeconds  Timeout for waits
     */
    public static void typeAndSelectSuggestion(String inputType, String inputValue, String textToType,
                                               String dropdownClass, int suggestionIndex, int timeoutSeconds) {
    	
        sendKeys(inputType, inputValue, timeoutSeconds, textToType);
        By suggestionLocator = By.cssSelector("ul." + dropdownClass + " li:nth-child(" + suggestionIndex + ")");
        WebElement suggestionItem = getWait(timeoutSeconds)
                .until(ExpectedConditions.elementToBeClickable(suggestionLocator));
        suggestionItem.click();
    }
    
    /**
     * Overloaded version with default timeout.
     */
    public static void typeAndSelectSuggestion(String inputType, String inputValue, String textToType,
                                               String dropdownClass, int suggestionIndex) {
        typeAndSelectSuggestion(inputType, inputValue, textToType, dropdownClass, suggestionIndex, DEFAULT_TIMEOUT);
    }
    
    /**
     * Builds an absolute path for a file in resources/testdata.
     * @param fileName Name of the file (e.g., "case07_updated.xml")
     * @return Absolute path as String
     */
    public static String getTestDataFilePath(String fileName) {
        return Paths.get("resources", "testdata", fileName).toAbsolutePath().toString();
    }
    
    /**
     * Selects an option from a <select> dropdown using different strategies.
     *
     * @param inputType      Locator strategy (e.g., "id", "class", "cssSelector")
     * @param inputValue     The locator value
     * @param matchBy        Match strategy: "value", "text", "partialtext", "index"
     * @param matchValue     The value to match (based on matchBy)
     * @param timeoutSeconds Timeout in seconds to wait for the dropdown
     */
    public static void selectDropdownOption(String inputType, String inputValue, String matchBy,
                                            String matchValue, int timeoutSeconds) {
        By locator = buildLocator(inputType, inputValue);
        WebElement dropdownElement = getWait(timeoutSeconds)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        Select dropdown = new Select(dropdownElement);

        switch (matchBy.toLowerCase()) {
            case "value":
                dropdown.selectByValue(matchValue);
                break;

            case "text":
                dropdown.selectByVisibleText(matchValue);
                break;

            case "partialtext":
                boolean selected = false;
                for (WebElement option : dropdown.getOptions()) {
                    if (option.getText().toLowerCase().contains(matchValue.toLowerCase())) {
                        dropdown.selectByVisibleText(option.getText());
                        selected = true;
                        break;
                    }
                }
                if (!selected) {
                    throw new NoSuchElementException("No option containing text: " + matchValue);
                }
                break;

            case "index":
                dropdown.selectByIndex(Integer.parseInt(matchValue));
                break;

            default:
                throw new IllegalArgumentException("Invalid matchBy: " + matchBy);
        }
    }
    
    /**
     * Overloaded version of selectDropdownOption using default timeout.
     * */
    public static void selectDropdownOption(String inputType, String inputValue, String matchBy, String matchValue) {
        selectDropdownOption(inputType, inputValue, matchBy, matchValue, DEFAULT_TIMEOUT);
    }

    public static void sline(String msg) {
        sline(msg, 80, '.');
    }

    public static void sline(String msg, int width, char fill) {
        int msgLen = msg.length();
        int totalFill = width - msgLen;
        if (totalFill <= 0) {
            System.out.println(msg); 
            return;
        }
        int left = totalFill / 2;
        int right = totalFill - left;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(fill);
        sb.append(msg);
        for (int i = 0; i < right; i++) sb.append(fill);
        System.out.println(sb);
    }
    
    /**
     * Converts a relative file path to an absolute path.
     *
     * @param relativePath Relative path to the file (e.g., "resources/testdata/case07_updated.xml")
     * @return Absolute path to the file
     */
    public static String getAbsolutePath(String relativePath) {
        return new File(relativePath).getAbsolutePath();
    }
    /**
     * Parses an HTML table into a list of row data maps using header text as keys.
     *
     * @param tableLocator Locator for the <table> element
     * @return List of row maps (column header → cell value)
     */
    public static List<Map<String, String>> parseTableRows(By tableLocator) {
        List<Map<String, String>> rowDataList = new ArrayList<>();

        WebElement table = getWait(DEFAULT_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(tableLocator));

        // Get table headers
        List<WebElement> headers = table.findElements(By.cssSelector("thead th"));
        List<String> columnNames = new ArrayList<>();
        for (WebElement header : headers) {
            columnNames.add(header.getText().trim());
        }

        // Get table rows (excluding <thead>)
        List<WebElement> rows = table.findElements(By.cssSelector("tbody tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            Map<String, String> rowMap = new HashMap<>();

            for (int i = 0; i < cells.size(); i++) {
                String header = (i < columnNames.size()) ? columnNames.get(i) : "Column" + i;
                rowMap.put(header, cells.get(i).getText().trim());
            }

            rowDataList.add(rowMap);
        }

        return rowDataList;
    }
    
 // ---------------- Generic Selenium helpers ----------------

    public static WebElement waitVisible(WebDriver driver, By locator, int timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitClickable(WebDriver driver, WebElement el, int timeoutSec) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.elementToBeClickable(el));
    }

    public static void scrollIntoView(WebDriver driver, WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    public static void jsClick(WebDriver driver, WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    // ---------------- Small text helpers ----------------

    public static Integer safeInt(String s) {
        if (s == null) return null;
        try {
            String digits = s.replaceAll("[^0-9-]", "");
            if (digits.isEmpty()) return null;
            return Integer.valueOf(digits);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> parseColonSeparatedList(String s) {
        List<String> out = new ArrayList<>();
        if (s == null) return out;
        for (String part : s.split(":")) {
            String t = part.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    // ---------------- PlanningItem persistence (no JSON libs) ----------------

    private static String jsonEscape(String s) {
        if (s == null) return null;
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String q(String s) { return s == null ? "null" : ("\"" + jsonEscape(s) + "\""); }

    private static String jsonArrayOfStrings(List<String> items) {
        if (items == null || items.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(q(items.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    // Build JSON object string from model.PlanningItemRecord
    public static String toJson(model.PlanningItemRecord r) {
        return "{"
                + "\"documentIdentifier\":" + q(r.documentIdentifier) + ","
                + "\"planningItemName\":" + q(r.planningItemName) + ","
                + "\"totalLines\":" + (r.totalLines == null ? "null" : r.totalLines) + ","
                + "\"ordersCancelled\":" + (r.ordersCancelled == null ? "null" : r.ordersCancelled) + ","
                + "\"cancelledOrdersList\":" + jsonArrayOfStrings(r.cancelledOrdersList) + ","
                + "\"sourceFileName\":" + q(r.sourceFileName) + ","
                + "\"processDate\":" + q(r.processDate) + ","
                + "\"status\":" + q(r.status) + ","
                + "\"shipToCompany\":" + q(r.shipToCompany) + ","
                + "\"shipFromCompany\":" + q(r.shipFromCompany)
                + "}";
    }

    public static void appendToJsonArrayFile(String relativePath, String jsonObject) {
        try {
            Path path = Paths.get(relativePath);
            if (path.getParent() != null) Files.createDirectories(path.getParent());

            if (!Files.exists(path) || Files.size(path) == 0) {
                String init = "[\n  " + jsonObject + "\n]\n";
                Files.write(path, init.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                return;
            }

            String existing = new String(Files.readAllBytes(path), StandardCharsets.UTF_8).trim();
            if (existing.endsWith("]")) {
                String updated = existing.substring(0, existing.length() - 1)
                        + ",\n  " + jsonObject + "\n]";
                Files.write(path, updated.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            } else {
                String init = "[\n  " + jsonObject + "\n]\n";
                Files.write(path, init.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            }
        } catch (Exception e) {
            e.printStackTrace(); // or your ExceptionHandler
        }
    }

    // Convenience: convert IntegrationDetails → PlanningItemRecord
    public static model.PlanningItemRecord toPlanningItemRecord(model.IntegrationDetails d) {
        model.PlanningItemRecord r = new model.PlanningItemRecord();
        r.documentIdentifier  = d.documentIdentifier;
        r.planningItemName    = d.planningItemName;
        r.totalLines          = d.totalLines;
        r.ordersCancelled     = d.ordersCancelled;
        r.cancelledOrdersList = d.cancelledOrdersList;
        r.sourceFileName      = d.fileName;
        r.processDate         = d.processDate;
        r.status              = d.status;
        r.shipToCompany       = d.shipToCompany;
        r.shipFromCompany     = d.shipFromCompany;
        return r;
    }


}
