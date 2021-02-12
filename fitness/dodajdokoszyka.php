<?php
	session_start();

    // sprawdzenie czy dany podrukt jest juz dodany
    
    if(!(in_array($_GET['id_produktu'], $_SESSION['koszyk']))){ // jesli nie to dodaj
        
		array_push($_SESSION['koszyk'], $_GET['id_produktu']); // do koszyka dodaj dany produkt
		$_SESSION['message'] = 'Produkt dodany!';
	}
	else{
		$_SESSION['message'] = 'Produkt jest już w koszyku!';
    }

	header('location: skleppo.php');
