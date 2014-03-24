
import org.scalatest._

class TestGame extends FlatSpec with Matchers {

  "A new game" should "have nine squares" in {
    Game.newBoard().length should be (9)
  }

  "Start player" should "be PlayerOne" in {
    Game.startingPlayer() should be (Player.One)
  }

  "Next player" should "be PlayerOne if no Player is current" in {
    Game.nextPlayer(null) should be (Player.One)
  }

  "Next player" should "be PlayerOne if PlayerTwo is current" in {
    Game.nextPlayer(Player.Two) should be (Player.One)
  }

  "Next player" should "be PlayerTwo if PlayerOne is current" in {
    Game.nextPlayer(Player.One) should be (Player.Two)
  }

  "0" should "be an allowed move" in {
    Game.isLegalMove(0) should be (true)
  }

  "8" should "be an allowed move" in {
    Game.isLegalMove(8) should be (true)
  }

  "9" should "not be an allowed move" in {
    Game.isLegalMove(9) should be (false)
  }

  "-1" should "not be an allowed move" in {
    Game.isLegalMove(-1) should be (false)
  }

  "Two players" should "exist" in {
    Player.One.key should be ("O")
    Player.Two.key should be ("X")
  }

  "New board" should "have all free spaces" in {
    val board = new Array[Player.Item](3)
    Game.isSpaceFree(board, 0) should be (true)
  }

