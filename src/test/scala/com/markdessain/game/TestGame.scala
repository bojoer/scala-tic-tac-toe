package com.markdessain.game

import org.scalatest._

class TestGame extends FlatSpec with Matchers {

  "A new com.markdessain.game" should "have nine cells" in {
    Game.newBoard().length should be (9)
  }

  "Starting Player" should "be Player One" in {
    Game.startingPlayer() should be (Player.One)
  }

  "Next Player" should "be Player One if there is no current player" in {
    Game.nextPlayer(null) should be (Player.One)
  }

  it should "be Player One if Player Two is the current player" in {
    Game.nextPlayer(Player.Two) should be (Player.One)
  }

  it should "be Player Two if Player One is the current player" in {
    Game.nextPlayer(Player.One) should be (Player.Two)
  }

  "Legal Move" should "allow 0" in {
    Game.isLegalMove(0) should be (true)
  }

  it should "allow 8" in {
    Game.isLegalMove(8) should be (true)
  }

  it should "not allow 9" in {
    Game.isLegalMove(9) should be (false)
  }

  it should "not allow -1" in {
    Game.isLegalMove(-1) should be (false)
  }

  "Two players" should "exist" in {
    Player.One.key should be ("O")
    Player.Two.key should be ("X")
  }

  "Free Space" should "be true in empty board" in {
    val board = new Array[Player.Item](3)
    Game.isSpaceFree(board, 0) should be (true)
  }

