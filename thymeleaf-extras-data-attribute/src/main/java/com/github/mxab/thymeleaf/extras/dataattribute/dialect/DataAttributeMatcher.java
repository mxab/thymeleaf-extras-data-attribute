package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import java.util.Map;
import java.util.Set;

import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;

public class DataAttributeMatcher implements
		IProcessorMatcher<Element> {
	@Override
	public boolean matches(Node node, ProcessorMatchingContext context) {
		Element element = (Element) node;

		if (context.getDialect() instanceof DataAttributeDialect) {
			String dialectPrefix = context.getDialectPrefix();
			Map<String, Attribute> attributeMap = element.getAttributeMap();
			Set<String> keySet = attributeMap.keySet();
			for (String key : keySet) {
				if (key.startsWith(dialectPrefix)) {
					return true;
				}
			}
		}
		return false;

	}

	@Override
	public Class<? extends Element> appliesTo() {
		return Element.class;
	}
}