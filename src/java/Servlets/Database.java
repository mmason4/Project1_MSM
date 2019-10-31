package Servlets;

import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Database {    
    private Connection getConnection() {        
        Connection conn = null;        
        try {            
            Context envContext = new InitialContext();            
            Context initContext  = (Context)envContext.lookup("java:/comp/env");            
            DataSource ds = (DataSource)initContext.lookup("jdbc/db_pool");            
            conn = ds.getConnection();
             
        }                
        catch (Exception e) { e.printStackTrace(); }
        return conn;    
    }
    
    protected Connection getDBConnection() {
        return this.getConnection();
    }
    
    protected String insertEmployee(String firstName, String lastName, String displayName, String sessionID){
        ResultSet keys;
        String key = "";
        Database db = new Database();
        Connection connection = db.getDBConnection();
        String query = "INSERT INTO registration_db.registrations (firstname,lastname,displayname,sessionid) values (?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, displayName);
            statement.setString(4, sessionID);
            
            int res = statement.executeUpdate();
            
            if(res == 1){
                keys = statement.getGeneratedKeys();
                if(keys.next()){
                    key = keys.getString(1);
                }
            }
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return key;
    }
}