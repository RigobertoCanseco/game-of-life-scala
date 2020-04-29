package com.rigobertocanseco.gameOfLife

import scala.util.Random

case class Cell(alive: Boolean)
case class Neighbors(cells: Array[Cell])

case class Grid(cells: Array[Array[Cell]]) {
  def intToGrid(array: Array[Array[Int]], rowMax: Int, colMax: Int): Grid = {
    Grid((for (row <- 0 until rowMax; col <- 0 until colMax) yield
      Cell(if (array(row)(col) == 1) true else false)).toArray.grouped(rowMax).toArray)
  }

  def getGridRandom(rowMax: Int, colMax: Int): Grid =
    Grid((for (_ <- 0 until rowMax; _ <- 0 until colMax) yield Cell(Random.nextBoolean()))
      .toArray.grouped(rowMax).toArray)

}


class GameOfLife(gridInitial: Grid, rowMax: Int, colMax: Int) {

  def nextEvolution(): Grid = {
    Grid((for (row <- 0 until rowMax; col <- 0 until colMax) yield
      Cell(alive(row, col, gridInitial.cells(row)(col).alive))).toArray.grouped(rowMax).toArray)
  }

  private def alive(row: Int, col: Int, isAlive: Boolean): Boolean = {
    var array: Array[Cell] = Array[Cell]()

    for(i <- (if(row == 0) 0 else row - 1) to (if(row == rowMax - 1) row else row + 1);
        j <- (if(col == 0) 0 else col - 1) to (if(col == colMax - 1) col else col + 1)) {
      if(!(row == i && col == j)){
        array :+= gridInitial.cells(i)(j)
      }
    }

    val cellsAlive = array.count(_.alive)

    if (isAlive && (cellsAlive > 3 || cellsAlive < 2)) false
    else if(!isAlive && cellsAlive == 3) true
    else isAlive
  }
}
