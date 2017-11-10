/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmaintenanceapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.util.converter.DefaultStringConverter;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.scene.control.TableCell;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import java.util.Optional;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author BICHENG XIAO
 */
public class FXMLCarMaintenanceController implements Initializable {

    @FXML
    private TableView<TrackingDetails> carmTableView;
    @FXML
    private Button btnLoad;
    @FXML
    private TableColumn<TrackingDetails, String> columnDescription;
    @FXML
    private TableColumn<TrackingDetails, String> columnName;
    @FXML
    private TableColumn<TrackingDetails, String> columnStreet;
    @FXML
    private TableColumn<TrackingDetails, String> columnCity;
    @FXML
    private TableColumn<TrackingDetails, String> columnState;
    @FXML
    private TableColumn<TrackingDetails, String> columnZipcode;
    @FXML
    private TableColumn<TrackingDetails, String> columnDate;
    @FXML
    private TableColumn<TrackingDetails, String> columnCost;
     @FXML
    private TableColumn<TrackingDetails, String> columnParts;
    @FXML
    private TableColumn<TrackingDetails, String> columnWarrantyUnits;
    @FXML
    private TableColumn<TrackingDetails, String> columnWarrantyTime;
    @FXML
    private TableColumn<TrackingDetails, String> columnComments;
    @FXML
    private TextField userInputTextField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button btnExit;
    @FXML
    private ToolBar toolBarTop;
    @FXML
    private ComboBox<String> descriptionComboBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField stateTextField;
    @FXML
    private TextField zipcodeTextField;
    @FXML
    private DatePicker dateTextField;
    @FXML
    private TextField costTextField;
    @FXML
    private TableColumn<TrackingDetails, Boolean> columnDelete;
    
    //Initialize observable list to hold out database data
    private ObservableList<TrackingDetails> data;
    private DBConnection dc;
    private String [] serviceTypeArray;
    private String [] maintenanceTypeArray;
    private String [] repairTypeArray;
    private HashMap<String,Integer> serviceTypeMapM;
    private HashMap<String,Integer> serviceTypeMapR;
    private Image logoImage;
    @FXML
    private Button btnAddNew;
    @FXML
    private ComboBox<String> serviceTypeComboBox;
    @FXML
    private ImageView logoImgView;
    @FXML
    private TextField commentsTextField;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        dc = new DBConnection();
        
        //load data from Database
        loadDataFromDatabase();
        
