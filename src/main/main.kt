import main.elo.Elo

fun main(){

    val elo = Elo(1324f, 1234f)
    elo.eloRating(16, 1)

    val elo2 = Elo(1324f, 1234f)
    elo2.eloRating(16, 2)

    val elo3 = Elo(1624f, 1134f)
    elo3.eloRating(25, 1)

    val elo4 = Elo(1200f, 1000f)
    elo4.eloRating(30, 1)

    val elo5 = Elo(1200f, 1000f)
    elo5.eloRating(30, 0)

    val elo6 = Elo(1200f, 1000f)
    elo6.eloRating(16, 0)

//    val karl = Rating()


}
