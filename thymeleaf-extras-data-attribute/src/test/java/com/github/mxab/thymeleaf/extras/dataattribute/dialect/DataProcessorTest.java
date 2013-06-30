package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Arguments.class, StandardExpressionProcessor.class })
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
		PowerMockito.mockStatic(StandardExpressionProcessor.class,
				new Answer<Object>() {

					@Override
					public Object answer(InvocationOnMock invocation)
							throws Throwable {

						return "bar";
					}
				});
		Arguments arguments = PowerMockito.mock(Arguments.class);
		dataProcessor.doProcess(arguments, processorMatchingContext, element);

		assertThat(element.hasAttribute("data:foo"), is(false));

		assertThat(element.getAttributeValue("data-foo"), equalTo("bar"));

	}

	@Test
	public void testProcessWhenPropertyIsMissing() throws Exception {
		Element element = new Element("div");
		element.setAttribute("data:foo", "${missing}");

		ProcessorMatchingContext processorMatchingContext = new ProcessorMatchingContext(
				new DataAttributeDialect(), "data");
		PowerMockito.mockStatic(StandardExpressionProcessor.class,
				new Answer<Object>() {

					@Override
					public Object answer(InvocationOnMock invocation)
							throws Throwable {

						return null;
					}
				});
		Arguments arguments = PowerMockito.mock(Arguments.class);
		dataProcessor.doProcess(arguments, processorMatchingContext, element);

		assertThat(element.hasAttribute("data:foo"), is(false));

		assertThat(element.getAttributeValue("data-foo"), equalTo(""));

	}

	@Test
	public void testPrecendence() {
		assertThat(dataProcessor.getPrecedence(), equalTo(1100));
	}

}
