package com.markdessain.game

object Player extends Enumeration {
  val One = Value(0)
  val Two = Value(1)

  def find(i : Int) : Player.Value = i match {
    case 0 => Player.One
    case 1 => Player.Two
    case _ => null
  }

  def startingPlayer() : Player.Value = { return nextPlayer(null) }

  def nextPlayer(currentPlayer : Player.Value) : Player.Value = currentPlayer match {
    case Player.Two => Player.One
    case Player.One => Player.Two
    case _ => Player.One
  }
}
