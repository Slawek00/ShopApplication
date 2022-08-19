<?php
header('Content-Type: application/json; charset=utf-8');

$servername = "localhost";
$username = "root";
$password = "";

$conn = new mysqli($servername, $username, $password);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$conn->select_db("online_shop");
$result = $conn->query("SELECT * FROM products");
$list = [];
while ($currentResult = $result->fetch_assoc()) {
    $list[] = $currentResult;
}
echo json_encode($list,JSON_PRETTY_PRINT);
$conn->close();




