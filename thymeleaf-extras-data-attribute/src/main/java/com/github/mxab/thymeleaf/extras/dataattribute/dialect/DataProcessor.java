package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import java.util.Map;

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

		for (Attribute attribute : attributeMap.values()) {
			String dataAttrName = attribute.getUnprefixedNormalizedName();
			String attributeName = attribute.getNormalizedName();

			final String attributeValue = element
					.getAttributeValue(attributeName);

			final Object result = StandardExpressionProcessor
					.processExpression(arguments, attributeValue);

			element.setAttribute(String.format("data-%s", dataAttrName),
					result.toString());
			element.removeAttribute(attributeName);

		}

		return ProcessorResult.ok();

	}
}
