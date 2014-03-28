package com.markdessain.game

import scala.util.Random
import spray.json._
import DefaultJsonProtocol._

object Player extends Enumeration {
  val One = Item("O")
  val Two = Item("X")

  final case class Item(name: String) extends Val {}

  def find(name : String) : Player.Item = {
    if(name == "O") {
      return Player.One
    }
    if(name == "X") {
      return Player.Two
    }

    return null
  }

  implicit object PlayerJsonFormat extends RootJsonFormat[Player.Item] {
    def write(player: Player.Item) : JsString = {
      if(player == null) {
        return JsString("")
      } else {
        return JsString(player.name)
      }
    }

    def read(value: JsValue) : Player.Item = {
      return Player.find(value.toString()(1).toString())
    }
  }
}


object TicTacToe{
  def newBoard() : Array[Player.Item] = { return new Array[Player.Item](9) }

  def startingPlayer() : Player.Item = { return nextPlayer(null) }

  def nextPlayer(currentPlayer : Player.Item) : Player.Item = {
    if(currentPlayer == Player.One) {
      return Player.Two
    } else {
      return Player.One
    }
  }

  def isEndGame(board: Array[Player.Item]) : Boolean = {
    return board.forall(c => c != null) || TicTacToe.checkBoardWin(board) != null
  }

  def isLegalMove(move:Int) : Boolean = {
    return (move >= 0 && move <= 8)
  }

  def isSpaceFree(board: Array[Player.Item], move:Int) : Boolean = {
    return board(move) == null
  }

  def move(board: Array[Player.Item], player: Player.Item, position: Int) : Array[Player.Item] = {
    val newBoard = board.clone()
    if(isLegalMove(position) && isSpaceFree(board, position)) {
      newBoard(position) = player
    }
    return newBoard
  }

  def botMove(board: Array[Player.Item], player: Player.Item) : Array[Player.Item] = {

    val almostWinner = checkBoardAlmostWinningPosition(board)
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

  def checkRowWin(row: Array[Player.Item]) : Player.Item = {
    if(row.forall(cell => cell == row(0))){
      return row(0)
    } else {
      return null
    }
  }

  def checkBoardWin(board: Array[Player.Item]) : Player.Item = {
    for(x <- possibleWins()) {
      val rowValues = for (i <- x) yield board(i)
      val rowWin = checkRowWin(rowValues)
      if(rowWin != null) {
        return rowWin
      }
    }
    return null
  }

  def almostWinningPosition(row: Array[Player.Item]) : Int = {
    val group = row.groupBy(x=>x)
    if(group.keys.size == 2 && row.count(c => c == null) == 1) {
      return row.indexWhere(c => c == null)
    } else {
      return -1
    }
  }

  def checkBoardAlmostWinningPosition(board: Array[Player.Item]) : Int = {
    for(x <- possibleWins()) {
      val rowValues = for (i <- x) yield board(i)
      val position = almostWinningPosition(rowValues)
      if(position != -1) {
        return x(position)
      }
    }
    return -1
  }

  def possibleWins() : Array[Array[Int]] = {
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

  def displayBoard(board: Array[Player.Item]) : String = {
    val a = for (x <- board) yield if(x == null) " " else x.name

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

  def displayWinner(board: Array[Player.Item]) : String = {
    val winner = TicTacToe.checkBoardWin(board)
    if(winner == null) {
      return "Game is a Draw!"
    } else {
      return "Winner is Player %s (%s)!" format (winner, winner.name)
    }
  }

  def displayResult(board: Array[Player.Item]) : String = {
    return "\n%s\n%s" format (displayBoard(board), displayWinner(board))
  }

  def boardToString(board: Array[Player.Item]) : String = {
    return board.toJson.prettyPrint
  }

  def stringToBoard(boardString: String) : Array[Player.Item] = {
    val jsonAst = JsonParser(boardString)

    return jsonAst.convertTo[Array[Player.Item]]
  }
}
