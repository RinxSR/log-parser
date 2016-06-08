<%--
  Created by IntelliJ IDEA.
  User: rinxsr
  Date: 06.06.16
  Time: 16:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>MyLogParser</title>
  </head>
  <body>

  <form action="LogFileHandling" method="post" enctype="multipart/form-data">
    <p>Выберите *.log файл для обработки</p>
    <p><input type="file" name="fileToHandling">
      <input type="submit" value="Обработать файл"></p>
  </form>

  </body>
</html>
