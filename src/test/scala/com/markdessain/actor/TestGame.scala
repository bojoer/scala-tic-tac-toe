
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

    val response =
"""{
  "board": ["", "", "", "", "", "", "", "", ""],
  "next_player": "O",
  "winner": ""
}"""

    Get("/new") ~> routes ~> check {
      responseAs[String] should be (response)
    }
  }

  "/move" should "return the new board" in {
    val move =
"""{
  "board": ["", "", "", "", "", "", "", "", ""],
  "current_player": "X",
  "position": 1
}"""

    val response =
"""{
  "board": ["", "X", "", "", "", "", "", "", ""],
  "next_player": "O",
  "winner": ""
}"""

    Get ("/move", HttpEntity(ContentType(`application/json`, `UTF-8`), move)) ~> routes ~> check {
      responseAs[String] should be (response)
    }
  }

  it should "return the winner" in {
    val move =
"""{
  "board": ["X", "X", "", "", "", "", "", "", ""],
  "current_player": "X",
  "position": 2
}"""

    val response =
"""{
  "board": ["X", "X", "X", "", "", "", "", "", ""],
  "next_player": "O",
  "winner": "X"
}"""

    Get ("/move", HttpEntity(ContentType(`application/json`, `UTF-8`), move)) ~> routes ~> check {
      responseAs[String] should be (response)
    }

  }

  it should "not allow an illegal move" in {
    val move =
      """{
  "board": ["", "O", "", "", "", "", "", "", ""],
  "current_player": "X",
  "position": 10
}"""

    val response =
      """{
  "board": ["", "O", "", "", "", "", "", "", ""],
  "next_player": "X",
  "winner": ""
}"""

    Get ("/move", HttpEntity(ContentType(`application/json`, `UTF-8`), move)) ~> routes ~> check {
      responseAs[String] should be (response)
    }

  }

}
