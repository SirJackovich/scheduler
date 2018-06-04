package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {
  private final IntegerProperty appointmentID;
  private final StringProperty start;
  private final StringProperty name;
  private final StringProperty type;
  private final IntegerProperty customerID;
  private final StringProperty customerName;
  private final IntegerProperty userID;
  private final StringProperty userName;

  /**
   * Constructor with some initial data.
   * 
   * @param appointmentID
   * @param start
   * @param name
   * @param type
   * @param customerID
   * @param customerName
   * @param userID
   * @param userName
   */
  public Appointment(Integer appointmentID, String start, String name, String type, Integer customerID, String customerName, Integer userID, String userName) {
      this.appointmentID = new SimpleIntegerProperty(appointmentID);
      this.start = new SimpleStringProperty(start);
      this.name = new SimpleStringProperty(name);
      this.type = new SimpleStringProperty(type);
      this.customerID = new SimpleIntegerProperty(customerID);
      this.customerName = new SimpleStringProperty(customerName);
      this.userID = new SimpleIntegerProperty(userID);
      this.userName = new SimpleStringProperty(userName);
  }
    
  public int getID(){
    return this.appointmentID.get();
  }

  public IntegerProperty IDProperty() {
    return appointmentID;
  }
  
  public void setStart(String start){
    this.start.set(start);
  }

  public String getStart(){
    return this.start.get();
  }
    
  public StringProperty startProperty() {
    return start;
  }

  public void setName(String name){
    this.name.set(name);
  }

  public String getName(){
    return this.name.get();
  }
    
  public StringProperty nameProperty() {
    return name;
  }

  public void setType(String type){
    this.type.set(type);
  }

  public String getType(){
    return this.type.get();
  }
    
  public StringProperty typeProperty() {
    return type;
  }

  public void setCustomerID(Integer customerID){
    this.customerID.set(customerID);
  }

  public Integer getCustomerID(){
    return this.customerID.get();
  }
    
  public IntegerProperty customerIDProperty() {
    return customerID;
  }
  
  public void setCustomerName(String customerName){
    this.customerName.set(customerName);
  }

  public String getCustomerName(){
    return this.customerName.get();
  }
    
  public StringProperty customerNameProperty() {
    return customerName;
  }
  
  public void setUserID(Integer userID){
    this.userID.set(userID);
  }

  public Integer getUserID(){
    return this.userID.get();
  }
    
  public IntegerProperty userIDProperty() {
    return userID;
  }
  
  public void setUserName(String userName){
    this.userName.set(userName);
  }

  public String getUserName(){
    return this.userName.get();
  }
    
  public StringProperty userNameProperty() {
    return userName;
  }
}
