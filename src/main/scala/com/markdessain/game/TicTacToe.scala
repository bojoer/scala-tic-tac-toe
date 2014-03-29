package com.markdessain.game

import scala.util.Random
import spray.json._
import spray.json.JsonParser
import spray.json.DefaultJsonProtocol._
import spray.httpx.SprayJsonSupport


object PlayerEnum extends Enumeration {
  val One = Value("O")
  val Two = Value("X")

  final case class Player(name: String) extends Val {}

  def find(i : Int) : PlayerEnum.Value = {
    if(i == 0) {
      return PlayerEnum.One
    }
    if(i == 1) {
      return PlayerEnum.Two
    }

    return null
  }

  implicit object PlayerJsonFormat extends RootJsonFormat[PlayerEnum.Value] {
    def write(player: PlayerEnum.Value) : JsNumber = {
      if(player == null) {
        return JsNumber(-1)
      } else {
        return JsNumber(player.id)
      }
    }

    def read(value: JsValue) : PlayerEnum.Value = {
      return PlayerEnum.find(value.convertTo[Int])
    }
  }
}


case class MoveInput(board: Array[PlayerEnum.Value], current_player: PlayerEnum.Value, position: Int)
case class MoveOutput(board: Array[PlayerEnum.Value], next_player: PlayerEnum.Value, winner: PlayerEnum.Value)


object MoveJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
//  implicit val PlayerFormat = jsonFormat1(Player.Item)
  implicit val MoveInputFormat = jsonFormat3(MoveInput)
  implicit val MoveOutputFormat = jsonFormat3(MoveOutput)
}




object TicTacToe{
  def newBoard() : Array[PlayerEnum.Value] = { return new Array[PlayerEnum.Value](9) }

  def startingPlayer() : PlayerEnum.Value = { return nextPlayer(null) }

  def nextPlayer(currentPlayer : PlayerEnum.Value) : PlayerEnum.Value = {
    if(currentPlayer == PlayerEnum.One) {
      return PlayerEnum.Two
    } else {
      return PlayerEnum.One
    }
  }

  def isEndGame(board: Array[PlayerEnum.Value]) : Boolean = {
    return board.forall(c => c != null) || TicTacToe.checkBoardWin(board) != null
  }

  def isLegalMove(move:Int) : Boolean = {
    return (move >= 0 && move <= 8)
  }

  def isSpaceFree(board: Array[PlayerEnum.Value], move:Int) : Boolean = {
    return board(move) == null
  }

  def canMove(board: Array[PlayerEnum.Value], position: Int) : Boolean = {
    return isLegalMove(position) && isSpaceFree(board, position)
  }

  def move(board: Array[PlayerEnum.Value], player: PlayerEnum.Value, position: Int) : Array[PlayerEnum.Value] = {
    val newBoard = board.clone()
    if(canMove(board, position)) {
      newBoard(position) = player
    }
    return newBoard
  }

  def botMove(board: Array[PlayerEnum.Value], player: PlayerEnum.Value) : Array[PlayerEnum.Value] = {

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

  def checkRowWin(row: Array[PlayerEnum.Value]) : PlayerEnum.Value = {
    if(row.forall(cell => cell == row(0))){
      return row(0)
    } else {
      return null
    }
  }

  def checkBoardWin(board: Array[PlayerEnum.Value]) : PlayerEnum.Value = {
    for(x <- possibleWins()) {
      val rowValues = for (i <- x) yield board(i)
      val rowWin = checkRowWin(rowValues)
      if(rowWin != null) {
        return rowWin
      }
    }
    return null
  }

  def checkBoardWinnerName(board: Array[PlayerEnum.Value]) : String = {
    val winner = TicTacToe.checkBoardWin(board)

    if(winner == null) {
      return ""
    }else{
      return winner.toString()
    }
  }

  def almostWinningPosition(row: Array[PlayerEnum.Value]) : Int = {
    val group = row.groupBy(x=>x)
    if(group.keys.size == 2 && row.count(c => c == null) == 1) {
      return row.indexWhere(c => c == null)
    } else {
      return -1
    }
  }

  def checkBoardAlmostWinningPosition(board: Array[PlayerEnum.Value]) : Int = {
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

  def displayBoard(board: Array[PlayerEnum.Value]) : String = {
    val a = for (x <- board) yield if(x == null) " " else x

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

  def displayWinner(board: Array[PlayerEnum.Value]) : String = {
    val winner = TicTacToe.checkBoardWin(board)
    if(winner == null) {
      return "Game is a Draw!"
    } else {
      return "Winner is Player %s!" format (winner)
    }
  }

  def displayResult(board: Array[PlayerEnum.Value]) : String = {
    return "\n%s\n%s" format (displayBoard(board), displayWinner(board))
  }

  def boardToString(board: Array[PlayerEnum.Value]) : String = {
    return board.toJson.prettyPrint
  }

  def stringToBoard(boardString: String) : Array[PlayerEnum.Value] = {
    val jsonAst = JsonParser(boardString)

    return jsonAst.convertTo[Array[PlayerEnum.Value]]
  }
}
