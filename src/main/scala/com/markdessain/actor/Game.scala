package com.markdessain.actor

import akka.actor.Actor
import spray.routing._
import com.markdessain.game._
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport

object MoveJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val BoardFormat = jsonFormat1(Board)
  implicit val MoveInputFormat = jsonFormat3(MoveInput)
  implicit val MoveOutputFormat = jsonFormat3(MoveOutput)
}

import MoveJsonSupport._


trait Game extends HttpService {

  val newRoute = path("new") {
    def newBoard(s: String) : MoveOutput = {
      val board = TicTacToe.newBoard()
      val player = TicTacToe.startingPlayer()
      return MoveOutput(board, player.name, "")
    }
    get {
      handleWith(newBoard)
    }
  }

  val moveRoute = path("move") {
    def playMove(move: MoveInput) : MoveOutput = {
      if(TicTacToe.canMove(move.board, move.position)) {
        val player = Player.find(move.current_player)
        val newBoard = TicTacToe.move(move.board, player, move.position)
        val nextPlayer = TicTacToe.nextPlayer(player)
        val winner = TicTacToe.checkBoardWinnerName(newBoard)
        return MoveOutput(newBoard, nextPlayer.name, winner)
      }else{
        return MoveOutput(move.board, move.current_player, "")
      }
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