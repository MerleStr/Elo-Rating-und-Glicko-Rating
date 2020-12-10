package tests

import glicko.Calculator
import glicko.PeriodResults
import glicko.Rating
import glicko.Constants
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class testGlicko{

    val calculator: Calculator = Calculator(0.06, 0.5)
    val results: PeriodResults = PeriodResults(null)
    val ratings = mutableListOf<Rating>()
    val constants = Constants


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

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return (kotlin.math.round(this * multiplier) / multiplier).toDouble()
    }


    private fun printResults(text: String, ratings: MutableList<Rating>) {
        println("\n$text... ")
        ratings.forEach { println(it) }
    }

    @Test
    fun test() {
        initPlayer()

        printResults("Before", ratings)

        assertEquals(0.0, ratings[0].getGlicko2Rating(),"0.00001" )
        assertEquals(1.1513, (ratings[0].getGlicko2RatingDeviation()).round(4),"0.00001" )

        results.addResult(ratings[0], ratings[1])
        results.addResult(ratings[2], ratings[0])
        results.addResult(ratings[3], ratings[0])

        calculator.updateRatings(results)

        printResults("After", ratings)

        assertEquals( 1464.05, (ratings[0].getRating()).round(2), "0.01" )
        assertEquals( 151.52, (ratings[0].getRatingDeviation()).round(2), "0.01")
        assertEquals( 0.059996, (ratings[0].getVolatility()).round(6), "0.01" )

        // test that opponent 4 has had appropriate calculations applied
        assertEquals( constants.DEFAULT_RATING, ratings[4].getRating())
        assertTrue( constants.DEFAULT_DEVIATION <= ratings[4].getRatingDeviation() )
        assertEquals(constants.DEFAULT_VOLATILITY, ratings[4].getVolatility())

    }

}