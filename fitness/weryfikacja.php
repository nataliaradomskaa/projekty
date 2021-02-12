<?php
session_start();

require_once "polaczenie.php";
mysqli_report(MYSQLI_REPORT_STRICT);

try {
    $polaczenie = new mysqli("localhost", "root", "", "fitnessclub");
    $polaczenie2 = new mysqli("localhost", "root", "", "dane");

    if (($polaczenie->connect_errno != 0) || ($polaczenie2->connect_errno != 0)) {
        throw new Exception(mysqli_connect_errno());
    } else {

        if (isset($_POST['weryfikuj_studenta'])) {

            $_SESSION['blad'] = "";
            $nr_albumu = $_POST['nr_albumu'];
            $weryfikuj = true;

            if (!(preg_match('/^\d+$/', $nr_albumu))) {
                $_SESSION['blad'] = "Numer albumu jest ciągiem cyfr!";
                $weryfikuj = false;
                header('Location: student.php');
            }

            if ($weryfikuj == true) {
                $sql = "SELECT * FROM osoba WHERE nr_albumu = '" . $nr_albumu . "' ";

                $result = mysqli_query($polaczenie2, $sql);
                if (mysqli_num_rows($result) > 0) {

                    $row = mysqli_fetch_array($result);

                    $_SESSION['zweryfikowany'] = true;
                    $_SESSION['nr_albumu'] = $row['nr_albumu'];
                    $id_karnetu = 5; // karnet studencki 

                    $row = mysqli_fetch_row(mysqli_query($polaczenie, "SELECT czas_trwania FROM karnet WHERE id_karnetu ='" . $id_karnetu . "'")); // wchodze do bazy po id i sprawdzam ile trwa karnet
                    $czas_trwania = $row[0]; // ile miesiecy jest wazny karnet
                    $today = date('Y-m-d'); // pobieram dzisiejsza date we wskazanym stringu formacie
                    $endDate =  date('Y-m-d', strtotime($today . "+" . $czas_trwania . " month")); // tworzenie daty wygasniecia karnetu 

                    // dodanie karnetu do zamowien
                    $id_uzyt = $_SESSION['id_uzytkownika'];
                    $sql = "INSERT INTO zamowienie_karnetu VALUES (NULL, '$id_uzyt', '$id_karnetu', '$today', '$endDate')";

                    if (mysqli_query($polaczenie, $sql)) {
                        header('Location: historiazamowien.php'); // przekierowanie
                    } else {
                        echo " Błąd: " . $sql . "<br>" . mysqli_error($polaczenie);
                    }

                    $polaczenie->close();
                } else {
                    $_SESSION['blad'] = "Nie ma takiego numeru albumu!";
                    header('Location: student.php');
                }
            }
        }
    }
} catch (Exception $e) {
    echo '<span style="color:red;">Błąd serwera! Przepraszamy za niedogodności i prosimy o wizytę w innym terminie!</span>';
    echo '<br />Informacja developerska: ' . $e;
}
