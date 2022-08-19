<?php
session_start();

$servername = "localhost";
$username = "root";
$password = "";
$conn = new mysqli($servername, $username, $password);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$conn->select_db("online_shop");

$orderId = $_SESSION['Order_id'];
$order_itemId = $_SESSION['Order_id_item'];
$data = $_SESSION['Data'];
$Today = date("Y-m-d");
$conn->query("INSERT INTO orders(id, Data_zam, Status) VALUES ('$orderId', '$Today','Realizacja')");

foreach ($data as $row){
    $product = $row['Product_id'];
    $amount = $row['Amount'];
    $conn->query("INSERT INTO order_item(id, Order_id, Product_id, Amount) VALUES ('$order_itemId','$orderId','$product','$amount')");
    $order_itemId++;
}

$conn->close();



