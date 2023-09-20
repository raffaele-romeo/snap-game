package game

import domain._
import org.scalatest.flatspec.AnyFlatSpec

class SnapTest extends AnyFlatSpec {
  private val gameContext =
    GameContext(
      numberOfPlayers = 2,
      numberOfDecks = 1,
      matchCardsOn = MatchCardOpt.Value
    )

  "game.Snap.updatePlayersInfo" should "update players successfully when snap occurs" in {
    val currentPlayerId = 0
    val card = Card(Suit.Spades, "A")

    val player1 =
      Player(
        id = 0,
        stack = List(card.copy(value = "Q"), card.copy(value = "K")),
        deck = List(card)
      )

    val player2 = Player(
      id = 1,
      stack = List(
        card.copy(value = "A"),
        card.copy(value = "5"),
        card.copy(suit = Suit.Clubs)
      ),
      deck = List(card.copy(suit = Suit.Clubs))
    )

    val snap = new Snap(gameContext)

    val expectedOutput = List(
      Player(
        id = 0,
        stack = List(
          card.copy(value = "A"),
          card.copy(value = "5"),
          card.copy(suit = Suit.Clubs),
          card.copy(value = "Q"),
          card.copy(value = "K"),
          card
        ),
        deck = List.empty
      ),
      Player(
        id = 1,
        stack = List.empty,
        deck = List(card.copy(suit = Suit.Clubs))
      )
    )

    val output =
      snap.updatePlayersInfo(
        currentPlayerId,
        List(player1, player2),
        fastestPlayerIdToCallSnap = 1
      )

    assert(output === expectedOutput)
  }

  it should "update players successfully when snap does not occur" in {
    val currentPlayerId = 0
    val card = Card(Suit.Spades, "Q")

    val player1 =
      Player(
        id = 0,
        stack = List(card.copy(value = "Q"), card.copy(value = 10.toString)),
        deck = List(card)
      )

    val player2 =
      Player(
        id = 1,
        stack =
          List(card.copy(value = 9.toString), card.copy(value = 5.toString)),
        deck = List(card.copy(value = 4.toString))
      )

    val snap = new Snap(gameContext)

    val expectedOutput = List(
      Player(
        id = 0,
        stack =
          List(card.copy(value = "Q"), card.copy(value = 10.toString), card),
        deck = List.empty
      ),
      Player(
        id = 1,
        stack =
          List(card.copy(value = 9.toString), card.copy(value = 5.toString)),
        deck = List(card.copy(value = 4.toString))
      )
    )

    val output =
      snap.updatePlayersInfo(
        currentPlayerId,
        List(player1, player2),
        fastestPlayerIdToCallSnap = 0
      )

    assert(output === expectedOutput)
  }
}
