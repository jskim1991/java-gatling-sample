package videogamedb;

import java.time.Duration;
import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import io.gatling.javaapi.jdbc.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class RecordedSimulation extends Simulation {

  private HttpProtocolBuilder httpProtocol = http
    .baseUrl("https://www.videogamedb.uk")
    .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
    .acceptHeader("image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
  
  private Map<CharSequence, String> headers_0 = Map.ofEntries(
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Sec-Fetch-Dest", "empty"),
    Map.entry("Sec-Fetch-Mode", "cors"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-GPC", "1"),
    Map.entry("accept", "application/json"),
    Map.entry("sec-ch-ua", "Brave\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "macOS")
  );
  
  private Map<CharSequence, String> headers_1 = Map.ofEntries(
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Sec-Fetch-Dest", "image"),
    Map.entry("Sec-Fetch-Mode", "no-cors"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-GPC", "1"),
    Map.entry("sec-ch-ua", "Brave\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "macOS")
  );
  
  private Map<CharSequence, String> headers_6 = Map.ofEntries(
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Content-Type", "application/json"),
    Map.entry("Origin", "https://www.videogamedb.uk"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Sec-Fetch-Dest", "empty"),
    Map.entry("Sec-Fetch-Mode", "cors"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-GPC", "1"),
    Map.entry("accept", "application/json"),
    Map.entry("sec-ch-ua", "Brave\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "macOS")
  );
  
  private Map<CharSequence, String> headers_8 = Map.ofEntries(
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Content-Type", "application/json"),
    Map.entry("Origin", "https://www.videogamedb.uk"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Sec-Fetch-Dest", "empty"),
    Map.entry("Sec-Fetch-Mode", "cors"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-GPC", "1"),
    Map.entry("accept", "application/json"),
    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwNDI0NTY3OSwiZXhwIjoxNzA0MjQ5Mjc5fQ.6pBkVPpNZr9sNmiVfnRCy5596xzmWHDYBQzubG9qxT8"),
    Map.entry("sec-ch-ua", "Brave\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "macOS")
  );
  
  private Map<CharSequence, String> headers_12 = Map.ofEntries(
    Map.entry("Cache-Control", "no-cache"),
    Map.entry("Origin", "https://www.videogamedb.uk"),
    Map.entry("Pragma", "no-cache"),
    Map.entry("Sec-Fetch-Dest", "empty"),
    Map.entry("Sec-Fetch-Mode", "cors"),
    Map.entry("Sec-Fetch-Site", "same-origin"),
    Map.entry("Sec-GPC", "1"),
    Map.entry("accept", "application/json"),
    Map.entry("authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwNDI0NTY3OSwiZXhwIjoxNzA0MjQ5Mjc5fQ.6pBkVPpNZr9sNmiVfnRCy5596xzmWHDYBQzubG9qxT8"),
    Map.entry("sec-ch-ua", "Brave\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24"),
    Map.entry("sec-ch-ua-mobile", "?0"),
    Map.entry("sec-ch-ua-platform", "macOS")
  );


  private ScenarioBuilder scn = scenario("RecordedSimulation")
    .exec(
      http("request_0")
        .get("/api/videogame")
        .headers(headers_0),
      pause(15),
      http("request_1")
        .get("/swagger-ui/favicon-32x32.png?v=3.0.4")
        .headers(headers_1)
        .resources(
          http("request_2")
            .get("/swagger-ui/favicon-32x32.png?v=3.0.4")
            .headers(headers_1)
        ),
      pause(7),
      http("request_3")
        .get("/api/videogame/2")
        .headers(headers_0),
      pause(8),
      http("request_4")
        .get("/swagger-ui/favicon-32x32.png?v=3.0.4")
        .headers(headers_1),
      pause(1),
      http("request_5")
        .get("/swagger-ui/favicon-32x32.png?v=3.0.4")
        .headers(headers_1),
      pause(2),
      http("request_6")
        .post("/api/authenticate")
        .headers(headers_6)
        .body(RawFileBody("videogamedb/recordedsimulation/0006_request.json")),
      pause(24),
      http("request_7")
        .get("/swagger-ui/favicon-32x32.png?v=3.0.4")
        .headers(headers_1),
      pause(2),
      http("request_8")
        .post("/api/videogame")
        .headers(headers_8)
        .body(RawFileBody("videogamedb/recordedsimulation/0008_request.json")),
      pause(8),
      http("request_9")
        .get("/swagger-ui/favicon-32x32.png?v=3.0.4")
        .headers(headers_1),
      pause(3),
      http("request_10")
        .put("/api/videogame/3")
        .headers(headers_8)
        .body(RawFileBody("videogamedb/recordedsimulation/0010_request.json")),
      pause(2),
      http("request_11")
        .get("/swagger-ui/favicon-32x32.png?v=3.0.4")
        .headers(headers_1),
      pause(3),
      http("request_12")
        .delete("/api/videogame/3")
        .headers(headers_12)
    );

  {
	  setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
  }
}
