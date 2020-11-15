<html>
<head>
    <title>SpringBoot整合Freemarker</title>
</head>
<body>

<table border="1" align="center" width="50%">
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Age</th>
    </tr>
    <#list users as user>
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.age}</td>
        </tr>
    </#list>
</table>
</body>

</html>