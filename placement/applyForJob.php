<?php
	require 'connection.php';

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require "Source.php";
		$source = new Source();

		$job_id = $source->validateData($_POST['job_id']);
		$std_id = $source->getIdByEmail($source->validateData($_POST['email']));

		try {
			$source->doApply($job_id,$std_id);
		} catch (PDOException $e) {
			$existingkey = "Integrity constraint violation: 1062 Duplicate entry";
			if (strpos($e->getMessage(), $existingkey) !== FALSE) {
				echo json_encode("APPLIED");
			} else {
				throw $e;
			}
		}
	}
?>
