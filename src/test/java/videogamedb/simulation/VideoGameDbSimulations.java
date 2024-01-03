package videogamedb.simulation;


import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.nothingFor;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class VideoGameDbSimulations extends Simulation {

    ChainBuilder getAllVideoGames =
            exec(http("Get all video games")
                    .get("/videogame"));

    ChainBuilder getSpecificGame =
            exec(http("Get specific game")
                    .get("/videogame/2"));

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "5"));
    final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "10"));
    final int TEST_DURATION = Integer.parseInt(System.getProperty("TEST_DURATION", "20"));

    @Override
    public void before() {
        System.out.println("USERS: " + USER_COUNT);
        System.out.println("RAMP_DURATION: " + RAMP_DURATION);
        System.out.println("TEST_DURATION: " + TEST_DURATION);
    }

    ScenarioBuilder scn = scenario("Video game db - Section 7 code")
            .forever().on(
                    exec(getAllVideoGames)
                            .pause(Duration.ofMillis(500))
                            .exec(getSpecificGame)
                            .pause(Duration.ofMillis(500))
                            .exec(getAllVideoGames)
            );

    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                        // atOnceUsers(5),
                        // rampUsers(10).during(20)
                        //constantUsersPerSec(5).during(10).randomized()
                        // rampUsersPerSec(1).to(5).during(20).randomized()
                        // atOnceUsers(10),
                        rampUsers(USER_COUNT).during(RAMP_DURATION)
                ).protocols(httpProtocol)
        ).maxDuration(TEST_DURATION);
    }
}
