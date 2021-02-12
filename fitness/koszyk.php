<?php
session_start();

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
    <title>Koszyk</title>
    <meta http-equiv="X-Ua-Compatible" content="IE=edge,chrome=1">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="main.css">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&amp;subset=latin-ext" rel="stylesheet">
    <link rel="stylesheet" href="css/fontello.css">
</head>

<body>
    <div class="container">
        <form method="POST" action="zaktualizuj-koszyk.php">
            <table class="table table-bordered">
                <thead class="thead-light">
                    <tr>
                        <th class="text-center" width="60%" scope="col">Nazwa</th>
                        <th class="text-center" width="15%" scope="col">Cena</th>
                        <th class="text-center" width="10%" scope="col">Ilość</th>
                        <th class="text-center" width="15%" scope="col">Suma</th>
                    </tr>
                </thead>


                <tbody>
                    <?php

                    $suma = 0; // podsumowanie ceny produktow 
                    if (!empty($_SESSION['koszyk'])) {

                        $index = 0;
                        if (!isset($_SESSION['qty_array'])) {
                            $_SESSION['qty_array'] = array_fill(0, count($_SESSION['koszyk']), 1); // inicjalizacja ilosci produktow w koszyku jedynkami
                        }
                        $indeksyprod = implode(',', $_SESSION['koszyk']);
                        $sql = mysqli_query($polaczenie, "SELECT * FROM produkt WHERE id_produktu IN ($indeksyprod)");

                        if ($sql == false) {
                            echo "Błąd";
                        }
                    ?>

                        <?php while ($wiersz = mysqli_fetch_array($sql)) { ?>

                            <tr class="wysrodkuj">
                                <th class="text-center" scope="row"><?php echo $wiersz['nazwa']; ?></th>
                                <td><?php echo number_format($wiersz['cena'], 2); ?></td>
                                <input type="hidden" name="indeksy[]" value="<?php echo $index; ?>">
                                <!-- qty_array dla indexu 0,1,2 -->
                                <td><input type="number" min="1" value="<?php echo $_SESSION['qty_array'][$index]; ?>" name="qty_<?php echo $index; ?>"></td>
                                <td><?php echo number_format($_SESSION['qty_array'][$index] * $wiersz['cena'], 2); ?></td>
                                <?php $suma += $_SESSION['qty_array'][$index] * $wiersz['cena']; ?>
                            </tr>

                        <?php $index++;
                        }
                    } else {
                        ?>
                        <tr>
                            <td colspan="4" class="text-center">Brak produktów w koszyku</td>
                        </tr>
                    <?php
                    }
                    ?>
                                        <tr>
                        <td colspan="3" align="right"><b>Podsumowanie</b></td>
                        <td colspan="1" align="center"><b><?php echo number_format($suma, 2); ?></b></td>
                    </tr>
                </tbody>
            </table>
            <a href="skleppo.php" class="btn btn-primary"> Wróć</a>
            <button type="submit" class="btn btn-success" name="zaktualizuj">Zaktualizuj</button>
            <a href="wyczysc-koszyk.php" class="btn btn-danger"> Wyczyść koszyk</a>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

</body>

</html>