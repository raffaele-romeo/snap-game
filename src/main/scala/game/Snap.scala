package game

import domain._

import scala.util.Random

class Snap(gameContext: GameContext) {
  private val deck: Deck =
    (1 to gameContext.numberOfDecks)
      .map(_ => Deck.generate)
      .reduce(_ + _)
      .shuffle

  private val playersWithoutDeck: List[Player] =
    (0 until gameContext.numberOfPlayers)
      .map(id => Player(id))
      .toList

  private val playersWithDeck = dealCardsToPlayers(deck, playersWithoutDeck)

  def run(): List[Player] = {
    var players = playersWithDeck
    var turnNumber = 0

    while (!players.exists(_.totalScore == deck.cards.size)) {
      players.foreach(p => println(p.toString))
      val updatedPlayers = updatePlayersInfo(
        turnNumber,
        players,
        fastestPlayerIdToCallSnap = Random.between(0, 2)
      )

      players = removePlayersWithNoCards(updatedPlayers)
      turnNumber += 1
    }

    players
  }

  def getWinner(players: List[Player]): Player =
    players.maxBy(_.totalScore)

  private def removePlayersWithNoCards(players: List[Player]): List[Player] = {
    val newPlayers = players.filter(_.totalScore > 0)

    newPlayers.indices
      .foldLeft(newPlayers.toArray) {
        case (players, id) =>
          val player = players(id)
          players.updated(id, player.copy(id = id))
      }
      .toList
  }

  private[game] def updatePlayersInfo(
    turnNumber: Int,
    players: List[Player],
    fastestPlayerIdToCallSnap: Int
  ): List[Player] = {
    val playersSize = players.size
    val currentPlayerId = getPlayerId(turnNumber, playersSize)
    val previousPlayerId = getPlayerId(turnNumber - 1, playersSize)

    val otherPlayers = players.filterNot(
      player => Set(currentPlayerId, previousPlayerId)(player.id)
    )

    val currentPlayer = players(currentPlayerId).play
    val previousPlayer = players(previousPlayerId)

    val currentPlayersPlaying = List(previousPlayer, currentPlayer)
    val updatedPlayers = currentPlayersPlaying ++ otherPlayers

    val output = if (isSnap(currentPlayersPlaying.flatMap(_.lastCard))) {
      val (fastestPlayerToCallSnap, slowestPlayerToCallSnap) =
        if (fastestPlayerIdToCallSnap == 0)
          (currentPlayersPlaying.last, currentPlayersPlaying.head)
        else (currentPlayersPlaying.head, currentPlayersPlaying.last)

      List(
        fastestPlayerToCallSnap.addStack(slowestPlayerToCallSnap.stack),
        slowestPlayerToCallSnap.emptyStack()
      ) ++ otherPlayers
    } else {
      updatedPlayers
    }

    output.sortBy(_.id)
  }

  private def getPlayerId(id: Int, numberOfPlayers: Int): Int =
    Math.floorMod(id, numberOfPlayers)

  private def isSnap(playersLastCard: List[Card]): Boolean = {
    def isSnap[T](l: List[T]) = l.distinct.size < playersLastCard.size

    gameContext.matchCardsOn match {
      case MatchCardOpt.Suit  => isSnap(playersLastCard.map(_.suit))
      case MatchCardOpt.Value => isSnap(playersLastCard.map(_.value))
      case MatchCardOpt.Both  => isSnap(playersLastCard.map(_.toString))
    }
  }

  private def dealCardsToPlayers(deck: Deck,
                                 players: List[Player]): List[Player] =
    deck.cards.zipWithIndex
      .foldLeft(players.toArray) {
        case (players, (card, index)) =>
          val playerId = getPlayerId(index, gameContext.numberOfPlayers)
          val player = players(playerId)

          players.update(playerId, player.addCardToDeck(card))
          players
      }
      .toList
}
