package videogamedb.fundamentals;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.repeat;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class VideoGameDb extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://www.videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            ;

    static ChainBuilder getAllGames = repeat(3)
            .on(exec(http("Get all video games")
                    .get("/videogame")
                    .check(status().is(200))
                    .check(jsonPath("$[?(@.id==1)].name").is("Resident Evil 4"))
                    .check(jmesPath("[? id == `1`].name").ofList().is(List.of("Resident Evil 4")))
                    .check(jmesPath("length([? category == 'Shooter'])").is("2"))
                    .check(jmesPath("length([])").is("10"))
            ));

    static ChainBuilder getGameWithId =
            repeat(10, "gameId").on(
                    exec(http("Get specific game with id: #{gameId}")
                            .get("/videogame/#{gameId}")
                            .check(status().in(200, 201, 202))
                    )
            );

    static ChainBuilder authenticate =
            exec(http("Authenticate")
                    .post("/authenticate")
                    .body(StringBody(
                            """
                                    {
                                      "password": "admin",
                                      "username": "admin"
                                    }
                                    """
                    ))
                    .check(jmesPath("token").saveAs("jwtToken"))
            );

    static ChainBuilder createNewGame =
            exec(http("Create a new game")
                    .post("/videogame")
                    .header("Authorization", "Bearer #{jwtToken}")
                    .body(StringBody(
                            """
                                    {
                                      "category": "Platform",
                                      "name": "Mario",
                                      "rating": "Mature",
                                      "releaseDate": "2012-05-04",
                                      "reviewScore": 85
                                    }
                                    """
                    ))
            );


    ScenarioBuilder scn = scenario("Video Game DB - Section 5 code")
            .exec(getAllGames)
            .pause(Duration.ofMillis(300))
            .exec(getGameWithId)
            .pause(Duration.ofMillis(300))
            .repeat(2).on(
                    exec(getAllGames)
            )
            .pause(Duration.ofMillis(300))
            .exec(authenticate, createNewGame)

            ;



    {
        setUp(scn.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
