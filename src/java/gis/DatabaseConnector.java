package gis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aim
 * Class creating a database connection. It contains functions to fire queries which
 * return result and which update database
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConnector {
    Connection c=null;
    public void Dbc()
    {
         try {
         Class.forName("org.postgresql.Driver");
         c = DriverManager
            .getConnection("jdbc:postgresql://localhost:5433/postgres",
            "postgres", "stars");
      } catch (Exception e) {
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      System.out.println("Opened database successfully");
    
    }
    
    public void main(String args[]) {
    
    }  
    public void close()
   {
        try {
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    
   //Function for query which returns a result set 
   public ResultSet st( String sql)
           
   {
       ResultSet rs= null;
       try{
       Statement s=c.createStatement();

       rs = s.executeQuery(sql);
       System.out.println(sql);
       System.out.println("DatabaseConnector sql : "+sql);
       return rs;
       }
       catch( Exception e)
       {
           System.out.println("error");
           return rs;
       }
       
   }
   
   //Function which doesnot return a result set only updates a database
   public void stonly( String sql)
           
   {
       
       try{
       Statement s=c.createStatement();
       s.executeUpdate(sql);
       
       System.out.println("DatabaseConnector sql : "+sql);
       
       }
       catch( Exception e)
       {
           System.out.printf(sql);
           System.out.println("error");
       }
       
   }
}
