
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
  "board": [-1, -1, -1, -1, -1, -1, -1, -1, -1],
  "next_player": 0,
  "winner": -1
}"""

    Get("/new") ~> routes ~> check {
      responseAs[String] should be (response)
    }
  }

  "/move" should "return the new board" in {
    val move =
"""{
  "board": [-1, -1, -1, -1, -1, -1, -1, -1, -1],
  "current_player": 1,
  "position": 1
}"""

    val response =
"""{
  "board": [-1, 1, -1, -1, -1, -1, -1, -1, -1],
  "next_player": 0,
  "winner": -1
}"""

    Get ("/move", HttpEntity(ContentType(`application/json`, `UTF-8`), move)) ~> routes ~> check {
      responseAs[String] should be (response)
    }
  }

  it should "return the winner" in {
    val move =
"""{
  "board": [1, 1, -1, -1, -1, -1, -1, -1, -1],
  "current_player": 1,
  "position": 2
}"""

    val response =
"""{
  "board": [1, 1, 1, -1, -1, -1, -1, -1, -1],
  "next_player": 0,
  "winner": 1
}"""

    Get ("/move", HttpEntity(ContentType(`application/json`, `UTF-8`), move)) ~> routes ~> check {
      responseAs[String] should be (response)
    }

  }

  it should "not allow an illegal move" in {
    val move =
      """{
  "board": [-1, 0, -1, -1, -1, -1, -1, -1, -1],
  "current_player": 1,
  "position": 10
}"""

    val response =
      """{
  "board": [-1, 0, -1, -1, -1, -1, -1, -1, -1],
  "next_player": 1,
  "winner": -1
}"""

    Get ("/move", HttpEntity(ContentType(`application/json`, `UTF-8`), move)) ~> routes ~> check {
      responseAs[String] should be (response)
    }

  }

}
