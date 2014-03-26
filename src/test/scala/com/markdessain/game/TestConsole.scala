package com.markdessain.game

import com.markdessain.game.Console
import java.io.Console
import org.scalatest._

import java.io._

class TestConsole extends FlatSpec with Matchers {

  "A winning game by Player One" should "end in a win" in {
    // O|O|O
    // -----
    // X|X|
    // -----
    //  | |

    val moves = Array(0, 3, 1, 4, 2)
    testGame(moves, "Winner is Player One (O)!")
  }

  "A winning game by Player Two" should "end in a win" in {
    // O|O|
    // -----
    // X|X|X
    // -----
    // O| |

    val moves = Array(0, 3, 1, 4, 6, 5)
    testGame(moves, "Winner is Player Two (X)!")
  }


  "A drawing game" should "end in a draw" in {
    // O|X|O
    // -----
    // X|O|O
    // -----
    // X|O|X

    val moves = Array(0, 1, 4, 8, 5, 3, 2, 6, 7)
    testGame(moves, "Game is a Draw!")
  }

  def testGame(moves: Array[Int], result: String) {

    val input = moves.mkString("\n")

    val reader = new StringReader(input)
    val br = new BufferedReader(reader)

    val writer = new StringWriter()
    val bw = new BufferedWriter(writer)

    Console.playGame(br, bw)

    val winningMessage = writer.toString().reverse.take(result.length).reverse
    winningMessage should be (result)
  }
}

