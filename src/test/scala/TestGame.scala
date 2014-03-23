
import org.scalatest._

class TestGame extends FlatSpec with Matchers {
  "A new game" should "have nine squares" in {
    Game.newBoard().length should be (9)
  }

  "0" should "be an allowed move" in {
    Game.isLegalMove(0) should be (true)
  }

  "8" should "be an allowed move" in {
    Game.isLegalMove(8) should be (true)
  }

  "9" should "not be an allowed move" in {
    Game.isLegalMove(9) should be (false)
  }

  "-1" should "not be an allowed move" in {
    Game.isLegalMove(-1) should be (false)
  }

  "Two players" should "exist" in {
    Player.One.id should be (1)
    Player.Two.id should be (2)
  }

  "New board" should "have all free spaces" in {
    val board = Array(0,0,0)
    Game.isSpaceFree(board, 0) should be (true)
  }

  "Existing board" should "not have all free spaces" in {
    val board = Array(1,0,0)
    Game.isSpaceFree(board, 0) should be (false)
  }

  "Move" should "add the player onto the board" in {
    val board = Array(0,0,0)
    val player = Player.One
    val position = 0

    val expectedBoard = Array(1,0,0)

    Game.move(board, player, position) should be (expectedBoard)
  }

  "Two moves" should "add the player onto the board" in {
    val board = Array(0,0,0)
    val playerOne = Player.One
    val positionOne = 0

    val newBoard = Game.move(board, playerOne, positionOne)
    val playerTwo = Player.Two
    val positionTwo = 1

    val expectedBoard = Array(1,2,0)

    Game.move(newBoard, playerTwo, positionTwo) should be (expectedBoard)
  }

}