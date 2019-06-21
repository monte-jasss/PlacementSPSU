<?php
	require_once "Traits.php";

	class Source {
		use Insert;
		use Validate;
		use Fetch;
		use Upsert;
		use Delete;
	}
?>
