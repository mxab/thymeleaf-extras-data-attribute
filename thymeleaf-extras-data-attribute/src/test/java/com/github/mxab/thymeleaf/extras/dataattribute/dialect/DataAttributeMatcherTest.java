package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorMatchingContext;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeMatcher;

public class DataAttributeMatcherTest {

	private DataAttributeMatcher matcher;

	@Before
	public void before() {
		this.matcher = new DataAttributeMatcher();
	}

	@Test
	public void testAppliesTo() throws Exception {

		Assert.assertEquals(Element.class, matcher.appliesTo());
	}

	@Test
	public void testMatcher() throws Exception {
		Element element = new Element("div");
		element.setAttribute("data:foo", "${'bar'}");
		ProcessorMatchingContext processorMatchingContext = new ProcessorMatchingContext(
				new DataAttributeDialect(), "data");

		boolean matches = matcher.matches(element, processorMatchingContext);
		assertThat(matches, is(true));
	}
}
