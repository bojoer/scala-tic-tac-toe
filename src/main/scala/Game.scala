object Player extends Enumeration {
  val One = Value(1)
  val Two = Value(2)
}

object Game{
  def newBoard() : Array[Player.Value] = { return new Array[Player.Value](9) }

  def isLegalMove(move:Int) : Boolean = {
    return (move >= 0 && move <= 8)
  }

  def isSpaceFree(board: Array[Player.Value], move:Int) : Boolean = {
    return board(move) == null
  }

  def move(board: Array[Player.Value], player: Player.Value, position: Int) : Array[Player.Value] = {
    if(isLegalMove(position) && isSpaceFree(board, position)) {
      board(position) = player
    }
    return board
  }

  def checkRowWin(row: Array[Player.Value]) : Player.Value = {
    if(row.forall(cell => cell == row(0))){
      return row(0)
    } else {
      return null
    }
  }
  
  def checkBoardWin(board: Array[Player.Value]) : Player.Value = {
    for(x <- possibleWins()) {
      val rowValues = for (i <- x) yield board(i)
      val rowWin = checkRowWin(rowValues)
      if(rowWin != null) {
        return rowWin
      }
    }
    return null
  }

  def possibleWins() : Array[Array[Int]] = {
    return Array(
      Array(0, 1, 2),
      Array(3, 4, 5),
      Array(6, 7, 8),
      Array(0, 3, 6),
      Array(1, 4, 7),
      Array(2, 5, 8),
      Array(0, 4, 8),
      Array(2, 4, 6)
    )
  }
}
