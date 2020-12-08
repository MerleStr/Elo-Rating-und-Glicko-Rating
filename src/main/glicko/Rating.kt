package main.glicko

class Rating(userId: String, initRating : Double?, initRatingDeviation : Double?, initVolatility : Double?) {

    private var uid : String = userId
    private var rating = 0.0
    private var ratingDeviation = 0.0
    private var volatility = 0.0
    private var numberOfResults = 0

    private var workingRating = 0.0
    private var workingRatingDeviation = 0.0
    private var workingVolatility = 0.0
    private val constant = Constants

    init {
        uid = userId
        if (initRating == null || initRatingDeviation == null || initVolatility == null){
            rating = constant.DEFAULT_RATING
            ratingDeviation = constant.DEFAULT_DEVIATION
            volatility = constant.DEFAULT_VOLATILITY
        } else {
            rating = initRating
            ratingDeviation = initRatingDeviation
            volatility = initVolatility
        }
    }

    override fun toString(): String = "$uid / $rating / $ratingDeviation / $volatility / $numberOfResults"

    fun incrementNumberOfResults(increment: Int) {
        numberOfResults += + increment
    }

    fun getGlicko2Rating(): Double {
        println("\nRating in rating: $rating")
        val tmp = constant.convertRatingToGlicko2Scale(rating)
        println("\nRating To Glicko 2 Scale: $tmp\n")

        return tmp
    }

    fun getGlicko2RatingDeviation(): Double = (constant.convertRatingDeviationToGlicko2Scale(ratingDeviation)).round(4)

// ----------------------------------------------------------------------
//    fun setGlicko2Rating(rating: Double) {
//        this.rating = Calcutator.convertRatingToOriginalGlickoScale(rating)
//    }

    fun setGlicko2Rating(rating: Double) {
        this.rating = constant.convertRatingToOriginalGlickoScale(rating)
    }

    fun setGlicko2RatingDeviation(ratingDeviation: Double) {
        this.ratingDeviation = constant.convertRatingDeviationToOriginalGlickoScale(ratingDeviation)
    }

    fun setRating(workingRating: Double) {
        this.rating = rating
    }

    fun setVolatility(workingVolatility: Double) {
        this.volatility = volatility
    }

    fun setRatingDeviation(ratingDeviation: Double) {
        this.ratingDeviation = ratingDeviation
    }

    fun setWorkingVolatility(workingVolatility: Double) {
        this.workingVolatility = workingVolatility
    }

    fun setWorkingRating(workingRating: Double) {
        this.workingRating = workingRating
    }

    fun setWorkingRatingDeviation(workingRatingDeviation: Double) {
        this.workingRatingDeviation = workingRatingDeviation
    }
//
    fun finaliseRating() {
        setGlicko2Rating(workingRating)
        setGlicko2RatingDeviation(workingRatingDeviation)
        setVolatility(workingVolatility)
        setWorkingRatingDeviation(0.0)
        setWorkingRating(0.0)
        setWorkingVolatility(0.0)
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return (kotlin.math.round(this * multiplier) / multiplier).toDouble()
    }

}