        //bind options to ComboBox - Service Type
        ObservableList<String> options = FXCollections.observableArrayList(serviceTypeArray);
        descriptionComboBox.setItems(options);
    }    

    @FXML
    private void loadDataFromDatabase() {
        ResultSet rs = null;
        int serviceTypeCount = 0;
        String inputSearchTxt = "";
        String inputServiceType = "";
        String inputStartDate = "";
        String inputEndDate = "";
        String maintenanceTypeStr = "";
        String repairTypeStr = "";
        
        try {
            Connection conn = dc.connectToMysql();
            //get quantity of Maintenance Types
            String sql = "SELECT COUNT(U.service_type) FROM " +
"(SELECT 'M' as service_type, M.maintenance_id, M.description FROM MaintenanceTypes M UNION ALL SELECT 'R' as service_type, R.repair_id, R.description FROM RepairTypes R) U;";
            System.out.println(sql);
            rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                serviceTypeCount = rs.getInt(1);
            }
            serviceTypeArray = new String[serviceTypeCount+2];
            serviceTypeMapM = new HashMap<String,Integer>();
            serviceTypeMapR = new HashMap<String,Integer>();
            
            boolean isMaintenanceTagAdded = false;
            boolean isRepairTagAdded = false;
            
            //get all Service Types (Maintenance + Repair), store in an String Array
            sql = "SELECT 'M' as service_type, M.maintenance_id, M.description FROM MaintenanceTypes M UNION ALL SELECT 'R' as service_type, R.repair_id, R.description FROM RepairTypes R;";
            System.out.println(sql);
            rs = conn.createStatement().executeQuery(sql);
            serviceTypeCount = 0;
            
            while (rs.next()) {
                if(rs.getString(1).equals("M") && !isMaintenanceTagAdded){
                    serviceTypeArray[serviceTypeCount] = "**** 1 - MAINTENANCE SERVICES ****";
                    isMaintenanceTagAdded = true;
                    serviceTypeCount++;
                }
                
                if(rs.getString(1).equals("R") && !isRepairTagAdded){
                    serviceTypeArray[serviceTypeCount] = "**** 2 - REPAIR SERVICES ****";
                    isRepairTagAdded = true;
                    serviceTypeCount++;
                }
                
                serviceTypeArray[serviceTypeCount] = rs.getString(3);
                serviceTypeCount++;
                if(rs.getString(1).equals("M")){
                    maintenanceTypeStr += rs.getString(3) + "%";
                    serviceTypeMapM.put(rs.getString(3), rs.getInt(2));
                } else {
                    repairTypeStr += rs.getString(3) + "%";
                    serviceTypeMapR.put(rs.getString(3), rs.getInt(2));
                    
                }
            }
            
            maintenanceTypeArray = maintenanceTypeStr.split("%");
            repairTypeArray = repairTypeStr.split("%");
            
            //get DataSet of table view
            sql = "SELECT * FROM " +
"(SELECT M1.id, M1.service_id, M1.service_type, M2.description, M1.name, M1.street, M1.city, M1.state, M1.zipcode, LEFT(M1.date,10) as date, M1.cost, M1.comments, 'NA' as parts, 'NA' as warranty_units, 'NA' as warranty_time FROM MaintenanceAndRepairs M1, MaintenanceTypes M2 WHERE M1.service_id = M2.maintenance_id AND M1.service_type='M' UNION ALL " +
"SELECT M3.id, M3.service_id, M3.service_type, M4.description, M3.name, M3.street, M3.city, M3.state, M3.zipcode, LEFT(M3.date,10) as date, M3.cost, M3.comments, M4.parts, M4.warranty_units, M4.warranty_time FROM MaintenanceAndRepairs M3, RepairTypes M4 WHERE M3.service_id = M4.repair_id AND M3.service_type='R') U WHERE U.name <> '' ";
            
            //Add query condition: Name and Street
            if(!userInputTextField.getText().trim().equals("")){
                inputSearchTxt = userInputTextField.getText().trim();
                sql += "AND (name = '" + inputSearchTxt + "' OR name LIKE '%"+ inputSearchTxt +"%' ";
                sql += "OR street = '" + inputSearchTxt + "' OR street LIKE '%"+ inputSearchTxt +"%') ";
            }
            
            //Add query condition: Service type
            if(serviceTypeComboBox.getValue() != null){
                inputServiceType = serviceTypeComboBox.getValue();
                if(inputServiceType.equals("Maintenance")){
                    sql += "AND service_type = 'M' ";
                } else if(inputServiceType.equals("Repair")){
                    sql += "AND service_type = 'R' ";
                }
            }
            
            //Add query condition: Start Date
            if(startDatePicker.getValue() != null){
                inputStartDate = startDatePicker.getValue().toString();
                sql += "AND date >= '" + inputStartDate + "' ";
            }
            
            //Add query condition: End Date
            if(endDatePicker.getValue() != null){
                inputEndDate = endDatePicker.getValue().toString();
                sql += "AND date <= '" + inputEndDate + "' ";
            }
            
            
            data = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            sql += ";";
            System.out.println(sql);
            rs = conn.createStatement().executeQuery(sql);
            
            while (rs.next()) {
                //get data from ResultSet
                data.add(new TrackingDetails(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
                                                rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), 
                                                rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), false));
            }

        } catch (SQLException ex) {
            System.err.println("Error"+ex);
        }
        
        //Set cell value factory to tableview.  
        //NB.PropertyValue Factory must be the same with the one set in model class.
        columnDescription.setCellFactory(new PropertyValueFactory<>("description"));
        columnDescription.setCellValueFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), FXCollections.observableArrayList(serviceTypeArray)));
        columnDescription.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>(){
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setDescription(t.getNewValue());
                    String newValue = t.getNewValue();
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    
                    String newServiceType =serviceTypeMapM.containsKey(newValue)?"M":"R";
                    String oldServiceType = ((TrackingDetails) t.getTableView().getItems().get(t.getTablePosition().getRow())).getServiceType();
                    int newServiceId = serviceTypeMapM.containsKey(newValue)?serviceTypeMapM.get(newValue):serviceTypeMapR.get(newValue);
                    
                    if(newServiceType.equals(oldServiceType)){
                        updateColumn(" M.service_id = "+ newServiceId +" WHERE M.id = "+ id);                        
                        //updateColumn(" M.service_id = "+ newServiceId +", M.service_type = '" + newServiceType + "' WHERE M.id = "+ id);
                    } else {
                        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                        _alert.setTitle("Editing Alert");
                        _alert.setHeaderText("");
                        _alert.setContentText("You are not allowed to change the service type between [Maintenance] and [Repair]");
                        _alert.show();      
                    }
                }
        });
        
        columnName.setCellFactory(new PropertyValueFactory<>("name"));
        columnName.setCellValueFactory(TextFieldTableCell.forTableColumn());
        columnName.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>() {
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){
                    String newValue = t.getNewValue();
                    if(newValue.equals("")){
                        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                        _alert.setTitle("Editing Alert");
                        _alert.setHeaderText("");
                        _alert.setContentText("The value can not be null");
                        _alert.show();
                        
                        loadDataFromDatabase();
                        return;
                    }
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    updateColumn("M.name = \""+ newValue +"\" WHERE M.id = "+ id);
                }
            }
        );
        
        columnStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        columnStreet.setCellFactory(TextFieldTableCell.forTableColumn());
        columnStreet.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>() {
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){
                    String newValue = t.getNewValue();
                    
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setStreet(t.getNewValue());
                    
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    updateColumn("M.street = \""+ newValue +"\" WHERE M.id = "+ id);
                }
            }
        );
        
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        columnCity.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCity.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>() {
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){
                    String newValue = t.getNewValue();
                    
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setCity(t.getNewValue());
                    
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    updateColumn("M.city = \""+ newValue +"\" WHERE M.id = "+ id);
                }
            }
        );
        
        columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
        columnState.setCellFactory(TextFieldTableCell.forTableColumn());
        columnState.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>() {
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){
                    String newValue = t.getNewValue();
                    
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setState(t.getNewValue());
                    
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    updateColumn("M.state = \""+ newValue +"\" WHERE M.id = "+ id);
                }
            }
        );
        
        columnZipcode.setCellValueFactory(new PropertyValueFactory<>("zipcode"));
        columnZipcode.setCellFactory(TextFieldTableCell.forTableColumn());
        columnZipcode.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>() {
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){
                    String newValue = t.getNewValue();
                    
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setZipcode(t.getNewValue());
                    
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    updateColumn("M.zipcode = \""+ newValue +"\" WHERE M.id = "+ id);
                }
            }
        );
        
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnDate.setCellFactory(TextFieldTableCell.forTableColumn());
        /*columnDate.setCellFactory(new Callback<TableColumn<TrackingDetails, String>, TableCell<TrackingDetails, String>>() {
            @Override
            public TableCell<TrackingDetails, String> call(TableColumn<TrackingDetails, String> p) {
                return new DatePickerCell();
            }
        });*/
        columnDate.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>() {
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){    
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setDate(t.getNewValue());
                    String newValue = t.getNewValue();
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    updateColumn("M.date = \""+ newValue +"\" WHERE M.id = "+ id);
                }
            }
        );
       
        columnCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        columnCost.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCost.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>() {
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){
                    
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setCost(t.getNewValue());
                    String newValue = t.getNewValue();
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    updateColumn("M.cost = "+ newValue +" WHERE M.id = "+ id);
                }
            }
        );
       
        columnComments.setCellValueFactory(new PropertyValueFactory<>("comments"));
        columnComments.setCellFactory(TextFieldTableCell.forTableColumn());
        columnComments.setOnEditCommit(
            new EventHandler<CellEditEvent<TrackingDetails, String>>() {
                @Override
                public void handle(CellEditEvent<TrackingDetails, String> t){
                    
                    ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setComments(t.getNewValue());
                    String newValue = t.getNewValue();
                    int id = ((TrackingDetails) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).getId();
                    updateColumn("M.comments = \""+ newValue +"\" WHERE M.id = "+ id);
                }
            }
        );
        
        columnParts.setCellValueFactory(new PropertyValueFactory<>("parts"));
        columnWarrantyUnits.setCellValueFactory(new PropertyValueFactory<>("warrantyUnits"));
        columnWarrantyTime.setCellValueFactory(new PropertyValueFactory<>("warrantyTime"));
        
        
        //Add "DELETE" button into the TableView
        columnDelete.setSortable(false);
        columnDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TrackingDetails, Boolean>,ObservableValue<Boolean>>() {
		@Override
		public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TrackingDetails, Boolean> p) {
                    return new SimpleBooleanProperty(p.getValue() != null);
		}
        }); 
	columnDelete.setCellFactory(new Callback<TableColumn<TrackingDetails, Boolean>, TableCell<TrackingDetails, Boolean>>() {
            @Override
            public TableCell<TrackingDetails, Boolean> call(TableColumn<TrackingDetails, Boolean> p) {
		return new ButtonCell(carmTableView);
            }
	});
        
        carmTableView.setItems(null);
        carmTableView.setEditable(true);
        carmTableView.setItems(data);      
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }
    
    //Execute "update" sql
    private void updateColumn(String sqlStr){
        String sql = "UPDATE MaintenanceAndRepairs M SET "+ sqlStr +";";
        System.out.println(sql);
        try{
            Connection conn = dc.connectToMysql();
            conn.createStatement().execute(sql);
        }catch (SQLException ex) {
            System.err.println("Error"+ex);
        }
    }
    
    //Execute "delete" sql
    private boolean deleteRow(int id){
        String sql = "DELETE FROM MaintenanceAndRepairs WHERE id = " + id +";";
        System.out.println(sql);
        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION);
        _alert.setTitle("Confirmation Alert");
        _alert.setHeaderText("");
        _alert.setContentText("Are you sure to delete this record?");
        Optional<ButtonType> result = _alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                Connection conn = dc.connectToMysql();
                conn.createStatement().execute(sql);
                return true;
            }catch (SQLException ex) {
                System.err.println("Error"+ex);
            }
        }
        return false;
    }
    
    //Execute "delete" sql
    private boolean directSqlExecute(String sql){
            try{
                Connection conn = dc.connectToMysql();
                conn.createStatement().execute(sql);
                return true;
            }catch (SQLException ex) {
                Alert _alert = new Alert(Alert.AlertType.ERROR);
                        _alert.setTitle("SQL Execution Error");
                        _alert.setHeaderText("");
                        _alert.setContentText("SQL Execution Error: \n - "+ex);
                        _alert.show();
                System.err.println("Error"+ex);
                return false;
            }
    }

    @FXML
    private void quitApplication(ActionEvent event) {
        Alert _alert = new Alert(Alert.AlertType.CONFIRMATION);
        _alert.setTitle("Quit Alert");
        _alert.setHeaderText("");
        _alert.setContentText("Are you sure to quit?");
        Optional<ButtonType> result = _alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.exit(0);
        }   
    }

    @FXML
    private void addNewRecord(ActionEvent event) {
        String name = null;
        String street = null;
        String city = null;
        String state = null;
        String zipcode = null;
        String date = null;
        String cost = null;
        String description = null;
        String serviceId = null;
        String serviceType = null;
        String comments = null;
        if(descriptionComboBox.getValue() == null ||
                descriptionComboBox.getValue().toString().equals("**** 1 - MAINTENANCE SERVICES ****") || 
                descriptionComboBox.getValue().toString().equals("**** 2 - REPAIR SERVICES ****") ||
                nameTextField.getText().isEmpty() ||
                dateTextField.getValue() == null ||
                costTextField.getText().isEmpty() ){
            Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                        _alert.setTitle("Adding Alert");
                        _alert.setHeaderText("");
                        _alert.setContentText("The * marked items can not be null");
                        _alert.show();
        } else {
            name = nameTextField.getText();
            street = streetTextField.getText();
            city = cityTextField.getText();
            state = stateTextField.getText();
            zipcode = zipcodeTextField.getText();
            date = dateTextField.getValue().toString();
            cost = costTextField.getText();
            description = descriptionComboBox.getValue().toString();
            if(serviceTypeMapM.containsKey(description)){
                serviceId = serviceTypeMapM.get(description).toString();
                serviceType = "M";
            } else {
                serviceId = serviceTypeMapR.get(description).toString();
                serviceType = "R";
            }
            comments = commentsTextField.getText();
            String sqlStr = "INSERT INTO MaintenanceAndRepairs (name, street, city, state, zipcode, date, cost, service_id, service_type, comments) "
                    + "VALUES ('"+name+"', '"+street+"', '"+city+"', '"+state+"', '"+zipcode+"', '"+date+"', "+cost+", "+serviceId+", '"+serviceType+"','"+comments+"')";
            
            if(directSqlExecute(sqlStr)){
                Alert _alert = new Alert(Alert.AlertType.INFORMATION);
                        _alert.setTitle("Adding Alert");
                        _alert.setHeaderText("");
                        _alert.setContentText("Successfully saved!");
                        _alert.show();
                descriptionComboBox.setValue(null);
                nameTextField.setText(null);
                cityTextField.setText(null);
                streetTextField.setText(null);
                stateTextField.setText(null);
                zipcodeTextField.setText(null);
                dateTextField.setValue(null);
                costTextField.setText(null);
                commentsTextField.setText(null);
                this.loadDataFromDatabase();
            } else {
                Alert _alert = new Alert(Alert.AlertType.ERROR);
                        _alert.setTitle("Adding Alert");
                        _alert.setHeaderText("");
                        _alert.setContentText("Add new record failed!");
                        _alert.show();
            }
        }
    }
    
    //DatePickerCell
    private class DatePickerCell extends TableCell<TrackingDetails, String>{
        final DatePicker cellDataPicker = new DatePicker();
        
        //Display button if the row is not empty
	@Override
	protected void updateItem(String t, boolean empty) {
            super.updateItem(t, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    //setText(null);
                    setGraphic(cellDataPicker);
                } else {
                    setText(getItem().toString());
                    setGraphic(null);
                }
            }
        }
    }
  
    //private inner class ButtonCell
    private class ButtonCell extends TableCell<TrackingDetails, Boolean> {
	final Button cellButton = new Button("Delete");

	ButtonCell(final TableView tblView){
            String cssStr = "-fx-text-fill: #FFFFFF;" +
                            "  -fx-background-color: #888888;" +
                            "  -fx-border-color: #666666;";
            cellButton.setStyle(cssStr);
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
		public void handle(ActionEvent t) {
                    TrackingDetails currentRow = (TrackingDetails) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                    int id = ButtonCell.this.getTableView().getItems().get(0).getId();
                    //remove selected item from the table list, then refresh the data
                    if(deleteRow(id)){
                        loadDataFromDatabase();
                    }  
		}
            });
	}
        //Display button if the row is not empty
	@Override
	protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
        }
    }
}