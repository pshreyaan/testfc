package model;

public class IntegrationDetails {
  public String documentIdentifier;

  // Header rows
  public String processDate;      // e.g., "8/21/25 4:50:02 AM IST"
  public String direction;        // "Inbound / IF"
  public String docType;          // "Demand Forecast / RNetDemandForecast"
  public String status;           // "Success / SUCCESS (10)"
  public String shipToCompany;    // "Goodyear / 30"
  public String shipFromCompany;  // "Orion Engineered Carbons / 20"
  public String fileName;         // anchor text in 'File' row

  // From “Details:”
  public String planningItemName; // "FC01004467924-83062D"
  public Integer totalLines;      // 8
  public Integer ordersCancelled; // 5
  public java.util.List<String> cancelledOrdersList = new java.util.ArrayList<>();
}
