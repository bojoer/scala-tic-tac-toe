package com.markdessain.game

import org.scalatest._

class TestTicTacTow extends FlatSpec with Matchers {

  "A new com.markdessain.game" should "have nine cells" in {
    TicTacToe.newBoard().length should be (9)
  }

  "Starting Player" should "be Player One" in {
    TicTacToe.startingPlayer() should be (Player.One)
  }

  "Next Player" should "be Player One if there is no current player" in {
    TicTacToe.nextPlayer(null) should be (Player.One)
  }

  it should "be Player One if Player Two is the current player" in {
    TicTacToe.nextPlayer(Player.Two) should be (Player.One)
  }

  it should "be Player Two if Player One is the current player" in {
    TicTacToe.nextPlayer(Player.One) should be (Player.Two)
  }

  "Legal Move" should "allow 0" in {
    TicTacToe.isLegalMove(0) should be (true)
  }

  it should "allow 8" in {
    TicTacToe.isLegalMove(8) should be (true)
  }

  it should "not allow 9" in {
    TicTacToe.isLegalMove(9) should be (false)
  }

  it should "not allow -1" in {
    TicTacToe.isLegalMove(-1) should be (false)
  }

  "Two players" should "exist" in {
    Player.One.name should be ("O")
    Player.Two.name should be ("X")
  }

  "Free Space" should "be true in empty board" in {
    val board = new Array[Player.Item](3)
    TicTacToe.isSpaceFree(board, 0) should be (true)
  }

