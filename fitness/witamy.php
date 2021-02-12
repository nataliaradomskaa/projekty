<?php

	session_start();
	
	if (!isset($_SESSION['udanarejestracja']))
	{
		header('Location: index.php');
		exit();
	}
	else
	{
		unset($_SESSION['udanarejestracja']);
	}

	//Usuwanie błędów rejestracji
	if (isset($_SESSION['error_imie'])) unset($_SESSION['error_imie']);
	if (isset($_SESSION['error_nazwisko'])) unset($_SESSION['error_nazwisko']);
	if (isset($_SESSION['error_login'])) unset($_SESSION['error_login']);
	if (isset($_SESSION['error_email'])) unset($_SESSION['error_email']);
	if (isset($_SESSION['error_haslo'])) unset($_SESSION['error_haslo']);
	
?>

<!DOCTYPE HTML>
<html lang="pl">
<head>
	<meta charset="utf-8" />
	<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> --> 
	<title>Fitness Club</title>
</head>

<body>
	
	Dziękujemy za rejestrację! Możesz zalogować się na swoje konto!<br /><br />
	
	<a href="logowanie.php">Zaloguj się na swoje konto!</a>
	<br /><br />

</body>
</html>