  "Existing board" should "not have all free spaces" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    Game.isSpaceFree(board, 0) should be (false)
  }

  "New board" should "should not be ended" in {
    val board = Game.newBoard()
    Game.isEndGame(board) should be (false)
  }

  "Winning board" should "should be ended" in {
    val board = Game.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One
    Game.isEndGame(board) should be (true)
  }

  "Full board" should "should be ended" in {
    val board = new Array[Player.Item](1)
    board(0) = Player.One
    Game.isEndGame(board) should be (true)
  }

  "Move" should "add the player onto the board" in {
    val board = new Array[Player.Item](3)
    val player = Player.One
    val position = 0

    val expectedBoard = Array(Player.One, null, null)

    Game.move(board, player, position) should be (expectedBoard)
  }

  "Two moves" should "add the player onto the board" in {
    val board = new Array[Player.Item](3)
    val playerOne = Player.One
    val positionOne = 0

    val newBoard = Game.move(board, playerOne, positionOne)
    val playerTwo = Player.Two
    val positionTwo = 1

    val expectedBoard = Array(Player.One, Player.Two, null)

    Game.move(newBoard, playerTwo, positionTwo) should be (expectedBoard)
  }

  "A bot move" should "be in a random location" in {
    val board = Game.newBoard()
    val player = Player.One

    val newBoard = Game.botMove(board, player)

    newBoard.count(c => c == Player.One) should be (1)
    newBoard.count(c => c == Player.Two) should be (0)
  }

  "Two bot moves" should "be in a random location" in {
    val board = Game.newBoard()
    val player = Player.One

    val newBoardOne = Game.botMove(board, player)
    val newBoardTwo = Game.botMove(newBoardOne, player)

    newBoardTwo.count(c => c == Player.One) should be (2)
    newBoardTwo.count(c => c == Player.Two) should be (0)
  }

  "Bot" should "try to block an almost winning move" in {
    val board = Game.newBoard()
    board(0) = Player.Two
    board(1) = Player.Two
    val player = Player.One

    val newBoard = Game.botMove(board, player)

    newBoard(2) should be (Player.One)
  }

  "Bot" should "try and got for a win" in {
    val board = Game.newBoard()
    board(0) = Player.One
    board(3) = Player.One
    val player = Player.One

    val newBoard = Game.botMove(board, player)

    newBoard(6) should be (Player.One)
  }

  "A row win" should "not be set if no cells are set to a player" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    Game.checkRowWin(board) should be (null)
  }

  "A row win" should "not be set if the cells are mixed" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.Two
    board(2) = Player.One

    Game.checkRowWin(board) should be (null)
  }

  "A row win" should "be set if all cells are the same" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One

    Game.checkRowWin(board) should be (Player.One)
  }

  "No rows" should "mean no winner" in {
    val board = Game.newBoard()
    Game.checkBoardWin(board) should be (null)
  }

  "A row" should "mean a winner" in {
    val board = Game.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One
    Game.checkBoardWin(board) should be (Player.One)
  }

  "A column" should "mean a winner" in {
    val board = Game.newBoard()
    board(1) = Player.One
    board(4) = Player.One
    board(7) = Player.One
    Game.checkBoardWin(board) should be (Player.One)
  }

  "A diagnal" should "mean a winner" in {
    val board = Game.newBoard()
    board(2) = Player.One
    board(4) = Player.One
    board(6) = Player.One
    Game.checkBoardWin(board) should be (Player.One)
  }

  "A almost row win" should "not be set if no cells are set to a player" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    Game.almostWinningPosition(board) should be (-1)
  }

  "A almost row win" should "not be set if mutliple players are in the group" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.Two
    Game.almostWinningPosition(board) should be (-1)
  }

  "A almost row win" should "be set if two of the same player are in the group" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(2) = Player.One
    Game.almostWinningPosition(board) should be (1)
  }

  "No nearly completed rows" should "mean no almost winner" in {
    val board = Game.newBoard()
    Game.checkBoardAlmostWinningPosition(board) should be (-1)
  }

  "A nearly completed row" should "mean an almost winner" in {
    val board = Game.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    Game.checkBoardAlmostWinningPosition(board) should be (2)
  }

  "A nearly completed column" should "mean an almost winner" in {
    val board = Game.newBoard()
    board(4) = Player.One
    board(7) = Player.One
    Game.checkBoardAlmostWinningPosition(board) should be (1)
  }

  "A nearly completed diagnal" should "mean an almost winner" in {
    val board = Game.newBoard()
    board(2) = Player.One
    board(6) = Player.One
    Game.checkBoardAlmostWinningPosition(board) should be (4)
  }

  "possible wins" should "be 8 possible cell groups" in {
    val wins = Array(
      Array(0, 1, 2),
      Array(3, 4, 5),
      Array(6, 7, 8),
      Array(0, 3, 6),
      Array(1, 4, 7),
      Array(2, 5, 8),
      Array(0, 4, 8),
      Array(2, 4, 6)
    )
    Game.possibleWins().deep should be (wins.deep)
  }

  "Empty board" should "show the board" in {
    val board = Game.newBoard()
    Game.displayBoard(board) should be (" | | \n-----\n | | \n-----\n | | \n")
  }

  "Board with PlayerOne" should "show the board" in {
    val board = Game.newBoard()
    board(0) = Player.One
    Game.displayBoard(board) should be ("O| | \n-----\n | | \n-----\n | | \n")
  }

  "Board with PlayerTwo" should "show the board" in {
    val board = Game.newBoard()
    board(5) = Player.Two
    Game.displayBoard(board) should be (" | | \n-----\n | |X\n-----\n | | \n")
  }

  "Board with few moves" should "show the board" in {
    val board = Game.newBoard()
    board(5) = Player.Two
    board(8) = Player.One
    board(2) = Player.Two
    Game.displayBoard(board) should be (" | |X\n-----\n | |X\n-----\n | |O\n")
  }

  "A non winning game" should "display a draw message" in {
    val board = Game.newBoard()
    Game.displayWinner(board) should be ("Game is a Draw!")
  }

  "A winning game" should "display the winner message" in {
    val board = Game.newBoard()
    board(5) = Player.Two
    board(8) = Player.Two
    board(2) = Player.Two
    Game.displayWinner(board) should be ("Winner is Player Two (X)!")
  }

  "A game" should "show show the result" in {
    val board = Game.newBoard()
    board(0) = Player.Two
    board(1) = Player.Two
    board(2) = Player.Two
    Game.displayResult(board) should be ("\nX|X|X\n-----\n | | \n-----\n | | \n\nWinner is Player Two (X)!")
  }

}