  it should "be false if space is filled" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    TicTacToe.isSpaceFree(board, 0) should be (false)
  }

  "End Game" should "be false in empty board" in {
    val board = TicTacToe.newBoard()
    TicTacToe.isEndGame(board) should be (false)
  }

  it should "be true in winning board" in {
    val board = TicTacToe.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One
    TicTacToe.isEndGame(board) should be (true)
  }

  it should "be true in full board" in {
    val board = new Array[Player.Item](1)
    board(0) = Player.One
    TicTacToe.isEndGame(board) should be (true)
  }

  "Move" should "add the player onto the board" in {
    val board = new Array[Player.Item](3)
    val player = Player.One
    val position = 0

    val expectedBoard = Array(Player.One, null, null)

    TicTacToe.move(board, player, position) should be (expectedBoard)
  }

  it should "add multiple players onto the board" in {
    val board = new Array[Player.Item](3)
    val playerOne = Player.One
    val positionOne = 0

    val newBoard = TicTacToe.move(board, playerOne, positionOne)
    val playerTwo = Player.Two
    val positionTwo = 1

    val expectedBoard = Array(Player.One, Player.Two, null)

    TicTacToe.move(newBoard, playerTwo, positionTwo) should be (expectedBoard)
  }

  "Bot Move" should "be in a random location" in {
    val board = TicTacToe.newBoard()
    val player = Player.One

    val newBoard = TicTacToe.botMove(board, player)

    newBoard.count(c => c == Player.One) should be (1)
    newBoard.count(c => c == Player.Two) should be (0)
  }

  it should "add multiple moves into random locations" in {
    val board = TicTacToe.newBoard()
    val player = Player.One

    val newBoardOne = TicTacToe.botMove(board, player)
    val newBoardTwo = TicTacToe.botMove(newBoardOne, player)

    newBoardTwo.count(c => c == Player.One) should be (2)
    newBoardTwo.count(c => c == Player.Two) should be (0)
  }

  it should "try to block an almost winning move" in {
    val board = TicTacToe.newBoard()
    board(0) = Player.Two
    board(1) = Player.Two
    val player = Player.One

    val newBoard = TicTacToe.botMove(board, player)

    newBoard(2) should be (Player.One)
  }

  it should "try and go for a win" in {
    val board = TicTacToe.newBoard()
    board(0) = Player.One
    board(3) = Player.One
    val player = Player.One

    val newBoard = TicTacToe.botMove(board, player)

    newBoard(6) should be (Player.One)
  }

  ignore should "try and go for a win even if it can block an opponent" in {
    // TODO: Currently it will try and block an opponent instead of winning.
    val board = TicTacToe.newBoard()
    board(0) = Player.Two
    board(1) = Player.Two
    board(6) = Player.One
    board(7) = Player.One
    val player = Player.One

    val newBoard = TicTacToe.botMove(board, player)

    newBoard(8) should be (Player.One)
  }

  "Check Row Win" should "not be a win if not all cells are filled" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    TicTacToe.checkRowWin(board) should be (null)
  }

  it should "not be a win if the cells are mixed" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.Two
    board(2) = Player.One

    TicTacToe.checkRowWin(board) should be (null)
  }

  it should "be a win if all cells are the same" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One

    TicTacToe.checkRowWin(board) should be (Player.One)
  }

  "Check Board Win" should "mean no winner if no groups are filled" in {
    val board = TicTacToe.newBoard()
    TicTacToe.checkBoardWin(board) should be (null)
  }

  it should "mean a winner if a row is filled" in {
    val board = TicTacToe.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    board(2) = Player.One
    TicTacToe.checkBoardWin(board) should be (Player.One)
  }

  it should "mean a winner if a column is filled" in {
    val board = TicTacToe.newBoard()
    board(1) = Player.One
    board(4) = Player.One
    board(7) = Player.One
    TicTacToe.checkBoardWin(board) should be (Player.One)
  }

  it should "mean a winner if a diagonal is filled" in {
    val board = TicTacToe.newBoard()
    board(2) = Player.One
    board(4) = Player.One
    board(6) = Player.One
    TicTacToe.checkBoardWin(board) should be (Player.One)
  }

  "Almost Winning Position" should "be -1 if only one cell is set" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    TicTacToe.almostWinningPosition(board) should be (-1)
  }

  it should "be -1 if multiple players are in the group" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(1) = Player.Two
    TicTacToe.almostWinningPosition(board) should be (-1)
  }

  it should "be the empty cell where a player already controls the two other cells" in {
    val board = new Array[Player.Item](3)
    board(0) = Player.One
    board(2) = Player.One
    TicTacToe.almostWinningPosition(board) should be (1)
  }

  "Check Board Almost Winning Positions" should "be -1 if no almost complete groups exist" in {
    val board = TicTacToe.newBoard()
    TicTacToe.checkBoardAlmostWinningPosition(board) should be (-1)
  }

  it should "be the missing position in the row if a row is almost complete" in {
    val board = TicTacToe.newBoard()
    board(0) = Player.One
    board(1) = Player.One
    TicTacToe.checkBoardAlmostWinningPosition(board) should be (2)
  }

  it should "be the missing position in the column if a column is almost complete" in {
    val board = TicTacToe.newBoard()
    board(4) = Player.One
    board(7) = Player.One
    TicTacToe.checkBoardAlmostWinningPosition(board) should be (1)
  }

  it should "be the missing position in the diagonal if a diagonal is almost complete" in {
    val board = TicTacToe.newBoard()
    board(2) = Player.One
    board(6) = Player.One
    TicTacToe.checkBoardAlmostWinningPosition(board) should be (4)
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
    TicTacToe.possibleWins().deep should be (wins.deep)
  }

  "Display board" should "show an empty board" in {
    val board = TicTacToe.newBoard()
    TicTacToe.displayBoard(board) should be (" | | \n-----\n | | \n-----\n | | \n")
  }

  it should "show the board with Player One" in {
    val board = TicTacToe.newBoard()
    board(0) = Player.One
    TicTacToe.displayBoard(board) should be ("O| | \n-----\n | | \n-----\n | | \n")
  }

  it should "show the board with Player Two" in {
    val board = TicTacToe.newBoard()
    board(5) = Player.Two
    TicTacToe.displayBoard(board) should be (" | | \n-----\n | |X\n-----\n | | \n")
  }

  it should "show the board with a few moves from both players" in {
    val board = TicTacToe.newBoard()
    board(5) = Player.Two
    board(8) = Player.One
    board(2) = Player.Two
    TicTacToe.displayBoard(board) should be (" | |X\n-----\n | |X\n-----\n | |O\n")
  }

  "Display Winner" should "display a draw message if no winner exist" in {
    val board = TicTacToe.newBoard()
    TicTacToe.displayWinner(board) should be ("Game is a Draw!")
  }

  it should "display the winner message if a winner exists" in {
    val board = TicTacToe.newBoard()
    board(5) = Player.Two
    board(8) = Player.Two
    board(2) = Player.Two
    TicTacToe.displayWinner(board) should be ("Winner is Player Two (X)!")
  }

  "Display Result" should "show show the result" in {
    val board = TicTacToe.newBoard()
    board(0) = Player.Two
    board(1) = Player.Two
    board(2) = Player.Two
    TicTacToe.displayResult(board) should be ("\nX|X|X\n-----\n | | \n-----\n | | \n\nWinner is Player Two (X)!")
  }

  "Board" should "convert as a string with no moves" in {
    val board = TicTacToe.newBoard()
    TicTacToe.boardToString(board) should be ("""["", "", "", "", "", "", "", "", ""]""")
  }

  it should "convert as a string with moves" in {
    val board = TicTacToe.newBoard()
    board(3) = Player.One
    board(8) = Player.Two
    TicTacToe.boardToString(board) should be ("""["", "", "", "O", "", "", "", "", "X"]""")
  }

  it should "be generated from a string with no moves" in {
    val boardString = """["", "", "", "", "", "", "", "", ""]"""
    val board = TicTacToe.newBoard()
    TicTacToe.stringToBoard(boardString) should be (board)
  }

  it should "be generated from a string with moves" in {
    val boardString = """["", "", "", "O", "", "", "", "", "X"]"""
    val board = TicTacToe.newBoard()
    board(3) = Player.One
    board(8) = Player.Two
    TicTacToe.stringToBoard(boardString) should be (board)
  }


}