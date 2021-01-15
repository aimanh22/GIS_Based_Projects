/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var find_point_counter=0;

//Function to display the map names for selecting from among the ones which have been rendered. It creates a dynamic field 
//whose  columns are fetched after the map is selected
function display_map_chooser()
    {
        try{
        $('#maprendersettings').remove();
        }
        catch(Exception){}
        try{
        $('#map_choose').remove();
        }
        catch(Exception){}
        try{
        $("#table_div").remove();
        }
        catch(Exception){}
        try{
        $('#data_buffer_inner').remove();
        }
        catch(Exception){}
        try{
        $('#create_buffer_inner').remove();
        }
        catch(Exception){}
        try{
        $("#output").hide();
        }
        catch(Exception){}
        try{
        $("#distdiv").hide();
        }
        catch(Exception){}
        try{
        $("#areadiv").hide();
        }
        catch(Exception){}
        try{
        $("#outermeasure").hide();
        }
        catch(Exception){}
        map.removeControl(control);
        for(key in measureControls) {
                    var control = measureControls[key];
                    control.deactivate();
                }            

        try{
            toggleControl(null);
            info.deactivate();
            }
        catch(Exception)
        {
    
        }           
        var i=1;
        var htmltext="<div id=\"map_choose\" class=\"chooserRow cf\"><div id=\"map_find_place\"><label> Select the Map:</label><select id=\"map_chooser\" onchange=\"find_place_column();\"><option>none</option>";
        for(i=0;i<map_list.length;i++)
        {
            var data=map_name_list[i];
            if(!(data[0]=="f" && data[1]=="i"&& data[2]=="n"&& data[3]=="d" ) && !( data[0]=="b" && data[1]=="u"&& data[2]=="f"&& data[3]=="f"&& data[4]=="e"&& data[5]=="r")&& !( data[0]=="s" && data[1]=="p"&& data[2]=="a"&& data[3]=="t"&& data[4]=="i"&& data[5]=="a"))
              {
               htmltext+="<option>"+data+"</option>"; 
              }
        }
        htmltext=htmltext+"</select></div></div></div>";
        $('#find_place').append(htmltext);
        try{
            $("#column_chooser").remove();
            
        }
        catch(Exception){}
        try{
            
            $("#column_label").remove();
        }
        catch(Exception){}
        try{
            
            $("#value_chooser").remove();
        }
        catch(Exception){}
        try{
            
            $("#value_label").remove();
        }
        catch(Exception){}
        try{    
            $("#find_point_button").remove();
        }
        catch(Exception){}
    }
                
//Function to display the columns of the map selected. It gets the names of all the columns through servlet                
function find_place_column()
    {
        
        try{
            $("#column_chooser").remove();
            
        }
        catch(Exception){}
        try{
            
            $("#column_label").remove();
        }
        catch(Exception){}
        try{
            
            $("#value_chooser").remove();
        }
        catch(Exception){}
        try{
            
            $("#value_label").remove();
        }
        catch(Exception){}
        try{    
            $("#find_point_button").remove();
        }
        catch(Exception){}
        
        
        
        //User selecting the map
        //display_map_chooser();

        //Getting the name of the map. In geoserver it has a pattern resourcelibrary:name while in database saved just by name. The 
        //conversions below are for getting the geoserver name to database name
        var tmp_map_name1=$("#map_chooser").val(); 
        var tmp_map_name=tmp_map_name1.substring(tmp_map_name1.indexOf(":")+1); 

        //dropdown to select the columns
        var html_find_place="<label id=\"column_label\"> Select the column:</label> <select id=\"column_chooser\" onchange=\"find_place_point();\"><option>none</option>";

        //getting the column names through servlet
        $.ajax({
            url: "http://localhost:8081/GIS_Buffer_Analyzer_Application_1/Create_place?val=col&mapname=" + tmp_map_name.toLowerCase(),
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'json',
            type: 'POST',
            success: function(data) {                        
              $.each(data,function(i,data){
                  html_find_place+="<option>"+data+"</option>";
              });  
            },
            error: function() {
                alert("Error in receving data...!!!");
            }
        });   
        html_find_place+="</select>";
        //add the dropdown field to form
        $('#map_find_place').append(html_find_place);
   }

// Function to display the values of the column selected. It gets the names of all the cells through servlet
function find_place_point()
    {
        try{
            
            $("#value_chooser").remove();
        }
        catch(Exception){}
        try{
            
            $("#value_label").remove();
        }
        catch(Exception){}
        try{    
            $("#find_point_button").remove();
        }
        catch(Exception){}
        
        
        //Getting the values of the map and the column in the format required
        var tmp_map_name1=$("#map_chooser").val(); 
        var tmp_map_name=tmp_map_name1.substring(tmp_map_name1.indexOf(":")+1);  
        var tmp_column_name=$("#column_chooser").val();
        $("#map_detail").remove();
    
        //Creating a string to add the dropdown menu
        var html_find_place="<div id=\"map_detail\"><label id=\"value_label\"> Select the value:</label> <select id=\"value_chooser\">";

        //Getting the values of the column
        $.ajax({
            url: "http://localhost:8081/WebApplication5/Create_place?val=attribute&mapname=" + tmp_map_name.toLowerCase()+"&colname="+tmp_column_name,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'json',
            type: 'POST',
            success: function(data) {
                $.each(data,function(i,data){
                  html_find_place+="<option>"+data+"</option>";
              });  
            },
            error: function() {
                alert("Error in receving data...!!!");
            }
        });
        html_find_place+="</select></div><div><input id=\"find_point_button\" type=\"button\" value=\"Find Point\" onclick=\"find_place_main();\">";
        
        //adding dropdown to the form
        $('#map_find_place').append(html_find_place);
    }
    
//Getting the value of the point and creating a layer and displaying it    
function find_place_main()
    {

        //Getting the values required
        var tmp_map_name1=$("#map_chooser").val(); 
        var tmp_map_name=tmp_map_name1.substring(tmp_map_name1.indexOf(":")+1);  
        var tmp_column_name=$("#column_chooser").val();
        var tmp_value=$("#value_chooser").val();
        
        $.ajax({
            url: "http://localhost:8081/GIS_Buffer_Analyzer_Application_1/Create_place?val=map&mapname=" + tmp_map_name.toLowerCase()+"&colname="+tmp_column_name+"&attval="+tmp_value+"&count="+find_point_counter,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'json',
            type: 'POST',
            success: function(data) {

                var tmp_map= new OpenLayers.Layer.WMS(
                "find_point"+(find_point_counter+1),
                "http://localhost:8080/geoserver/cite/wms",
                {layers:'find_point'+(find_point_counter+1) ,
                transparent: "true",
                format: "image/png",
                //sld_body:strSld_body
                },
                {isBaseLayer: false, visibility: true}
                );

                //Rendering and adding the map name to the list
                map.addLayers([tmp_map]);
                map_list[map_list.length]=tmp_map;
                map_name_list[map_name_list.length]="find_point"+find_point_counter;

            },
            error: function() {
                alert("Error in receving data...!!!");
            }
        });
        
        //if(find_point_counter<1)
        //{
            $("#map_choose").hide();
        //}
        //else
        //{
        //    $("#map_choose").remove();
        //}
        find_point_counter++;
        
    }

