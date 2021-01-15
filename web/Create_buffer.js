/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//The number of properties of interest in the buffer
var no_of_buffer_attributes;    
var column_list=[];
var ajax_url_map="";
var no_of_map_attributes=0;
var create_buffer_counter=0;

//Adds the form for buffer creation
function create_buffer()

{
        try{
        $('#maprendersettings').remove();
        }
        catch(Exception){}
        try{
        $('#map_choose').hide();
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
    try{
    $("#table_div").remove();
    }
    catch(Exception){}
    try{
        map.removeControl(control);
        for(key in measureControls) {
                    var control = measureControls[key];
                    control.deactivate();
                }            
            }
            catch(Exception){}
        try{
            toggleControl(null);
            info.deactivate();
            }
        catch(Exception)
        {
    
        }
 
          //  alert(find_point_counter);
    //Choose centre if not chosen already
        //if(find_point_counter<1)
        //{
            display_map_chooser();
        //}
        
    //$("#create_buffer_inner").remove();
    //alert("createbuffer");
    var htmltext="";
    $("#createbufferdiv").append("<div  class=\"noOfMaps enterMaps cf\"  id=\"create_buffer_inner\"><div id=\"nbuf\"><label>Enter the number of buffers:</label><input type=\"text\" id=\"number_of_buffers\"><input id=\"nbuffer\"  type=\"button\" value=\"Submit\" onclick=\"create_buffer_2();\"></div></div>");
}

//Get the information for the number of each buffer to be created
function create_buffer_2()
    {
        //alert("createbuffer2");
        $("#nbuf").append("<label>"+$("#number_of_buffers").val()+"</label>");
        $("#nbuffer").remove();
        $("#number_of_buffers").hide();
        $("#number_of_buffers");
        //$("#nbuf").append("<label>"+$("#number_of_buffers").val()+"</label>");
        //Getting the buffer numbers and number of map rendered
        var no_of_buffers=$("#number_of_buffers").val();
        var no_of_maps=map_list.length;
        var i=1;
        var htmltext="";
        
        //Fields to input the radius of each buffer
        for(i=1;i<=no_of_buffers;i++)
        {
            htmltext+="<div><label>Enter the range/radius for buffer</label>"+i+":<input id=\"range"+i+"\" type=\"text\"></div><div><label>Choose the color for buffer</label>"+i+":<input id=\"color_range"+i+"\" type=\"color\"></div>";
        }
        htmltext+="<div class=\"chooseData\"><label class=\"fNone\">Choose data points:</label>";
        
        //Fields to select the columns of interest
        for(i=1;i<=no_of_maps;i++)
        {
            var flag_select=0;
            
            var tmp_map_name=map_name_list[i-1].substring(map_name_list[i-1].indexOf(":")+1);
            //alert(tmp_map_name);
            
            $.ajax({
                url: "http://localhost:8081/WebApplication5/Create_place?val=col&mapname=" + tmp_map_name.toLowerCase(),
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                dataType: 'json',
                type: 'POST',
                success: function(data) {
                    if(flag==0)
                    {
                        htmltext+="<div class=\"lblAttri\"  >Select the attribute for map"+i+":</div>";flag_select++;
                    }
                    $.each(data,function(j,data){
                      htmltext+="<label class=\"lblCheck\"><input type=\"checkbox\" id=\""+tmp_map_name+";"+data+"\" onclick=\"create_ajax_map_url(this.id);\">"+data+"</label>";
                      column_list[column_list.length]=tmp_map_name+"_"+data;
                  });  
                },
                error: function() {
                    alert("Error in receving data...!!!");
                }
            }); 
        }
        htmltext+="</div></div><input type=\"button\" value=\"Submit\" onclick=\"create_buffer_ajax();\"></div>";
        
        //Adding the fields to get information for buffer creation
        $("#create_buffer_inner").append(htmltext);

    }
    
//Dummy function, for testing    
function select_buffer_centre()
    {
        find_place_column();
        create_buffer_ajax();
    }

//Function to create a string of param (names of columns of interest) to be passed to the servlet
function create_ajax_map_url(attributeId)
    {
        console.log("no_of_map_attributes : "+no_of_map_attributes);
        //alert("ajax_map_url");
        //  ajax_url_map+="&map"+no_of_map_attributes+"="+$("#map"+no_of_map_attributes).val().substring($("#map"+no_of_map_attributes).val().indexOf(":")+1).toLowerCase();//map attributes not just maps
        ajax_url_map+="&map"+(no_of_map_attributes+1)+"="+attributeId;
        console.log("attributeId : "+attributeId);

        no_of_map_attributes=no_of_map_attributes+1;
        console.log("no_of_map_attributes : "+no_of_map_attributes);

    }
    
//Function to create buffers with the help of servlet    
function create_buffer_ajax()
    {
            
        

        //Getting the values required for centre and buffers
        var tmp_map_name1=$("#map_chooser").val(); 
        var tmp_map_name=tmp_map_name1.substring(tmp_map_name1.indexOf(":")+1);  
        var tmp_column_name=$("#column_chooser").val();
        var tmp_value=$("#value_chooser").val();
        var no_of_attributes=column_list.length;
        var no_of_buffer=$("#number_of_buffers").val();
        var ajax_url_range="";

        create_buffer_counter++;
        
        //creating string containg the radius of each buffer to be passed to the servlet
        for(var i=1;i<=no_of_buffer;i++)
        {
            ajax_url_range+="&range"+i+"="+$("#range"+i).val();
        }

        //Creating the buffers through servlet
        alert(create_buffer_counter);
        $.ajax({
            url: "http://localhost:8081/GIS_Buffer_Analyzer_Application_1/Buffer_Create_buffer?mapname=" + tmp_map_name.toLowerCase()+"&colname="+tmp_column_name+"&attval="+tmp_value+"&no_of_buffers="+no_of_buffer+ajax_url_range+"&create_buffer_counter="+create_buffer_counter+"&no_of_maps="+no_of_map_attributes+ajax_url_map,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            dataType: 'json',
            type: 'POST',
            success: function(data) {
                var k=1;
                var buffer_color;
                //adding each buffer table as a layer
                $.each(data,function(j,data){
                    buffer_color=$('#color_range'+k).val();
                //alert(data);
                //alert("hi i am getting into the success function");
                var theSLD="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
        "<StyledLayerDescriptor version=\"1.0.0\" "+ 
        " xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" "+ 
        " xmlns=\"http://www.opengis.net/sld\" "+ 
        " xmlns:ogc=\"http://www.opengis.net/ogc\" "+ 
         " xmlns:xlink=\"http://www.w3.org/1999/xlink\" "+ 
         " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
          "<!-- a Named Layer is the basic building block of an SLD document -->"+
          "<NamedLayer>"+
            "<Name>cite:"+data+"</Name>"+
            "<UserStyle>"+
            "<!-- Styles can have names, titles and abstracts -->"+
              "<Title>Default Point</Title>"+
              "<Abstract>A sample style that draws a point</Abstract>"+
              "<!-- FeatureTypeStyles describe how to render different features -->"+
              "<!-- A FeatureTypeStyle for rendering points -->"+
              "<FeatureTypeStyle>"+
                "<Rule>"+
                  "<Name>rule1</Name>"+
                  "<LineSymbolizer>"+
            "<Stroke>"+
              "<CssParameter name=\"stroke\">"+buffer_color+"</CssParameter>"+
              "<CssParameter name=\"strokewidth\">20</CssParameter>"+
            "</Stroke>"+
          "</LineSymbolizer>"+
                "</Rule>"+
              "</FeatureTypeStyle>"+
            "</UserStyle>"+
          "</NamedLayer>"+
        "</StyledLayerDescriptor>";

                var tmp_map= new OpenLayers.Layer.WMS(
                "cite:"+data,
                "http://localhost:8080/geoserver/cite/wms",
                {layers:'cite:'+data ,
                transparent: "true",
                format: "image/png",
                sld_body:theSLD
                },
                {isBaseLayer: false, visibility: true}
                );
                map.addLayers([tmp_map]);
                map_list[map_list.length]=tmp_map;
                map_name_list[map_name_list.length]="cite:"+data;
                k++;
                }); 

                
            },
            error: function() {
                alert("Error in receving data...!!!");
            }
        });
        $("#create_buffer_inner").remove();
        if(find_point_counter==1)
        {
            $("#map_choose").remove();
        }
        
    }

