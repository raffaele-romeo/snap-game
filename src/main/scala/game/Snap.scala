package game

import domain._

import scala.util.Random

class Snap(gameContext: GameContext) {
  private val deck: Deck =
    (1 to gameContext.numberOfDecks).map(_ => Deck.generate).reduce(_ + _)

  private val numberOfTurns: Int = deck.cards.size / gameContext.numberOfPlayers

  private val splitDeck = deck.cards.grouped(numberOfTurns).toArray

  private val players: List[Player] =
    (0 until gameContext.numberOfPlayers)
      .map(id => Player(id, splitDeck(id)))
      .toList

  def run(): Player = {
    val output = (0 until numberOfTurns).foldLeft(players) {
      case (players, turnNumber) =>
        updatePlayersInfo(
          turnNumber,
          players,
          fastestPlayerIdToCallSnap = Random.shuffle(players.map(_.id)).head
        )
    }

    getWinner(output)
  }

  private[game] def updatePlayersInfo(
    turnNumber: Int,
    players: List[Player],
    fastestPlayerIdToCallSnap: Int
  ): List[Player] = {
    val currentPlayerId = getCurrentPlayerId(turnNumber)
    val otherPlayerId = getOtherPlayerId(currentPlayerId)

    val currentPlayer = getPlayerById(currentPlayerId, players).play
    val otherPlayer = getPlayerById(otherPlayerId, players).play

    val playersLastCard: List[Card] =
      List(currentPlayer.lastCard, otherPlayer.lastCard).flatten

    val updatedPlayers = List(currentPlayer, otherPlayer)

    val output = if (isSnap(playersLastCard)) {
      val slowestPlayerIdToCallSnap = getOtherPlayerId(
        fastestPlayerIdToCallSnap
      )
      val slowestPlayerToCallSnap =
        getPlayerById(slowestPlayerIdToCallSnap, updatedPlayers)

      List(
        getPlayerById(fastestPlayerIdToCallSnap, updatedPlayers)
          .addStack(slowestPlayerToCallSnap.stack),
        getPlayerById(slowestPlayerIdToCallSnap, updatedPlayers).emptyStack()
      )
    } else {
      updatedPlayers
    }

    output.sortBy(_.id)
  }

  private def getWinner(players: List[Player]): Player =
    players.maxBy(_.totalScore)

  private def getCurrentPlayerId(id: Int): Int =
    id % gameContext.numberOfPlayers

  private def getPlayerById(id: Int, players: List[Player]): Player =
    players(id)

  private def isSnap(playersLastCard: List[Card]): Boolean = {
    def isSnap[T](l: List[T]) = l.distinct.size < playersLastCard.size

    gameContext.matchCardsOn match {
      case MatchCardOpt.Suit  => isSnap(playersLastCard.map(_.suit))
      case MatchCardOpt.Value => isSnap(playersLastCard.map(_.value))
      case MatchCardOpt.Both  => isSnap(playersLastCard.map(_.toString))
    }
  }

  private def getOtherPlayerId(id: Int): Int = if (id == 0) 1 else 0
}
