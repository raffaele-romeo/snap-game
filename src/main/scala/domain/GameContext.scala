package domain

final case class GameContext private (numberOfPlayers: Int = 2,
                                      numberOfDecks: Int,
                                      matchCardsOn: MatchCardOpt)

object GameContext {
  def make(numberOfDecks: Int, matchCardsOn: MatchCardOpt): GameContext =
    GameContext(matchCardsOn = matchCardsOn, numberOfDecks = numberOfDecks)
}

sealed trait MatchCardOpt

object MatchCardOpt {
  final case object Suit extends MatchCardOpt
  final case object Value extends MatchCardOpt
  final case object Both extends MatchCardOpt

  def values = List(Suit, Value, Both)
}
