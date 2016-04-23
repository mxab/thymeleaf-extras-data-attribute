package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

public class DialectTemplateEngineTest {

	@Test
	public void test() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		AbstractMessageResolver messageResolver = new DummyMessageResolverExtension();
		templateEngine.setMessageResolver(messageResolver);
		templateEngine.addDialect(new DataAttributeDialect());

		String process = templateEngine.process("src/test/resources/test.html",
				new Context());
		Document document = Jsoup.parse(process);

		Element element = document.getElementsByTag("body").get(0);

		assertThat(element.attr("data-foo"), equalTo("bar"));
		assertThat(element.attr("data-msg"), equalTo("Hello World"));
	}

	@Test
	public void testMissingProperty() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();

		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		AbstractMessageResolver messageResolver = new DummyMessageResolverExtension();
		templateEngine.setMessageResolver(messageResolver);
		templateEngine.addDialect(new DataAttributeDialect());

		String process = templateEngine.process(
				"src/test/resources/testMissing.html", new Context());
		Document document = Jsoup.parse(process);

		Element element = document.getElementsByTag("body").get(0);

		assertThat("data-foo attribute is not rendered",
				element.hasAttr("data-foo"), is(false));

	}

	private final class DummyMessageResolverExtension extends
			AbstractMessageResolver {
		@Override
		public String resolveMessage(ITemplateContext context, Class<?> origin,
				String key, Object[] messageParameters) {
			return key.equals("my.message") ? "Hello World" : null;
		}

		@Override
		public String createAbsentMessageRepresentation(
				ITemplateContext context, Class<?> origin, String key,
				Object[] messageParameters) {
			return null;
		}
	}
}
