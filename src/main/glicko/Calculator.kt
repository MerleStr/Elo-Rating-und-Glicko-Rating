package glicko

import kotlin.math.*

class Calculator(initVolatility: Double?, tau: Double?) {
    private var tau = 0.0
    private var defaultVolatility = 0.0

    init {

        if (initVolatility == null || tau == null) {

            defaultVolatility = Constants.DEFAULT_VOLATILITY
            this.tau = Constants.DEFAULT_TAU

        } else {

            defaultVolatility = initVolatility
            this.tau = tau

        }
    }

    fun updateRatings(results: PeriodResults) {
        for (player in results.getParticipants()) {
            if (results.getResults(player).isNotEmpty()) {
                calculateNewRating(player, results.getResults(player))
            } else {
                player.setWorkingRating(player.getRating())
                player.setWorkingRatingDeviation(calculateNewRD(player.getGlicko2RatingDeviation(), player.getVolatility()))
                player.setWorkingVolatility(player.getVolatility())
            }
        }

        // now iterate through the participants and confirm their new ratings
        for (player in results.getParticipants()) {
            player.finaliseRating()
            //println("\n" + player.toString())
        }

        // lastly, clear the result set down in anticipation of the next rating period
        results.clear()
    }

    private fun calculateNewRating(player: Rating, results: List<Result>) {
        // Abweichung in die Glicko-2 Skala (14)
        val phi: Double = player.getGlicko2RatingDeviation()

        // Schwankung des Spielers
        val sigma = player.getVolatility()

        // ln(σ^2)
        val a = ln(sigma.pow(2.0))

        // (18)
        val delta = subFunctionV(player, results) * outcomeBasedRating(player, results) //delta(player, results)
        val v = subFunctionV(player, results)


        // step 5.2
        var A = a
        var B = 0.0
        // ∆^2 > Φ^2+υ
        if (delta.pow(2.0) > phi.pow(2.0) + v) {
            // B = ln(∆^2 − Φ^2 − v)
            B = ln(delta.pow(2.0) - phi.pow(2.0) - v)
        } else { // ∆^2 <= Φ^2 + v
            var k = 1.0 // Sei k = 1
            // B = a - kτ --> initial
            B = a - k * abs(tau)
            // gehe wieder zu (ii)
            while (subFunctionF(B, delta, phi, v, a, tau) < 0) {
                k++
                B = a - k * abs(tau)
            }
        }

        // step 5.3
        var fA = subFunctionF(A, delta, phi, v, a, tau)
        var fB = subFunctionF(B, delta, phi, v, a, tau)

        // step 5.4
        while (abs(B - A) > Constants.getDefaultConvergenceTolerance()) {
            val ratingC = A + (A - B) * fA / (fB - fA)
            val fC = subFunctionF(ratingC, delta, phi, v, a, tau)
            if (fC * fB < 0) {
                A = B
                fA = fB
            } else {
                fA /= 2.0
            }
            B = ratingC
            fB = fC
        }

        val newSigma = exp(A / 2.0)

        player.setWorkingVolatility(newSigma)

        // Step 6
        val phiStar = calculateNewRD(phi, newSigma)

        // Step 7
        val newPhi = 1.0 / sqrt(1.0 / phiStar.pow(2.0) + 1.0 / v)

        player.setWorkingRating((player.getGlicko2Rating() + newPhi.pow(2.0) * outcomeBasedRating(player, results)))
        player.setWorkingRatingDeviation(newPhi)
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

//    private fun delta(player: Rating, results: List<Result>): Double {
//        return subFunctionV(player, results) * outcomeBasedRating(player, results)
//    }

    private fun calculateNewRD(phi: Double, sigma: Double): Double {
        return sqrt(phi.pow(2.0) + sigma.pow(2.0))
    }

    private fun outcomeBasedRating(player: Rating, results: List<Result>): Double {
        var outcomeBasedRating = 0.0

        for (result in results) {
            outcomeBasedRating = (outcomeBasedRating
                    + (subFunctionG(result.getOpponent(player).getGlicko2RatingDeviation())
                    * (result.getScore(player) - subFunctionE(
                    player.getGlicko2Rating(),
                    result.getOpponent(player).getGlicko2Rating(),
                    result.getOpponent(player).getGlicko2RatingDeviation()))))
        }
        return outcomeBasedRating
    }


}