/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gis;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Aim
 */
public class Buffer_query_operation extends HttpServlet {

        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
           
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printout = response.getWriter();
            
            String query=request.getParameter("query").replace("plus", "+");
            
            //Getting the values of tables on which to perform data operations and no of tables
            int no_of_tables=Integer.parseInt(request.getParameter("no_of_tables"));
            
            String tables[]=new String[no_of_tables];
            
            for(int i=1;i<=no_of_tables;i++)
            {
                tables[i-1]=request.getParameter("table"+i).toLowerCase();
            }
            
            //Intializing the database object
            DatabaseConnector info = new DatabaseConnector();
            info.Dbc();

            String root_table=tables[0].substring(tables[0].indexOf("_")+1);
                   root_table=root_table.substring(root_table.indexOf("_")+1);
                   root_table=root_table.substring(root_table.indexOf("_")+1);
                   root_table=root_table.substring(root_table.indexOf("_")+1,root_table.lastIndexOf("_"));
            ResultSet col_name=info.st("select column_name,data_type from information_schema.columns where table_name='"+root_table+"' and column_name like '%name%'");
               
                String column="";
                String data_type="";
                while(col_name.next())
                {
                     column=col_name.getString("column_name");
                     data_type=col_name.getString("data_type");
                     break;
                }
            String sql_query="SELECT "+root_table+"."+column+","+query+"from "+root_table;
            for(int i=0;i<no_of_tables;i++)
            {
                
                sql_query+=","+tables[i];
                
            }
            
            JSONObject JObject = new JSONObject();
            JSONArray rowArray  = new JSONArray();
            
            rowArray.put(column);
            rowArray.put("Value");
            
            JObject.put("row0",rowArray);
            System.out.print(JObject);
            System.out.print(sql_query);
            ResultSet result=info.st(sql_query);
            
            int counter=0;
                
                while(result.next())
                {
                    
                    JSONArray rowArr  = new JSONArray();
                    for(int i=0; i<rowArray.length();i++)
                    {
                        
                             rowArr.put(result.getString(column));
                 
                                  try{
                                        rowArr.put(result.getDouble("?column?"));
                                    }
                                    catch(Exception e)
                                    {
                                        rowArr.put(result.getInt("?column?")); 
                                    }
                
                        
                    }
                    counter++;
                    JObject.put("row"+counter,rowArr);
                }
        }
        
        catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Throwing an error");}
        
    }
}
