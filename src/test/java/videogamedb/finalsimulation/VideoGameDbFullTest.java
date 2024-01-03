package videogamedb.finalsimulation;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.bodyString;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.jsonFile;
import static io.gatling.javaapi.core.CoreDsl.nothingFor;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class VideoGameDbFullTest extends Simulation {

    final int USERS = Integer.parseInt(System.getProperty("USERS", "5"));
    final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "10"));
    final int TEST_DURATION = Integer.parseInt(System.getProperty("TEST_DURATION", "15"));


    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://www.videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");


    FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/gameJsonFile.json").random();


    ChainBuilder getAllVideoGames = exec(http("Get all video games")
            .get("/videogame")
            .check(status().is(200)));
    ChainBuilder authenticate = exec(http("Authenticate")
            .post("/authenticate")
            .body(StringBody("""
                    {
                      "password": "admin",
                      "username": "admin"
                    }
                    """))
            .check(jmesPath("token").saveAs("jwtToken")));

    ChainBuilder createVideoGame =
            feed(jsonFeeder).
                    exec(http("Create a new game - #{name}")
                            .post("/videogame")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .body(StringBody("""
                                    {
                                      "name": "#{name}",
                                      "releaseDate": "#{releaseDate}",
                                      "reviewScore": #{reviewScore},
                                      "category": "#{category}",
                                      "rating": "#{rating}"
                                    }
                                    """)));
    ChainBuilder getLastPostedGame = exec(http("Get last posted game - #{name}")
            .get("/videogame/#{id}")
            .check(jmesPath("name").isEL("#{name}")));

    ChainBuilder deleteGame = exec(http("Delete game - #{name}")
            .delete("/videogame/#{id}")
            .header("Authorization", "Bearer #{jwtToken}")
            .check(bodyString().is("Video game deleted")));


    ScenarioBuilder scn = scenario("Video Game DB - final simulation")
            .forever().on(
                    exec(getAllVideoGames)
                            .pause(Duration.ofMillis(150))
                            .exec(authenticate)
                            .pause(Duration.ofMillis(150))
                            .exec(createVideoGame)
                            .pause(Duration.ofMillis(150))
                            .exec(getLastPostedGame)
                            .pause(Duration.ofMillis(150))
                            .exec(deleteGame)
            );


    {
        setUp(scn.injectOpen(
                nothingFor(5),
                rampUsers(USERS).during(RAMP_DURATION)
        ))
                .protocols(httpProtocol)
                .maxDuration(TEST_DURATION);
    }

    @Override
    public void before() {
        System.out.println("USERS: " + USERS);
        System.out.println("RAMP_DURATION: " + RAMP_DURATION);
        System.out.println("TEST_DURATION: " + TEST_DURATION);
    }

    @Override
    public void after() {
        System.out.println("FINISHED");
    }
}
