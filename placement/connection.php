<?php
	ini_set('display_errors', 1);
	ini_set('log_errors', 1);

	$servername = "localhost";
	$username = "root";
	$password = "";
	$dbname = "db_spsu";
	$charset = 'utf8mb4';

	$dsn = "mysql:host=$servername;dbname=$dbname;charset=$charset";
	$opt = [
		PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
		PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
		PDO::ATTR_EMULATE_PREPARES   => false,
	];

	try {
		$conn = new PDO($dsn, $username, $password, $opt);
	}
	catch (PDOException $e) {
		exit("Error: " . $e->getMessage());
	}
?>
