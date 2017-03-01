<?php

$servername = "localhost:8889/testLogin";
$username = "root";
$password = "root";
$dbname = "login";

try {
    	$conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }
catch(PDOException $e)
    {
    	die("Problème de connexion");
    }

?>
