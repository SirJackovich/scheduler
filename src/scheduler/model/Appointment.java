package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {
  private final IntegerProperty appointmentID;
  private final StringProperty start;
  private final StringProperty title;
  private final StringProperty type;
  private final IntegerProperty customerID;
  private final IntegerProperty userID;
  private final StringProperty description;
  private final StringProperty location;
  private final StringProperty contact;
  private final StringProperty URL;
  private final StringProperty end;

  /**
   * Constructor with some initial data.
   * 
   * @param appointmentID
   * @param start
   * @param title
   * @param type
   * @param customerID
   * @param userID
   * @param description
   * @param location
   * @param contact
   * @param URL
   * @param end
   */
  public Appointment(
      Integer appointmentID, 
      String start, 
      String title, 
      String type, 
      Integer customerID, 
      Integer userID, 
      String description,
      String location,
      String contact,
      String URL,
      String end) {
    this.appointmentID = new SimpleIntegerProperty(appointmentID);
    this.start = new SimpleStringProperty(start);
    this.title = new SimpleStringProperty(title);
    this.type = new SimpleStringProperty(type);
    this.customerID = new SimpleIntegerProperty(customerID);
    this.userID = new SimpleIntegerProperty(userID);
    this.description = new SimpleStringProperty(description);
    this.location = new SimpleStringProperty(location);
    this.contact = new SimpleStringProperty(contact);
    this.URL = new SimpleStringProperty(URL);
    this.end = new SimpleStringProperty(end);
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

  public void setTitle(String title){
    this.title.set(title);
  }

  public String getTitle(){
    return this.title.get();
  }
    
  public StringProperty titleProperty() {
    return title;
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
  
  public void setUserID(Integer userID){
    this.userID.set(userID);
  }

  public Integer getUserID(){
    return this.userID.get();
  }
    
  public IntegerProperty userIDProperty() {
    return userID;
  }
  
  public void setDescription(String description){
    this.description.set(description);
  }

  public String getDescription(){
    return this.description.get();
  }
    
  public StringProperty descriptionProperty() {
    return description;
  }
  
  public void setLocation(String location){
    this.location.set(location);
  }

  public String getLocation(){
    return this.location.get();
  }
    
  public StringProperty locationProperty() {
    return location;
  }
  
  public void setContact(String contact){
    this.contact.set(contact);
  }

  public String getContact(){
    return this.contact.get();
  }
    
  public StringProperty contactProperty() {
    return contact;
  }
  
  public void setURL(String URL){
    this.URL.set(URL);
  }

  public String getURL(){
    return this.URL.get();
  }
    
  public StringProperty URLProperty() {
    return URL;
  }
  
  public void setEnd(String end){
    this.end.set(end);
  }

  public String getEnd(){
    return this.end.get();
  }
    
  public StringProperty endProperty() {
    return end;
  }
}
