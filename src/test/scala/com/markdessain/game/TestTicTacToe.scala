package com.markdessain.game

import org.scalatest._

class TestTicTacTow extends FlatSpec with Matchers {

  "A new com.markdessain.game" should "have nine cells" in {
    TicTacToe.newBoard().length should be (9)
  }

  "Starting Player" should "be Player One" in {
    TicTacToe.startingPlayer() should be (PlayerEnum.One)
  }

  "Next Player" should "be Player One if there is no current player" in {
    TicTacToe.nextPlayer(null) should be (PlayerEnum.One)
  }

  it should "be Player One if Player Two is the current player" in {
    TicTacToe.nextPlayer(PlayerEnum.Two) should be (PlayerEnum.One)
  }

  it should "be Player Two if Player One is the current player" in {
    TicTacToe.nextPlayer(PlayerEnum.One) should be (PlayerEnum.Two)
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
    PlayerEnum.One.toString() should be ("O")
    PlayerEnum.Two.toString() should be ("X")
    PlayerEnum.One.id should be (0)
    PlayerEnum.Two.id should be (1)
  }

  "Free Space" should "be true in empty board" in {
    val board = new Array[PlayerEnum.Value](3)
    TicTacToe.isSpaceFree(board, 0) should be (true)
  }

  it should "be false if space is filled" in {
    val board = new Array[PlayerEnum.Value](3)
    board(0) = PlayerEnum.One
    TicTacToe.isSpaceFree(board, 0) should be (false)
  }

  "End Game" should "be false in empty board" in {
    val board = TicTacToe.newBoard()
    TicTacToe.isEndGame(board) should be (false)
  }

  it should "be true in winning board" in {
    val board = TicTacToe.newBoard()
    board(0) = PlayerEnum.One
    board(1) = PlayerEnum.One
    board(2) = PlayerEnum.One
    TicTacToe.isEndGame(board) should be (true)
  }

  it should "be true in full board" in {
    val board = new Array[PlayerEnum.Value](1)
    board(0) = PlayerEnum.One
    TicTacToe.isEndGame(board) should be (true)
  }

  "Move" should "add the player onto the board" in {
    val board = new Array[PlayerEnum.Value](3)
    val player = PlayerEnum.One
    val position = 0

    val expectedBoard = Array(PlayerEnum.One, null, null)

    TicTacToe.move(board, player, position) should be (expectedBoard)
  }

  it should "add multiple players onto the board" in {
    val board = new Array[PlayerEnum.Value](3)
    val playerOne = PlayerEnum.One
    val positionOne = 0

    val newBoard = TicTacToe.move(board, playerOne, positionOne)
    val playerTwo = PlayerEnum.Two
    val positionTwo = 1

    val expectedBoard = Array(PlayerEnum.One, PlayerEnum.Two, null)

    TicTacToe.move(newBoard, playerTwo, positionTwo) should be (expectedBoard)
  }

  "Can Move" should "be True if legal spot" in {
    val board = new Array[PlayerEnum.Value](3)
    TicTacToe.canMove(board, 2) should be (true)
  }

  it should "be False it someone is already in the space" in {
    val board = new Array[PlayerEnum.Value](3)
    board(2) = PlayerEnum.One
    TicTacToe.canMove(board, 2) should be (false)
  }

  "Bot Move" should "be in a random location" in {
    val board = TicTacToe.newBoard()
    val player = PlayerEnum.One

    val newBoard = TicTacToe.botMove(board, player)

    newBoard.count(c => c == PlayerEnum.One) should be (1)
    newBoard.count(c => c == PlayerEnum.Two) should be (0)
  }

  it should "add multiple moves into random locations" in {
    val board = TicTacToe.newBoard()
    val player = PlayerEnum.One

    val newBoardOne = TicTacToe.botMove(board, player)
    val newBoardTwo = TicTacToe.botMove(newBoardOne, player)

    newBoardTwo.count(c => c == PlayerEnum.One) should be (2)
    newBoardTwo.count(c => c == PlayerEnum.Two) should be (0)
  }

