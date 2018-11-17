<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title></title>
</head>

<body>
    <h1>Witaj w inteligentnym domu!</h1>
<br />

<p>
    <g:form action="sendMessage">
        <label for="number">Wiadomość:</label>
        <g:textField name="number" />

        <g:submitButton name="submit" />
    </g:form>
</p>

<br />
    Twoje pokoje:
<p>
    <g:each in="${rooms}" var="roomName">
        <g:link uri="/${roomName.name}">Pokój ${roomName.name}</g:link>
    </g:each>

</p>
</body>
</html>