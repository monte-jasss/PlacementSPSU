<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$email = $source->validateData($_POST['email']);
		$title = $source->validateData($_POST['title']);
		$description = $source->validateData($_POST['description']);
		$requirement = $source->validateData($_POST['requirement']);
		$opening = $source->validateData($_POST['opening']);
		$lastdate = $source->validateData($_POST['lastdate']);

		try {
			$source->addJob($source->getIdByEmail($email), $title, $description, $requirement, $opening, $lastdate);
		} catch (PDOException $e) {
			$existingkey = "Integrity constraint violation: 1062 Duplicate entry";
			if (strpos($e->getMessage(), $existingkey) !== FALSE) {
				echo json_encode("Duplicate Entry !!");
			} else {
				throw $e;
			}
		}
	}
?>
