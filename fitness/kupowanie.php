<?php
session_start();

require_once "polaczenie.php";
mysqli_report(MYSQLI_REPORT_STRICT);

try {
    $polaczenie = new mysqli($host, $user, $haslo, $bazadanych);

    if ($polaczenie->connect_errno != 0) {
        throw new Exception(mysqli_connect_errno());
    } else {

        if (isset($_POST['kupkarnet'])) {

            if ($_POST['id_karnetu'] == 5) { // weryfikacja kupna karnetu studenckiego
                header('Location: student.php');
            } else {

                $id_karnetu = $_POST['id_karnetu']; // odebranie id karnetu do zakupu

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
            }
        }
    }
} catch (Exception $e) {
    echo '<span style="color:red;">Błąd serwera! Przepraszamy za niedogodności i prosimy o wizytę w innym terminie!</span>';
    echo '<br />Informacja developerska: ' . $e;
}
