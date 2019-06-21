<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$email = $source->validateData($_POST['email']);

		try {
			$source->getProfile($source->getIdByEmail($email));
		} catch (PDOException $e) {
			throw $e;
		}
	}
?>
