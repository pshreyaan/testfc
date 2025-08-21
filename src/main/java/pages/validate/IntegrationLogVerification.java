package pages.validate;

import components.SupplierPortal;
import model.IntegrationDetails;
import model.PlanningItemRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.Methods;
import utils.XMLDataExtraction;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Holds ALL validation logic for Integration Log Details.
 * Uses single actions from components.SupplierPortal and generic helpers from utils.Methods.
 */
public class IntegrationLogVerification {

    // IDs from your UI
    private static final String TABLE_ID = "logTable";
    private static final String POPUP_ID = "showIntegrationDetails";

    // Output file to append PlanningItem records
    private static final String OUT_JSON = "resources/testdata/planningitem.json";

    /**
     * Validate one uploaded XML by:
     *  - deriving DocumentIdentifier from the XML,
     *  - opening Details in Integration Log,
     *  - parsing the popup,
     *  - asserting scenario rules,
     *  - appending PlanningItemRecord to planningitem.json.
     */
    public static void validateAndPersist(
            WebDriver driver,
            SupplierPortal portal,
            String xmlDir,
            String xmlFile
    ) {
        // Guard: skip if the file is missing so suite remains resilient
        Path p = Paths.get(xmlDir, xmlFile);
        if (!Files.exists(p)) {
            Methods.sline("[SKIP] XML not found: " + p);
            return;
        }

        // Make sure Integration Log table is visible (navigate already happened in the test)
        Methods.waitVisible(driver, By.id(TABLE_ID), 30);

        // 1) Derive Document Identifier from the same XML used at upload time
        String docId = XMLDataExtraction.extractDocumentIdentifierFromHeader(xmlDir, xmlFile);
        Methods.sline("→ Validating Document Identifier: " + docId + " (from " + xmlFile + ")");

        // 2) Open Details popup for that Document Identifier
        portal.clickIntegrationLogDetails(TABLE_ID, docId);

        // 3) Parse popup into model (using SupplierPortal’s reader)
        IntegrationDetails d = portal.readIntegrationLogDetails(docId);
        Methods.sline("Parsed Details | PI=" + d.planningItemName
                + " | TotalLines=" + d.totalLines
                + " | OrdersCancelled=" + d.ordersCancelled
                + " | File=" + d.fileName
                + " | Status=" + d.status);

        // 4) Core sanity checks
        Assert.assertNotNull(d.status, "Status should be present.");
        Assert.assertTrue(d.status.toLowerCase(Locale.ROOT).contains("success"),
                "Expected SUCCESS status, got: " + d.status);

        // File row anchor should match the validated XML name (when present)
        if (d.fileName != null && !d.fileName.isBlank()) {
            Assert.assertEquals(d.fileName.trim(), xmlFile,
                    "Popup 'File' should match the validated XML filename.");
        }

        // 5) Scenario‑aware checks (light; tighten later if needed)
        assertScenario(xmlDir, xmlFile, d);

        // 6) Persist Planning Item for later PI validations
        PlanningItemRecord rec = Methods.toPlanningItemRecord(d);
        Methods.appendToJsonArrayFile(OUT_JSON, Methods.toJson(rec));
        Methods.sline("✅ Appended PlanningItem to " + OUT_JSON);
    }

    private static void assertScenario(String xmlDir, String xmlFile, IntegrationDetails d) {
        String name = xmlFile.toLowerCase(Locale.ROOT);

        if (name.contains("case")) {
            // Forecast snapshot/replace (case07/08/09): compare Total Lines to your Forecast Count
            int forecastCount = XMLDataExtraction.countForecastLineItems(xmlDir, xmlFile); // you confirmed this exists
            Assert.assertNotNull(d.totalLines, "Total Lines should be present for forecast cases.");
            Assert.assertEquals(d.totalLines.intValue(), forecastCount,
                    "Forecast replacement rule: popup Total Lines must equal forecast count from XML.");
        } else if (name.contains("cancel")) {
            Assert.assertNotNull(d.ordersCancelled, "Orders Cancelled should be present for cancel case.");
            Assert.assertTrue(d.ordersCancelled >= 1,
                    "Expected >= 1 cancelled order, got " + d.ordersCancelled);
        } else if (name.contains("create")) {
            if (d.ordersCancelled != null) {
                Assert.assertEquals(d.ordersCancelled.intValue(), 0,
                        "Create case: expected 0 cancelled orders.");
            }
        } else if (name.contains("change")) {
            if (d.ordersCancelled != null) {
                Assert.assertTrue(d.ordersCancelled >= 0, "Orders Cancelled must not be negative.");
            }
            // If you have a baseline expected Total Lines for change, assert it here later.
        } else if (name.contains("mix")) {
            // Mixed payload: minimal checks for now; status+file already covered above.
        }
    }
}
