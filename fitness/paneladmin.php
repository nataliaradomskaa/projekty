<?php
session_start();

if (!isset($_SESSION['zalogowany']) || (!($_SESSION['rodzaj_uzytkownika'] == 'admin')))
	{
		header('Location: index.php');
		exit();
    }

?>

<!DOCTYPE html>
<html lang="pl">

<head>
    <meta charset="utf-8">
    <title>Panel administracyjny</title>
    <meta http-equiv="X-Ua-Compatible" content="IE=edge">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="main.css">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&amp;subset=latin-ext" rel="stylesheet">
    <link rel="stylesheet" href="css/fontello.css">
</head>

<body>
    <header>
        <div class="wyloguj"><a href="wyloguj.php">Wyloguj się</a></div>
        <h1 class="logo">Fitness Club</h1>
        <nav>
            <div class="nav1">
                <ul class="menu">
                    <li><a href="podgladsklepu.php">Podgląd sklepu</a></li>
                    <li><a href="paneladmin.php">Panel administracyjny</a></li>
                </ul>
            </div>
        </nav>
    </header>

    <main>
        <div class="produkt">
            <button onclick="myFunction()">Dodaj nowy produkt</button>
        </div>
        <div class="karnet">
            <button onclick="myFunction2()">Dodaj nowy karnet</button>
        </div>
        <div style="clear: both"></div>

        <!-- Dodawanie produktu -->

        <div id="div1" style="display:none">
            <form action="dodawanie-produktu.php" method="post">
                <div class="formularz2">
                    <div class="wprowadz-dane">

                        <?php
                        if (isset($_SESSION['blad'])) {
                            echo '<div class="error">' . $_SESSION['blad'] . '</div>';
                            unset($_SESSION['blad']);
                        }
                        ?>

                        <div class="form">
                            <label class="label">Nazwa produktu:</label>
                            <input type="text" class="input" name="nazwa_produktu">
                        </div>
                        <div class="form">
                            <label class="label">Producent:</label>
                            <input type="text" class="input" id="producent" name="producent">
                        </div>
                        <div class="form">
                            <label class="label">Cena:</label>
                            <input type="text" class="input" id="cena" name="cena">
                        </div>
                        <div class="form">
                            <label class="label">Opis produktu:</label>
                            <textarea rows="4" class="input" cols="50" name="opis"></textarea>
                        </div>
                        <div class="form">
                            <label class="label">Zdjęcie:</label>
                            <input type="file" class="zdjecie" id="zdjecie" name="zdjecie" accept="image/x-png,image/jpeg">
                        </div>

                        <input type="submit" value="Dodaj" name="dodaj_produkt">

                    </div>
                </div>
            </form>
        </div>

        <!-- Dodawanie karnetu -->

        <div id="div2" style="display:none">
            <form action="dodawanie-karnetu.php" method="post">
                <div class="formularz2">
                    <div class="wprowadz-dane">

                        <?php
                        if (isset($_SESSION['blad'])) {
                            echo '<div class="error">' . $_SESSION['blad'] . '</div>';
                            unset($_SESSION['blad']);
                        }
                        ?>

                        <div class="form">
                            <label class="label">Nazwa karnetu:</label>
                            <input type="text" class="input" name="nazwa_k">
                        </div>
                        <div class="form">
                            <label class="label">Cena:</label>
                            <input type="text" class="input" id="cena_k" name="cena_k">
                        </div>
                        <div class="form">
                            <label class="label">Czas trwania [w miesiącach]:</label>
                            <input type="number" min="1" max="12" class="input" id="czas_k" name="czas_k">
                        </div>
                        <div class="form">
                            <label class="label">Uwaga:</label>
                            <textarea rows="4" class="input" cols="50" name="uwaga_k"></textarea>
                        </div>

                        <input type="submit" value="Dodaj" name="dodaj_karnet">

                    </div>
                </div>
            </form>
        </div>


    </main>

    <script>
        function myFunction() {
            var x = document.getElementById("div1");
            if (x.style.display === "none") {
                x.style.display = "block";
            } else {
                x.style.display = "none";
            }
        }

        function myFunction2() {
            var x = document.getElementById("div2");
            if (x.style.display === "none") {
                x.style.display = "block";
            } else {
                x.style.display = "none";
            }
        }
    </script>

</body>

</html>