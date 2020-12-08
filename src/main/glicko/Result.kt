package main.glicko

class Result(player1: Rating, player2: Rating, isDraw: Boolean) {

    private var winner: Rating = player1
    private var loser: Rating = player2
    private var isGameDraw: Boolean = isDraw

    init {
        if (!validPlayers(winner, loser)) throw java.lang.IllegalArgumentException()
    }

    private fun validPlayers(player1: Rating, player2: Rating): Boolean = player1 != player2

    fun participated(player: Rating): Boolean = winner == player || loser == player

    fun getWinner() : Rating = winner
    fun getLoser() : Rating = loser

    /**
     * Returns the "score" for a match.
     *
     * @param player
     * @return 1 for a win, 0.5 for a draw and 0 for a loss
     * @throws IllegalArgumentException
     */
    @Throws(IllegalArgumentException::class)
    fun getScore(player: Rating): Double {
        return if (isGameDraw) {
            POINTS_FOR_DRAW
        } else {
            when {
                winner == player -> POINTS_FOR_WIN
                loser == player -> POINTS_FOR_LOSS
                else -> throw IllegalArgumentException("Player " + player.uid + " did not participate in match")
            }
        }
    }

    /**
     * Given a particular player, returns the opponent.
     *
     * @param player
     * @return opponent
     */
    fun getOpponent(player: Rating): Rating {
        return when {
            winner == player -> loser
            loser == player -> winner
            else -> throw IllegalArgumentException("Player " + player.uid + " did not participate in match")
        }
    }

    companion object {
        private const val POINTS_FOR_WIN = 1.0
        private const val POINTS_FOR_LOSS = 0.0
        private const val POINTS_FOR_DRAW = 0.5
    }
}