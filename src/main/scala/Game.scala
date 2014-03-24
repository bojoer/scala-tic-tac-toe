object Player extends Enumeration {
  val One = Item("O")
  val Two = Item("X")

  final case class Item(key: String) extends Val {}
}

object Game{
  def newBoard() : Array[Player.Item] = { return new Array[Player.Item](9) }

  def startingPlayer() : Player.Item = { return nextPlayer(null) }

  def nextPlayer(currentPlayer : Player.Item) : Player.Item = {
    if(currentPlayer == Player.One) {
      return Player.Two
    } else {
      return Player.One
    }
  }

  def isEndGame(board: Array[Player.Item]) : Boolean = {
    return board.forall(c => c != null) || Game.checkBoardWin(board) != null
  }

  def isLegalMove(move:Int) : Boolean = {
    return (move >= 0 && move <= 8)
  }

  def isSpaceFree(board: Array[Player.Item], move:Int) : Boolean = {
    return board(move) == null
  }

  def move(board: Array[Player.Item], player: Player.Item, position: Int) : Array[Player.Item] = {
    val newBoard = board.clone()
    if(isLegalMove(position) && isSpaceFree(board, position)) {
      newBoard(position) = player
    }
    return newBoard
  }

  def checkRowWin(row: Array[Player.Item]) : Player.Item = {
    if(row.forall(cell => cell == row(0))){
      return row(0)
    } else {
      return null
    }
  }
  
  def checkBoardWin(board: Array[Player.Item]) : Player.Item = {
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

  def displayBoard(board: Array[Player.Item]) : String = {
    val a = for (x <- board) yield if(x == null) " " else x.key

    return "%s|%s|%s\n-----\n%s|%s|%s\n-----\n%s|%s|%s\n" format(
      a(0),
      a(1),
      a(2),
      a(3),
      a(4),
      a(5),
      a(6),
      a(7),
      a(8)
    )
  }
}
