<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$email = $source->validateData($_POST['email']);
		$pswd = $source->validateData($_POST['password']);

		try {
			$source->doLogin($email, md5($pswd));
		} catch (PDOException $e) {
			throw $e;
		}
	}
?>
