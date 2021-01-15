//Function to enable the get information services in popup menu
function get_info()
{
    //Removing all other functionalities
    try{
    $("#table_div").remove();
    }
    catch(Exception){}
    try{
    $('#maprendersettings').remove();
    }
    catch(Exception){}
    try{
    $("#map_choose").remove();
    }
    catch(Exception){}
    
    try{
    $("#data_buffer_inner").remove();
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
   
    map.removeControl(control);
    for(key in measureControls) {
                var control = measureControls[key];
                control.deactivate();
            }            
            
    //Setting up proxy for the popup        
    var proxyProtocol = window.location.protocol;
    var proxyHost = window.location.host;
    OpenLayers.ProxyHost = "http://localhost:8081/WebApplication5/proxy.jsp?resourceUrl=";
    
    //Enabling the control for popup(an event on mouse click)
    map.addControl(info);
    info.activate();
    
}

//Function to disable the control for popup
function get_info_remover()
{
    
    map.removeControl(info);
    info.deactivate();
    OpenLayers.ProxyHost="";
}