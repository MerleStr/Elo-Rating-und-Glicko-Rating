package main.elo

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.round


class Elo(ratingA_old: Float, ratingB_old: Float){

    var ratingA = ratingA_old
    var ratingB = ratingB_old

    private fun probability(ratingA_prob : Float, ratingB_prob: Float): Float {
        return 1.0f * 1.0f / (1 + 1.0f * 10.toDouble().pow((1.0f * (ratingA_prob - ratingB_prob) / 400).toDouble()).toFloat())
    }

    fun eloRating( K: Int, d: Int ) {

        printRatings()

        // calculate Probability
        val probabilityB = probability(ratingA, ratingB)
        val probabilityA = probability(ratingB, ratingA)

        // Updating the Elo Ratings
        when(d){
            // Win for A
            1 -> {
                ratingA += K * (1 - probabilityA)
                ratingB += K * (0 - probabilityB)
            }
            // Lose for A
            2 -> {
                ratingA += K * (0 - probabilityA)
                ratingB += K * (1 - probabilityB)
            }
            // Draw
            else -> {
                ratingA += (K * (0.5 - probabilityA)).toFloat()
                ratingB += (K * (0.5 - probabilityB)).toFloat()
            }
        }

        ratingA = ratingA.round(2)
        ratingB = ratingB.round(2)

        printRoundRating(ratingA, ratingB)
    }


    private fun printRoundRating(rRatingA : Float, rRatingB : Float) {
        print("\n---\nUpdated Ratings:\n")
        print("\nRating Person A = $rRatingA " +
                "\nRating Person B = $rRatingB " +
                "\nRating Difference = ${((rRatingA - rRatingB).absoluteValue).round(2)}")
        print("\n---------------------------------------------")
    }

    private fun printRatings() {
        print("\n---------------------------------------------")
        print("\nOld Ratings:\n")
        print("\nRating Person A = $ratingA " +
                "\nRating Person B = $ratingB " +
                "\nRating Difference = ${(ratingA - ratingB).absoluteValue}")
    }

    private fun Float.round(decimals: Int): Float {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return (round(this * multiplier) / multiplier).toFloat()
    }

}
