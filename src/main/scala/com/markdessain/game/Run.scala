package com.markdessain.game

import java.io._

object Run {
  def main(args: Array[String]) {
    val br = getBufferedReader()
    val bw = getBufferedWriter()
    playGame(br, bw)
  }

  def playGame(br: BufferedReader, bw: BufferedWriter) = {
    var board = TicTacToe.newBoard()
    var player = TicTacToe.startingPlayer()

    while(!TicTacToe.isEndGame(board)){
      val input = playerInput(board, player, br, bw)
      val newBoard = TicTacToe.move(board, player, input.toInt)

      if(board.deep == newBoard.deep) {
        println("Invalid Move, please try again")
      } else {
        board = newBoard
        player = TicTacToe.nextPlayer(player)
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
    bw.write(TicTacToe.displayBoard(board))
    bw.write("\n")
    bw.write("Player %s> " format (player))
    bw.flush()
    return br.readLine()
  }

  def displayResult(board: Array[Player.Item], bw: BufferedWriter) {
    val result = TicTacToe.displayResult(board)
    bw.write(result)
    bw.flush()
  }
}