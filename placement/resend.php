<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$name='User';
		$email = $source->validateData($_POST['email']);
		$otp = $source->generateNumericOTP();

		try {
				$source->updateOTP($email, $otp);
				$source->sendMail($name,$email, $otp);
		} catch (PDOException $e) {
				throw $e;
		}
	}
?>
