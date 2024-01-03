package videogamedb.feeders;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class VideoGameDbCsvFeeders extends Simulation {

    static final FeederBuilder.FileBased<String> csvFeeder = csv("data/gameCsvFile.csv")
            .circular();
    static ChainBuilder getSpecificGame =
            feed(csvFeeder)
                    .exec(http("Get video game with id #{gameId}")
                            .get("/videogame/#{gameId}")
                            .check(jmesPath("name").isEL("#{gameName}"))
                    );

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://www.videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    ScenarioBuilder scn = scenario("Video Game DB using Feeder")
            .repeat(10).on(
                    exec(getSpecificGame)
            );


    {
        setUp(scn.injectOpen(OpenInjectionStep.atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
