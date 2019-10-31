package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

@WebServlet(name = "Registration", urlPatterns = {"/registration"})
public class Registration extends HttpServlet {

    protected void processPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(postAttendee(request.getParameter("data")));
        }
    }
    
    protected void processGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(generateResultTable(request.getParameter("id")));
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processPost(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
    protected String generateResultTable(String code){
        String result = "<table>";
        result += "<tr>";
        result += "<th>id</th>";
        result += "<th>firstname</th>";
        result += "<th>lastname</th>";
        result += "<th>displayname</th>";
        result += "<th>sessionid</th></tr>";
        
        try {
            String query = "SELECT * FROM registrations r where sessionid like " + code;
            
            Database db = new Database();
            Connection conn = db.getDBConnection();
            
            PreparedStatement statement = conn.prepareStatement(query);
            
            ResultSet results = statement.executeQuery();
            
            while(results.next()){
                String row = "<tr>";
                row += "<td>" + results.getString("id") + "</td>";
                row += "<td>" + results.getString("firstname") + "</td>";
                row += "<td>" + results.getString("lastname") + "</td>";
                row += "<td>" + results.getString("displayname") + "</td>";
                row += "<td>" + results.getString("sessionid") + "</td>";
                row += "</tr>";
                result += row;
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        result +=  "</table>";
        
        return result;
    }
    
    protected String postAttendee(String data){
        String[] info = data.split(";");
        
        String firstName,lastName,displayName,session;
        firstName = info[0];
        lastName = info[1];
        displayName = info[2];
        session = info[3];
        
        JSONObject json = new JSONObject();
        json.put("name", displayName);
        
        Database update = new Database();
        
        String regNum = generateRegistrationCode(update.insertEmployee(firstName, lastName, displayName, session));        
        json.put("regID", regNum);
        
        return JSONObject.toJSONString(json);
    }
    
    protected String generateRegistrationCode(String primaryKey){
        String code = "R";
        
        for(int i = 0 ; i < (6 - primaryKey.length()); ++i){
            code += "0";
        }
        
        code += primaryKey;
        
        return code;
    }
}