<!DOCTYPE html>
<html lang="pl">

<head>
    <meta charset="utf-8">
    <title>Kalkulator BMI</title>
    <meta http-equiv="X-Ua-Compatible" content="IE=edge">

    <link rel="stylesheet" href="main.css">
</head>

<body>
    <div class="formularz">
        <div class="wprowadz-dane">

            <div class="form">
                <label class="label">Wzrost: </label>
                <input type="text" id="wz" class="input" />
            </div>
            <div class="form">
                <label class="label">Waga: </label>
                <input type="text" id="wg" class="input" />
            </div>
            <p id="wynik"></p>
            <p id="komentarz"></p>
            <button id="btn" onclick="BMI()">Oblicz</button>
            <br /><br />
            <a href="index.php">Strona główna</a>
        </div>
    </div>
    <script>
    function BMI() {
        var wzrost = document.getElementById("wz").value;
        var waga = document.getElementById("wg").value;
        var bmi = waga / (wzrost / 100 * wzrost / 100);
        n = bmi.toFixed(2);
        document.getElementById("wynik").innerHTML = "Twoje BMI wynosi " + n;

        if (bmi < 18.5) {
            document.getElementById("komentarz").innerHTML = "Niedowaga!";
        }
        if ((bmi > 18.5) && (bmi < 24.99)) {
            document.getElementById("komentarz").innerHTML = "Wartość prawidłowa";
        }
        if ((bmi > 25) && (bmi < 29.99)) {
            document.getElementById("komentarz").innerHTML = "Nadwaga!";
        }
        if ((bmi > 30) && (bmi < 34.99)) {
            document.getElementById("komentarz").innerHTML = "I stopień otyłości!";
        }
        if ((bmi > 35) && (bmi < 39.99)) {
            document.getElementById("komentarz").innerHTML = "II stopień otyłości!";
        }
        if (bmi > 40) {
            document.getElementById("komentarz").innerHTML = "III stopień otyłości!";
        }
    }
</script>
</body>

</html>