<!DOCTYPE html>
<html lang="">
<head>
    <title>Team checker</title>
</head>
<body>
<h1>Team checker application</h1><hr>
<h4>Создание комнат</h4>
<h2></h2>
<form action="/team/create" method="post">
    Имя: <input type="text" name="username"/><br><br>
    <input type="submit" value="Добавить группу"/>
</form>
<#list teams as team>
    <div>
        <p><b>${team.getCode()} </b> | <a href="/team/${team.code}"> Подробнее.. </a> </p>
    </div>
<#else>
    <h3>Товаров нет</h3>
</#list>
<hr>
<h3>Войти в комнату</h3>
<form action="/team/enter" method="post">
    Имя: <input type="text" name="username"/><br><br>
    Номер комнаты: <input type="text" name="teamId"/><br><br>
    <input type="submit" value="Войти в комнату"/>
</form>
<hr>
<form action="/team/delete" method="post">
    Номер комнаты: <input type="text" name="code"/><br><br>
    <input type="submit" value="Удалить комнату"/>
</form>
</body>
</html>