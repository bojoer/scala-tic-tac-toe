import java.io._

object Main {
  def main(args: Array[String]) {
    val br = getBufferedReader()
    val bw = getBufferedWriter()
    playGame(br, bw)
  }

  def playGame(br: BufferedReader, bw: BufferedWriter) = {
    var board = Game.newBoard()
    var player = Game.startingPlayer()

    while(!Game.isEndGame(board)){
      val input = playerInput(board, player, br, bw)
      val newBoard = Game.move(board, player, input.toInt)

      if(board.deep == newBoard.deep) {
        println("Invalid Move, please try again")
      } else {
        board = newBoard
        player = Game.nextPlayer(player)
      }
    }
    displayResult(board, bw)
  }

  def getBufferedReader() : BufferedReader = {
    val reader = new InputStreamReader(System.in)
    return new BufferedReader(reader)
  }

  def getBufferedWriter() : BufferedWriter = {
    val writer = new OutputStreamWriter(System.out)
    return new BufferedWriter(writer)
  }

  def playerInput(board: Array[Player.Item], player: Player.Item, br: BufferedReader, bw: BufferedWriter) : String = {
    bw.write("\n")
    bw.write("Current Board:")
    bw.write("\n")
    bw.write(Game.displayBoard(board))
    bw.write("\n")
    bw.write("Player %s> " format (player))
    bw.flush()
    return br.readLine()
  }

  def displayResult(board: Array[Player.Item], bw: BufferedWriter) {
    val result = Game.displayResult(board)
    bw.write(result)
    bw.flush()
  }
}