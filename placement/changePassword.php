<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$id = -1;
		$email = $source->validateData($_POST['email']);
		$pass = md5($source->validateData($_POST['password']));

		try {
			$id = $source->getIdByEmail($email);
			echo $id;
			if($id==""){
				echo json_encode("User Not Found !!");
			} else {
				$source->changePassword($email,$pass);
			}
		} catch (PDOException $e) {
			throw $e;
		}
	}
?>
