<?php
session_start();

require_once "polaczenie.php";
mysqli_report(MYSQLI_REPORT_STRICT);

try {
    $polaczenie = new mysqli($host, $user, $haslo, $bazadanych);

    if ($polaczenie->connect_errno != 0) {
        throw new Exception(mysqli_connect_errno());
    } else {

        if (isset($_POST['aktualizuj'])) {
            $starehaslo = $_POST['starehaslo'];
            $nowehaslo1 = $_POST['nowehaslo1'];
            $nowehaslo2 = $_POST['nowehaslo2'];
            $login = $_SESSION['login'];

            $zmien = true;
            $sql = "SELECT * FROM uzytkownicy WHERE login = '" . $login . "'";
            $rezultat = mysqli_query($polaczenie, $sql);
            $wiersz = $rezultat->fetch_assoc();

            if ((strlen($nowehaslo1) < 8) || (strlen($nowehaslo1) > 15)) {
                $zmien = false;
                $_SESSION['error_haslo'] = "Nowe hasło musi posiadać od 8 do 15 znaków!";
                header('Location: haslo.php');
            }
            if ($nowehaslo1 != $nowehaslo2) {
                $zmien = false;
                $_SESSION['error_haslo'] = "Podane hasła są różne!";
                header('Location: haslo.php');
            }
            if (password_verify($starehaslo, $wiersz['haslo']) == false) {
                $zmien = false;
                $_SESSION['error_haslo'] = "Wpisano złe stare hasło!";
                header('Location: haslo.php');
            }

            if ($zmien == true) {

                $haslo_hash = password_hash($nowehaslo1, PASSWORD_DEFAULT);

                //dodawanie nowego hasla do bazy
                $sql2 = "UPDATE uzytkownicy SET haslo = '" . $haslo_hash . "' WHERE login = '" . $login . "'";
                if (mysqli_query($polaczenie, $sql2)) {
                    $_SESSION['dobre_haslo'] = "Hurra! Hasło zostało zaktualizowane.";
                    unset($_SESSION['error_haslo']);
                    $rezultat->free_result();
                    header('Location: haslo.php');
                } else {
                    echo " Error: " . $sql2 . "<br>" . mysqli_error($polaczenie);
                }
            }
        }
    }
    $polaczenie->close();
} catch (Exception $e) {
    echo '<span style="color:red;">Błąd serwera! Przepraszamy za niedogodności i prosimy o wizytę w innym terminie!</span>';
    echo '<br />Informacja developerska: ' . $e;
}
