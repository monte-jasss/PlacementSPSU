<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$status = $source->validateData($_POST['status']);
		$id = $source->validateData($_POST['id']);

		try {
			$source->setStatus($id,$status);
		} catch (PDOException $e) {
			throw $e;
		}
	}
?>
