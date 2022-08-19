<?php
header('Content-Type: application/json; charset=utf-8');
$data = file_get_contents("php://input");
file_put_contents('order.txt', $data);
require_once 'get_last_id.php';
require_once 'decode_request.php';
require_once 'new_order.php';



