package com.markdessain.actor

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import com.markdessain.game.{Player, TicTacToe}
import spray.json.{DefaultJsonProtocol, JsObject, JsonParser}
import spray.httpx.SprayJsonSupport

case class Board(cells: Array[Player.Item])
case class MoveInput(board: Array[Player.Item], current_player: String, position: Int)
case class MoveOutput(board: Array[Player.Item], next_player: String)

object MoveJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val BoardFormat = jsonFormat1(Board)
  implicit val MoveInputFormat = jsonFormat3(MoveInput)
  implicit val MoveOutputFormat = jsonFormat2(MoveOutput)
}

import MoveJsonSupport._


trait Game extends HttpService {

  val newRoute = path("new") {
    get {
      val board = TicTacToe.newBoard()
      respondWithMediaType(`text/html`) {
        complete { TicTacToe.boardToString(board) }
      }
    }
  }

  val moveRoute = path("move") {
    def playMove(move: MoveInput) : MoveOutput = {
      println(move)
      val player = Player.find(move.current_player)
      val newBoard = TicTacToe.move(move.board, player, move.position)
      val nextPlayer = TicTacToe.nextPlayer(player)

      println(newBoard)
      return MoveOutput(newBoard, nextPlayer.name)
    }

    get {
      handleWith(playMove)
    }
  }

  val routes = newRoute ~ moveRoute
}

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class GameActor extends Actor with Game {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(routes)
}