package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
  private final IntegerProperty customerID;
  private final StringProperty name;
  private final IntegerProperty addressID;
  // private final StringProperty phone;

  /**
   * Constructor with some initial data.
   * 
   * @param customerID
   * @param name
   * @param addressID
   * 
   */
  public Customer(Integer customerID, String name, Integer addressID) {
      this.customerID = new SimpleIntegerProperty(customerID);
      this.name = new SimpleStringProperty(name);
      this.addressID = new SimpleIntegerProperty(addressID);
      // this.phone = new SimpleStringProperty(phone);
  }
    
  public int getID(){
    return this.customerID.get();
  }

  public IntegerProperty IDProperty() {
    return customerID;
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

  public void setAddressID(Integer addressID){
    this.addressID.set(addressID);
  }

  public Integer getAddressID(){
    return this.addressID.get();
  }
    
  public IntegerProperty addressIDProperty() {
    return addressID;
  }

//  public void setPhone(String phone){
//    this.phone.set(phone);
//  }
//
//  public String getPhone(){
//    return this.phone.get();
//  }
//    
//  public StringProperty phoneProperty() {
//    return phone;
//  }
}
