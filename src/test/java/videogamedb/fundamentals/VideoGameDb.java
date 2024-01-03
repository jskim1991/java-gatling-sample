package videogamedb.fundamentals;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class VideoGameDb extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://www.videogamedb.uk/api")
            .acceptHeader("application/json");

    ScenarioBuilder scn = scenario("Video Game DB - Section 5 code")
            .exec(http("Get all video games - 1st call")
                    .get("/videogame")
                    .check(status().is(200))
                    .check(jsonPath("$[?(@.id==1)].name").is("Resident Evil 4"))
                    .check(jmesPath("[? id == `1`].name").ofList().is(List.of("Resident Evil 4")))
                    .check(jmesPath("length([? category == 'Shooter'])").is("2"))
            )
            .pause(1)


            .exec(http("Get specific game")
                    .get("/videogame/1")
                    .check(status().in(200, 201, 202))
                    .check(jmesPath("name").is("Resident Evil 4"))
            )
            .pause(1, 2)


            .exec(http("Get all video games and correlate gameId")
                    .get("/videogame")
                    .check(status().not(500), status().not(404))
                    .check(jsonPath("$[1].id").saveAs("gameId"))
                    .check(jmesPath("length([])").is("10"))
            )
            .pause(Duration.ofMillis(200))


            .exec(http("Get specific game with saved game ID - #{gameId}")
                    .get("/videogame/#{gameId}")
                    .check(jmesPath("name").is("Gran Turismo 3"))
            )
            ;


    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