  it should "try to block an almost winning move" in {
    val board = TicTacToe.newBoard()
    board(0) = PlayerEnum.Two
    board(1) = PlayerEnum.Two
    val player = PlayerEnum.One

    val newBoard = TicTacToe.botMove(board, player)

    newBoard(2) should be (PlayerEnum.One)
  }

  it should "try and go for a win" in {
    val board = TicTacToe.newBoard()
    board(0) = PlayerEnum.One
    board(3) = PlayerEnum.One
    val player = PlayerEnum.One

    val newBoard = TicTacToe.botMove(board, player)

    newBoard(6) should be (PlayerEnum.One)
  }

  ignore should "try and go for a win even if it can block an opponent" in {
    // TODO: Currently it will try and block an opponent instead of winning.
    val board = TicTacToe.newBoard()
    board(0) = PlayerEnum.Two
    board(1) = PlayerEnum.Two
    board(6) = PlayerEnum.One
    board(7) = PlayerEnum.One
    val player = PlayerEnum.One

    val newBoard = TicTacToe.botMove(board, player)

    newBoard(8) should be (PlayerEnum.One)
  }

  "Check Row Win" should "not be a win if not all cells are filled" in {
    val board = new Array[PlayerEnum.Value](3)
    board(0) = PlayerEnum.One
    TicTacToe.checkRowWin(board) should be (null)
  }

  it should "not be a win if the cells are mixed" in {
    val board = new Array[PlayerEnum.Value](3)
    board(0) = PlayerEnum.One
    board(1) = PlayerEnum.Two
    board(2) = PlayerEnum.One

    TicTacToe.checkRowWin(board) should be (null)
  }

  it should "be a win if all cells are the same" in {
    val board = new Array[PlayerEnum.Value](3)
    board(0) = PlayerEnum.One
    board(1) = PlayerEnum.One
    board(2) = PlayerEnum.One

    TicTacToe.checkRowWin(board) should be (PlayerEnum.One)
  }

  "Check Board Win" should "mean no winner if no groups are filled" in {
    val board = TicTacToe.newBoard()
    TicTacToe.checkBoardWin(board) should be (null)
  }

  it should "mean a winner if a row is filled" in {
    val board = TicTacToe.newBoard()
    board(0) = PlayerEnum.One
    board(1) = PlayerEnum.One
    board(2) = PlayerEnum.One
    TicTacToe.checkBoardWin(board) should be (PlayerEnum.One)
  }

  it should "mean a winner if a column is filled" in {
    val board = TicTacToe.newBoard()
    board(1) = PlayerEnum.One
    board(4) = PlayerEnum.One
    board(7) = PlayerEnum.One
    TicTacToe.checkBoardWin(board) should be (PlayerEnum.One)
  }

  it should "mean a winner if a diagonal is filled" in {
    val board = TicTacToe.newBoard()
    board(2) = PlayerEnum.One
    board(4) = PlayerEnum.One
    board(6) = PlayerEnum.One
    TicTacToe.checkBoardWin(board) should be (PlayerEnum.One)
  }


  "Check Board Winner Name" should "mean empty string if no winner" in {
    val board = TicTacToe.newBoard()
    TicTacToe.checkBoardWinnerName(board) should be ("")
  }

  it should "mean the players name if a winner" in {
    val board = TicTacToe.newBoard()
    board(0) = PlayerEnum.One
    board(1) = PlayerEnum.One
    board(2) = PlayerEnum.One
    TicTacToe.checkBoardWinnerName(board) should be ("O")
  }

  "Almost Winning Position" should "be -1 if only one cell is set" in {
    val board = new Array[PlayerEnum.Value](3)
    board(0) = PlayerEnum.One
    TicTacToe.almostWinningPosition(board) should be (-1)
  }

  it should "be -1 if multiple players are in the group" in {
    val board = new Array[PlayerEnum.Value](3)
    board(0) = PlayerEnum.One
    board(1) = PlayerEnum.Two
    TicTacToe.almostWinningPosition(board) should be (-1)
  }

