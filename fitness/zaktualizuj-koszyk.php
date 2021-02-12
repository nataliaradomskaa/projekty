<?php
	session_start();
	if(isset($_POST['zaktualizuj'])){
		foreach($_POST['indeksy'] as $key){
			$_SESSION['qty_array'][$key] = $_POST['qty_'.$key];
		}

		$_SESSION['message'] = 'Koszyk zaktualizowany!';
		header('location: koszyk.php');
	}
?>
