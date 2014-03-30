package com.markdessain.game

import scala.util.Random


object TicTacToe{
  def newBoard() : Array[Player.Value] = { return new Array[Player.Value](9) }

  def isEndGame(board: Array[Player.Value]) : Boolean = {
    return board.forall(c => c != null) || TicTacToe.checkBoardWin(board) != null
  }

  def isLegalMove(move:Int) : Boolean = {
    return (move >= 0 && move <= 8)
  }

  def isSpaceFree(board: Array[Player.Value], move:Int) : Boolean = {
    return board(move) == null
  }

  def canMove(board: Array[Player.Value], position: Int) : Boolean = {
    return isLegalMove(position) && isSpaceFree(board, position)
  }

  def move(board: Array[Player.Value], player: Player.Value, position: Int) : Array[Player.Value] = {
    val newBoard = board.clone()
    if(canMove(board, position)) {
      newBoard(position) = player
    }
    return newBoard
  }

  def botMove(board: Array[Player.Value], player: Player.Value) : Array[Player.Value] = {

    val almostWinner = checkBoardForWinningMoves(board)
    var position = -1

    if(almostWinner != -1) {
      position = almostWinner
    } else {
      var availablePositions = Array.empty[Int]

      for(i <- 0 until board.length){
        if(board(i) == null) {
          availablePositions = availablePositions :+ i
        }
      }

      position = Random.shuffle(availablePositions.toList).head
    }
    return move(board, player, position)
  }

  def checkGroupWin(row: Array[Player.Value]) : Player.Value = {
    if(row.forall(cell => cell == row(0))){
      return row(0)
    } else {
      return null
    }
  }

  def checkBoardWin(board: Array[Player.Value]) : Player.Value = {
    for(x <- winGroups()) {
      val rowValues = for (i <- x) yield board(i)
      val rowWin = checkGroupWin(rowValues)
      if(rowWin != null) {
        return rowWin
      }
    }
    return null
  }

  def winningPosition(row: Array[Player.Value]) : Int = {
    val group = row.groupBy(x=>x)
    if(group.keys.size == 2 && row.count(c => c == null) == 1) {
      return row.indexWhere(c => c == null)
    } else {
      return -1
    }
  }

  def checkBoardForWinningMoves(board: Array[Player.Value]) : Int = {
    for(x <- winGroups()) {
      val rowValues = for (i <- x) yield board(i)
      val position = winningPosition(rowValues)
      if(position != -1) {
        return x(position)
      }
    }
    return -1
  }

  def winGroups() : Array[Array[Int]] = {
    return Array(
      Array(0, 1, 2),
      Array(3, 4, 5),
      Array(6, 7, 8),
      Array(0, 3, 6),
      Array(1, 4, 7),
      Array(2, 5, 8),
      Array(0, 4, 8),
      Array(2, 4, 6)
    )
  }

  def displayBoard(board: Array[Player.Value]) : String = {
    val a = for (x <- board) yield if(x == null) " " else x.id

    return "%s|%s|%s\n-----\n%s|%s|%s\n-----\n%s|%s|%s\n" format(
      a(0),
      a(1),
      a(2),
      a(3),
      a(4),
      a(5),
      a(6),
      a(7),
      a(8)
    )
  }

  def displayWinner(board: Array[Player.Value]) : String = {
    val winner = TicTacToe.checkBoardWin(board)
    if(winner == null) {
      return "Game is a Draw!"
    } else {
      return "Winner is Player %s!" format (winner)
    }
  }

  def displayResult(board: Array[Player.Value]) : String = {
    return "\n%s\n%s" format (displayBoard(board), displayWinner(board))
  }

}
