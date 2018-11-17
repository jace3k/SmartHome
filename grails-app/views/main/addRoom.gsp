<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
    <h1>Dodaj nowy pokój</h1>
<g:form action="addRoom" >
    <label for="roomName">Nazwa</label>
    <g:textField name="roomName" />
    <g:submitButton name="Dodaj" />
</g:form>
</body>
</html>