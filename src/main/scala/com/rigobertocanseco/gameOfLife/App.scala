package com.rigobertocanseco.gameOfLife


import scalafx.Includes._
import scalafx.animation.Timeline
import scalafx.animation.Timeline.Indefinite

import scala.math.random
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.effect.BlendMode.Overlay
import scalafx.scene.effect.BoxBlur
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.CycleMethod.NoCycle
import scalafx.scene.shape.Circle
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape.StrokeType.Outside

import scala.io.{BufferedSource, Source}
import scala.language.postfixOps


object App extends JFXApp {
  val size = 60
  var arrayBoolean: Array[Boolean] = getGridInitial
  var grid: Grid = Grid((for (a <- arrayBoolean) yield Cell(a)).grouped(size).toArray)
  _print(grid, size, size)
  //val gameOfLife = new GameOfLife(grid, 80, 80)
  //grid = gameOfLife.nextEvolution()
  //_print(grid, 80, 80)

  var cells: List[Rectangle] = List[Rectangle]()
  arrayBoolean.zipWithIndex.foreach {
    case (e, i) =>
      cells :+= new Rectangle {
        width = 5
        height = 5
        x = (i % size) * 10
        y = (i / size) * 10
        fill = if (e) Color.DarkGreen else Color.Black opacity 0.05
        //centerX = random * 800
        //centerY = random * 600
        //radius = 150
        //fill = White opacity 0.05
        stroke = White opacity 0.16
        strokeWidth = 1
        strokeType = Outside
        effect = new BoxBlur(2, 2, 3)
      }
  }

  stage = new PrimaryStage {
    title = "Game of life"
    maximized = false
    width = 600
    height = 600
    minWidth = 600
    maxHeight = 600
    maxWidth = 600
    minHeight = 600

    scene = new Scene {
      _scene =>
      fill = Black
      maximized = false
      minWidth = 600
      maxHeight = 600
      maxWidth = 600
      minHeight = 600
      content = cells
    }
  }

  var grid1: Grid = Grid((for (a <- arrayBoolean) yield Cell(a)).grouped(size).toArray)

  for(i <- 0 to 10) {
    val gameOfLife = new GameOfLife(grid1, size, size)
    grid1 = gameOfLife.nextEvolution()
    _print(grid1, size, size)
  }


  new Timeline {
    cycleCount = Indefinite
    autoReverse = true


    keyFrames =
      for ((cell, i) <- cells.zipWithIndex) yield
        at(1 s) {
          Set(cell.fill -> (if (grid.cells(i / size)(i % size).alive) Color.DarkGreen else Color.Black))
        }

    val gameOfLife2 = new GameOfLife(grid, 60, 60)
    grid = gameOfLife2.nextEvolution()
    _print(grid, 60, 60)

    keyFrames.++(for ((cell, i) <- cells.zipWithIndex) yield
          at(2 s) {
            Set(cell.fill -> (if (grid.cells(i / 60)(i % 60).alive) Color.DarkGreen else Color.Black))
          })(collection.breakOut)

  }.play()

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

  def getGridInitial: Array[Boolean] = {
    val filename = "/home/rigoberto/Documentos/projects/github/game-of-life-scala/src/main/file.txt"
    val bufferedSource: BufferedSource = Source.fromFile(filename)
    val array: Array[Boolean] = (for (line <- bufferedSource.getLines; c <- line.iterator) yield
      if (c.equals('1')) true else false).toArray
    bufferedSource.close
    array
  }
}