package main.glicko

class PeriodResults (participants: HashSet<Rating>?){
    private var results = mutableListOf<Result>()
    private var participants = hashSetOf<Rating>()

    init {
        if (participants.isNullOrEmpty()) this.participants
            else this.participants = participants
    }

    /**
     * Add a result to the set.
     */
    fun addResult(winner: Rating?, loser: Rating?) = results.add(Result(winner!!, loser!!, false))
    fun addDraw(player1: Rating?, player2: Rating?) = results.add(Result(player1!!, player2!!, true))

    /**
     * Get a list of the results for a given player.
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