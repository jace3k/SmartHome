<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>

<body>
<nav class="navbar navbar-expand-md navbar-dark navbar-static-top bg-dark">
    <g:link controller="main" action="index"><div class="navbar-brand">SENSOR DOJO</div></g:link>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item"><g:link class="nav-link" controller="main" action="index">Home</g:link></li>
            <li class="nav-item"><g:link class="nav-link" controller="main" action="addRoom">Dodaj pokój</g:link></li>
            <li class="nav-item"><g:link class="nav-link" uri="/help">Pomoc</g:link></li>
        </ul>
    </div>
</nav>

<g:layoutBody/>

<div class="footer" role="contentinfo">
    Sensor Dojo - Aplikacja utworzona w celach edukacyjnych. Projekt Zespołowy.
</div>


<asset:javascript src="application.js"/>

</body>
</html>
