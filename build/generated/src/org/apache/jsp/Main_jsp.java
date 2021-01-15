package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class Main_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n");
      out.write("\t<head>\n");
      out.write("\t\t<title>GIS_Buffer_Analyzer</title>\n");
      out.write("\n");
      out.write("                <!-- Applying jquery-->\n");
      out.write("                <script type=\"text/javascript\" src=\"js/jquery-1.5.2.min.js\"></script>\n");
      out.write("     \n");
      out.write("                <!-- Applying the styles-->\n");
      out.write("\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />\n");
      out.write("                <link rel=\"stylesheet\" type=\"text/css\" href=\"css/style1.css\" />\n");
      out.write("                <link rel=\"stylesheet\" type=\"text/css\" href=\"css/styles.css\" />\n");
      out.write("                <link rel=\"stylesheet\" type=\"text/css\" href=\"css/style_blue.css\" />\n");
      out.write("\n");
      out.write("\t\t\n");
      out.write("                <!--All the functionalities-->\n");
      out.write("                <script src=\"Map_settings.js\"></script>\n");
      out.write("                <script src=\"Get_information.js\"></script>\n");
      out.write("                <script src=\"Measure_distance.js\"></script>\n");
      out.write("                <script src=\"Measure_area.js\"></script>\n");
      out.write("                <script src=\"Measure_dist_area.js\"></script>\n");
      out.write("                <script src=\"Data_synthesis.js\"></script>\n");
      out.write("                <script src=\"Find_place.js\"></script>\n");
      out.write("                <script src=\"Create_buffer.js\"></script>\n");
      out.write("               \n");
      out.write("\t</head>\n");
      out.write("\t<body>\n");
      out.write("            <div id=\"wrapper\">\n");
      out.write("  \n");
      out.write("                <header id=\"header\">\n");
      out.write("                    <div class=\"container\">\n");
      out.write("                        \n");
      out.write("                        <div class=\"cf\"><div class=\"help\"><!--<a href=\"help.html\"><img src=\"img/ico-help.png\" alt=\"Help\" width=\"40\"></a>--></div>\n");
      out.write("                            <h1 id=\"logo\">GIS<span>BUFFER</span>ANALYZER </h1>\n");
      out.write("                        </div>\n");
      out.write("                        <!--The tabs for each functionality-->\n");
      out.write("                        <div id=\"functionsa\"> <!--change the css id to functions-->\n");
      out.write("                            <ul>\n");
      out.write("                                <li>\n");
      out.write("                                    <button id=\"Description\" href=\"IntroPage.jsp\">About</button>\n");
      out.write("                                </li>\n");
      out.write("                                <li>\n");
      out.write("                                    <button onClick=\"display_map_set_form();\" id=\"map_render_settings\">Render Map</button>\n");
      out.write("                                </li>\n");
      out.write("                                <li>\n");
      out.write("                                    <button onClick=\"get_info();\" id=\"Getinfo\">Get Information</button>\n");
      out.write("                                </li>\n");
      out.write("                                <li>\n");
      out.write("                                    <button onClick=\"toggleControl(this);\" id=\"measuredist\">Measure Distance</button>\n");
      out.write("                                </li>\n");
      out.write("                                <li>\n");
      out.write("                                    <button onClick=\"toggleControl(this);\" id=\"measurearea\">Measure Area</button>\n");
      out.write("                                </li>\n");
      out.write("                                <li>\n");
      out.write("                                    <button onClick=\"display_map_chooser();\" id=\"findplace\">Find A Place</button>\n");
      out.write("                                </li>\n");
      out.write("                                <li>\n");
      out.write("                                    <button onClick=\"create_buffer();\" id=\"createbuffer\">Create Buffers</button>\n");
      out.write("                                </li>\n");
      out.write("                                <li>\n");
      out.write("                                    <button onClick=\"data_buffer();\" id=\"Datahandling\">Data Operations</button>\n");
      out.write("                                </li>\n");
      out.write("                            </ul>\n");
      out.write("                        </div>\n");
      out.write("                    </div>\n");
      out.write("                </header>\n");
      out.write("\n");
      out.write("                <!-- displaying the map-->\n");
      out.write("                <div id=\"displaymap\">\n");
      out.write("                    <div id=\"map\" class=\"smallmap\"></div>\n");
      out.write("                    <script src=\"OpenLayers.js\"></script>\n");
      out.write("                    <script src=\"Map_display_render.js\"></script>\n");
      out.write("                </div>\n");
      out.write("\n");
      out.write("                <!--skeletal structure for dynamically adding and deleting fields for each functionality-->\n");
      out.write("                <div class=\"container\">\n");
      out.write("                    <div class=\"mapsetCenterBlock\">      \n");
      out.write("                        <div id=\"mapsettings\" name=\"mapsettings\"></div>\n");
      out.write("\n");
      out.write("                        <div id=\"Get_info\" name=\"Get_info\"></div>\n");
      out.write("\n");
      out.write("                        <div id=\"measure_dist\" name=\"measure_dist\"></div>\n");
      out.write("                        \n");
      out.write("                        <div id=\"output\" class=\"distLayoutBox cf\" hidden=\"true\"></div> <!--apply some styling here-->\n");
      out.write("                            <div id=\"distdiv\" hidden=\"true\" class=\"distunitsBox cf\">\n");
      out.write("                                    <label>UNIT SELECTED</label>\n");
      out.write("                                    <select id=\"distunits\" name=\"distunits\"  onchange=\"unitchange(this)\"> \n");
      out.write("                                        <option>metres</option>\n");
      out.write("                                        <option>decimetres</option>\n");
      out.write("                                        <option>centimetres</option>\n");
      out.write("                                        <option>milimetres</option>\n");
      out.write("                                        <option>miles</option>\n");
      out.write("                                        <option>nautical miles</option>\n");
      out.write("                                        <option>yards</option>\n");
      out.write("                                        <option>feet</option>\n");
      out.write("                                        <option>inches</option>\n");
      out.write("                                    </select>\n");
      out.write("                                </div>\n");
      out.write("                            \n");
      out.write("                            <div id=\"areadiv\" hidden=\"true\" class=\"distunitsBox cf\">\n");
      out.write("                                    <label>UNIT SELECTED</label>\n");
      out.write("                                    <select id=\"areaunits\" name=\"areaunits\"  onchange=\"unitchange(this)\">\n");
      out.write("                                        <option>metres</option>\n");
      out.write("                                        <option>hectares</option>\n");
      out.write("                                        <option>miles</option>\n");
      out.write("                                        <option>acres</option>\n");
      out.write("                                        <option>feet</option>\n");
      out.write("                                        <option>yard</option>\n");
      out.write("                                    </select>\n");
      out.write("                                </div>\n");
      out.write("                            \n");
      out.write("                        </div>\n");
      out.write("                        \n");
      out.write("                        <div id=\"measure_area\" name=\"measure_area\"></div>\n");
      out.write("                        \n");
      out.write("\n");
      out.write("                        <div id=\"find_place\" name=\"find_place\"></div>\n");
      out.write("\n");
      out.write("                        <div id=\"createbufferdiv\" name=\"createbufferdiv\"></div>\n");
      out.write("\n");
      out.write("                        <div id=\"databufferdiv\" name=\"databufferdiv\"></div>\n");
      out.write("                    </div>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("            \n");
      out.write("\t</body>\n");
      out.write("</html>\n");
      out.write("\n");
      out.write("              ");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
