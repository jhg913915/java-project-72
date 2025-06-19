package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.testtools.JavalinTest;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AppTest {

    private static MockWebServer serverStub;
    private Javalin webApp;
    private static final Path HTML_RESOURCE = Paths.get("src/test/resources/example.html").toAbsolutePath();

    private final Context dummyContext = mock(Context.class);

    @BeforeAll
    static void setupStubServer() throws IOException {
        serverStub = new MockWebServer();
        String htmlContent = Files.readString(HTML_RESOURCE, StandardCharsets.UTF_8);
        serverStub.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));
        serverStub.start();
    }

    @AfterAll
    static void tearDownStubServer() throws IOException {
        serverStub.shutdown();
    }

    @BeforeEach
    final void initApp() throws SQLException, IOException {
        webApp = App.getApp();
    }

    @Test
    final void homepageShouldLoad() {
        JavalinTest.test(webApp, (server, http) -> {
            var res = http.get("/");
            assertThat(res.code()).isEqualTo(200);
            assertThat(res.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    final void urlsOverviewShouldBeAvailable() {
        JavalinTest.test(webApp, (server, http) -> {
            var res = http.get("/urls");
            assertThat(res.code()).isEqualTo(200);
        });
    }

    @Test
    final void postingValidUrlShouldSucceed() {
        JavalinTest.test(webApp, (server, http) -> {
            var form = "url=https://www.example.com";
            var response = http.post("/urls", form);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    final void duplicateUrlShouldBeIgnored() {
        JavalinTest.test(webApp, (server, http) -> {
            var urlPayload = "url=https://www.example.com";
            http.post(NamedRoutes.urlsPath(), urlPayload);
            http.post(NamedRoutes.urlsPath(), urlPayload);

            var firstVisit = http.get(NamedRoutes.urlPath("1"));
            var secondVisit = http.get(NamedRoutes.urlPath("2"));

            assertThat(UrlRepository.getEntities().size()).isEqualTo(1);
            assertThat(firstVisit.code()).isEqualTo(200);
            assertThat(secondVisit.code()).isEqualTo(404);
        });
    }

    @Test
    final void requestingNonexistentUrlShouldFail() {
        JavalinTest.test(webApp, (server, http) -> {
            var result = http.get("/urls/9999");
            assertThat(result.code()).isEqualTo(404);
        });
    }

    @Test
    final void checkingUrlShouldCreateCheckRecord() {
        JavalinTest.test(webApp, (server, http) -> {
            String urlFromStub = serverStub.url("/").toString();
            Url newUrl = new Url(urlFromStub);
            UrlRepository.save(newUrl);

            var checkResult = http.post(NamedRoutes.checksPath(String.valueOf(newUrl.getId())));
            assertThat(checkResult.code()).isEqualTo(200);

            List<UrlCheck> checks = UrlCheckRepository.findByUrlId(newUrl.getId());
            assertThat(checks).hasSize(1);

            UrlCheck check = checks.getFirst();
            assertThat(check.getUrlId()).isEqualTo(newUrl.getId());
            assertThat(check.getStatusCode()).isEqualTo(200);

            String bodyContent = checkResult.body().string();
            System.out.println("Returned HTML: " + bodyContent);

            assertThat(bodyContent).contains("Анализатор страниц");
            assertThat(check.getH1()).contains("H1 tag content");
            assertThat(check.getDescription()).contains("Content of description");
        });
    }
}
