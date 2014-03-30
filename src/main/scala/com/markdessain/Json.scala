package com.markdessain

import spray.json._
import spray.httpx.SprayJsonSupport
import com.markdessain.game.Player

case class MoveInput(board: Array[Player.Value], current_player: Player.Value, position: Int)
case class MoveOutput(board: Array[Player.Value], next_player: Player.Value, winner: Player.Value)


object MoveJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {

  implicit object PlayerJsonFormat extends RootJsonFormat[Player.Value] {
    def write(player: Player.Value) : JsNumber = {
      if(player == null) {
        return JsNumber(-1)
      } else {
        return JsNumber(player.id)
      }
    }

    def read(value: JsValue) : Player.Value = {
      return Player.find(value.convertTo[Int])
    }
  }

  implicit val MoveInputFormat = jsonFormat3(MoveInput)
  implicit val MoveOutputFormat = jsonFormat3(MoveOutput)
}