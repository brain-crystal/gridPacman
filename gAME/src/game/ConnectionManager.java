/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import java.sql.*;
import java.sql.Connection;

/**
 *
 * @author DELL LATITUDE E6410
 */
public class ConnectionManager {
    public static Connection getConnection()
    {
       Connection c=null;
       try{
           Class.forName("com.mysql.jdbc.Driver"); 
           c=DriverManager.getConnection("jdbc:mysql://localhost:3306/game","root","root");
       }catch(Exception e)
       {
           e.printStackTrace();
       }
       return c;
    }   
}
