package domain

import org.scalatest.flatspec.AnyFlatSpec

class DeckSpec extends AnyFlatSpec {

  "A Deck" should "have 52 cards shuffled" in {
    val deck = Deck.generate

    assert(deck.cards.size === 52)
    assert(deck.cards !== deck.cards.sortBy(_.value))
  }

  it should "combine multiple decks successfully" in {
    val deck = Deck.generate + Deck.generate + Deck.generate

    assert(deck.cards.size === 52 * 3)
  }
}
