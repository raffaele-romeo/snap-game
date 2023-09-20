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

  private val playersWithDeck = dealCardToPlayers(deck, playersWithoutDeck)

  def run(): List[Player] =
    deck.cards.indices.foldLeft(playersWithDeck) {
      case (players, turnNumber) =>
        updatePlayersInfo(
          turnNumber,
          players,
          fastestPlayerIdToCallSnap = Random.between(0, 2)
        )
    }

  def getWinner(players: List[Player]): Player =
    players.maxBy(_.totalScore)

  private def dealCardToPlayers(deck: Deck,
                                players: List[Player]): List[Player] =
    deck.cards.zipWithIndex
      .foldLeft(players.toArray) {
        case (players, (card, index)) =>
          val playerId = getPlayerId(index)
          val player = players(playerId)

          players.update(playerId, player.addCardToDeck(card))
          players
      }
      .toList

  private[game] def updatePlayersInfo(
    turnNumber: Int,
    players: List[Player],
    fastestPlayerIdToCallSnap: Int
  ): List[Player] = {
    val currentPlayerId = getPlayerId(turnNumber)
    val previousPlayerId = getPlayerId(turnNumber - 1)

    val otherPlayers = players.filterNot(
      player => Set(currentPlayerId, previousPlayerId)(player.id)
    )

    val currentPlayer = getPlayerById(currentPlayerId, players).play
    val previousPlayer = getPlayerById(previousPlayerId, players)

    val currentPlayersPlaying = List(previousPlayer, currentPlayer)
    val updatedPlayers = currentPlayersPlaying ++ otherPlayers

    val output = if (isSnap(currentPlayersPlaying.flatMap(_.lastCard))) {
      val slowestPlayerToCallSnap =
        getOtherPlayer(fastestPlayerIdToCallSnap, currentPlayersPlaying)

      List(
        getPlayerById(fastestPlayerIdToCallSnap, currentPlayersPlaying)
          .addStack(slowestPlayerToCallSnap.stack),
        slowestPlayerToCallSnap.emptyStack()
      ) ++ otherPlayers
    } else {
      updatedPlayers
    }

    output.sortBy(_.id)
  }

  private def getPlayerId(id: Int): Int =
    Math.floorMod(id, gameContext.numberOfPlayers)

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

  private def getOtherPlayer(id: Int, players: List[Player]): Player =
    if (id == 0) players.last else players.head
}
