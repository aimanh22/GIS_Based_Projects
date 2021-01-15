/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gis;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Aim
 * Servlet used for obtaining all tables in the database, all the columns of the a particular table and 
 * all values in a particular column.
 * The servlet is also used to create a geom table find_point to be rendered
 */
public class Create_place extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printout = response.getWriter();
            
            JSONObject TbObj = new JSONObject();
            JSONObject JObject = new JSONObject();
            JSONArray ColArr  = new JSONArray();
            
            DatabaseConnector info = new DatabaseConnector();
            String val= request.getParameter("val");
            info.Dbc();
            String table_name;
            String column_name;
            String attribute_name;
            System.out.print(val);
            
            //To display the tables in the database. Query is fired for the same.
            if(val.equals("table"))
            {
                System.out.print("val equals table");
                System.out.print(val);

                ResultSet col_name=info.st("SELECT table_name from INFORMATION_SCHEMA.tables where table_schema='public'and table_type='BASE TABLE'");
                int i=0;
                while(col_name.next())
                {
                    String column=col_name.getString("table_name");
                
                    JObject.put("col"+i,column);
                    i++;
                }
                 
                System.out.print(JObject);
                printout.print(JObject);
                printout.flush();
            }
            //To display the columns in the table. Query is fired for the same.
            if(val.equals("col"))
            {
                System.out.print("val equals col");
                System.out.print(val);
                
                //Getting the value of table
                table_name=request.getParameter("mapname");

                ResultSet col_name=info.st("select column_name from information_schema.columns where table_name='"+table_name+"'");
                int i=0;
                while(col_name.next())
                {
                    String column=col_name.getString("column_name");
                    JObject.put("col"+i,column);
                    i++;
                }
                System.out.print(JObject);
                printout.print(JObject);
                printout.flush();
            }
            //To display the values in the column. Query is fired for the same.
            if(val.equals("attribute"))
            {
                //Getting the values of the table and column
                table_name=request.getParameter("mapname");
                column_name=request.getParameter("colname");
                
                ResultSet att_name=info.st("select distinct "+column_name+" from "+ table_name);
                int i=0;
                while(att_name.next())
                {
                    String attribute=att_name.getString(column_name);
                    JObject.put("col"+i,attribute);
                    i++;
                }    
                printout.print(JObject);
                printout.flush();
            }
            //To create a table from the geom of the table,column,value selected. Query is fired for the same.
            if(val.equals("map"))
            {
                //Getting the values of table,column and value
                table_name=request.getParameter("mapname");
                column_name=request.getParameter("colname");
                attribute_name=request.getParameter("attval");
                int counter=Integer.parseInt(request.getParameter("count"))+1;
                System.out.println("here see me"+counter);
                String geom_column="";
                ResultSet geog_name=info.st("select column_name from information_schema.columns where table_name='"+table_name+"' and (column_name='geog' or\n" +
" column_name='geom')");
                while(geog_name.next())
                {
                    geom_column=geog_name.getString("column_name");
                }    
                
                info.stonly("Drop table if exists find_point"+counter);
                
                info.stonly("Create table find_point"+counter+"(geog) as (SELECT "+geom_column+" from "+ table_name+ " where "+column_name+" = '"+attribute_name+"')");
                
               
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Throwing an error");
        }
           
    }
}
