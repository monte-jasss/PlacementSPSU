<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$id = $source->validateData($_POST['id']);

		try {
			$source->getProfile($id);
		} catch (PDOException $e) {
			throw $e;
		}
	}
?>
