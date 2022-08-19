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
$result_order = $conn->query("SELECT id FROM orders ORDER BY id DESC LIMIT 1");
$result_order_item = $conn->query("SELECT id FROM order_item ORDER BY id DESC LIMIT 1");


$list_order_id = [];
$list_order_item_id = [];


while ($current_order = $result_order->fetch_assoc()) {
    $list_order_id[] = $current_order;
}

while ($current_order_item = $result_order_item->fetch_assoc()) {
    $list_order_item_id[] = $current_order_item;
}

$conn->close();


if($list_order_id==null){
    $last_id_order = 1;
}else{
    $last_id_order = implode(end($list_order_id));
    $last_id_order = (int)$last_id_order+1;
    $last_id_order = (string)$last_id_order;
}

if($list_order_item_id==null){
    $last_id_order_item = 1;
}else{
    $last_id_order_item = implode(end($list_order_item_id));
    $last_id_order_item = (int)$last_id_order_item+1;
    $last_id_order_item = (string)$last_id_order_item;
}

if(isset($_SESSION['Order_id'])) {
    unset($_SESSION['Order_id']);
}

if(isset($_SESSION['Order_id_item'])) {
    unset($_SESSION['Order_id_item']);
}

$_SESSION['Order_id'] = $last_id_order;
$_SESSION['Order_id_item'] = $last_id_order_item;




