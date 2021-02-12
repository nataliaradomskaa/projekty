<?php
session_start();

require_once "polaczenie.php";
mysqli_report(MYSQLI_REPORT_STRICT);

try {
    $polaczenie = new mysqli($host, $user, $haslo, $bazadanych);

    if ($polaczenie->connect_errno != 0) {
        throw new Exception(mysqli_connect_errno());
    } else {

        if (isset($_POST['usun_karnet'])) {

            $id_karnetu = $_POST['id_karnetu'];


            $sql = "DELETE FROM karnet WHERE id_karnetu = '" . $id_karnetu . "'";

            if (mysqli_query($polaczenie, $sql)) {
                unset($_SESSION['blad']);
                header('Location: podgladkarnetow.php');
            } else {
                echo " Error: " . $sql . "<br>" . mysqli_error($polaczenie);
                // header('Location: podgladkarnetow.php');
            }
        }
    }
    $polaczenie->close();
} catch (Exception $e) {
    echo '<span style="color:red;">Błąd serwera! Przepraszamy za niedogodności i prosimy o wizytę w innym terminie!</span>';
    echo '<br />Informacja developerska: ' . $e;
}
