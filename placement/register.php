<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$name = $source->validateData($_POST['name']);
		$email = $source->validateData($_POST['email']);
		$mobile = $source->validateData($_POST['mobile']);
		$pswd = $source->validateData($_POST['password']);
		$type = $source->validateData($_POST['type']);
		$otp = $source->generateNumericOTP();

		try {
			if($type==0){
				$source->doRegistration($name, $email, $mobile, md5($pswd), $type, $otp);
				$source->sendMail($name,$email, $otp);
			} else {
				$source->doRegistration($name, $email, $mobile, md5($pswd), $type, 0);
			}
		} catch (PDOException $e) {
			$existingkey = "Integrity constraint violation: 1062 Duplicate entry";
			if (strpos($e->getMessage(), $existingkey) !== FALSE) {
				echo json_encode("Email or Mobile already exist !!");
			} else {
				throw $e;
			}
		}
	}
?>
