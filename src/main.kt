import elo.Elo
import glicko.Rating

fun main(){

    val elo = Elo(1324f, 1234f)
    elo.eloRating(16, true)

    val elo2 = Elo(1324f, 1234f)
    elo2.eloRating(16, false)

    val elo3 = Elo(1624f, 1134f)
    elo3.eloRating(25, false)

    val elo4 = Elo(1200f, 1000f)
    elo4.eloRating(30, true)

    val karl = Rating()


}
