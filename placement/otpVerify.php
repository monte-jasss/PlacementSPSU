<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$email = $source->validateData($_POST['email']);
		$otp = $source->validateData($_POST['otp']);

		try {
				$c = $source->otpVerify($email, $otp);
				if($c==1){
					$source->activateAccount($email);
				} else {
					echo json_encode("FAILED");
				}
		} catch (PDOException $e) {
				throw $e;
		}
	}
?>
