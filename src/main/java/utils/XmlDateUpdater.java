package utils;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class XmlDateUpdater {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Updates forecast dates in the given XML file and writes output to resources/testdata/{fileName}_updated.xml.
     * @param fileName the input file name, e.g. "case07.xml"
     * @return the output file path (for upload usage)
     */
    public static String updateForecastDates(String fileName) {
        try {
            String inputPath = "resources/xml/" + fileName;

            String fileBaseName = fileName.contains(".")
                    ? fileName.substring(0, fileName.lastIndexOf('.'))
                    : fileName;
            String outputFileName = fileBaseName + "_updated.xml";
            String outputPath = "resources/testdata/" + outputFileName;

            File inputFile = new File(inputPath);
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();

            removeEmptyTextNodes(doc.getDocumentElement());

            NodeList dateTimeRanges = doc.getElementsByTagName("DateTimeRange");
            for (int i = 0; i < dateTimeRanges.getLength(); i++) {
                Node node = dateTimeRanges.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element dateRange = (Element) node;
                    String newDate = LocalDate.now().plusDays(i).format(formatter);
                    dateRange.getElementsByTagName("FromDateTime").item(0).setTextContent(newDate);
                    dateRange.getElementsByTagName("ToDateTime").item(0).setTextContent(newDate);
                }
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outputFile);
            transformer.transform(source, result);

            Methods.sline("Updated XML written to: " + outputPath);
            return outputPath;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to process XML: " + fileName, e);
        }
    }

    private static void removeEmptyTextNodes(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.hasChildNodes()) {
                removeEmptyTextNodes(child);
            }
        }
    }
    
    public static void main(String args[]) {
    	updateForecastDates("case07.xml");
    }
}
