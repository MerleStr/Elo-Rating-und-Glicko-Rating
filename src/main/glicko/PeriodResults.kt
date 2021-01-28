package glicko

class PeriodResults(participants: HashSet<Rating>?) {
    private var results = mutableListOf<Result>()
    private var participants = hashSetOf<Rating>()

    init {
        if (participants.isNullOrEmpty()) this.participants
        else this.participants = participants
    }

    fun addResult(winner: Rating?, loser: Rating?) = results.add(Result(winner!!, loser!!, false))
    fun addDraw(player1: Rating?, player2: Rating?) = results.add(Result(player1!!, player2!!, true))

    fun getResults(player: Rating?): List<Result> {
        val filteredResults: MutableList<Result> = ArrayList()
        results.forEach {
            if (it.participated(player!!)) filteredResults.add(it)
        }
        return filteredResults
    }

    fun getParticipants(): Set<Rating> {

        results.forEach {
            participants.add(it.getWinner())
            participants.add(it.getLoser())
        }
        return participants
    }

    fun addParticipants(rating: Rating?) = participants.add(rating!!)

    fun clear() = results.clear()

}