package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Arguments.class, StandardExpressions.class })
public class DataProcessorTest {
	DataProcessor dataProcessor;

	@Before
	public void before() {
		dataProcessor = new DataProcessor();
	}

	@Test
	public void testGetMatcher() throws Exception {
		IProcessorMatcher<? extends Node> matcher = dataProcessor.getMatcher();
		assertThat(matcher, is(notNullValue()));
		assertThat(matcher, instanceOf(DataAttributeMatcher.class));

	}

	@Test
	public void testProcess() throws Exception {
		Element element = new Element("div");
		element.setAttribute("data:foo", "${'bar'}");

		ProcessorMatchingContext processorMatchingContext = new ProcessorMatchingContext(
				new DataAttributeDialect(), "data");

		mockStandardExpression("bar");
		Arguments arguments = PowerMockito.mock(Arguments.class);
		dataProcessor.doProcess(arguments, processorMatchingContext, element);

		assertThat("no data:foo attribute is left",
				element.hasAttribute("data:foo"), is(false));

		assertThat("data-foo attribute has value of bar",
				element.getAttributeValue("data-foo"), equalTo("bar"));

	}

	public void mockStandardExpression(final String parseResult) {

		final IStandardExpression expression = Mockito.mock(
				IStandardExpression.class, new Answer<Object>() {

					@Override
					public Object answer(InvocationOnMock invocation)
							throws Throwable {

						return parseResult;
					}
				});
		final IStandardExpressionParser expressionParser = Mockito.mock(
				IStandardExpressionParser.class, new Answer<Object>() {

					@Override
					public Object answer(InvocationOnMock invocation)
							throws Throwable {

						return expression;
					}
				});

		PowerMockito.mockStatic(StandardExpressions.class,
				new Answer<IStandardExpressionParser>() {

					@Override
					public IStandardExpressionParser answer(
							InvocationOnMock invocation) throws Throwable {

						return expressionParser;
					}
				});
	}

	@Test
	public void testProcessWhenPropertyIsMissing() throws Exception {
		Element element = new Element("div");
		element.setAttribute("data:foo", "${missing}");

		ProcessorMatchingContext processorMatchingContext = new ProcessorMatchingContext(
				new DataAttributeDialect(), "data");
		mockStandardExpression(null);
		Arguments arguments = PowerMockito.mock(Arguments.class);
		dataProcessor.doProcess(arguments, processorMatchingContext, element);

		assertThat("no data:foo attribute is left",
				element.hasAttribute("data:foo"), is(false));

		assertThat("data-foo attribute is not rendered",
				element.hasAttribute("data-foo"), is(false));

	}

	@Test
	public void testPrecendence() {
		assertThat("precendence is 1100", dataProcessor.getPrecedence(),
				equalTo(1100));
	}

}
