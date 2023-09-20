package domain

final case class GameContext(numberOfPlayers: Int,
                             matchCardsOn: MatchCardOpt,
                             numberOfDecks: Int)

sealed trait MatchCardOpt

object MatchCardOpt {
  final case object Suit extends MatchCardOpt
  final case object Value extends MatchCardOpt
  final case object Both extends MatchCardOpt

  def values = List(Suit, Value, Both)
}
