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
 *  Servlet to display data from data operation on the tables as passed from data_synthesis.js. There are four operations
 * 1)To fetch the entire table
 * 2)To find out the data between two buffer rings
 * 3)To find intersection among the tables
 * 4)To compare maps for tables which have similar columns
 * A query is fired, the name of table is put in the Json object passed back to data_synthesis.js
 */
public class Buffer_data_operation_display extends HttpServlet {

        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            System.out.println("in the servlet buffer data operation display");
        try {
    
            System.out.println("in the servlet buffer data operation display");
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printout = response.getWriter();
            
            //Getting the data operation
            String query_type=request.getParameter("querytype");
            
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
            
            
            
            //Creating a table as per the data operation selected. Then saving it in display_table
            JSONObject JObject = new JSONObject();
            String sql_query="";
            String query="";
            //Entire table
            if(query_type.equals("select"))
            {
                String geom_column="";
                ResultSet geog_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and (column_name='geog' or" +
" column_name='geom')");
                while(geog_name.next())
                {
                    geom_column=geog_name.getString("column_name");
                }
                query="CREATE TABLE displaydata(geog) as select "+geom_column+" from "+tables[0];
            }
            //Table containing the data between buffers
            else if(query_type.equals("difference1"))
            {
                ResultSet col_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and column_name!='geog' and and column_name!='geom'");
                int i=0;
                String column="";
                while(col_name.next())
                {
                     column=col_name.getString("column_name");
                    i++;
                }
                String geom_column="";
                ResultSet geog_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and (column_name='geog' or" +
" column_name='geom')");
                while(geog_name.next())
                {
                    geom_column=geog_name.getString("column_name");
                }
                if(i==1)
                {
                sql_query+="CREATE table as data_between"+tables[0]+"_"+tables[1]+" (SELECT b1."+column+" from "+tables[i-1]+"  b1 where NOT exists (SELECT * from "+tables[1]+" b2 where b1."+column+"= b2."+column+"))";
                query="CREATE TABLE displaydata(geog) as select "+geom_column+" from data_between"+tables[0]+"_"+tables[1];
                }
                
            }
            //Table containing the intersection of data
            else if(query_type.equals("intersection"))
            {
                ResultSet col_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and column_name!='geog' and column_name!='geom'");
                int i=0;
                String column="";
                while(col_name.next())
                {
                     column=col_name.getString("column_name");
                    i++;
                }
                String geom_column="";
                ResultSet geog_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and (column_name='geog' or" +
" column_name='geom')");
                while(geog_name.next())
                {
                    geom_column=geog_name.getString("column_name");
                }
                if(i==1)
                {
                    for(int j=0;j<no_of_tables;j++)
                    {
                        if(j==1)
                        {
                            sql_query+="CREATE TABLE AS intersection_"+column+" (SELECT "+column+" from "+tables[j]+")";   
                    
                        }
                        else
                        {
                            sql_query+="INTERSECT SELECT "+column+" from "+tables[j];
                        }
                    }
                    query="CREATE TABLE displaydata(geog) as select "+geom_column+" from intersection_"+column;
                }
                
            }
            //Table containing the difference between individual cells of the tables chosen
            else if(query_type.equals("difference2"))
            {
                ResultSet col_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and column_name!='geog' and column_name!='geom'");
                int i=0;
                String column="";
                while(col_name.next())
                {
                     column=col_name.getString("column_name");
                    i++;
                }
                 String geom_column="";
                ResultSet geog_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and (column_name='geog' or" +
" column_name='geom')");
                while(geog_name.next())
                {
                    geom_column=geog_name.getString("column_name");
                }
                if(i==1 && no_of_tables==2)
                {
                    sql_query+="CREATE tables as changes_"+column+"(SELECT a."+column+"-"+"b."+column+" as "+column+"_change from "+tables[0]+" a, "+tables[1]+" b)";
                    query="CREATE TABLE displaydata(geog) as select "+geom_column+" from changes_"+column;
                }
                
            }
            info.stonly("Drop table if exists displaydata");
            info.stonly(sql_query);
            info.stonly(query);
            JObject.put("a","a");
            System.out.println(JObject);
            printout.print(JObject);
            printout.flush();
            
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Throwing an error");
        }
    }
}
