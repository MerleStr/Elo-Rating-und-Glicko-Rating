package glicko

class Rating(userId: String, initRating : Double?, initRatingDeviation : Double?, initVolatility : Double?) {

    private var uid : String = userId
    private var rating = 0.0
    private var ratingDeviation = 0.0
    private var volatility = 0.0
    private var numberOfResults = 0

    private var workingRating = 0.0
    private var workingRatingDeviation = 0.0
    private var workingVolatility = 0.0

    init {
        uid = userId
        if (initRating == null || initRatingDeviation == null || initVolatility == null){
            rating = Constants.DEFAULT_RATING
            ratingDeviation = Constants.DEFAULT_DEVIATION
            volatility = Constants.DEFAULT_VOLATILITY
        } else {
            rating = initRating
            ratingDeviation = initRatingDeviation
            volatility = initVolatility
        }
    }

    fun getGlicko2Rating(): Double = Constants.convertRatingToGlicko2Scale(rating)

    fun getGlicko2RatingDeviation(): Double = Constants.convertRatingDeviationToGlicko2Scale(ratingDeviation)

    fun getUId() = uid

    fun getRating() = rating

    fun getRatingDeviation() = ratingDeviation

    fun getVolatility() = volatility

// ----------------------------------------------------------------------

    fun setGlicko2Rating(rating: Double) {
        this.rating = Constants.convertRatingToOriginalGlickoScale(rating)
    }

    fun setGlicko2RatingDeviation(ratingDeviation: Double) {
        this.ratingDeviation = Constants.convertRatingDeviationToOriginalGlickoScale(ratingDeviation)
    }

    fun setVolatility(workingVolatility: Double) : Double{
        this.volatility = workingVolatility
        return this.volatility
    }

    fun setWorkingVolatility(workingVolatility: Double) : Double {
        this.workingVolatility = workingVolatility
        return workingVolatility
    }

    fun setWorkingRating(workingRating: Double) {
        this.workingRating = workingRating
    }

    fun setWorkingRatingDeviation(workingRatingDeviation: Double) {
        this.workingRatingDeviation = workingRatingDeviation
    }

    // ----------------------------------------------------------------------


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
        return (kotlin.math.round(this * multiplier) / multiplier)
    }

    override fun toString(): String = "$uid / ${rating.round(2)} / " +
            "${ratingDeviation.round(2)} / ${volatility.round(7)}/ $numberOfResults"

    fun incrementNumberOfResults(increment: Int) {
        numberOfResults += + increment
    }
}

