package Elo

import java.math.RoundingMode
import kotlin.math.absoluteValue
import kotlin.math.round


class Elo(ratingA_old: Float, ratingB_old: Float){

    var ratingA = ratingA_old
    var ratingB = ratingB_old

    private fun probability(ratingA_prob : Float, ratingB_prob: Float): Float {
        return 1.0f * 1.0f / (1 + 1.0f * Math.pow(10.toDouble(), (1.0f * (ratingA_prob - ratingB_prob) / 400).toDouble()).toFloat())
    }


    private fun roundFloat(number : Float){

    }

    fun eloRating(
        K: Int, d: Boolean
    ) {
        printRatings()
        // calculate Probability
        val probabilityB = probability(ratingA, ratingB)
        val probabilityA = probability(ratingB, ratingA)

        // Updating the Elo Ratings
        if (d) {
            ratingA += K * (1 - probabilityA)
            ratingB += K * (0 - probabilityB)
        } else {
            ratingA += K * (0 - probabilityA)
            ratingB += K * (1 - probabilityB)
        }

        printRoundRating()
    }


    private fun printRatings() {
        print("\n---------------------------------------------")
        print("\nOld Ratings:\n")
        print("\nRating Person A = $ratingA " +
                "\nRating Person B = $ratingB " +
                "\nRating Difference = ${(ratingA - ratingB).absoluteValue}")
    }

    private fun printRoundRating() {
        print("\n---\nUpdated Ratings:\n")
        print("\nRating Person A = ${round(ratingA)} " +
                "\nRating Person B = ${round(ratingB)} " +
                "\nRating Difference = ${(round(ratingA) - round(ratingB)).absoluteValue}")
        print("\n---------------------------------------------")
    }

}

fun main(){

    val elo = Elo(1324f, 1234f)
    elo.eloRating(16, true)

    val elo2 = Elo(1324f, 1234f)
    elo2.eloRating(16, false)

    val elo3 = Elo(1624f, 1134f)
    elo3.eloRating(25, false)

    val elo4 = Elo(1200f, 1000f)
    elo4.eloRating(30, true)

}
