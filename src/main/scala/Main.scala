import domain.{GameContext, MatchCardOpt}
import game.Snap

import scala.io.StdIn.readLine
import scala.util.Try

object Main {
  def main(args: Array[String]): Unit = {
    println("Enter number of card decks. At least 1 deck is required ")
    val numberOfDecksStr = readLine()
    val numberOfDecks = validNumberOfDecks(numberOfDecksStr)

    val possibleMatchingOpt: List[MatchCardOpt] =
      if (numberOfDecks == 1) List(MatchCardOpt.Suit, MatchCardOpt.Value)
      else
        MatchCardOpt.values

    println(
      s"How card should be matched? Possible values are: ${possibleMatchingOpt.map(_.toString).mkString(", ")} "
    )
    val matchCardOptStr = readLine()

    val gameContext = GameContext.make(
      numberOfDecks,
      validMatchCardOpt(numberOfDecks, matchCardOptStr)
    )

    val snap = new Snap(gameContext)
    val winner = snap.run()

    println(
      s"The winner player is player ${winner.id} and total score ${winner.totalScore}"
    )
  }

  private def validMatchCardOpt(decks: Int,
                                matchCardOpt: String): MatchCardOpt =
    matchCardOpt.toLowerCase match {
      case "suit"              => MatchCardOpt.Suit
      case "both" if decks > 1 => MatchCardOpt.Both
      case _                   => MatchCardOpt.Value
    }

  private def validNumberOfDecks(decks: String): Int =
    Try(decks.toInt).toOption match {
      case Some(value) if value >= 1 => value
      case _                         => 1
    }
}
