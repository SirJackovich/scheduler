package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Appointment {
  private static int ID = 0;
  private final IntegerProperty appointmentID;
  private final StringProperty name;
  private final StringProperty type;
  private final IntegerProperty customerID;

  /**
   * Constructor with some initial data.
   * 
   * @param name
   * @param type
   * @param customerID
   */
  public Appointment(String name, String type, Integer customerID) {
      this.appointmentID = new SimpleIntegerProperty(generateID());
      this.name = new SimpleStringProperty(name);
      this.type = new SimpleStringProperty(type);
      this.customerID = new SimpleIntegerProperty(customerID);
  }
    
  public int getID(){
    return this.appointmentID.get();
  }

  public IntegerProperty IDProperty() {
    return appointmentID;
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

  private int generateID(){
    return ID++;
  }
}
