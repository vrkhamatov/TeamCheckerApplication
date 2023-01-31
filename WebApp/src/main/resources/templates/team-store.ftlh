<!DOCTYPE html>
<html lang="">
<head>
    <title>Team checker</title>
</head>
<body>
<h1>Team check</h1><hr>
<h4>Состав комнаты</h4>
<#list listOfUsers as users>
    <div>
        <p><b>${users}</b> </p>
    </div>
</#list>
<hr>
</body>
</html>