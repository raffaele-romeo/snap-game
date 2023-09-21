package domain

final case class Player(id: Int,
                        deck: List[Card] = List.empty,
                        stack: List[Card] = List.empty) {
  val name: String = s"Player ${id}"

  def play: Player = {
    if (deck.isEmpty && stack.isEmpty)
      copy(deck = List.empty, stack = List.empty)
    else if (deck.isEmpty)
      copy(deck = stack.tail, stack = List(stack.head))
    else
      copy(deck = deck.tail, stack = stack :+ deck.head)
  }
  def lastCard: Option[Card] = stack.lastOption
  def totalScore: Int = deck.size + stack.size
  def addCardToStack(card: Card): Player = copy(stack = stack :+ card)
  def addCardToDeck(card: Card): Player = copy(deck = deck :+ card)
  def addStack(stack: List[Card]): Player =
    copy(stack = stack concat this.stack)
  def emptyStack(): Player = copy(stack = List.empty)
}
