<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$obj = [];
		array_push($obj,array(
				$source->validateData($_POST['email']),
				$source->validateData($_POST['name']),
				$source->validateData($_POST['fname']),
				$source->validateData($_POST['course']),
				$source->validateData($_POST['semester']),
				$source->validateData($_POST['_10']),
				$source->validateData($_POST['_12']),
				$source->validateData($_POST['cgpa']),
				$source->validateData($_POST['interest']),
				$source->validateData($_POST['skill']),
				$source->validateData($_POST['project']),
				$source->validateData($_POST['training']),
				$source->validateData($_POST['certificate'])
			)
		);

		try {
			$type = $source->getIdByEmail($obj[0][0]);
			$action = $source->checkProfile($type);
			if($action==0){
				$source->createProfile($type,$obj[0]);
			} else if($action==1) {
				$source->updateProfile($type,$obj[0]);
			}
		} catch (PDOException $e) {
			$existingkey = "Integrity constraint violation: 1062 Duplicate entry";
			if (strpos($e->getMessage(), $existingkey) !== FALSE) {
				echo json_encode("Already Created or Failed to Update !!");
			} else {
				throw $e;
			}
		}
	}
?>
