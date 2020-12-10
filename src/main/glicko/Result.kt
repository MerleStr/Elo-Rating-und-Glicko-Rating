package glicko

class Result(player1: Rating, player2: Rating, isDraw: Boolean) {

    private var winner: Rating = player1
    private var loser: Rating = player2
    private var isGameDraw: Boolean = isDraw

    init {
        require(validPlayers(winner, loser))
    }

    private fun validPlayers(player1: Rating, player2: Rating): Boolean = player1 != player2

    fun participated(player: Rating): Boolean = winner == player || loser == player

    fun getWinner() : Rating = winner
    fun getLoser() : Rating = loser


    @Throws(IllegalArgumentException::class)
    fun getScore(player: Rating): Double {
        return if (isGameDraw) {
            POINTS_FOR_DRAW
        } else {
            when {
                winner == player -> POINTS_FOR_WIN
                loser == player -> POINTS_FOR_LOSS
                else -> throw IllegalArgumentException("Player " + player.getUId() + " did not participate in match")
            }
        }
    }

    fun getOpponent(player: Rating): Rating {
        return when {
            winner == player -> loser
            loser == player -> winner
            else -> throw IllegalArgumentException("Player " + player.getUId() + " did not participate in match")
        }
    }

    companion object {
        private const val POINTS_FOR_WIN = 1.0
        private const val POINTS_FOR_LOSS = 0.0
        private const val POINTS_FOR_DRAW = 0.5
    }
}