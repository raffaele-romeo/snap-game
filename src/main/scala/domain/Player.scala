package domain

final case class Player(id: Int,
                        deck: List[Card],
                        stack: List[Card] = List.empty) {
  def play: Player = copy(deck = deck.tail, stack = stack :+ deck.head)
  def lastCard: Option[Card] = stack.lastOption
  def totalScore: Int = stack.size
  def addCard(card: Card): Player = copy(stack = stack :+ card)
  def addStack(stack: List[Card]): Player =
    copy(stack = stack concat this.stack)
  def emptyStack(): Player = copy(stack = List.empty)
}
