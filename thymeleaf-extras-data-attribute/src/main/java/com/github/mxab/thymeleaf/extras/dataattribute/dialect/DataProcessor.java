package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import org.attoparser.util.TextUtil;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeDefinition;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.element.IElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.processor.element.MatchingAttributeName;
import org.thymeleaf.processor.element.MatchingElementName;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.util.StandardProcessorUtils;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.EscapedAttributeUtils;
import org.unbescape.html.HtmlEscape;

public class DataProcessor extends AbstractProcessor implements IElementTagProcessor {

	public static final int PRECEDENCE = 1100;

	private final String dialectPrefix;
	private final MatchingAttributeName matchingAttributeName;

	public DataProcessor(TemplateMode templateMode, String dialectPrefix) {
		super(templateMode, PRECEDENCE);
		this.dialectPrefix = dialectPrefix;
		this.matchingAttributeName = MatchingAttributeName.forAllAttributesWithPrefix(templateMode, dialectPrefix);
	}

	@Override
	public MatchingElementName getMatchingElementName() {
		return null;
	}

	@Override
	public MatchingAttributeName getMatchingAttributeName() {
		return matchingAttributeName;
	}

	@Override
	public void process(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		for (IAttribute attribute : tag.getAllAttributes()) {
			AttributeDefinition attributeDefinition = attribute.getDefinition();
			AttributeName attributeName = attributeDefinition.getAttributeName();
			if (attributeName.isPrefixed() && TextUtil.equals(getTemplateMode().isCaseSensitive(), attributeName.getPrefix(), dialectPrefix)) {
				processDataAttribute(context, attribute, attributeDefinition, attributeName, structureHandler);
			}
		}
	}

	private void processDataAttribute(ITemplateContext context, IAttribute attribute, AttributeDefinition attributeDefinition, AttributeName attributeName, IElementTagStructureHandler structureHandler) {
		try {
			IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());
			String attributeValue = EscapedAttributeUtils.unescapeAttribute(context.getTemplateMode(), attribute.getValue());

			Object expressionResult;
			if (attributeValue != null) {
				IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
				expressionResult = expression.execute(context);
			} else {
				expressionResult = null;
			}

			String targetAttrCompleteName = String.format("data-%s", attributeName.getAttributeName());
			String newAttributeValue = HtmlEscape.escapeHtml4Xml(expressionResult == null ? null : expressionResult.toString());

			structureHandler.removeAttribute(targetAttrCompleteName);
			StandardProcessorUtils.replaceAttribute(structureHandler, attributeName, attributeDefinition, targetAttrCompleteName, (newAttributeValue == null ? "" : newAttributeValue));
		} catch (TemplateProcessingException e) {
			if (!e.hasTemplateName()) {
				e.setTemplateName(attribute.getTemplateName());
			}

			if (!e.hasLineAndCol()) {
				e.setLineAndCol(attribute.getLine(), attribute.getCol());
			}

			throw e;
		} catch (Exception e) {
			throw new TemplateProcessingException("Error during execution of processor '" + getClass().getName() + "'", attribute.getTemplateName(), attribute.getLine(), attribute.getCol(), e);
		}
	}
}
