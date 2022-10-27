package com.gatling.tests.api
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation


class PostPutDeleteAPI extends Simulation {

  //protocol
  val httpProtocol=http
    .baseUrl("https://reqres.in/api/")

  //scenario
  val createUserScenario=scenario("Create User")
    .exec(
      http("create user req")
        .post("users")
        .header("content-type", "application/json")
        .asJson
        .body(RawFileBody("data/user.json")).asJson
//        .body(StringBody(
//        """
//            |{
//            |    "name": "morpheus",
//            |    "job": "leader"
//            |}
//            |""".stripMargin)).asJson
        .check(
          status is 201,
        jsonPath("$.name") is "morpheus"
        )
          )
    .pause("1")
  val updateUserScenario=scenario("Update User")
    .exec(
      http("update user")
        .put("users/2")
        .body(RawFileBody("data/user.json")).asJson
        .check(
          status in 200,
          jsonPath("$.name") is "John"
        )
    )
//setup
setUp(
  createUserScenario.inject(rampUsers(5).during(5)),
  updateUserScenario.inject(rampUsers(5).during(5)
  )

    .protocols(httpProtocol)
)


}
