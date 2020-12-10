package glicko

object Constants {

    val DEFAULT_RATING = 1500.0
    val DEFAULT_DEVIATION = 350.0
    val DEFAULT_VOLATILITY = 0.06
    val DEFAULT_TAU = 0.75
    val MULTIPLIER = 173.7178
    val CONVERGENCE_TOLERANCE = 0.000001


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

//    fun getDefaultRating(): Double {
//        return DEFAULT_RATING
//    }
//
//
//    fun getDefaultVolatility(): Double {
//        return DEFAULT_VOLATILITY
//    }
//
//
//    fun getDefaultRatingDeviation(): Double {
//        return DEFAULT_DEVIATION
//    }

    fun getDefaultConvergenceTolerance(): Double {
        return CONVERGENCE_TOLERANCE
    }

}