import elo.Elo
import glicko.Calculator
import glicko.PeriodResults
import glicko.Rating
import java.util.*

fun main() {

    //initElo2();
    initGlicko()
    var exit = true
    do{
        println("\nWillkommen bei der Berechnung ihres Ratings")
        println("Geben Sie E für Elo oder G für Glicko Rating Rating ein.")
        val option = readLine()!!

        when(option.toUpperCase()){
            "E" -> initElo()
            "G" -> initGlicko()
            "ELO" -> elo()
            else -> exit = false
        }
    } while (exit)
}

fun initElo() {
    var anna = 1435.0
    var olaf = 1345.0
    var julia = 1565.0
    // WIn 1 Lose 2 Draw 0
    val elo = Elo()

    val win = 1
    val lose = 0
    val draw = 2


    // Round 1
    elo.eloRating(anna, julia, 20, win)
    anna = elo.updateRatingA()
    julia = elo.updateRatingB()

    // Round 2
    elo.eloRating(anna, julia, 20, draw)
    anna = elo.updateRatingA()
    julia = elo.updateRatingB()

    // Round 3
    elo.eloRating(julia, olaf, 20, win)
    julia = elo.updateRatingA()
    olaf = elo.updateRatingB()

    // Round 4
    elo.eloRating(julia, anna, 20, lose)
    julia = elo.updateRatingA()
    anna = elo.updateRatingB()

    // Round 5
    elo.eloRating(olaf, anna, 20, draw)
    olaf = elo.updateRatingA()
    anna = elo.updateRatingB()

    // Round 6
    elo.eloRating(anna, olaf, 20, lose)
    anna = elo.updateRatingA()
    olaf = elo.updateRatingB()

    // Round 7
    elo.eloRating(anna, julia, 20, win)
    anna = elo.updateRatingA()
    julia = elo.updateRatingB()

    // Round 8
    elo.eloRating(olaf, anna, 20, win)
    olaf = elo.updateRatingA()
    anna = elo.updateRatingB()

    // Round 9
    elo.eloRating(olaf, julia, 20, win)
    julia = elo.updateRatingB()

    // Round 10
    elo.eloRating(anna, julia, 20, lose)


}
/*
fun initElo(){
    val anna = 1435.0
    val olaf = 1345.0
    val julia = 1565.0
    val berechnung = Elo(anna, julia)
    berechnung.eloRating(20, 1)

    val berechnung2 = Elo(1448.58, 1551.42)
    berechnung2.eloRating(20, 0)

    val berechnung3 = Elo(1548.54, olaf)
    berechnung3.eloRating(20, 1)

    val berechnung4 = Elo(1553.27, 1451.46)
    berechnung4.eloRating(20, 2)

    val berechnung5 = Elo(1340.27, 1464.31)
    berechnung5.eloRating(20, 0)

    val berechnung6 = Elo(1460.88, 1343.7)
    berechnung6.eloRating(20, 2)

    val berechnung7 = Elo(1447.63, 1540.42)
    berechnung7.eloRating(20, 1)

    val berechnung8 = Elo(1343.7, 1460.24)
    berechnung8.eloRating(20, 1)

    val berechnung9 = Elo(1356.93, 1527.81)
    berechnung9.eloRating(20, 0)

    val berechnung10 = Elo(1441.01, 1523.25)
    berechnung10.eloRating(20, 2)
}
*/

fun elo() {
    val input = Scanner(System.`in`)
    val elo = Elo()

    println("Geben Sie Ihr Raitng ein")
    val rating1 = input.nextDouble()

    println("Geben Sie das gegnerische Raitng ein")
    val rating2 = input.nextDouble()

    println("Geben Sie den Entwicklungskoeffizienten (10, 20 o 40) ein")
    var k = input.nextInt()
    if (k != 10 || k != 20 || k != 40) {
        println("Falsche Eingange Wert wird auf 40 gesetzt ")
        k = 40
    }

    println("Geben Sie ein ob sie erfolglos (2), erfolgreich (1) oder unentschieden (0) gespielt haben?")
    var d = input.nextInt()
    if (d != 1 || d != 0 || d != 2) {
        println("Falsche Eingange Wert wird auf 0 gesetzt ")
        d = 0
    }

    println("Das Ergebnis: \n")
    elo.eloRating(rating1, rating2, k, d)

}


fun initGlicko() {
//    val glickoTest = TestGlicko()
//    glickoTest.test()

    val calculator = Calculator(0.06, 0.5)
    val results = PeriodResults(null)
    val ratings = mutableListOf<Rating>()

    val player1 = Rating("Anna", 1435.0, 150.0, 0.06)
    val player2 = Rating("Olaf", 1345.0, 150.0, 0.06)
    val player3 = Rating("Julia", 1565.0, 150.0, 0.06)

    ratings.add(player1)
    ratings.add(player2)
    ratings.add(player3)

    println("inital Rating")
    ratings.forEach { println(it) }
    println("\n")

    // Round 1 - 10
    results.addResult(ratings[0], ratings[2])
    results.addDraw(ratings[0], ratings[2])
    results.addResult(ratings[2], ratings[1])
    results.addResult(ratings[0], ratings[2])
    results.addDraw(ratings[1], ratings[0])
    results.addResult(ratings[1], ratings[0])
    results.addResult(ratings[0], ratings[2])
    results.addResult(ratings[1], ratings[0])
    results.addResult(ratings[1], ratings[2])
    results.addResult(ratings[0], ratings[2])

    println("End Rating")
    calculator.updateRatings(results)
    ratings.forEach { println(it) }

}


