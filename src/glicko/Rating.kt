package glicko

class Rating {

    val uid : String? = null
    val rating = 0.0
    var ratingDeviation = 0.0
    val volatility = 0.0
    var numberOfResults = 0

    var workingRating = 0.0
    var workingRatingDeviation = 0.0
    var workingVolatility = 0.0


    override fun toString(): String = "$uid / $rating " +
            "/ $ratingDeviation / $volatility $numberOfResults "

    fun incrementNumberOfResults(increment: Int) {
        numberOfResults += + increment
    }

    fun getGlicko2Rating(): Double = Calculator.convertRatingToGlicko2Scale(rating)

    fun getGlicko2RatingDeviation(): Double = Calculator.convertRatingDeviationToGlicko2Scale(ratingDeviation)

// ----------------------------------------------------------------------
//    fun setGlicko2Rating(rating: Double) {
//        rating = Calcutator.convertRatingToOriginalGlickoScale(rating)
//    }
//
//    fun setVolatility(volatility: Double) {
//        this.volatility = volatility
//    }
//
//    fun setRatingDeviation(ratingDeviation: Double) {
//        this.ratingDeviation = ratingDeviation
//    }
//
//    fun setGlicko2RatingDeviation(ratingDeviation: Double) {
//        this.ratingDeviation = Calculator.convertRatingDeviationToOriginalGlickoScale(ratingDeviation)
//    }
//
//
//    fun setWorkingVolatility(workingVolatility: Double) {
//        this.workingVolatility = workingVolatility
//    }
////
//    fun setWorkingRating(workingRating: Double) {
//        this.workingRating = workingRating
//    }
//
//    fun setWorkingRatingDeviation(workingRatingDeviation: Double) {
//        this.workingRatingDeviation = workingRatingDeviation
//    }
//
//    fun finaliseRating() {
//        setGlicko2Rating(workingRating)
//        setGlicko2RatingDeviation(workingRatingDeviation)
//        setVolatility(workingVolatility)
//        setWorkingRatingDeviation(0.0)
//        setWorkingRating(0.0)
//        setWorkingVolatility(0.0)
//    }
}

