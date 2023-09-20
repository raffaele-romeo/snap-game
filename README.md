# Game Rules

* The application should ask the user how many playing card decks to play with (also possibly how many players,
  but can also default to just 2)
* The application should ask the user whether cards should be matched: on suit, value, or both.
* The application should shuffle the decks before play commences.
* Games of snap should then be simulated between players.
* Cards are played one at a time in order by each player in the game, onto their stack.
  When a player deals a card that matches a card at the top of another player's stack, the first player to 'shout' snap 'takes' the two piles.
* The stop condition can be either after the first round when all cards were dealt,
  with the winner being the player who scored more cards, or after multiple rounds once one player holds all the cards.
  Whichever is easier / makes more sense to you.
* You may choose single or multithreaded approach whichever suits you better, but do include some form of randomness.

## Assumptions

* A minimum of one deck is necessary for gameplay. 
* The game is exclusively for two players. 
* When playing with just one deck, if the user's answer is "both" for matching cards option, the game automatically defaults to matching cards based on their values. 
* The stop condition occurs when the deck is completely exhausted.