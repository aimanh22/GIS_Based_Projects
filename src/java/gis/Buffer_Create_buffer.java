/*
r * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gis;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author Aim
 */

//Servlet to create tables for each buffer geometry and buffer information. The param are passed through the Create_buffer.js
//which includes ranges and the attributes of interest in the buffer (e.g. areaname). The function st_buffer is used to create 
//a geometry which is then joined with the columns of interest to create a table containing the data of interst in the buffer
//For each attribute only one table is created to allow for future data operations on it (even after the session ends)
public class Buffer_Create_buffer extends HttpServlet {

        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    
    @Override
    // A post ajax call is made from Create_buffer.js 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
           
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter printout = response.getWriter();
            
            //Getting the values of the param passed
            String[] mapValues = request.getParameterValues("name");
            String table_name=request.getParameter("mapname");
            String column_name=request.getParameter("colname");
            String attribute_name=request.getParameter("attval");
            
            int no_of_maps=Integer.parseInt(request.getParameter("no_of_maps"));
            int no_of_buffers=Integer.parseInt(request.getParameter("no_of_buffers"));
            int counter=Integer.parseInt(request.getParameter("create_buffer_counter"));
            
            //Creating an array for the radius of each buffer
            int range[]=new int[no_of_buffers];
            
            //Creating an array containg the table names including the column of interest from which data is to be extracted
            String maps[]=new String[no_of_maps];
            
            //Setting the buffer ranges
            for(int i=1;i<=no_of_buffers;i++)
            {
                range[i-1]=Integer.parseInt(request.getParameter("range"+i));
            }
            
            //Setting the maps in the format tablename;columnname
            for(int i=1;i<=no_of_maps;i++)
            {
                maps[i-1]=request.getParameter("map"+i);
                
            }
            
            //Intializing the database object
            DatabaseConnector info = new DatabaseConnector();
            info.Dbc();
            
            //Getting the value of lat and lon of centre point
            ResultSet latlon=info.st("select lat,lon from "+table_name+" where "+column_name+" = '"+attribute_name+"'");
            
            double lat=0.0;
            double lon=0.0;
            while(latlon.next())
            {
                lat = latlon.getDouble("lat");
                lon = latlon.getDouble("lon");
            }
            
            //for testing
            System.out.println(lat+" "+lon);
            System.out.print("here in ajax");
            
            System.out.print("no of bufferes : "+no_of_buffers);
            System.out.print("no of maps : "+no_of_maps);
            
            //Json object to print the tables finally created
            JSONObject JObject = new JSONObject();
            JSONArray JArr  = new JSONArray();
            int k=0;
            
            //Loop to create a table containing the geometry of a circle with resp range for each buffer. The inner 
            //loop then joins the geom with the table_column of interest for each buffer. 
            for(int i=0;i<no_of_buffers;i++)
            {
                System.out.print("map");
                info.stonly("Drop table if exists buffer_"+counter+"_"+(i+1));
                
                info.stonly("CREATE TABLE buffer_"+counter+"_"+(i+1)+" as (SELECT ST_Buffer(ST_geomfromewkt('POINT("+lon+" "+lat+")'),"+range[i]+"*0.009))");
        
                k++;
                for(int j=0;j<no_of_maps;j++)
                {
                info.stonly("Drop table if exists buffer_"+range[i]+"_"+(int)Math.ceil(Math.abs(lon))+"_"+(int)Math.ceil(Math.abs(lat))+"_"+maps[j].substring(0, maps[j].lastIndexOf(";"))+"_"+maps[j].substring(maps[j].lastIndexOf(";")+1));
                String geom_column="";
                ResultSet geog_name=info.st("select column_name from information_schema.columns where table_name='"+maps[j].substring(0, maps[j].lastIndexOf(";"))+"' and (column_name='geog' or\n" +
" column_name='geom')");
                while(geog_name.next())
                {
                    geom_column=geog_name.getString("column_name");
                }
                String sql = "CREATE TABLE buffer_"+
                        range[i]+"_"+(int)Math.ceil(Math.abs(lon))+"_"+(int)Math.ceil(Math.abs(lat))+"_"+maps[j].substring(0, maps[j].lastIndexOf(";"))+"_"+maps[j].substring(maps[j].lastIndexOf(";")+1)+
                        " as SELECT t."+maps[j].substring(maps[j].lastIndexOf(";")+1)+",t."+geom_column+
                        " from public."+maps[j].substring(0, maps[j].lastIndexOf(";"))+
                        " t RIGHT JOIN public.buffer_"+counter+"_"+(i+1)+
                        " b ON ST_DWithin(t."+geom_column+",b.st_buffer,0)";
                
                k++;
                info.stonly(sql);
                
                }
                String s="buffer_"+counter+"_"+(i+1);
                JObject.put("table"+i,s);
                System.out.println(JObject);
            }
            
            System.out.println(JObject);
            printout.print(JObject);
            printout.flush();
            
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Throwing an error");
        }
    }

}
