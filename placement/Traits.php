<?php
	require_once "connection.php";
	use PHPMailer\PHPMailer\PHPMailer;
	use PHPMailer\PHPMailer\Exception;
	require 'vendor/autoload.php';

	trait Insert {
		public function doRegistration($n, $e, $m, $p, $t, $o) {
			GLOBAL $conn;

			$a=0;
			if($t==1){
				$a=1;
			}

			$query = "INSERT INTO tbl_user VALUES ( NULL, :n, :e, :m, :p, :t, :o, :a, '')";
			$stmt = $conn->prepare($query);
			$stmt->execute(['n' => $n, 'e' => $e, 'm' => $m, 'p' => $p, 't' => $t, 'o' => $o, 'a' => $a]);

			/* will perform the same thing
			$stmt->bindParam(':e',$e,PDO::PARAM_STR);
			$stmt->bindParam(':m',$m,PDO::PARAM_INT);
			$stmt->bindParam(':p',$p,PDO::PARAM_STR);

			$stmt->execute(); */

			$lastInsertId = $conn->lastInsertId();
			if($lastInsertId>0) {
				echo json_encode("SUCCESS");
			}
			else {
				echo json_encode("FAILED");
			}

			$conn = NULL;
		}

		public function addJob($i, $t, $d, $r, $o, $l) {
			GLOBAL $conn;
			$query = "INSERT INTO tbl_job VALUES ( NULL, :i, :t, :d, :r, :o, :l)";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i, 't' => $t, 'd' => $d, 'r' => $r, 'o' => $o, 'l' => $l]);

			$lastInsertId = $conn->lastInsertId();
			if($lastInsertId>0) {
				echo json_encode("SUCCESS");
			}
			else {
				echo json_encode("FAILED");
			}

			$conn = NULL;
		}

		public function doApply($j,$s){
			GLOBAL $conn;
			$query = "INSERT INTO tbl_apply (student_id, job_id) VALUES (:s, :j)";
			$stmt = $conn->prepare($query);
			$stmt->execute(['j' => $j, 's' => $s]);

			$lastInsertId = $conn->lastInsertId();
			if($lastInsertId>0) {
				echo json_encode("SUCCESS");
			}
			else {
				echo json_encode("FAILED");
			}

			$conn = NULL;
		}

		public function sendMail($n,$e, $p){
			$mail = new PHPMailer(true);

			try {
				  //$mail->SMTPDebug = 2;
			    $mail->isSMTP();
			    $mail->Host = 'smtp.gmail.com';
			    $mail->SMTPAuth = true;
					$mail->Username = 'mlakshkar5@gmail.com';
					$mail->Password = 'Monte.jas@361';
					$mail->SetFrom = 'no-reply@gmail.com';
			    $mail->SMTPSecure = 'tls';
			    $mail->Port = 587;

			    $mail->setFrom('no-reply@gmail.com');
			    $mail->addAddress($e, 'Monu Lakshkar');

			    $mail->isHTML(true);                                  // Set email format to HTML
			    $mail->Subject = 'One Time Password';
			    $mail->Body    = 'Dear '.$n.',<br> your One Time Password for <b>Placement Cell</b> account verification is<br><center><b style="color:red;font-size:200%">'.$p.'</center>';

			    $mail->send();
			} catch (Exception $e) {
			    //echo "{$mail->ErrorInfo}";
			}
		}

		public function insertFCM($e,$fcm){
			GLOBAL $conn;

			$query = "UPDATE tbl_user SET token=:fcm WHERE email=:e";
			$stmt = $conn->prepare($query);
			$stmt->execute(['fcm' => $fcm,'e' => $e]);

			$count = $stmt->rowCount();
			if($count =='0'){
			    echo "Failed !";
			}
			else{
			    echo "Success !";
			}

			$conn = NULL;
		}
	}

	trait Fetch{
		public function doLogin($e, $p){
			GLOBAL $conn;

			$query = "SELECT password,type,active FROM tbl_user WHERE email=:e";
			$stmt = $conn->prepare($query);
			$stmt->execute(['e' => $e]);
			$row = $stmt->fetch(PDO::FETCH_ASSOC);

			if(!$row) exit(json_encode("NOT"));
			if($row['active']===1){
				if($row['password']===$p){
					if($row['type']===0) echo json_encode("STUDENT");
					else if($row['type']===1) echo json_encode("COMPANY");
					else if($row['type']===2) echo json_encode("ADMIN");
				} else {
					echo json_encode("WRONG");
				}
			} else {
				echo json_encode("ACTIVE");
			}
		}

		public function otpVerify($e,$o){
			GLOBAL $conn;

			$result = [];
			$query = "SELECT otp FROM tbl_user WHERE email=:e";
			$stmt = $conn->prepare($query);
			$stmt->execute(['e' => $e]);
			$item = $stmt->fetch(PDO::FETCH_ASSOC);
			if($item['otp']==$o){
				return 1;
			} else {
				return 0;
			}
		}

		public function getAllCurrentJob($i){
			GLOBAL $conn;

			$result = [];
			$query = "SELECT * FROM tbl_job WHERE company_id=:i";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i]);
			while($item = $stmt->fetch(PDO::FETCH_ASSOC)){
				array_push($result,$item);
			}
			echo json_encode($result);
		}

		public function getAllUserByType($t){
			GLOBAL $conn;

			$result = [];
			$query = "SELECT id, name, email, active FROM tbl_user WHERE type=:t";
			$stmt = $conn->prepare($query);
			$stmt->execute(['t' => $t]);
			while($item = $stmt->fetch(PDO::FETCH_ASSOC)){
				array_push($result,$item);
			}
			echo json_encode($result);
		}

		public function getAllJob(){
			GLOBAL $conn;

			$result = [];
			$query = "SELECT * FROM tbl_job";
			$stmt = $conn->prepare($query);
			$stmt->execute();
			while($item = $stmt->fetch(PDO::FETCH_ASSOC)){
				array_push($result,$item);
			}
			echo json_encode($result);
		}

		public function getJobDetail($i){
			GLOBAL $conn;

			$result = [];
			$query = "SELECT A.*, B.name FROM tbl_job AS A INNER JOIN tbl_user AS B ON A.company_id = B.id WHERE A.id=:i";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i]);
			$item = $stmt->fetch(PDO::FETCH_ASSOC);
			echo json_encode($item);
		}

		public function getMyRequest($i){
			GLOBAL $conn;

			$result = [];
			$query = "SELECT A.status, B.title, C.name FROM tbl_apply AS A, tbl_job AS B, tbl_user AS C WHERE student_id=:i AND A.job_id = B.id AND B.company_id = C.id";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i]);
			while($item = $stmt->fetch(PDO::FETCH_ASSOC)){
				array_push($result,$item);
			}
			echo json_encode($result);
		}

		public function getApplicationList($i){
			GLOBAL $conn;

			$result = [];
			$query = "SELECT A.id, A.student_id, B.name, B.f_name, A.status FROM tbl_apply AS A, tbl_profile AS B WHERE A.student_id=B.student_id AND A.job_id=:i";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i]);
			while($item = $stmt->fetch(PDO::FETCH_ASSOC)){
				array_push($result,$item);
			}
			echo json_encode($result);
		}

		public function getProfile($i){
			GLOBAL $conn;

			$result = [];
			$query = "SELECT name, f_name, course, semester, _10, _12, cgpa, interest, skill, project, training, certificate FROM tbl_profile WHERE student_id=:i";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i]);
			$item = $stmt->fetch(PDO::FETCH_ASSOC);
			if(!$item){
				array_push($result,"NOT");
				exit(json_encode($result));
			}
			array_push($result,$item['name']);
			array_push($result,$item['f_name']);
			array_push($result,$item['course']);
			array_push($result,$item['semester']);
			array_push($result,$item['_10']);
			array_push($result,$item['_12']);
			array_push($result,$item['cgpa']);
			array_push($result,$item['interest']);
			array_push($result,$item['skill']);
			array_push($result,$item['project']);
			array_push($result,$item['training']);
			array_push($result,$item['certificate']);
			echo json_encode($result);
		}

		public function getIdByEmail($e){
			GLOBAL $conn;

			$query = "SELECT id FROM tbl_user WHERE email=:e";
			$stmt = $conn->prepare($query);
			$stmt->execute(['e' => $e]);
			$row = $stmt->fetch(PDO::FETCH_ASSOC);

			return $row['id'];
		}

		public function checkProfile($i){
			GLOBAL $conn;

			$query = "SELECT id FROM tbl_profile WHERE student_id=:i";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i]);
			$row = $stmt->fetch(PDO::FETCH_ASSOC);

			if(!$row) return 0;
			else return 1;
		}
	}

	/**
	 *
	 */
	trait Upsert{
		public function activateAccount($e){
			GLOBAL $conn;

			$query = "UPDATE tbl_user SET active=1,otp=0 WHERE email=:e";
			$stmt = $conn->prepare($query);
			$stmt->execute(['e' => $e]);
			$stmt->fetch(PDO::FETCH_ASSOC);

			$count = $stmt->rowCount();
			if($count =='0') {
				echo json_encode("FAILED");
			}
			else {
				echo json_encode("SUCCESS");
			}
			$conn = NULL;
		}

		public function createProfile($t,$obj){
			GLOBAL $conn;

			$query = "INSERT INTO tbl_profile VALUES (NULL, :i, :n, :f, :c, :s, :tn, :tw, :cg, :ir, :sk, :pr, :tr, :ce)";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $t, 'n' => $obj[1], 'f' => $obj[2], 'c' => $obj[3], 's' => $obj[4], 'tn' => $obj[5], 'tw' => $obj[6],
			'cg' => $obj[7], 'ir' => $obj[8], 'sk' => $obj[9], 'pr' => $obj[10],'tr' => $obj[11],'ce' => $obj[12]]);

			$lastInsertId = $conn->lastInsertId();
			if($lastInsertId>0) {
				echo json_encode("SUCCESS");
			}
			else {
				echo json_encode("FAILED");
			}

			$conn = NULL;
		}

		function updateProfile($t,$obj){
			GLOBAL $conn;

			$query = "UPDATE tbl_profile SET name=:n, f_name=:f, course=:c, semester=:s, _10=:tn, _12=:tw, cgpa=:cg, interest=:ir, skill=:sk, project=:pr, training=:tr, certificate=:ce WHERE student_id=:i";
			$stmt = $conn->prepare($query);
			$stmt->execute(['n' => $obj[1], 'f' => $obj[2], 'c' => $obj[3], 's' => $obj[4], 'tn' => $obj[5], 'tw' => $obj[6],
			'cg' => $obj[7], 'ir' => $obj[8], 'sk' => $obj[9], 'pr' => $obj[10], 'tr' => $obj[11], 'ce' => $obj[12], 'i' => $t]);

			$count = $stmt->rowCount();
			if($count =='0') {
				echo json_encode("FAILED");
			}
			else {
				echo json_encode("UPDATED");
			}
			$conn = NULL;
		}

		public function updateOTP($e,$o){
			GLOBAL $conn;

			$query = "UPDATE tbl_user SET otp=:o WHERE email=:e";
			$stmt = $conn->prepare($query);
			$stmt->execute(['o' => $o, 'e' => $e]);

			$count = $stmt->rowCount();
			if($count =='0') {
				echo json_encode("FAILED");
			}
			else {
				echo json_encode("SUCCESS");
			}
			$conn = NULL;
		}

		public function changePassword($e,$p){
			GLOBAL $conn;

			$query = "UPDATE tbl_user SET password=:p WHERE email=:e";
			$stmt = $conn->prepare($query);
			$stmt->execute(['p' => $p, 'e' => $e]);

			$count = $stmt->rowCount();
			if($count =='0') {
				echo json_encode("FAILED");
			}
			else {
				echo json_encode("SUCCESS");
			}
			$conn = NULL;
		}

		public function setStatus($i,$s){
			GLOBAL $conn;

			$query = "UPDATE tbl_apply SET status=:s WHERE id=:i";
			$stmt = $conn->prepare($query);
			$stmt->execute(['s' => $s, 'i' => $i]);

			$count = $stmt->rowCount();
			if($count =='0') {
				echo json_encode("FAILED");
			}
			else {
				echo json_encode("SUCCESS");
			}
			$conn = NULL;
		}
	}

	trait Delete {
		public function deleteJob($i) {
			GLOBAL $conn;

			$result = [];
			$query = "DELETE FROM tbl_job WHERE id=:i";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i]);

			$count = $stmt->rowCount();
			if($count=='0') {
				echo json_encode("FAILED");
			}
			else {
				echo json_encode("SUCCESS");
			}
		}

		public function deleteUser($i,$t) {
			GLOBAL $conn;

			$result = [];
			$query = "DELETE FROM tbl_user WHERE id=:i AND type=:t";
			$stmt = $conn->prepare($query);
			$stmt->execute(['i' => $i, 't' => $t]);

			$count = $stmt->rowCount();
			if($count=='0') {
				echo json_encode("FAILED");
			}
			else {
				echo json_encode("SUCCESS");
			}
		}
	}

	trait Validate {
		public function validateData($data) {
			$data = trim($data);
			$data = stripslashes($data);
			$data = htmlspecialchars($data);
			return $data;
		}

		public function generateNumericOTP() {
		    $generator = "1357902468";
		    $result = "";
		    for ($i = 1; $i <= 6; $i++) {
		        $result .= substr($generator, (rand()%(strlen($generator))), 1);
		    }
		    return $result;
		}
	}
?>
