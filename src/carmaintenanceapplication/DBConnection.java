package carmaintenanceapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BICHENG XIAO
 */
public class DBConnection {
    public Connection connectToMysql(){
        try{
            /*
            String url = "jdbc:mysql://localhost:3306/bx34";
            String user = "root";
            String password = "china";
            */
            
            String url = "jdbc:mysql://sql.njit.edu:3306/bx34";
            String user = "bx34";
            String password = "Xx0SnUuM";
            
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        }catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
