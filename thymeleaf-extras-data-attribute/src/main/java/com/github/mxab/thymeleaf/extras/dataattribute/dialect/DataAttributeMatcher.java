package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import java.util.Collection;
import java.util.Map;

import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.NestableAttributeHolderNode;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;

public class DataAttributeMatcher implements
		IProcessorMatcher<NestableAttributeHolderNode> {
	@Override
	public boolean matches(Node node, ProcessorMatchingContext context) {
		NestableAttributeHolderNode element = (NestableAttributeHolderNode) node;

		if (context.getDialect() instanceof DataAttributeDialect) {
			String dialectPrefix = context.getDialectPrefix();
			Map<String, Attribute> attributeMap = element.getAttributeMap();
			Collection<Attribute> values = attributeMap.values();
			for (Attribute attribute : values) {
				String prefixFromAttributeName = Attribute
						.getPrefixFromAttributeName(attribute
								.getNormalizedName());
				if (dialectPrefix.equals(prefixFromAttributeName)) {
					return true;
				}
			}

		}
		return false;

	}

	@Override
	public Class<? extends NestableAttributeHolderNode> appliesTo() {
		return NestableAttributeHolderNode.class;
	}
}