  it should "be false if space is filled" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    Game.isSpaceFree(board, 0) should be (false)
  }

  "End Game" should "be false in empty board" in {
    val board = Game.newBoard()
    Game.isEndGame(board) should be (false)
  }

  it should "be true in winning board" in {
    val board = Game.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One
    Game.isEndGame(board) should be (true)
  }

  it should "be true in full board" in {
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

  it should "add multiple players onto the board" in {
    val board = new Array[Player.Item](3)
    val playerOne = Player.One
    val positionOne = 0

    val newBoard = Game.move(board, playerOne, positionOne)
    val playerTwo = Player.Two
    val positionTwo = 1

    val expectedBoard = Array(Player.One, Player.Two, null)

    Game.move(newBoard, playerTwo, positionTwo) should be (expectedBoard)
  }

  "Bot Move" should "be in a random location" in {
    val board = Game.newBoard()
    val player = Player.One

    val newBoard = Game.botMove(board, player)

    newBoard.count(c => c == Player.One) should be (1)
    newBoard.count(c => c == Player.Two) should be (0)
  }

  it should "add multiple moves into random locations" in {
    val board = Game.newBoard()
    val player = Player.One

    val newBoardOne = Game.botMove(board, player)
    val newBoardTwo = Game.botMove(newBoardOne, player)

    newBoardTwo.count(c => c == Player.One) should be (2)
    newBoardTwo.count(c => c == Player.Two) should be (0)
  }

  it should "try to block an almost winning move" in {
    val board = Game.newBoard()
    board(0) = Player.Two
    board(1) = Player.Two
    val player = Player.One

    val newBoard = Game.botMove(board, player)

    newBoard(2) should be (Player.One)
  }

  it should "try and go for a win" in {
    val board = Game.newBoard()
    board(0) = Player.One
    board(3) = Player.One
    val player = Player.One

    val newBoard = Game.botMove(board, player)

    newBoard(6) should be (Player.One)
  }

  ignore should "try and go for a win even if it can block an opponent" in {
    // TODO: Currently it will try and block an opponent instead of winning.
    val board = Game.newBoard()
    board(0) = Player.Two
    board(1) = Player.Two
    board(6) = Player.One
    board(7) = Player.One
    val player = Player.One

    val newBoard = Game.botMove(board, player)

    newBoard(8) should be (Player.One)
  }

  "Check Row Win" should "not be a win if not all cells are filled" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    Game.checkRowWin(board) should be (null)
  }

  it should "not be a win if the cells are mixed" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.Two
    board(2) = Player.One

    Game.checkRowWin(board) should be (null)
  }

  it should "be a win if all cells are the same" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One

    Game.checkRowWin(board) should be (Player.One)
  }

  "Check Board Win" should "mean no winner if no groups are filled" in {
    val board = Game.newBoard()
    Game.checkBoardWin(board) should be (null)
  }

  it should "mean a winner if a row is filled" in {
    val board = Game.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One
    Game.checkBoardWin(board) should be (Player.One)
  }

  it should "mean a winner if a column is filled" in {
    val board = Game.newBoard()
    board(1) = Player.One
    board(4) = Player.One
    board(7) = Player.One
    Game.checkBoardWin(board) should be (Player.One)
  }

  it should "mean a winner if a diagonal is filled" in {
    val board = Game.newBoard()
    board(2) = Player.One
    board(4) = Player.One
    board(6) = Player.One
    Game.checkBoardWin(board) should be (Player.One)
  }

  "Almost Winning Position" should "be -1 if only one cell is set" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    Game.almostWinningPosition(board) should be (-1)
  }

  it should "be -1 if multiple players are in the group" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.Two
    Game.almostWinningPosition(board) should be (-1)
  }

  it should "be the empty cell where a player already controls the two other cells" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(2) = Player.One
    Game.almostWinningPosition(board) should be (1)
  }

  "Check Board Almost Winning Positions" should "be -1 if no almost complete groups exist" in {
    val board = Game.newBoard()
    Game.checkBoardAlmostWinningPosition(board) should be (-1)
  }

  it should "be the missing position in the row if a row is almost complete" in {
    val board = Game.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    Game.checkBoardAlmostWinningPosition(board) should be (2)
  }

  it should "be the missing position in the column if a column is almost complete" in {
    val board = Game.newBoard()
    board(4) = Player.One
    board(7) = Player.One
    Game.checkBoardAlmostWinningPosition(board) should be (1)
  }

  it should "be the missing position in the diagonal if a diagonal is almost complete" in {
    val board = Game.newBoard()
    board(2) = Player.One
    board(6) = Player.One
    Game.checkBoardAlmostWinningPosition(board) should be (4)
  }

  "Possible Wins" should "be 8 possible cell groups" in {
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

  "Display board" should "show an empty board" in {
    val board = Game.newBoard()
    Game.displayBoard(board) should be (" | | \n-----\n | | \n-----\n | | \n")
  }

  it should "show the board with Player One" in {
    val board = Game.newBoard()
    board(0) = Player.One
    Game.displayBoard(board) should be ("O| | \n-----\n | | \n-----\n | | \n")
  }

  it should "show the board with Player Two" in {
    val board = Game.newBoard()
    board(5) = Player.Two
    Game.displayBoard(board) should be (" | | \n-----\n | |X\n-----\n | | \n")
  }

  it should "show the board with a few moves from both players" in {
    val board = Game.newBoard()
    board(5) = Player.Two
    board(8) = Player.One
    board(2) = Player.Two
    Game.displayBoard(board) should be (" | |X\n-----\n | |X\n-----\n | |O\n")
  }

  "Display Winner" should "display a draw message if no winner exist" in {
    val board = Game.newBoard()
    Game.displayWinner(board) should be ("Game is a Draw!")
  }

  it should "display the winner message if a winner exists" in {
    val board = Game.newBoard()
    board(5) = Player.Two
    board(8) = Player.Two
    board(2) = Player.Two
    Game.displayWinner(board) should be ("Winner is Player Two (X)!")
  }

  "Display Result" should "show show the result" in {
    val board = Game.newBoard()
    board(0) = Player.Two
    board(1) = Player.Two
    board(2) = Player.Two
    Game.displayResult(board) should be ("\nX|X|X\n-----\n | | \n-----\n | | \n\nWinner is Player Two (X)!")
  }

}