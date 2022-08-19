<?php
session_start();

$rows = file("order.txt");
$blacklist = "Content|--";

foreach($rows as $key => $row) {
    if(preg_match("/($blacklist)/", $row)) {
        unset($rows[$key]);
    }
}

$dataJSON = json_decode(implode($rows),true);
if(isset($_SESSION['Data'])){
    unset($_SESSION['Data']);
}
$_SESSION['Data'] = $dataJSON;




