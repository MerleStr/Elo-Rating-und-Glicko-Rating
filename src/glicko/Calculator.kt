package glicko

import kotlin.math.*

class Calculator {
    private var tau = 0.0
    private var defaultVolatility = 0.0

    fun RatingCalculator() {
        tau = DEFAULT_TAU
        defaultVolatility = DEFAULT_VOLATILITY
    }

    /**
     *
     * @param initVolatility  Initial volatility for new ratings
     * @param tau             How volatility changes over time
     */
    fun RatingCalculator( initVolatility: Double, tau: Double) {
        defaultVolatility = initVolatility
        this.tau = tau
    }

    fun updateRatings(results: PeriodResults) {
        for (player in results.getParticipants()) {
            if (results.getResults(player).isNotEmpty()) {
                calculateNewRating(player, results.getResults(player))
            } else {
                // if a player does not compete during the rating period, then only Step 6 applies.
                // the player's rating and volatility parameters remain the same but deviation increases
                player.workingRating = player.rating
                player.workingRatingDeviation = calculateNewRD(player.getGlicko2RatingDeviation(), player.volatility)
                player.workingVolatility = player.volatility
            }
        }

        // now iterate through the participants and confirm their new ratings
//        for (player in results.getParticipants()) {
//            player.finaliseRating()
//        }

        // lastly, clear the result set down in anticipation of the next rating period
        results.clear()
    }

    private fun calculateNewRating(player: Rating, results: List<Result>) {
        val phi: Double = player.getGlicko2RatingDeviation()
        val sigma = player.volatility
        val a = ln(sigma.pow(2.0))
        val delta = delta(player, results)
        val v = subFunctionV(player, results)

        // step 5.2 - set the initial values of the iterative algorithm to come in step 5.4
        var ratingA = a
        var ratingB = 0.0
        if (delta.pow(2.0) > phi.pow(2.0) + v) {
            ratingB = ln(delta.pow(2.0) - phi.pow(2.0) - v)
        } else {
            var k = 1.0
            ratingB = a - k * abs(tau)
            while (subFunctionF(ratingB, delta, phi, v, a, tau) < 0) {
                k++
                ratingB = a - k * abs(tau)
            }
        }

        // step 5.3
        var fA = subFunctionF(ratingA, delta, phi, v, a, tau)
        var fB = subFunctionF(ratingB, delta, phi, v, a, tau)

        // step 5.4
        while (abs(ratingB - ratingA) > CONVERGENCE_TOLERANCE) {
            val ratingC = ratingA + (ratingA - ratingB) * fA / (fB - fA)
            val fC = subFunctionF(ratingC, delta, phi, v, a, tau)
            if (fC * fB < 0) {
                ratingA = ratingB
                fA = fB
            } else {
                fA /= 2.0
            }
            ratingB = ratingC
            fB = fC
        }
        val newSigma = exp(ratingA / 2.0)
        player.workingVolatility = newSigma

        // Step 6
        val phiStar = calculateNewRD(phi, newSigma)

        // Step 7
        val newPhi = 1.0 / sqrt(1.0 / phiStar.pow(2.0) + 1.0 / v)

        // note that the newly calculated rating values are stored in a "working" area in the Rating object
        // this avoids us attempting to calculate subsequent participants' ratings against a moving target
        player.workingRating = (player.getGlicko2Rating() + newPhi.pow(2.0) * outcomeBasedRating(player, results))
        player.workingRatingDeviation = newPhi
        player.incrementNumberOfResults(results.size)
    }

    private fun subFunctionF(x: Double, delta: Double, phi: Double, v: Double, a: Double, tau: Double): Double {
        return exp(x) * (delta.pow(2.0) - phi.pow(2.0) - v - exp(x)) /
                (2.0 * (phi.pow(2.0) + v + exp(x)).pow(2.0)) -
                (x - a) / tau.pow(2.0)
    }

    private fun subFunctionG(deviation: Double): Double {
        return 1.0 / sqrt(1.0 + 3.0 * deviation.pow(2.0) / Math.PI.pow(2.0))
    }

    private fun subFunctionE(playerRating: Double, opponentRating: Double, opponentDeviation: Double): Double {
        return 1.0 / (1.0 + exp(-1.0 * subFunctionG(opponentDeviation) * (playerRating - opponentRating)))
    }

    private fun subFunctionV(player: Rating, results: List<Result>): Double {
        var v = 0.0
        for (result in results) {
            v += (subFunctionG(result.getOpponent(player).getGlicko2RatingDeviation()).pow(2.0)
                    * subFunctionE(player.getGlicko2Rating(),
                    result.getOpponent(player).getGlicko2Rating(),
                    result.getOpponent(player).getGlicko2RatingDeviation())
                    * (1.0 - subFunctionE(player.getGlicko2Rating(),
                    result.getOpponent(player).getGlicko2Rating(),
                    result.getOpponent(player).getGlicko2RatingDeviation())))
        }
        return v.pow(-1.0)
    }

    private fun delta(player: Rating, results: List<Result>): Double {
        return subFunctionV(player, results) * outcomeBasedRating(player, results)
    }

    private fun outcomeBasedRating(player: Rating, results: List<Result>): Double {
        var outcomeBasedRating = 0.0

        for (result in results){
            outcomeBasedRating = (outcomeBasedRating
                    + (subFunctionG(result.getOpponent(player).getGlicko2RatingDeviation())
                    * (result.getScore(player) - subFunctionE(
                    player.getGlicko2Rating(),
                    result.getOpponent(player).getGlicko2Rating(),
                    result.getOpponent(player).getGlicko2RatingDeviation()))))
        }
        return outcomeBasedRating
    }

    private fun calculateNewRD(phi: Double, sigma: Double): Double {
        return sqrt(phi.pow(2.0) + sigma.pow(2.0))
    }

    companion object {
        private const val DEFAULT_RATING = 1500.0
        private const val DEFAULT_DEVIATION = 350.0
        private const val DEFAULT_VOLATILITY = 0.06
        private const val DEFAULT_TAU = 0.75
        private const val MULTIPLIER = 173.7178
        private const val CONVERGENCE_TOLERANCE = 0.000001

        fun convertRatingToOriginalGlickoScale(rating: Double): Double {
            return rating * MULTIPLIER + DEFAULT_RATING
        }


        fun convertRatingToGlicko2Scale(rating: Double): Double {
            return (rating - DEFAULT_RATING) / MULTIPLIER
        }


        fun convertRatingDeviationToOriginalGlickoScale(ratingDeviation: Double): Double {
            return ratingDeviation * MULTIPLIER
        }


        fun convertRatingDeviationToGlicko2Scale(ratingDeviation: Double): Double {
            return ratingDeviation / MULTIPLIER
        }
        fun getDefaultRating(): Double {
            return DEFAULT_RATING
        }


        fun getDefaultVolatility(): Double {
            return DEFAULT_VOLATILITY
        }


        fun getDefaultRatingDeviation(): Double {
            return DEFAULT_DEVIATION
        }


    }
}