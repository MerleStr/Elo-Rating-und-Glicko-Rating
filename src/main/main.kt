import main.elo.Elo

fun main(){

    val elo = Elo(1324.0, 1234.0)
    elo.eloRating(16, 1)

    val elo2 = Elo(1324.0, 1234.0)
    elo2.eloRating(16, 2)

    val elo3 = Elo(1624.0, 1134.0)
    elo3.eloRating(25, 1)

    val elo4 = Elo(1200.0, 1000.0)
    elo4.eloRating(30, 1)

    val elo5 = Elo(1200.0, 1000.0)
    elo5.eloRating(30, 0)

    val elo6 = Elo(1200.0, 1000.0)
    elo6.eloRating(16, 0)

//    val karl = Rating()


}
