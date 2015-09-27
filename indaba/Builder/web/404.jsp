<%@page pageEncoding="UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="indabaTaglib" prefix="indaba"%>
<html>
    <head>
        <title>404 - Page not found!</title>
        <style type="text/css">
            body {
                background: #EEE; text-align: center;
                font-family: Arial, Helvetica;
            }

            img{ border: 1px solid #ccc; text-align:center; }

            p {
                color: red;
                font-weight: bold;
            }

        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <div>
            <img src="images/404.jpg" alt="Page not found!"/>
        </div>
        <p><indaba:msg key='src1000.key13'/><p>
    </body>
</html>
