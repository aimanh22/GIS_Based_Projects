/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//The data operation to be performed
var op_data="";

//The param to be based to servlet to fetch the data
var ajax_url_map_data="";
var no_of_data_tables=0;
var no_of_query_tables=0;

//Function adding the form 
function data_buffer()
    {
    op_data="";
     ajax_url_map_data="";
     no_of_data_tables=0;
     no_of_query_tables=0;
     ajax_query="";
     ajax_query_table="";

                try{
        $('#maprendersettings').remove();
        }
        catch(Exception){}
        try{
        $("#map_choose").remove();
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
    $("#table_div").remove();
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
           
        //alert("here in data buffer");
        var htmltext="<div id=\"data_buffer_inner\" class=\"noOfMaps enterMaps cf\"><label>Select the operation to be implemented:</label>"+
        "<select id=\"query_func\" onchange=\"data_buffer_2();\"><option>none</option><option>Display Data in entire Buffer</option><option>Display Data between buffer rings</option>"+
        "<option>Display data intersection of buffers</option><option>Compare maps</option><option>Total</option><option>Sum</option></select></div>";//<div><label id=\"cbqb\"><input  type=\"checkbox\" onclick=\"query_builder();\">Build your query</label></div>";
        $("#databufferdiv").append(htmltext);
    }
    
//Function to get the data operation, table names on which the operation is to performed    
function data_buffer_2()
    {    
        //alert("databuffer2");
        try{
            $('#cbqb').remove();
        }
        catch(Exception){}
        
        try{
            $('#table_select_box').remove();
        }
        catch(Exception){}
        try{
            $("#table_div").remove();
        }catch(Exception){}
        var htmltext="";
        var query_func=$("#query_func").val();

        //Setting the data operation
        if(query_func=="Display Data in entire Buffer"){
            op_data="select";
        }
        else if(query_func==("Display Data between buffer rings"))
        {
            op_data="difference1";
        }
        else if(query_func==("Display data intersection of buffers"))
        {
            op_data="intersection";
        }
        else if(query_func==("Compare maps"))
        {
            op_data="difference2";
        }
        else if(query_func==("Total"))
        {
            op_data="total";
        }
        else if(query_func==("Sum"))
        {
            op_data="sum";
        }
        //Dropdown of tables
        htmltext="</div><div id=\"table_select_box\" class='selcTbl'>"+
        "<h2>Select the table(s)</h2><ul>";

        //Getting the table names for dropdown
        $.ajax({
                        url: "http://localhost:8081/WebApplication5/Create_place?val=table",
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        dataType: 'json',
                        type: 'POST',
                        success: function(data) {
                            //alert("success function");
                            //alert(data);
                             
                          $.each(data,function(i,data){
if(( data[0]=="b" && data[1]=="u"&& data[2]=="f"&& data[3]=="f"&& data[4]=="e"&& data[5]=="r")&& (data.length!=10)){
                               htmltext+="<li><label><input type=\"checkbox\" id=\"data_"+data
                +"\" onclick=\"data_ajax_map_url(this.id);\">"+data+"</label></li>"; }
                          });  
                        },
                        error: function() {
                            alert("Error in receving data...!!!");
                        }
                });

        //Adding the fields for input        
        htmltext+="</ul></div><input type=\"button\" class=\"button\" value=\"submit\" onclick=\"data_ajax();\"><div class=\"cf tableData\">";
        $("#data_buffer_inner").append(htmltext);
    }
    
//Creating a string of param to be passed to servlet. It includes the table names    
function data_ajax_map_url(attributeId)
    {

        //alert("ajax_map_url");

        ajax_url_map_data+="&table"+(no_of_data_tables+1)+"="+attributeId.substring(attributeId.indexOf("_")+1);
        console.log("attributeId : "+attributeId);
        no_of_data_tables=no_of_data_tables+1;
        console.log("no_of_data_tables : "+no_of_data_tables);

    }
    

//Function which fetches the data of the data operation performed and displays it 
function data_ajax()
    {
        $('#data_buffer_inner').remove();
        try{
            $("#table_div").remove();
        }catch(Exception){}
        var htmltext="<div id=\"table_div\"><table class=\"tableData\">";
        var result_col;
        //alert("data_ajax");
        //alert("http://localhost:8081/WebApplication5/Buffer_data_operation?querytype=" +op_data+"&no_of_tables="+no_of_data_tables+ajax_url_map_data);
        
        //Works when one table selected for complete retrieval, two for comparison and difference
        if((!(op_data==("select"))&& no_of_data_tables!=1)||(!(op_data==("sum"))&& no_of_data_tables!=1)||(!(op_data==("total"))&& no_of_data_tables!=1)||(!(op_data==("difference1"))&& no_of_data_tables!=2)||(!(op_data==("difference2"))&& no_of_data_tables!=2))
        {
            //Fetching the data through servlet
            $.ajax({
                url: "http://localhost:8081/GIS_Buffer_Analyzer_Application_1/Buffer_data_operation?querytype=" +op_data+"&no_of_tables="+no_of_data_tables+ajax_url_map_data,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                dataType: 'json',
                type: 'POST',
                success: function(data) {
                    //alert(op_data);
                    if(op_data==("select"))
                    {
                        //alert("here");
                        //alert(data);
                        htmltext+="<tr>";
                        $.each(data,function(i,data){
                                      //alert(data);

                                      if(i=="row0"){

                                          $.each(data,function(j,data){
                                              if(data!='geog' && data!='geom')
                                              {
                                              htmltext+="<th>"+data+"</th>";
                                              }});
                                                
                                          }
                                  });  
                        htmltext+="</tr>";          
                    $.each(data,function(i,data){
                            htmltext+="<tr>";
                                if(i!="row0"){

                                          $.each(data,function(j,data){
                                              htmltext+="<td>"+data+"</td>";});
                                          htmltext+="</tr>";
                                          }
                                  });
                                  htmltext+="</tr>";
                    }
                    else
                    {
                    $.each(data,function(i,data){
                                      //alert(data);

                                      if(i=="row0"){htmltext+="<tr><th>"+data+"</th></tr>";

                                      }
                                      

                                  });  

                    $.each(data,function(i,data){
                                      //alert(data);

                                      if(i=="row0"){

                                      }
                                      else
                                      {
                                          htmltext+="<tr><td>"+data+"</td></tr>";
                                      }

                                  });  


                }
            },
                error: function() {
                    alert("Error in receving data...!!!");
                }
            });
            
            //Displaying the data
            htmltext+="</table><input type=\"button\" class=\"button mrgRight\" value=\"Display Data \" onclick=\"data_display_ajax();\"></div></div>"; ///<input type=\"button\" class=\"button\" value=\"Export Data \" onclick=\"export_data();\">
            $("#databufferdiv").append(htmltext);
        }
        else
        {
            alert("Check if the right combinationss were selected!!!\n");
        }

    }
    
//Function to show the data retrieved on the map    
function data_display_ajax()
    {
        
        var result_col;
        
        //alert("data_ajax");
        //alert("http://localhost:8081/WebApplication5/Buffer_data_operation_display?querytype=" +op_data+"&no_of_tables="+no_of_data_tables+ajax_url_map_data);
        
        if((!(op_data==("select"))&& no_of_data_tables!=1)||(!(op_data==("difference1"))&& no_of_data_tables!=2)||(!(op_data==("difference2"))&& no_of_data_tables!=2))
        {
            $.ajax({
                url: "http://localhost:8081/GIS_Buffer_Analyzer_Application_1/Buffer_data_operation_display?querytype=" +op_data+"&no_of_tables="+no_of_data_tables+ajax_url_map_data,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                dataType: 'json',
                type: 'POST',
                success: function(data) {
                    
                    var tmp_map= new OpenLayers.Layer.WMS(
                    "cite:displaydata",
                    "http://localhost:8080/geoserver/cite/wms",
                    {layers:'cite:displaydata' ,
                    transparent: "true",
                    format: "image/png"//,
                    //SLD_BODY:theSLD
                    },
                    {isBaseLayer: false, visibility: true}
                    );
                    map.addLayers([tmp_map]);
                    //map.zoomTo("8");
                    map_list[map_list.length]=tmp_map;
                    map_name_list[map_name_list.length]="cite:displaydata";


                },
                error: function() {
                    alert("Error in receving data...!!!");
                }
            });
            
        }
        else
        {
            alert("Check if the right combinationss were selected!!!\n");
        }
        try{
        $("#data_buffer_inner").remove();}
        catch(Exception){}
        try{
        $("#table_div").remove();
        }
        catch(Exception){}
    }      
    function export_data()
    {
        /*
         $("#table2excel").table2excel({
        // exclude CSS class
        exclude: ".noExl",
        name: "Excel Document Name"
        }); 
*/
    }
    function query_builder()
    {
        $('#data_buffer_inner').remove();
        try{
        $('#cbqb').remove();}
        catch(Exception){}
         var htmltext="<div id=\"data_buffer_inner\" class=\"noOfMaps enterMaps cf\"><div id=\"table_select_box\" class='selcTbl'>"+
        "<h2>Create the expression:</h2><ul>"+
        "<li><label><input type=\"checkbox\" id=\"data_(\" onclick=\"data_ajax_query_url(this.id);\">(</label></li>"+
        "<li><label><input type=\"checkbox\" id=\"data_)\" onclick=\"data_ajax_query_url(this.id);\">)</label></li>"+
        "<li><label><input type=\"checkbox\" id=\"data_plus\" onclick=\"data_ajax_query_url(this.id);\">+</label></li>"+
        "<li><label><input type=\"checkbox\" id=\"data_-\" onclick=\"data_ajax_query_url(this.id);\">-</label></li>"+
        "<li><label><input type=\"checkbox\" id=\"data_*\" onclick=\"data_ajax_query_url(this.id);\">*</label></li>"+
        "<li><label><input type=\"checkbox\" id=\"data_\" onclick=\"data_ajax_query_url(this.id);\"></label></li>";

        //Getting the table names for dropdown
        $.ajax({
                        url: "http://localhost:8081/WebApplication5/Create_place?val=table",
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        dataType: 'json',
                        type: 'POST',
                        success: function(data) {
                            //alert("success function");
                            //alert(data);
                             
                          $.each(data,function(i,data){
if(( data[0]=="b" && data[1]=="u"&& data[2]=="f"&& data[3]=="f"&& data[4]=="e"&& data[5]=="r")&& (data.length!=10)){
                               htmltext+="<li><label><input type=\"checkbox\" id=\"data_"+data
                +"\" onclick=\"data_ajax_query_url(this.id);\">"+data+"</label></li>"; }
                          });  
                        },
                        error: function() {
                            alert("Error in receving data...!!!");
                        }
                });

        //Adding the fields for input        
        htmltext+="</ul></div><input type=\"button\" class=\"button\" value=\"submit\" onclick=\"query_ajax();\"><div class=\"cf tableData\"><div>";
        $("#databufferdiv").append(htmltext);
    }
    var ajax_query="";
    var ajax_query_table="";
    function data_ajax_query_url(attributeId)
    {

        alert(ajax_query);
        if(attributeId=="data_("||attributeId=="data_)"||attributeId=="data_plus"||attributeId=="data_-"||attributeId=="data_*"||attributeId=="data_/")
        {  ajax_query+=" "+attributeId.substring(attributeId.indexOf("_")+1);}
        else
        {
         ajax_query_table+="&table"+(no_of_query_tables+1)+"="+attributeId.substring(attributeId.indexOf("_")+1);
         no_of_query_tables++;
         ajax_query+=" "+attributeId.substring(attributeId.indexOf("_")+1)+"."+attributeId.substring(attributeId.lastIndexOf("_")+1);
        }

    }
    function query_ajax(){
        
        $('#data_buffer_inner').remove();
        try{
            $("#table_div").remove();
        }catch(Exception){}
        var result_col;
        
        //Works when one table selected for complete retrieval, two for comparison and difference
        
            //Fetching the data through servlet
            alert("http://localhost:8081/GIS_Buffer_Analyzer_Application_1/Buffer_data_operation?querytype=query_builder"+"&no_of_tables="+no_of_query_tables+ajax_query_table+"&query="+ajax_query);
            $.ajax({
                url: "http://localhost:8081/GIS_Buffer_Analyzer_Application_1/Buffer_query_operation?&no_of_tables="+no_of_query_tables+ajax_query_table+"&query="+ajax_query,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                dataType: 'json',
                type: 'POST',
                success: function(data) {
                    //alert(op_data);
                    
                    $.each(data,function(i,data){
                                      //alert(data);

                                      if(i=="row0"){htmltext+="<tr><th>"+data+"</th></tr>";

                                      }
                                      

                                  });  

                    $.each(data,function(i,data){
                                      //alert(data);

                                      if(i=="row0"){

                                      }
                                      else
                                      {
                                          htmltext+="<tr><td>"+data+"</td></tr>";
                                      }

                                  });  


                
            },
                error: function() {
                    alert("Error in receving data...!!!");
                }
            });
            
            //Displaying the data
            htmltext+="</table><input type=\"button\" class=\"button mrgRight\" value=\"Display Data \" onclick=\"data_display_ajax();\"></div></div>"; ///<input type=\"button\" class=\"button\" value=\"Export Data \" onclick=\"export_data();\">
            $("#databufferdiv").append(htmltext);

    }
    