package videogamedb.feeders;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class VideoGameDbCustomFeeders extends Simulation {

    Iterator<Map<String, Object>> customFeeder =
            Stream.generate(
                    (Supplier<Map<String, Object>>) () -> {
                        Random random = new Random();
                        int id = random.nextInt(10) + 1;
                        return Map.of("gameId", id);
                    }
            ).iterator();

    LocalDate randomDate() {
        int hundredYears = 100 * 365;
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears));
    }

    Iterator<Map<String, Object>> customFeederForCreate =
        Stream.generate(
                (Supplier<Map<String, Object>>) () -> {
                    Random random = new Random();
                    var gameName = RandomStringUtils.randomAlphanumeric(5) + "-gameName";
                    var releaseDate = randomDate().toString();
                    var reviewScore = random.nextInt(100);
                    var category = RandomStringUtils.randomAlphanumeric(5) + "-category";
                    var rating = RandomStringUtils.randomAlphanumeric(4) + "-rating";
                    return Map.of(
                            "gameName", gameName,
                            "releaseDate", releaseDate,
                            "reviewScore", reviewScore,
                            "category", category,
                            "rating", rating
                    );
                }
        ).iterator();


    ChainBuilder getSpecificGame =
            feed(customFeeder)
                    .exec(http("Get video game with id #{gameId}")
                            .get("/videogame/#{gameId}")
                    );

    ChainBuilder authenticate =
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

    ChainBuilder createNewGame =
            feed(customFeederForCreate)
                    .exec(http("Create new video game - #{gameName}")
                            .post("/videogame")
                            .header("Authorization", "Bearer #{jwtToken}")
                            .body(ElFileBody("bodies/newGameTemplate.json")).asJson()
                    );

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://www.videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    ScenarioBuilder scn = scenario("Video Game DB using Feeder")
            .repeat(10).on(
                    exec(getSpecificGame)
            )
            .exec(authenticate)
            .repeat(10).on(
                    exec(createNewGame)
            )
            ;


    {
        setUp(scn.injectOpen(OpenInjectionStep.atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
