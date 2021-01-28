package elo

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.round


class Elo{

    var rA : Double = 0.0
    var rB : Double = 0.0

    private fun probability(ratingA : Double, ratingB: Double): Double {
        return 1.0f / (1 + 1.0f * (10.0).pow((1.0f * (ratingA - ratingB) / 400)))
    }

    fun eloRating(ratingA_old: Double, ratingB_old: Double, K: Int, d: Int ) {

        val win = 1
        val lose = 0
        val draw = 0.5
        var ratingB = ratingB_old
        var ratingA = ratingA_old

        printRatings(ratingA, ratingB)

        // calculate Probability
        val probabilityB = probability(ratingA_old, ratingB_old)
        val probabilityA = probability(ratingB_old, ratingA_old)

        // Updating the Elo Ratings
        when(d){
            // Lose for A
            0 -> {
                ratingA += K * (lose - probabilityA)
                ratingB += K * (win - probabilityB)
            }
            // Win for A
            1 -> {
                ratingA += K * (win - probabilityA)
                ratingB += K * (lose - probabilityB)
            }
            // Draw
            else -> {
                ratingA += (K * (draw - probabilityA))
                ratingB += (K * (draw - probabilityB))
            }
        }

        ratingA = ratingA.round(2)
        ratingB = ratingB.round(2)

        printRoundRating(ratingA, ratingB)

        rA = ratingA
        rB = ratingB
    }

    fun updateRatingA() :Double = rA
    fun updateRatingB() :Double = rB

    private fun printRoundRating(rRatingA : Double, rRatingB : Double) {
        print("\n---\nUpdated Ratings:\n")
        print("\nRating Person A = $rRatingA " +
                "\nRating Person B = $rRatingB " +
                "\nRating Difference = ${((rRatingA - rRatingB).absoluteValue).toDouble().round(2)}")
        print("\n---------------------------------------------")
    }

    private fun printRatings(ratingA : Double, ratingB : Double) {
         print("\n---------------------------------------------")
         print("\nOld Ratings:\n")
         print("\nRating Person A = $ratingA " +
                 "\nRating Person B = $ratingB " +
                 "\nRating Difference = ${(ratingA - ratingB).absoluteValue}")
     }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return (round(this * multiplier) / multiplier)
    }

}

