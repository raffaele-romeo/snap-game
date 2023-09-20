package domain

import scala.util.Random

final case class Deck private (cards: List[Card]) {
  def +(other: Deck): Deck = {
    Deck(this.cards ++ other.cards)
  }

  def shuffle: Deck = copy(cards = Random.shuffle(cards))
}

object Deck {
  def generate: Deck = {
    val cards = Suit.values.flatMap { suit =>
      ((2 to 10).map(_.toString).toList ++ List("J", "Q", "K", "A"))
        .map(value => Card(suit, value))
    }

    Deck(Random.shuffle(cards))
  }
}

final case class Card(suit: Suit, value: String) {
  override def toString = s"$value${suit.toString}"
}

sealed trait Suit

object Suit {
  final case object Spades extends Suit {
    override def toString: String = "S"
  }
  final case object Clubs extends Suit {
    override def toString: String = "C"
  }
  final case object Hearts extends Suit {
    override def toString: String = "H"
  }
  final case object Diamonds extends Suit {
    override def toString: String = "D"
  }

  def values: List[Suit] = List(Spades, Clubs, Hearts, Diamonds)
}
