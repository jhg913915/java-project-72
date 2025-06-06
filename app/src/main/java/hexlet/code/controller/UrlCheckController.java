package hexlet.code.controller;

import hexlet.code.dto.urls.UrlChecksPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlCheckController {

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Сайт не найден"));
        var urlChecks = UrlCheckRepository.findByUrlId(id);
        var checksPage = new UrlChecksPage(url);
        checksPage.setFlash(ctx.consumeSessionAttribute("flash"));
        checksPage.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", model("checksPage", checksPage));
    }

    public static void doCheck(Context ctx) throws SQLException, UnirestException {
        long targetId = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Url targetUrl = UrlRepository.findById(targetId)
                .orElseThrow(() -> new NotFoundResponse("Не удалось найти указанный URL"));
        try {
            URI uri = new URI(targetUrl.getName());
            URL validatedUrl = uri.toURL();
            HttpResponse<String> response = Unirest.get(validatedUrl.toString()).asString();
            int code = response.getStatus();
            String html = response.getBody();
            Document parsedHtml = Jsoup.parse(html);
            String pageTitle = parsedHtml.title();
            String h1 = parsedHtml.selectFirst("h1") != null
                    ? parsedHtml.selectFirst("h1").text()
                    : "";
            String metaDesc = parsedHtml.select("meta[name=description]").attr("content");
            UrlCheck check = new UrlCheck();
            check.setStatusCode(code);
            check.setTitle(pageTitle);
            check.setH1(h1);
            check.setDescription(metaDesc);
            UrlCheckRepository.save(check, targetUrl);
            ctx.sessionAttribute("flash", "Проверка успешно завершена");
            ctx.sessionAttribute("flashType", "alert-success");
        } catch (UnirestException | URISyntaxException | MalformedURLException error) {
            ctx.sessionAttribute("flash", "Не удалось подключиться к ресурсу");
            ctx.sessionAttribute("flashType", "alert-danger");
        }
        ctx.redirect(NamedRoutes.urlPath(targetId));
    }
}
