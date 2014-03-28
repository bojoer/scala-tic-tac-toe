
package com.markdessain.actor


import org.scalatest._
import spray.testkit.ScalatestRouteTest
import spray.http._
import spray.http.MediaTypes.{ `application/json` }
import spray.http.HttpCharsets.{ `UTF-8` }
import spray.http.ContentType

class TestGame extends FlatSpec with Matchers with ScalatestRouteTest with Game {
  def actorRefFactory = system

  "/new" should "return an empty board" in {
    Get("/new") ~> routes ~> check {
      responseAs[String] should be ("[\"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\", \"\"]")
    }
  }

  "/move" should "return the new board" in {
    val move = """{
      "board": ["", "", "", "", "", "", "", "", ""],
      "current_player": "X",
      "position": 1
    }"""

    val response = """{
  "board": ["", "X", "", "", "", "", "", "", ""],
  "next_player": "O"
}"""

    Get ("/move", HttpEntity(ContentType(`application/json`, `UTF-8`), move)) ~> routes ~> check {
      responseAs[String] should be (response)
    }

  }

}
