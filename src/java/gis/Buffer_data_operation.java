/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gis;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Aim
 * Servlet to perform data operation on the tables as passed from data_synthesis.js. There are four operations
 * 1)To fetch the entire table
 * 2)To find out the data between two buffer rings
 * 3)To find intersection among the tables
 * 4)To compare maps for tables which have similar columns
 * A query is fired, the result of which is put in the Json object passed back to data_synthesis.js
 */
public class Buffer_data_operation extends HttpServlet {

        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
           
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
            
            
            String sql_query="";
            
            //Firing the appropriate query for the data operation chosen
            if(query_type.equals("select"))
            {
                JSONObject JObject = new JSONObject();
                JSONArray rowArray  = new JSONArray();
                ArrayList colArr = new ArrayList();
                ArrayList dataArr = new ArrayList();
                
                
                ResultSet col_name=info.st("select column_name,data_type from information_schema.columns where table_name='"+tables[0]+"'");
                String column="";
                while(col_name.next())
                {
                     rowArray.put(col_name.getString("column_name"));
                     colArr.add(col_name.getString("column_name"));
                     dataArr.add(col_name.getString("data_type"));
                }
                JObject.put("row0",rowArray);
                
                sql_query="Select * from ";
                for(int i=0;i<no_of_tables;i++)
                {
                    sql_query+=tables[i];
                }
                
                ResultSet result=info.st(sql_query);
                int counter=0;
                
                while(result.next())
                {
                    
                    JSONArray rowArr  = new JSONArray();
                    for(int i=0; i<rowArray.length();i++)
                    {
                        
                       if(dataArr.get(i).equals("character varying"))
                        {   
                    
                             rowArr.put(result.getString(colArr.get(i).toString()));
                 
                        }
                        else if(dataArr.get(i).equals("numeric") || dataArr.get(i).equals("integer") )
                        {   
                                try{
                                        rowArr.put(result.getDouble(colArr.get(i).toString()));
                                    }
                                    catch(Exception e)
                                    {
                                        rowArr.put(result.getInt(colArr.get(i).toString())); 
                                    }
                
                        }
                    }
                    counter++;
                    JObject.put("row"+counter,rowArr);
                }
                System.out.println(JObject);
                printout.print(JObject);
                printout.flush();
            }
     
            //Tables usually having two columns geog and attribute. Gets value of data between buffers mentioned by 
            //excluding the values in the smaller buffer from the bigger buffer
            else 
            {    
            if(query_type.equals("difference1"))
            {
                ResultSet col_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and column_name!='geog' and column_name!='geom'");
                int i=0;
                String column="";
                while(col_name.next())
                {
                     column=col_name.getString("column_name");
                    i++;
                }
                if(i==1)
                {
                sql_query+="SELECT b1."+column+" from "+tables[i-1]+"  b1 where NOT exists (SELECT * from "+tables[1]+" b2 where b1."+column+"= b2."+column+")";
                }
            }
            //query to find the data common among tables
            else if(query_type.equals("intersection"))
            {
                ResultSet col_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and column_name!='geog' and column_name!='geom' ");
                int i=0;
                String column="";
                while(col_name.next())
                {
                     column=col_name.getString("column_name");
                    i++;
                }
                if(i==1)
                {
                    for(int j=0;j<no_of_tables;j++)
                    {
                        if(j==0)
                        {
                            sql_query+=" SELECT "+column+" from "+tables[j];   
                    
                        }
                        else
                        {
                            sql_query+=" INTERSECT SELECT "+column+" from "+tables[j];
                        }
                    }
                }
            }
            //Query to find the difference between cells of one table from the other. Tables have the same columns
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
                if(i==1 && no_of_tables==2)
                {
                    sql_query+="SELECT a."+column+"-"+"b."+column+" as "+column+"_change from "+tables[0]+" a, "+tables[1]+" b";
                }
            }
            else if(query_type.equals("total"))
            {
                sql_query="SELECT COUNT(*) from "+tables[0];
            }
            else if(query_type.equals("sum"))
            {
                ResultSet col_name=info.st("select column_name from information_schema.columns where table_name='"+tables[0]+"' and column_name!='geog' and column_name!='geom'");
                int i=0;
                String column="";
                while(col_name.next())
                {
                     column=col_name.getString("column_name");
                    i++;
                }
                sql_query="SELECT SUM(CAST ("+column+" as int)) from "+tables[0];
            }
            System.out.print(sql_query);
            ResultSet result=info.st(sql_query);
            JSONObject JObject = new JSONObject();
            
            int k=0;
            
            //Fetching values from the result set after firing the query
            ResultSet col_name=info.st("select column_name, data_type from information_schema.columns where table_name='"+tables[0]+"' and column_name!='geog' and column_name!='geom'");

            String column_name_result="";
            String data_type_result="";
            while(col_name.next())
            {
                 column_name_result=col_name.getString("column_name");
                 data_type_result=col_name.getString("data_type");
                k++;
            }
            if(query_type.equals("total"))
            {
                JObject.put("row0","Total_"+column_name_result);
            }
            if(query_type.equals("sum"))
            {
                JObject.put("row0","Sum_"+column_name_result);
            }
            else
            {
            JObject.put("row0",column_name_result);
            }
            int counter=1;
            
            //Fetching values according to the data type. Then putting it in the JObject
            while(result.next())
            {
                if(query_type.equals("total"))
                {
                    JObject.put("row"+counter,result.getInt("count"));
                }
                if(query_type.equals("sum"))
                {
                    JObject.put("row"+counter,result.getInt("sum"));
                }
                else if(data_type_result.equals("character varying"))
                {   
                    
                    JObject.put("row"+counter,result.getString(column_name_result));
                 
                }
                else if(data_type_result.equals("numeric") || data_type_result.equals("integer") )
                {   try{
                    try{
                    JObject.put("row"+counter,result.getInt(column_name_result));
                    }
                    catch(Exception e)
                    {
                       JObject.put("row"+counter,result.getDouble(column_name_result));
                    
                    }
                }
                catch(Exception e){
                    
                    JObject.put("row"+counter,result.getInt(column_name_result+"_change"));
                    
                }
                }
               
                
                counter++;        
            }
            System.out.println(JObject);
            printout.print(JObject);
            printout.flush();
            }   
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Throwing an error");}
        
    }
}
