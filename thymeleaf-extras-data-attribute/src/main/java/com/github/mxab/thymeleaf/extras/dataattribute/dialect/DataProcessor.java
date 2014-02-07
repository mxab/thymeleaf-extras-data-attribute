package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

public class DataProcessor extends AbstractProcessor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataProcessor.class);

	@Override
	public int getPrecedence() {
		return 1100;
	}

	@Override
	public IProcessorMatcher<? extends Node> getMatcher() {
		return new DataAttributeMatcher();
	}

	@Override
	protected ProcessorResult doProcess(Arguments arguments,
			ProcessorMatchingContext processorMatchingContext, Node node) {

		Map<String, Attribute> attributeMap;
		Element element = ((Element) node);
		attributeMap = element.getAttributeMap();
		String dialectPrefix = processorMatchingContext.getDialectPrefix();

		for (Attribute attribute : attributeMap.values()) {

			String dataAttrName = attribute.getUnprefixedNormalizedName();
			String attributeName = attribute.getNormalizedName();
			if (dialectPrefix.equals(attribute.getNormalizedPrefix())) {
				final String attributeValue = element
						.getAttributeValue(attributeName);

				final Object result = StandardExpressionProcessor
						.processExpression(arguments, attributeValue);
				if (result != null) {
					element.setAttribute(
							String.format("data-%s", dataAttrName),
							result.toString());
				}

				element.removeAttribute(attributeName);

			}

		}

		return ProcessorResult.ok();

	}
}
