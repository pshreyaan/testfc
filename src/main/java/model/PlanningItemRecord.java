package model;

public class PlanningItemRecord {
  public String documentIdentifier;
  public String planningItemName;
  public Integer totalLines;
  public Integer ordersCancelled;
  public java.util.List<String> cancelledOrdersList;

  public String sourceFileName;
  public String processDate;
  public String status;
  public String shipToCompany;
  public String shipFromCompany;
}
