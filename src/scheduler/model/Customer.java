package scheduler.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
  private static int ID = 0;
  private final IntegerProperty customerID;
  private final StringProperty name;
  private final StringProperty address;
  private final StringProperty phone;

  /**
   * Constructor with some initial data.
   * 
   * @param name
   * @param address
   * @param phone
   */
  public Customer(String name, String address, String phone) {
      this.customerID = new SimpleIntegerProperty(generateID());
      this.name = new SimpleStringProperty(name);
      this.address = new SimpleStringProperty(address);
      this.phone = new SimpleStringProperty(phone);
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

  public void setAddress(String address){
    this.address.set(address);
  }

  public String getAddress(){
    return this.address.get();
  }
    
  public StringProperty addressProperty() {
    return address;
  }

  public void setPhone(String phone){
    this.phone.set(phone);
  }

  public String getPhone(){
    return this.phone.get();
  }
    
  public StringProperty phoneProperty() {
    return phone;
  }

  private int generateID(){
    return ID++;
  }
}
