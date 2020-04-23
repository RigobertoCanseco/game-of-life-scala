package com.rigobertocanseco.gameOfLife

import swing._
import java.awt.{Graphics2D}
import java.awt.Color

case class Cell(alive:Boolean)
case class Neighbors(cells:Array[Cell])
case class Grid(cells:Array[Array[Cell]]){
    def intToGrid(array: Array[Array[Int]], rowMax:Int, colMax: Int): Grid = {
        Grid( (for(row <- 0 until rowMax; col <- 0 until colMax) yield Cell(if(array(row)(col) == 1) true else false)).toArray.grouped(rowMax).toArray)
    }
}

class GameOfLife(gridInitial:Grid, rowMax:Int, colMax: Int) {

    private def alive(row: Int, col: Int, isAlive: Boolean): Boolean = {
        var array: Array[Cell] = Array[Cell]()

        for(i <- (if(row == 0) 0 else row - 1) to (if(row == rowMax - 1) row else row + 1);
                    j <- (if(col == 0) 0 else col - 1) to (if(col == colMax - 1) col else col + 1)) {
            if(i != row && j != col) {
                array +:= gridInitial.cells(i)(j)
            }
        }

        val cellsAlive = array.count(_.alive)

        if( (cellsAlive > 3 && isAlive) || (cellsAlive < 2 && isAlive))
            false
        else if(cellsAlive == 3 && !isAlive)
            true
        else isAlive
    }

    def nextEvolution(): Grid = {
        Grid( (for(row <- 0 until rowMax; col <- 0 until colMax) yield
            Cell(alive(row, col, gridInitial.cells(row)(col).alive))).toArray.grouped(rowMax).toArray)
    }
}

object game {
    import java.awt.{Color => AWTColor}

    val width = 500
    val height = 500
    val bluishGray = new AWTColor(48, 99, 99)
    val bluishSilver = new AWTColor(210, 255, 255)
    val dataInitial:Grid = Grid(null).intToGrid(
        Array(
            Array(0,0,0,0,0),
            Array(0,1,1,1,0),
            Array(0,1,1,1,0),
            Array(0,1,1,1,0),
            Array(0,0,0,0,0),
        ), game.height/100, game.width/100)
    var data: Grid = _
    def update(d:Grid):Grid = {
        val gameOfLife = new GameOfLife( if(d == null) dataInitial else d, height/100, width/100)
        val newData = gameOfLife.nextEvolution()
        data = newData
        data
    }
}

object Draw extends SimpleSwingApplication {
    import java.awt.{Dimension, Graphics2D}

    var mainPanel = new DataPanel {
        preferredSize = new Dimension(game.width, game.height)
    }

    def fun(d:Grid): Grid = {
        val data:Grid = game.update(d)
        mainPanel.repaint()
        data
    }

    def top = new MainFrame {
        title = "Game of life"
        contents = mainPanel

        var grid: Grid = _

        val timer = new javax.swing.Timer(1000, Swing.ActionListener(e => {
            grid = fun(grid)
        }))
        timer.start()
    }
}

class DataPanel extends Panel {

    override def paintComponent(g: Graphics2D) {
        _print(game.data, game.height/100, game.width/100)
        g setColor Color.BLACK
        g fillRect (0, 0, game.width, game.height)
        g setColor game.bluishGray
        for(row <- 0 until game.height/100; col <- 0 until game.width/100) {
            if(game.data.cells(row)(col).alive)
                g fillRect (row*100, col*100, 100, 100)
        }
    }

    def _print(grid: Grid, rowMax: Int, colMax: Int): Unit = {
        println("=========================================")
        for (i <- 0 until rowMax) {
            print("||")
            for (j <- 0 until colMax) {
                print(s"${if (grid.cells(i)(j).alive) "*" else " "}")
            }
            print("||")
            println()
        }
        println(" =========================================")
    }
}
