package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.util.TimestampFormatter;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,2,2,2,4,4,6,6,8,8,9,9,9,9,10,10,10,13,13,26,26,29,29,29,32,32,32,32,32,32,32,34,34,39,39,40,40,41,41,43,43,43,46,46,46,48,48,49,49,51,51,56,56,56,56,56,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlsPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <section>\r\n        ");
				if (page.getFlash() != null) {
					jteOutput.writeContent("\r\n            <div class=\"rounded-0 m-0 alert alert-dismissible fade show ");
					jteOutput.setContext("div", "class");
					jteOutput.writeUserContent(page.getFlashType());
					jteOutput.setContext("div", null);
					jteOutput.writeContent("\" role=\"alert\">\r\n                <p class=\"m-0\">");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(page.getFlash());
					jteOutput.writeContent("</p>\r\n                <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\r\n            </div>\r\n        ");
				}
				jteOutput.writeContent("\r\n        <div class=\"container-lg mt-5\">\r\n            <h1>Сайты</h1>\r\n            <table class=\"table table-bordered table-hover mt-3\">\r\n                <thead>\r\n                <tr>\r\n                    <th class=\"col-1\">ID</th>\r\n                    <th>Имя</th>\r\n                    <th class=\"col-2\">Последняя проверка</th>\r\n                    <th class=\"col-1\">Код ответа</th>\r\n                </tr>\r\n                </thead>\r\n                <tbody>\r\n                ");
				for (var url : page.getUrls()) {
					jteOutput.writeContent("\r\n                    <tr>\r\n                        <td>\r\n                            ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(url.getId());
					jteOutput.writeContent("\r\n                        </td>\r\n                        <td>\r\n                            <a href=\"/urls/");
					jteOutput.setContext("a", "href");
					jteOutput.writeUserContent(url.getId());
					jteOutput.setContext("a", null);
					jteOutput.writeContent("\">");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(url.getName());
					jteOutput.writeContent("</a>\r\n                        </td>\r\n                        ");
					if (page.getChecks().isEmpty()) {
						jteOutput.writeContent("\r\n                            <td>\r\n                            </td>\r\n                            <td>\r\n                            </td>\r\n                        ");
					}
					jteOutput.writeContent("\r\n                        ");
					for (var check : page.getChecks()) {
						jteOutput.writeContent("\r\n                            ");
						if (check.getUrlId() == url.getId()) {
							jteOutput.writeContent("\r\n                                <td>\r\n                                    ");
							jteOutput.setContext("td", null);
							jteOutput.writeUserContent(check.getCreatedAt().format(TimestampFormatter.formatterType1));
							jteOutput.writeContent("\r\n                                </td>\r\n                                <td>\r\n                                    ");
							jteOutput.setContext("td", null);
							jteOutput.writeUserContent(check.getStatusCode());
							jteOutput.writeContent("\r\n                                </td>\r\n                            ");
						}
						jteOutput.writeContent("\r\n                        ");
					}
					jteOutput.writeContent("\r\n                    </tr>\r\n                ");
				}
				jteOutput.writeContent("\r\n                </tbody>\r\n            </table>\r\n        </div>\r\n    </section>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlsPage page = (UrlsPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
