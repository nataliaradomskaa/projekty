<?php
	session_start();
	unset($_SESSION['koszyk']);
	$_SESSION['message'] = 'Koszyk wyczyszczony!';
	header('location: koszyk.php');
?>