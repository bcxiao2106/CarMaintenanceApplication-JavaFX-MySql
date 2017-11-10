package carmaintenanceapplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;


/**
 *
 * @author BICHENG XIAO
 */
public class CarMaintenanceApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLCarMaintenance.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("StyleForFX.css").toExternalForm());
        
        stage.setResizable(false);

        stage.setTitle("Car Maintenance/Repair Tracker Application - BICHENG XIAO @ NJIT");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
