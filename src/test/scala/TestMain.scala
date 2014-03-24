
import org.scalatest._

import java.io._

class TestMain extends FlatSpec with Matchers{

  "A winning game by PlayerOne" should "end in a win" in {
    // O|O|O
    // -----
    // X|X|
    // -----
    //  | |

    testGame("0\n3\n1\n4\n2\n5", "Winner is Player One (O)!")
  }

  "A winning game by PlayerTwo" should "end in a win" in {
    // O|O|
    // -----
    // X|X|X
    // -----
    // O| |

    testGame("0\n3\n1\n4\n6\n5",  "Winner is Player Two (X)!")
  }


  "A drawing game" should "end in a draw" in {
    // O|X|O
    // -----
    // X|O|X
    // -----
    // O|O|X

    testGame("0\n1\n4\n8\n5\n3\n2\n6\n7", "Game is a Draw!")
  }

  def testGame(input: String, result: String) {

    val reader = new StringReader(input)
    val br = new BufferedReader(reader)

    val writer = new StringWriter()
    val bw = new BufferedWriter(writer)

    Main.playGame(br, bw)

    val winningMessage = writer.toString().reverse.take(result.length).reverse
    winningMessage should be (result)

  }
}

