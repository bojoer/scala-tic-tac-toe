package com.markdessain.actor

import akka.actor.Actor
import spray.routing._
import com.markdessain.game._
import com.markdessain.game.MoveJsonSupport._

trait Game extends HttpService {

  val newRoute = path("new") {
    def newBoard(s: String) : MoveOutput = {
      val board = TicTacToe.newBoard()
      val player = TicTacToe.startingPlayer()
      return MoveOutput(board, player, null)
    }
    get {
      handleWith(newBoard)
    }
  }

  val moveRoute = path("move") {
    def playMove(move: MoveInput) : MoveOutput = {
      val player = move.current_player
      if(TicTacToe.canMove(move.board, move.position)) {
        val newBoard = TicTacToe.move(move.board, player, move.position)
        val nextPlayer = TicTacToe.nextPlayer(player)
        val winner = TicTacToe.checkBoardWin(newBoard)
        return MoveOutput(newBoard, nextPlayer, winner)
      }else{
        return MoveOutput(move.board, player, null)
      }
    }
    get {
      handleWith(playMove)
    }
  }

  val routes = newRoute ~ moveRoute
}


class GameActor extends Actor with Game {
  def actorRefFactory = context
  def receive = runRoute(routes)
}