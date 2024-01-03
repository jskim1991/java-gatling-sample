package videogamedb.fundamentals;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class MyFirstTest extends Simulation {

    // 1. http configuration
    HttpProtocolBuilder httpProtocol = HttpDsl.http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    // 2. scenario definition
    ScenarioBuilder scn = CoreDsl.scenario("My first test")
            .exec(HttpDsl
                    .http("Get all games")
                    .get("/videogame"));


    // 3. load simulation
    {
        setUp(scn.injectOpen(OpenInjectionStep.atOnceUsers(1)))
                .protocols(httpProtocol);
    }

}
