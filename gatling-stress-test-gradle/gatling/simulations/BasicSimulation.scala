import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class BasicSimulation extends Simulation {

  val tokenHeaders: Map[String, String] = Map("Content-Type" -> """application/json""")
  val httpRequest: HttpProtocolBuilder = http.baseUrl("https://qed.onvio.com.br")

  val scn: ScenarioBuilder = scenario("Basic stress test")
    .exec(actionBuilder = http("hello world")
        .get("http://localhost:8080/hello/world")
        .check(bodyString.saveAs("BODY RESPONSE"))
    )
    .exec(session => {
        val response = session("BODY RESPONSE").as[String]
        println(s"Response body: \n$response")
        session
      }
    )
    setUp(
      scn.inject(
        atOnceUsers(10),
        constantUsersPerSec(5000) during 60
      )
    ).protocols(httpRequest)
}
