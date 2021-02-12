<?php
	session_start();
	session_unset(); 
	
if (isset($_SESSION['zalogowany'])) {
	unset($_SESSION['zalogowany']);
}

	header('Location: index.php');
?>
