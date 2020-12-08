package tests

import main.glicko.Calculator
import main.glicko.PeriodResults
import main.glicko.Rating
import org.junit.Test
import kotlin.test.assertEquals

internal class testGlicko{

    val calculator: Calculator = Calculator(0.06, 0.5)
    val results: PeriodResults = PeriodResults(null)
    val ratings = mutableListOf<Rating>()


    private fun initPlayer() {

        val player1 = Rating("Harry",1500.0, 200.0, 0.06) // the main player of Glickman's example
        val player2 = Rating("Paul",1400.0, 30.0, 0.06)
        val player3 = Rating("Linda",1550.0, 100.0, 0.06)
        val player4 = Rating("Erma",1700.0, 300.0, 0.06)
        val player5 = Rating("Syndra",null, null, null) // this player won't compete during the test

        ratings.add(player1)
        ratings.add(player2)
        ratings.add(player3)
        ratings.add(player4)
        ratings.add(player5)
    }

    private fun printResults(text: String, ratings: MutableList<Rating>) {
        println("\n$text... ")
        ratings.forEach { println(it) }
//        println(rating)
    }

    @Test
    fun test() {
        initPlayer()

//        for (i in 0..4) printResults("Player ${i+1}", ratings[i])

        printResults("Before", ratings)

        assertEquals(0.0, ratings[0].getGlicko2Rating(),"0.00001" )
        assertEquals(1.1513, ratings[0].getGlicko2RatingDeviation(),"0.00001" )

        results.addResult(ratings[0], ratings[1])
        results.addResult(ratings[2], ratings[0])
        results.addResult(ratings[3], ratings[0])

        calculator.updateRatings(results)

        printResults("After", ratings)

        assertEquals( 1464.06, ratings[0].rating, "0.01" )
        assertEquals( 151.52, ratings[0].ratingDeviation, "0.01")
        assertEquals( 0.05999, ratings[0].volatility, "0.01" )



    }


}