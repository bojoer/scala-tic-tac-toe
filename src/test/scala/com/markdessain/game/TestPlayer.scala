package com.markdessain.game

import org.scalatest._

class TestPlayer extends FlatSpec with Matchers {

  "Find Player" should "be Player One with a 0" in {
    Player.find(0) should be (Player.One)
  }

  it should "be Player Two with a 1" in {
    Player.find(1) should be (Player.Two)
  }

  it should "be null with a something else" in {
    Player.find(2) should be (null)
  }

  "Starting Player" should "be Player One" in {
    Player.startingPlayer() should be (Player.One)
  }

  "Next Player" should "be Player One if there is no current player" in {
    Player.nextPlayer(null) should be (Player.One)
  }

  it should "be Player One if Player Two is the current player" in {
    Player.nextPlayer(Player.Two) should be (Player.One)
  }

  it should "be Player Two if Player One is the current player" in {
    Player.nextPlayer(Player.One) should be (Player.Two)
  }

  "Two players" should "exist" in {
    Player.One.toString() should be ("One")
    Player.Two.toString() should be ("Two")
    Player.One.id should be (0)
    Player.Two.id should be (1)
  }
}