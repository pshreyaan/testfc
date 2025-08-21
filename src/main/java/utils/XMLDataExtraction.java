package utils;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLDataExtraction {

	/**
	 * Extracts the buyer information from the <From> section.
	 *
	 * @param folder   Either "xml" or "testdata"
	 * @param fileName The name of the XML file (e.g., "case07.xml")
	 */
	public static void extractBuyerInfo(String folder, String fileName) {
		try {
			String filePath = resolvePath(folder, fileName);
			Document doc = loadXmlDocument(filePath);
			Node buyerNode = doc.getElementsByTagName("From").item(0);
			printPartnerInfo(buyerNode, "Buyer");
		} catch (Exception e) {
			System.err.println("Error extracting buyer info:");
			e.printStackTrace();
		}
	}

	/**
	 * Extracts the seller information from the <To> section.
	 *
	 * @param folder   Either "xml" or "testdata"
	 * @param fileName The name of the XML file (e.g., "case07.xml")
	 */
	public static void extractSellerInfo(String folder, String fileName) {
		try {
			String filePath = resolvePath(folder, fileName);
			Document doc = loadXmlDocument(filePath);
			Node sellerNode = doc.getElementsByTagName("To").item(0);
			printPartnerInfo(sellerNode, "Seller");
		} catch (Exception e) {
			System.err.println("Error extracting seller info:");
			e.printStackTrace();
		}
	}

	/**
	 * Resolves the actual file path based on folder keyword ("xml" or "testdata").
	 */
	private static String resolvePath(String folderKeyword, String fileName) throws Exception {
		String baseFolder;
		switch (folderKeyword.toLowerCase()) {
		case "xml":
			baseFolder = "resources/xml/";
			break;
		case "testdata":
			baseFolder = "resources/testdata/";
			break;
		default:
			throw new IllegalArgumentException("Unknown folder keyword: " + folderKeyword);
		}

		File file = new File(baseFolder + fileName);
		if (!file.exists()) {
			throw new Exception("File not found: " + file.getPath());
		}

		return file.getPath();
	}

	/**
	 * Loads and parses the XML file.
	 */
	private static Document loadXmlDocument(String path) throws Exception {
		File xmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(xmlFile);
	}

	/**
	 * Prints partner information from the node.
	 */
	private static void printPartnerInfo(Node node, String label) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			Element info = (Element) element.getElementsByTagName("RNetPartnerInformation").item(0);

			String name = info.getElementsByTagName("RNetPartnerName").item(0).getTextContent();
			String identifier = info.getElementsByTagName("RNetPartnerIdentifier").item(0).getTextContent();

			System.out.println("[" + label + " Info]");
			System.out.println("Name: " + name);
			System.out.println("Identifier: " + identifier);
		}
	}

	/**
	 * Extracts the <DocumentIdentifier> from <RNetHeader><ThisDocumentIdentifier>.
	 *
	 * @param folder   Either "xml" or "testdata"
	 * @param fileName Name of the XML file
	 */
	public static void extractDocumentIdentifierFromHeader(String folder, String fileName) {
		try {
			String filePath = resolvePath(folder, fileName);
			Document doc = loadXmlDocument(filePath);

			NodeList headerList = doc.getElementsByTagName("RNetHeader");
			if (headerList.getLength() > 0) {
				Element headerElement = (Element) headerList.item(0);
				Element thisDocId = (Element) headerElement.getElementsByTagName("ThisDocumentIdentifier").item(0);
				String identifier = thisDocId.getElementsByTagName("DocumentIdentifier").item(0).getTextContent();
				System.out.println("[Document Identifier from Header]");
				System.out.println("Identifier: " + identifier);
			} else {
				System.out.println("RNetHeader not found in the document.");
			}
		} catch (Exception e) {
			System.err.println("Error extracting DocumentIdentifier from RNetHeader:");
			e.printStackTrace();
		}
	}

	/**
	 * Counts the number of <Forecast> elements under the main forecast line item.
	 *
	 * @param folder   Either "xml" or "testdata"
	 * @param fileName Name of the XML file
	 */
	public static void countForecastLineItems(String folder, String fileName) {
		try {
			String filePath = resolvePath(folder, fileName);
			Document doc = loadXmlDocument(filePath);

			NodeList forecastList = doc.getElementsByTagName("RNetDemandForecastLineItem");

			int totalForecasts = 0;
			for (int i = 0; i < forecastList.getLength(); i++) {
				Element lineItem = (Element) forecastList.item(i);
				NodeList forecasts = lineItem.getElementsByTagName("Forecast");
				totalForecasts += forecasts.getLength();
			}

			System.out.println("[Forecast Count]");
			System.out.println("Total Forecast entries: " + totalForecasts);
		} catch (Exception e) {
			System.err.println("Error counting Forecast entries:");
			e.printStackTrace();
		}
	}

	/**
	 * Extracts the PO number from
	 * <ReferenceInformation ReferenceType="PurchaseOrderNumber">.
	 *
	 * @param folder   Either "xml" or "testdata"
	 * @param fileName XML file name (e.g., "case07.xml")
	 */
	/**
	 * @param folder
	 * @param fileName
	 */
	/**
	 * @param folder
	 * @param fileName
	 */
	public static void extractPurchaseOrderNumber(String folder, String fileName) {
		try {
			String filePath = resolvePath(folder, fileName);
			Document doc = loadXmlDocument(filePath);

			NodeList referenceInfoList = doc.getElementsByTagName("ReferenceInformation");

			for (int i = 0; i < referenceInfoList.getLength(); i++) {
				Element referenceElement = (Element) referenceInfoList.item(i);
				String refType = referenceElement.getAttribute("ReferenceType");

				if ("PurchaseOrderNumber".equals(refType)) {
					Element docRef = (Element) referenceElement.getElementsByTagName("DocumentReference").item(0);
					String documentId = docRef.getElementsByTagName("DocumentIdentifier").item(0).getTextContent();
					System.out.println("[PO Number]");
					System.out.println("DocumentIdentifier: " + documentId);
					return; // stop after first match
				}
			}

			System.out.println("No PurchaseOrderNumber found.");
		} catch (Exception e) {
			System.err.println("Error extracting PurchaseOrderNumber:");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		extractBuyerInfo("xml", "case07.xml");
		extractSellerInfo("xml", "case07.xml");
		extractDocumentIdentifierFromHeader("xml", "case07.xml");
		countForecastLineItems("xml", "case07.xml");
		extractPurchaseOrderNumber("xml", "case07.xml");

	}
}
