package glicko

class PeriodResults {
    private var results = mutableListOf<Result>()
    private var participants = hashSetOf<Rating>()

    /**
     * Constructor that allows you to initialise the list of participants.
     *
     * @param participants (Set of Rating objects)
     */
    fun RatingPeriodResults(participants: HashSet<Rating>) {
        this.participants = participants
    }

    /**
     * Add a result to the set.
     *
     * @param winner
     * @param loser
     */
    fun addResult(winner: Rating?, loser: Rating?) = results.add(Result(winner!!, loser!!, false))
    fun addDraw(player1: Rating?, player2: Rating?) = results.add(Result(player1!!, player2!!, true))

    /**
     * Get a list of the results for a given player.
     *
     * @param player
     * @return List of results
     */
    fun getResults(player: Rating?): List<Result> {
        val filteredResults: MutableList<Result> = ArrayList()
        for (result in results) {
            if (result.participated(player!!)) {
                filteredResults.add(result)
            }
        }
        return filteredResults
    }

    /**
     * Get all the participants whose results are being tracked.
     *
     * @return set of all participants covered by the resultset.
     */
    fun getParticipants(): Set<Rating> {
        // Run through the results and make sure all players have been pushed into the participants set.
        for (result in results) {
            participants.add(result.getWinner())
            participants.add(result.getLoser())
        }
        return participants
    }

    /**
     * Add a participant to the rating period, e.g. so that their rating will
     * still be calculated even if they don't actually compete.
     *
     * @param rating
     */
    fun addParticipants(rating: Rating?) {
        participants.add(rating!!)
    }

    /**
     * Clear the resultset.
     */
    fun clear() {
        results.clear()
    }
}