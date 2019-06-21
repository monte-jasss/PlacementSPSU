<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$id = $source->validateData($_POST['id']);
		$type = $source->validateData($_POST['type']);

		try {
			$source->deleteUser($id,$type);
		} catch (PDOException $e) {
			throw $e;
		}
	}
?>
