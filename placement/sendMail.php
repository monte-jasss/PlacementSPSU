<?php
	use PHPMailer\PHPMailer\PHPMailer;
	use PHPMailer\PHPMailer\Exception;
	require 'vendor/autoload.php';

	$mail = new PHPMailer(true);
	$otp = 123456;

	try {
	    $mail->isSMTP();                                            // Set mailer to use SMTP
	    $mail->Host       = 'smtp.gmail.com';  // Specify main and backup SMTP servers
	    $mail->SMTPAuth   = true;                                   // Enable SMTP authentication
			$mail->Username = 'mlakshkar5@gmail.com';
			$mail->Password = 'Monte.jas@361';
			$mail->SetFrom = 'no-reply@gmail.com';                              // SMTP password
	    $mail->SMTPSecure = 'tls';                                  // Enable TLS encryption, `ssl` also accepted
	    $mail->Port       = 587;                                    // TCP port to connect to

	    $mail->setFrom('no-reply@gmail.com');
	    $mail->addAddress('monu.lakshkar@spsu.ac.in', 'Monu Lakshkar');

	    $mail->isHTML(true);                                  // Set email format to HTML
	    $mail->Subject = 'One Time Password';
	    $mail->Body    = '</b> your One Time Password for <b>Placement Cell</b> account verification is<br><center><b style="color:red;font-size:200%">'.generateNumericOTP().'</center>';

	    $mail->send();
	    echo 'Message has been sent';
	} catch (Exception $e) {
	    echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
	}
?>