  it should "be the empty cell where a player already controls the two other cells" in {
    val board = new Array[PlayerEnum.Value](3)
    board(0) = PlayerEnum.One
    board(2) = PlayerEnum.One
    TicTacToe.almostWinningPosition(board) should be (1)
  }

  "Check Board Almost Winning Positions" should "be -1 if no almost complete groups exist" in {
    val board = TicTacToe.newBoard()
    TicTacToe.checkBoardAlmostWinningPosition(board) should be (-1)
  }

  it should "be the missing position in the row if a row is almost complete" in {
    val board = TicTacToe.newBoard()
    board(0) = PlayerEnum.One
    board(1) = PlayerEnum.One
    TicTacToe.checkBoardAlmostWinningPosition(board) should be (2)
  }

  it should "be the missing position in the column if a column is almost complete" in {
    val board = TicTacToe.newBoard()
    board(4) = PlayerEnum.One
    board(7) = PlayerEnum.One
    TicTacToe.checkBoardAlmostWinningPosition(board) should be (1)
  }

  it should "be the missing position in the diagonal if a diagonal is almost complete" in {
    val board = TicTacToe.newBoard()
    board(2) = PlayerEnum.One
    board(6) = PlayerEnum.One
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
    board(0) = PlayerEnum.One
    TicTacToe.displayBoard(board) should be ("O| | \n-----\n | | \n-----\n | | \n")
  }

  it should "show the board with Player Two" in {
    val board = TicTacToe.newBoard()
    board(5) = PlayerEnum.Two
    TicTacToe.displayBoard(board) should be (" | | \n-----\n | |X\n-----\n | | \n")
  }

  it should "show the board with a few moves from both players" in {
    val board = TicTacToe.newBoard()
    board(5) = PlayerEnum.Two
    board(8) = PlayerEnum.One
    board(2) = PlayerEnum.Two
    TicTacToe.displayBoard(board) should be (" | |X\n-----\n | |X\n-----\n | |O\n")
  }

  "Display Winner" should "display a draw message if no winner exist" in {
    val board = TicTacToe.newBoard()
    TicTacToe.displayWinner(board) should be ("Game is a Draw!")
  }

  it should "display the winner message if a winner exists" in {
    val board = TicTacToe.newBoard()
    board(5) = PlayerEnum.Two
    board(8) = PlayerEnum.Two
    board(2) = PlayerEnum.Two
    TicTacToe.displayWinner(board) should be ("Winner is Player X!")
  }

  "Display Result" should "show show the result" in {
    val board = TicTacToe.newBoard()
    board(0) = PlayerEnum.Two
    board(1) = PlayerEnum.Two
    board(2) = PlayerEnum.Two
    TicTacToe.displayResult(board) should be ("\nX|X|X\n-----\n | | \n-----\n | | \n\nWinner is Player X!")
  }

  "Board" should "convert as a string with no moves" in {
    val board = TicTacToe.newBoard()
    TicTacToe.boardToString(board) should be ("""[-1, -1, -1, -1, -1, -1, -1, -1, -1]""")
  }

  it should "convert as a string with moves" in {
    val board = TicTacToe.newBoard()
    board(3) = PlayerEnum.One
    board(8) = PlayerEnum.Two
    TicTacToe.boardToString(board) should be ("""[-1, -1, -1, 0, -1, -1, -1, -1, 1]""")
  }

  it should "be generated from a string with no moves" in {
    val boardString = """[-1, -1, -1, -1, -1, -1, -1, -1, -1]"""
    val board = TicTacToe.newBoard()
    TicTacToe.stringToBoard(boardString) should be (board)
  }

  it should "be generated from a string with moves" in {
    val boardString = """[-1, -1, -1, 0, -1, -1, -1, -1, 1]"""
    val board = TicTacToe.newBoard()
    board(3) = PlayerEnum.One
    board(8) = PlayerEnum.Two
    TicTacToe.stringToBoard(boardString) should be (board)
  }


}