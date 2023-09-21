import domain.{GameContext, MatchCardOpt}
import game.Snap

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.Try

object Main {
  def main(args: Array[String]): Unit = {
    var continuePlaying = true

    while (continuePlaying) {
      val numberOfDecks = readUserInput(
        message = "Enter number of card decks. At least 1 deck is required ",
        validateNumberOfDecks
      )

      val possibleMatchingOpt: List[MatchCardOpt] =
        if (numberOfDecks == 1) List(MatchCardOpt.Suit, MatchCardOpt.Value)
        else
          MatchCardOpt.values

      val matchCardOpt = readUserInput(
        message = s"How cards should be matched? " +
          s"Possible values are: ${possibleMatchingOpt.map(_.toString).mkString(", ")} ",
        validateMatchCardOpt(numberOfDecks)
      )

      val numberOfPlayers = readUserInput(
        message = "Enter number of players. At least 2 players required ",
        validateNumberOfPlayers
      )

      val gameContext =
        GameContext(numberOfPlayers, matchCardOpt, numberOfDecks)

      val snap = new Snap(gameContext)

      val players = snap.run()
      val winner = snap.getWinner(players)

      println(
        s"The winner player is ${winner.name} and total score ${winner.totalScore}"
      )

      println("Do you want to play again? Answer yes/no ")
      continuePlaying = readLine() match {
        case "yes" => true
        case _     => false
      }
    }
  }

  private def validateNumberOfPlayers(numberOfPlayers: String): Option[Int] =
    Try(numberOfPlayers.toInt).toOption.filter(_ >= 2)

  private def validateMatchCardOpt(
    decks: Int
  )(matchCardOpt: String): Option[MatchCardOpt] =
    matchCardOpt.toLowerCase match {
      case "suit"              => Some(MatchCardOpt.Suit)
      case "value"             => Some(MatchCardOpt.Value)
      case "both" if decks > 1 => Some(MatchCardOpt.Both)
      case _                   => None
    }

  private def validateNumberOfDecks(decks: String): Option[Int] =
    Try(decks.toInt).toOption match {
      case Some(value) if value >= 1 => Some(value)
      case _                         => None
    }

  @tailrec
  def readUserInput[A](message: String, validator: String => Option[A]): A = {
    println(message)

    validator(readLine()) match {
      case Some(value) => value
      case None        => readUserInput(message, validator)
    }
  }
}
