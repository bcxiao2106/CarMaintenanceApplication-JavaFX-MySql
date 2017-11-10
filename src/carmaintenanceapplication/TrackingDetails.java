package carmaintenanceapplication;

import javafx.beans.property.*;

/**
 *
 * @author BICHENG XIAO
 */
public class TrackingDetails {
    private final IntegerProperty id;
    private final IntegerProperty serviceId;
    private final StringProperty serviceType;
    private final StringProperty description;
    private final StringProperty name;
    private final StringProperty street;
    private final StringProperty city;
    private final StringProperty state;
    private final StringProperty zipcode;
    private final StringProperty date;
    private final StringProperty cost;
    private final StringProperty comments;
    private final StringProperty parts;
    private final StringProperty warrantyUnits;
    private final StringProperty warrantyTime;
    private final BooleanProperty delete;
    
    public TrackingDetails(int id, int serviceId, String serviceType, String description, String name, String street, 
        String city, String state, String zipcode, String date, String cost, String comments, String parts, String warrantyUnits, String warrantyTime, boolean delete){
        this.id = new SimpleIntegerProperty(id);
        this.serviceId = new SimpleIntegerProperty(serviceId);
        this.serviceType = new SimpleStringProperty(serviceType);
        this.description = new SimpleStringProperty(description);
        this.name = new SimpleStringProperty(name);
        this.street = new SimpleStringProperty(street);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.zipcode = new SimpleStringProperty(zipcode);
        this.date = new SimpleStringProperty(date);
        this.cost = new SimpleStringProperty(cost);
        this.comments = new SimpleStringProperty(comments);
        this.parts = new SimpleStringProperty(parts);
        this.warrantyUnits = new SimpleStringProperty(warrantyUnits);
        this.warrantyTime = new SimpleStringProperty(warrantyTime);
        this.delete = new SimpleBooleanProperty(delete);
    }

    //getters
    public int getId(){
        return id.get();
    }

    public int getServiceId(){
        return serviceId.get();
    }
    
    public String getServiceType(){
        return serviceType.get();
    }

    public String getDescription(){
        return description.get();
    }

    public String getName(){
        return name.get();
    }

    public String getStreet(){
        return street.get();
    }

    public String getCity(){
        return city.get();
    }

    public String getState(){
        return state.get();
    }

    public String getZipcode(){
        return zipcode.get();
    }

    public String getDate(){
        return date.get();
    }

    public String getCost(){
        return cost.get();
    }

    public String getComments(){
        return comments.get();
    }
    
    public String getParts(){
        return parts.get();
    }
    
    public String getWarrantyUnits(){
        return warrantyUnits.get();
    }
    
    public String getWarrantyTime(){
        return warrantyTime.get();
    }
    
    public Boolean getDelete(){
        return delete.get();
    }

    //setters

    public void setId(int value){
        id.set(value);
    }

    public void setServiceId(int value){
        serviceId.set(value);
    }

    public void setServiceType(String value){
        serviceType.set(value);
    }
    
    public void setDescription(String value){
        description.set(value);
    }
    
    public void setName(String value){
        name.set(value);
    }

    public void setStreet(String value){
        street.set(value);
    }

    public void setCity(String value){
        city.set(value);
    }

    public void setState(String value){
        state.set(value);
    }

    public void setZipcode(String value){
        zipcode.set(value);
    }

    public void setDate(String value){
        date.set(value);
    }

    public void setCost(String value){
        cost.set(value);
    }

    public void setComments(String value){
        comments.set(value);
    }
    
    public void setParts(String value){
        parts.set(value);
    }
    
    public void setWarrantyUnits(String value){
        warrantyUnits.set(value);
    }
    
    public void setWarrantyTime(String value){
        warrantyTime.set(value);
    }
    
    public void setDelete(Boolean value){
        delete.set(value);
    }

    //property getters
    public IntegerProperty idProperty(){
        return id;
    }

    public IntegerProperty serviceIdProperty(){
        return serviceId;
    }
    
    public StringProperty serviceTypeProperty(){
        return serviceType;
    }
    
    public StringProperty descriptionProperty(){
        return description;
    }

    public StringProperty nameProperty(){
        return name;
    }

    public StringProperty streetProperty(){
        return street;
    }

    public StringProperty cityProperty(){
        return city;
    }

    public StringProperty stateProperty(){
        return state;
    }

    public StringProperty zipcodeProperty(){
        return zipcode;
    }

    public StringProperty dateProperty(){
        return date;
    }

    public StringProperty costProperty(){
        return cost;
    }

    public StringProperty commentsProperty(){
        return comments;
    }
    
    public StringProperty partsProperty(){
        return parts;
    }
    
    public StringProperty warrantyUnitsProperty(){
        return warrantyUnits;
    }
    
    public StringProperty warrantyTimeProperty(){
        return warrantyTime;
    }
    
    public BooleanProperty deleteProperty(){
        return delete;
    }

}