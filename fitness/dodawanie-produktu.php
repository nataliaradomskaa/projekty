<?php
session_start();

require_once "polaczenie.php";
mysqli_report(MYSQLI_REPORT_STRICT);

try {
    $polaczenie = new mysqli($host, $user, $haslo, $bazadanych);

    if ($polaczenie->connect_errno != 0) {
        throw new Exception(mysqli_connect_errno());
    } else {

        if (isset($_POST['dodaj_produkt'])) {

            $nazwa_produktu = $_POST['nazwa_produktu'];
            $producent = ($_POST['producent']);
            $cena = $_POST['cena'];
            $opis = $_POST['opis'];
            $zdjecie = $_POST['zdjecie'];
            $dodaj = true;

            if (empty($nazwa_produktu)) {
                $_SESSION['blad'] = "Nazwa produktu nie może być pusta!";
                $dodaj = false;
                header('Location: paneladmin.php');
            }

            if (empty($producent)) {
                $_SESSION['blad'] = "Nazwa producenta nie może być pusta!";
                $dodaj = false;
                header('Location: paneladmin.php');
            }
                $pattern = '/^(0|[1-9]\d*)(\.\d{2})?$/';
                if (preg_match($pattern, $_POST['cena']) == '0') {

                $_SESSION['blad'] = "Cena musi być liczbą rzeczywistą!";
                $dodaj = false;
                header('Location: paneladmin.php');
            }

            if (empty($opis)) {
                $_SESSION['blad'] = "Opis produktu nie może być pusty!";
                $dodaj = false;
                header('Location: paneladmin.php');
            }

            if ($dodaj == true) {

                $sql = "INSERT INTO produkt VALUES (NULL, '$nazwa_produktu', '$producent', '$cena', '$zdjecie', '$opis')";

                if (mysqli_query($polaczenie, $sql)) {
                    unset($_SESSION['blad']);
                    header('Location: podgladsklepu.php');
                } else {
                    echo " Error: " . $sql . "<br>" . mysqli_error($polaczenie);
                    // header('Location: paneladmin.php');
                }
            }
        }
    }
    $polaczenie->close();
} catch (Exception $e) {
    echo '<span style="color:red;">Błąd serwera! Przepraszamy za niedogodności i prosimy o wizytę w innym terminie!</span>';
    echo '<br />Informacja developerska: ' . $e;
}
