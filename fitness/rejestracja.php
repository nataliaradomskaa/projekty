<?php
session_start();

if ((isset($_SESSION['zalogowany'])) && ($_SESSION['zalogowany'] == true) && $_SESSION['id_uzytkownika'] = 'user') {
    header('Location: user-index.php');
    exit();
}

if (isset($_POST['login'])) {

    $zarejestruj = true;
    $imie = $_POST['imie'];
    $nazwisko = $_POST['nazwisko'];
    $login = $_POST['login'];
    $email = $_POST['email'];
    $haslo1 = $_POST['haslo1'];
    $haslo2 = $_POST['haslo2'];

    //walidacja imienia i nazwiska
    if (ctype_alpha($imie) == false) {
        $zarejestruj = false;
        $_SESSION['error_imie'] = "Imię musi składać się tylko z liter!";
    }
    if ((strlen($imie) < 3) || (strlen($imie) > 20)) {
        $zarejestruj = false;
        $_SESSION['error_imie'] = "Imię musi posiadać od 3 do 20 znaków!";
    }
    if (ctype_alpha($nazwisko) == false) {
        $zarejestruj = false;
        $_SESSION['error_nazwisko'] = "Nazwisko musi składać się tylko z liter!";
    }
    if ((strlen($nazwisko) < 3) || (strlen($nazwisko) > 40)) {
        $zarejestruj = false;
        $_SESSION['error_nazwisko'] = "Nazwisko musi posiadać od 3 do 40 znaków!";
    }

    //walidacja loginu
    if ((strlen($login) < 3) || (strlen($login) > 12)) {
        $zarejestruj = false;
        $_SESSION['error_login'] = "Login musi posiadać od 3 do 12 znaków!";
    }
    if (ctype_alnum($login) == false) {
        $zarejestruj = false;
        $_SESSION['error_login'] = "Login musi składać się z liter i/lub cyfr!";
    }

    //walidacja adresu e-mail
    $temp = filter_var($email, FILTER_SANITIZE_EMAIL);
    if ((filter_var($temp, FILTER_VALIDATE_EMAIL) == false) || ($temp != $email))
        $zarejestruj = false;
    $_SESSION['error_email'] = "Podaj poprawny adres e-mail!";

    //walidacja hasel 
    if ((strlen($haslo1) < 8) || (strlen($haslo1) > 15)) {
        $zarejestruj = false;
        $_SESSION['error_haslo'] = "Hasło musi posiadać od 8 do 15 znaków!";
    }
    if ($haslo1 != $haslo2) {
        $zarejestruj = false;
        $_SESSION['error_haslo'] = "Podane hasła są różne!";
    }
    $haslo_hash = password_hash($haslo1, PASSWORD_DEFAULT);

    require_once "polaczenie.php";
    mysqli_report(MYSQLI_REPORT_STRICT);

    try {
        $polaczenie = new mysqli($host, $user, $haslo, $bazadanych);
        if ($polaczenie->connect_errno != 0) {
            throw new Exception(mysqli_connect_errno());
        } else {
            //Czy email już istnieje?
            $rezultat = $polaczenie->query("SELECT id_uzytkownika FROM uzytkownicy WHERE email = '$email'");

            if (!$rezultat) throw new Exception($polaczenie->error);

            // $ile_takich_maili = $rezultat->num_rows;
            if (($rezultat->num_rows) > 0) {
                $zarejestruj = false;
                $_SESSION['error_email'] = "Podany adres e-mail jest już zajęty!";
            }

            // sprawdzenie czy nick jest juz zajety
            $rezultat = $polaczenie->query("SELECT id_uzytkownika FROM uzytkownicy WHERE login = '$login'");

            if (!$rezultat) throw new Exception($polaczenie->error);

            $ile_takich_nickow = $rezultat->num_rows; 
            if ($ile_takich_nickow > 0) {
                $zarejestruj = false;
                $_SESSION['error_login'] = "Podany login jest już zajęty!";
            }

            if ($zarejestruj == true) {
                if ($polaczenie->query("INSERT INTO uzytkownicy VALUES (NULL, '$imie', '$nazwisko', '$login', '$email', '$haslo_hash', 'user')")) {
                    $_SESSION['udanarejestracja'] = true;
                    header('Location: witamy.php');
                } else {
                    throw new Exception($polaczenie->error);
                }
            }

            $polaczenie->close();
        }
    } catch (Exception $e) {
        echo '<span style="color:red;">Błąd serwera! Przepraszamy za niedogodności i prosimy o rejestrację w innym terminie!</span>';
        echo '<br />Informacja developerska: ' . $e;
    }
}

?>



<!DOCTYPE html>
<html lang="pl">

<head>
    <meta charset="utf-8">
    <title>Rejestracja</title>

    <link rel="stylesheet" href="main.css">
</head>

<body>
    <form method="post">

        <div class="formularz">
            <!-- <div class="tytul">Rejestracja</div> -->
            <div class="wprowadz-dane">
                <div class="form">
                    <!-- ??????? -->
                    <label class="label">Imię: </label>
                    <input type="text" name="imie" class="input" placeholder="Podaj imię" />
                </div>
                <?php
                if (isset($_SESSION['error_imie'])) {
                    echo '<div class="error">' . $_SESSION['error_imie'] . '</div>';
                    unset($_SESSION['error_imie']);
                }
                ?>

                <div class="form">
                    <label class="label">Nazwisko: </label>
                    <input type="text" name="nazwisko" class="input" placeholder="Podaj nazwisko" />
                </div>
                <?php
                if (isset($_SESSION['error_nazwisko'])) {
                    echo '<div class="error">' . $_SESSION['error_nazwisko'] . '</div>';
                    unset($_SESSION['error_nazwisko']);
                }
                ?>

                <div class="form">
                    <label class="label">Login: </label>
                    <input type="text" name="login" class="input" placeholder="Wprowadź login" />
                </div>
                <?php
                if (isset($_SESSION['error_login'])) {
                    echo '<div class="error">' . $_SESSION['error_login'] . '</div>';
                    unset($_SESSION['error_login']);
                }
                ?>

                <div class="form">
                    <label class="label">E-mail: </label>
                    <input type="text" name="email" class="input" placeholder="Wprowadź adres e-mail" />
                </div>
                <?php
                if (isset($_SESSION['error_email'])) {
                    echo '<div class="error">' . $_SESSION['error_email'] . '</div>';
                    unset($_SESSION['error_email']);
                }
                ?>

                <div class="form">
                    <label class="label">Hasło: </label>
                    <input type="password" name="haslo1" class="input" placeholder="Wprowadź hasło" />
                </div>
                <?php
                if (isset($_SESSION['error_haslo'])) {
                    echo '<div class="error">' . $_SESSION['error_haslo'] . '</div>';
                    unset($_SESSION['error_haslo']);
                }
                ?>

                <div class="form">
                    <label class="label">Powtórz hasło: </label>
                    <input type="password" name="haslo2" class="input" placeholder="Powtórz wprowadzone hasło" />
                </div>
                <!-- <div class="button"><a href="#">Zarejestruj się</a></div> -->
                <!-- <button type="submit">Zarejestruj się</button> -->
                <!-- <a href="#" class="button">Link Button</a> -->
                <input type="submit" value="Zarejestruj się" />
                <br /> <br />
                <a href="index.php">Strona główna</a>


            </div>
        </div>
    </form>
</body>

</html>