package videogamedb.feeders;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.jsonFile;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class VideoGameDbJsonFeeders extends Simulation {

    static final FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/gameJsonFile.json")
            .random();
    static ChainBuilder getSpecificGame =
            feed(jsonFeeder)
                    .exec(http("Get video game with id #{id}")
                            .get("/videogame/#{id}")
                            .check(jmesPath("name").isEL("#{name}"))
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
