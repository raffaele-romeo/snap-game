package game

import domain._
import org.scalatest.flatspec.AnyFlatSpec

class SnapTest extends AnyFlatSpec {
  private val gameContext =
    GameContext.make(numberOfDecks = 1, MatchCardOpt.Value)

  "game.Snap.updatePlayersInfo" should "update players successfully when snap occurs" in {
    val card = Card(Suit.Spades, 13)

    val player1 =
      Player(
        id = 0,
        stack = List(card.copy(value = 11), card.copy(value = 10)),
        deck = List(card)
      )

    val player2 = Player(
      id = 1,
      stack = List(card.copy(value = 11), card.copy(value = 5)),
      deck = List(card.copy(suit = Suit.Clubs))
    )

    val snap = new Snap(gameContext)

    val expectedOutput = List(
      Player(
        id = 0,
        stack = List(
          card.copy(value = 11),
          card.copy(value = 5),
          card.copy(suit = Suit.Clubs),
          card.copy(value = 11),
          card.copy(value = 10),
          card
        ),
        deck = List.empty
      ),
      Player(id = 1, stack = List.empty, deck = List.empty)
    )

    val output =
      snap.updatePlayersInfo(
        List(player1, player2),
        fastestPlayerIdToCallSnap = 0
      )

    assert(output === expectedOutput)
  }

  it should "update players successfully when snap does not occur" in {
    val card = Card(Suit.Spades, 11)

    val player1 =
      Player(
        id = 0,
        stack = List(card.copy(value = 11), card.copy(value = 10)),
        deck = List(card)
      )

    val player2 =
      Player(
        id = 1,
        stack = List(card.copy(value = 11), card.copy(value = 5)),
        deck = List(card.copy(value = 4))
      )

    val snap = new Snap(gameContext)

    val expectedOutput = List(
      Player(
        id = 0,
        stack = List(card.copy(value = 11), card.copy(value = 10), card),
        deck = List.empty
      ),
      Player(
        id = 1,
        stack = List(
          card.copy(value = 11),
          card.copy(value = 5),
          card.copy(value = 4)
        ),
        deck = List.empty
      )
    )

    val output =
      snap.updatePlayersInfo(
        List(player1, player2),
        fastestPlayerIdToCallSnap = 0
      )

    assert(output === expectedOutput)
  }
}
