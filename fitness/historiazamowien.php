<?php
session_start();

if (!isset($_SESSION['zalogowany']) || (!($_SESSION['rodzaj_uzytkownika'] == 'user'))) {
    if ($_SESSION['rodzaj_uzytkownika'] == 'admin') {
        header('Location: paneladmin.php');
    } else {
        header('Location: index.php');
    }
    exit();
}


require_once "polaczenie.php";
mysqli_report(MYSQLI_REPORT_STRICT);

try {
    $polaczenie = new mysqli($host, $user, $haslo, $bazadanych);

    mysqli_query($polaczenie, "SET CHARSET utf8"); // wyswietlanie polskich znakow
    mysqli_query($polaczenie, "SET NAMES 'utf8' COLLATE 'utf8_polish_ci'");

    if ($polaczenie->connect_errno != 0) { // blad ostatniego polaczenia 
        throw new Exception(mysqli_connect_errno());
    }
} catch (Exception $e) {
    echo '<span style="color:red;">Błąd serwera! Przepraszamy za niedogodności i prosimy o wizytę w innym terminie!</span>';
    echo '<br />Błąd: ' . $e;
}
?>

<!DOCTYPE html>
<html lang="pl">

<head>
    <meta charset="utf-8">
    <title>Fitness Club - Moje konto</title>
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
                    <li><a href="index.php">Strona główna</a></li>
                    <li><a href="skleppo.php">Sklep</a></li>
					<li><a href="bmi.php">Kalkulator BMI</a></li>
					<li><a href="bmr.php">Kalkulator BMR</a></li>
                    <li><a href="historiazamowien.php">Moje konto</a></li>
                </ul>
            </div>
            <div class="nav2">
                <ul class="menu1">
                    <li><a href="historiazamowien.php">Historia zamówień</a></li>
                    <li><a href="haslo.php">Zmiana hasła</a></li>
                </ul>
            </div>
        </nav>
    </header>

    <!-- Slider -->
    <div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="fit1.jpg" class="d-block w-100" alt="" width="1340" height="250">
            </div>
            <div class="carousel-item">
                <img src="fit2.jpg" class="d-block w-100" alt="" width="1340" height="250">
            </div>
            <div class="carousel-item">
                <img src="fit3.jpg" class="d-block w-100" alt="" width="1340" height="250">
            </div>
        </div>
        <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        </a>
        <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
        </a>
    </div>
    <div class="karnety">
        <table class="table table-bordered">
            <thead class="thead-light">
                <tr>
                    <th class="text-center" width="30%" scope="col">Nazwa karnetu</th>
                    <th class="text-center" width="20%" scope="col">Koszt</th>
                    <th class="text-center" width="25%" scope="col">Data zakupu</th>
                    <th class="text-center" width="25%" scope="col">Data ważności</th>
                </tr>
            </thead>
            <tbody>
                <?php
                $sql = mysqli_query($polaczenie, "SELECT * FROM zamowienie_karnetu INNER JOIN karnet
                 ON zamowienie_karnetu.id_karnetu = karnet.id_karnetu WHERE id_uzytkownika ='" . $_SESSION['id_uzytkownika'] . "'");
                ?>

                <?php while ($wiersz = mysqli_fetch_array($sql)) { ?>
                    <!-- w zaleznosci ile jest karnetow do wyswietlenia -->
                    <tr class="wysrodkuj">
                        <th class="text-center" scope="row"><?php echo $wiersz['nazwa']; ?></th>
                        <td><?php echo $wiersz['cena']; ?></td>
                        <td><?php echo $wiersz['data_zakupu']; ?></td>
                        <td><?php echo $wiersz['data_waznosci']; ?></td>
                    </tr>
                <?php } ?>
            </tbody>
        </table>

    </div>

    <footer>
        <div class="info">
            <div class="pomoc">
                <ul>
                    <li><a href="#">Regulamin zakupów</a></li>
                    <li><a href="#">Zwroty i wymiany</a></li>
                    <li><a href="#">Kontakt</a></li>
                </ul>
            </div>
            <div class="media">
                <div class="mediapom">
                    <div class="fb">
                        <i class="icon-facebook"></i>
                    </div>
                    <div class="insta">
                        <i class="icon-instagram"></i>
                    </div>
                    <div class="yt">
                        <i class="icon-youtube"></i>
                    </div>
                    <div style="clear: both"></div>
                </div>
            </div>
            <div style="clear: both"></div>
        </div>
        <div class="copyright">
            &copy; 2020 Fitness Club
        </div>
    </footer>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

</body>

</html>