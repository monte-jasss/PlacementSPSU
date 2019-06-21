<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$t = $source->validateData($_POST['type']);

		try {
			$source->getAllUserByType($t);
		} catch (PDOException $e) {
			throw $e;
		}
	}
?>
