object Player extends Enumeration {
  val One = Value(1)
  val Two = Value(2)
}

object Game{
  def newBoard() : Array[Int] = { return new Array[Int](9) }

  def isLegalMove(move:Int) : Boolean = {
    return (move >= 0 && move <= 8)
  }

  def isSpaceFree(board: Array[Int], move:Int) : Boolean = {
    return board(move) == 0
  }

  def move(board: Array[Int], player: Player.Value, position: Int) : Array[Int] = {
    if(isLegalMove(position) && isSpaceFree(board, position)) {
      board(position) = player.id
    }
    return board
  }
}
