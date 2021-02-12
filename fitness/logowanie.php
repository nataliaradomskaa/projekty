<?php

session_start();

if ((isset($_SESSION['zalogowany'])) && ($_SESSION['zalogowany'] == true) && $_SESSION['id_uzytkownika'] = 'user') {
    header('Location: user-index.php');
    exit();
}
?>

<!DOCTYPE html>
<html lang="pl">

<head>
    <meta charset="utf-8">
    <title>Rejestracja</title>
    <meta http-equiv="X-Ua-Compatible" content="IE=edge">

    <link rel="stylesheet" href="main.css">
</head>

<body>
    <form action="zaloguj.php" method="post">
        <div class="formularz">
            <!-- <div class="tytul">Rejestracja</div> -->
            <div class="wprowadz-dane">

                <?php
                if (isset($_SESSION['blad'])) {
                    echo '<div class="error">' . $_SESSION['blad'] . '</div>';
                    unset($_SESSION['blad']);
                }
                ?>

                <div class="form">
                    <label class="label">Login: </label>
                    <input type="text" name="login" class="input" placeholder="Wprowadź login" />
                </div>
                <div class="form">
                    <label class="label">Hasło: </label>
                    <input type="password" name="haslo" class="input" placeholder="Wprowadź hasło" />
                </div>
                <!-- <div class="button"><a href="#">Zaloguj się</a></div> -->
                <input type="submit" value="Zaloguj się" />
                <br /><br />
                Nie masz jeszcze założonego konta?
                <a href="rejestracja.php">Zarejestruj się!</a>
            </div>
        </div>
    </form>
</body>

</html>