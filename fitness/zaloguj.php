<?php
session_start();

if ((!isset($_POST['login'])) || (!isset($_POST['haslo']))) {
	header('Location: index.php');
	exit();
}

require_once "polaczenie.php";
mysqli_report(MYSQLI_REPORT_STRICT);

try {
	$polaczenie = new mysqli($host, $user, $haslo, $bazadanych);

	if ($polaczenie->connect_errno != 0) {
		throw new Exception(mysqli_connect_errno());
	} else {
		$login = $_POST['login'];
		$haslo = $_POST['haslo'];

		$sql = "SELECT * FROM uzytkownicy WHERE login = '" . $login . "'";

		$rezultat = mysqli_query($polaczenie, $sql);
		if (mysqli_num_rows($rezultat) > 0)
		//if (($rezultat->num_rows) > 0) 
		{
			$wiersz = $rezultat->fetch_assoc();
			//$wiersz = mysqli_fetch_array($rezultat);
			if (password_verify($haslo, $wiersz['haslo'])) {
				$_SESSION['zalogowany'] = true;
				$_SESSION['id_uzytkownika'] = $wiersz['id_uzytkownika'];
				$_SESSION['rodzaj_uzytkownika'] = $wiersz['rodzaj_uzytkownika'];
				$_SESSION['login'] = $wiersz['login'];

				unset($_SESSION['blad']);
				$rezultat->free_result();
				if ($_SESSION['rodzaj_uzytkownika'] == 'user') {
					header('Location: user-index.php');
				}
				if ($_SESSION['rodzaj_uzytkownika'] == 'admin') {
					header('Location: paneladmin.php');
				}
			} else {
				$_SESSION['blad'] = "Nieprawidłowy login lub hasło!";
				//'<span style="color:red">Nieprawidłowy login lub hasło!</span>';
				header('Location: logowanie.php');
			}
		} else {
			$_SESSION['blad'] = '<span style="color:red">Nieprawidłowy login lub hasło!</span>';
			header('Location: logowanie.php');
		}
	}
	/*else
			{
				throw new Exception($polaczenie->error);
			}*/

	$polaczenie->close();
} catch (Exception $e) {
	echo '<span style="color:red;">Błąd serwera! Przepraszamy za niedogodności i prosimy o wizytę w innym terminie!</span>';
	echo '<br />Informacja developerska: ' . $e;
}
