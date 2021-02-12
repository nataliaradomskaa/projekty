<?php
session_start();

require_once "polaczenie.php";
mysqli_report(MYSQLI_REPORT_STRICT);

try {
    $polaczenie = new mysqli($host, $user, $haslo, $bazadanych);

    if ($polaczenie->connect_errno != 0) {
        throw new Exception(mysqli_connect_errno());
    } else {

        if (isset($_POST['dodaj_karnet'])) {

            $nazwa = $_POST['nazwa_k'];
            $cena = $_POST['cena_k'];
            $czas_trwania = $_POST['czas_k'];
            $uwaga = $_POST['uwaga_k'];
            $dodaj = true;

            if (empty($nazwa)) {
                $_SESSION['blad'] = "Nazwa karnetu nie może być pusta!";
                $dodaj = false;
                header('Location: paneladmin.php');
            }

            $pattern = '/^(0|[1-9]\d*)(\.\d{2})?$/';
            if (preg_match($pattern, $_POST['cena_k']) == '0') {

                $_SESSION['blad'] = "Cena musi być liczbą rzeczywistą!";
                $dodaj = false;
                header('Location: paneladmin.php');
            }

            if (empty($uwaga)) {
                $_SESSION['blad'] = "Opis produktu nie może być pusty!";
                $dodaj = false;
                header('Location: paneladmin.php');
            }

            if ($dodaj == true) {

                $sql = "INSERT INTO karnet VALUES (NULL, '$nazwa', '$cena', '$czas_trwania', '$uwaga')";

                if (mysqli_query($polaczenie, $sql)) {
                    unset($_SESSION['blad']);
                    header('Location: podgladkarnetow.php');
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
