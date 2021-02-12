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
                <label class="label">Wiek: </label>
                <input type="text" id="wiek" class="input" />
            </div>
            <div class="form">
                <label class="label">Wzrost: </label>
                <input type="text" id="wz" class="input" />
            </div>
            <div class="form">
                <label class="label">Waga: </label>
                <input type="text" id="wg" class="input" />
            </div>
            <div class="form">
                <label class="label">Płeć: </label>
                <div>
                    <input type="radio" id="m" name="plec" value="m" checked>
                    <label>Mężczyzna</label>
                </div>

                <div>
                    <input type="radio" id="k" name="plec" value="k">
                    <label>Kobieta</label>
                </div>
            </div>
            <div class="form">
                <label class="label">Stopień aktywności: </label>
                <select id="akt" class="input">
                    <option value="brak">Brak aktywności (praca siedząca)</option>
                    <option value="niska">Niska aktywność (praca siedząca i 1-2 treningi w tygodniu)</option>
                    <option value="srednia" selected="selected">Średnia aktywność (praca siedząca i 3-4 treningi w tygodniu)</option>
                    <option value="wysoka">Wysoka aktywność (praca fizyczna i 3-4 treningi w tygodniu)</option>
                    <option value="bwysoka">Bardzo wysoka aktywność (zawodowi sportowcy i osoby codziennie trenujące)</option>
                </select>
            </div>
            <p id="wynik"></p>
            <button id="btn" onclick="BMR()">Oblicz</button>
            <br /><br />
            <a href="index.php">Strona główna</a>
        </div>
    </div>

    <script>
    function BMR() {
        // metoda Harrisa-Benedicta
        var wiek = document.getElementById("wiek").value;
        var wzrost = document.getElementById("wz").value;
        var waga = document.getElementById("wg").value;

        var bmr = 0;
        if (document.getElementById("k").checked) {
            bmr = 655 + (9.6 * waga) + (1.8 * wzrost) - (4.7 * wiek);
        } else {
            bmr = 66 + (13.7 * waga) + (5 * wzrost) - (6.76 * wiek);
        }

        var a = document.getElementById("akt");

        if (a.options[a.selectedIndex].value == "brak") {
            bmr = 1.2 * bmr;
        }
        if (a.options[a.selectedIndex].value == "niska") {
            bmr = 1.3 * bmr;
        }

        if (a.options[a.selectedIndex].value == "srednia") {
            bmr = 1.5 * bmr;
        }
        if (a.options[a.selectedIndex].value == "wysoka") {
            bmr = 1.7 * bmr;
        }
        if (a.options[a.selectedIndex].value == "bwysoka") {
            bmr = 1.9 * bmr;
        }
        n = bmr.toFixed(2);
        document.getElementById("wynik").innerHTML = "Twoje zapotrzebowanie kaloryczne wynosi " + n;

    }
</script>

</body>

</html>