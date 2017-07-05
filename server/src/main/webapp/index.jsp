<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.larinia.servlet.HelloWorldFrame" %>
<%
    Object counterObj = session.getAttribute("counter");
    int counter = 0;
    if (counterObj != null && counterObj instanceof Integer) {
        counter = ((Integer) counterObj).intValue();
    }
    counter++;
    session.setAttribute("counter", new Integer(counter));
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cluster counter</title>
    </head>
    <body>
        <h1>Hello World!</h1>

        <!--%
                boolean flag = false;
                while (flag) {

                     System.out.println("AHaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                   }


            new HelloWorldFrame();


                %-->
                <% System.out.println("System.getProperties() is ######## "+System.getProperties());%>




        <table>
            <tr><td>Number of visits to this page</td><td><%=counter%></td></tr>
            <tr><td>request.getServerName() - Apache:</td> <td><%=request.getServerName()%></td></tr>
            <tr><td>request.getSession().getId():</td>  <td><%=request.getSession().getId()%></td></tr>
            <tr><td>java.net.InetAddress.getLocalHost().getHostName():</td><td><%=java.net.InetAddress.getLocalHost().getHostName()%></td></tr>
            <tr><td>Now Beautiful pictures</td></tr>
            <tr><td><img src="images/images_totoro.jpg" ></td></tr>

            <tr><td><img src="images/snowdrop.jpg" ></td></tr>
            <tr><td><img src="images/snowdrops2.jpg" ></td></tr>
        </table>



    </body>
</html>
