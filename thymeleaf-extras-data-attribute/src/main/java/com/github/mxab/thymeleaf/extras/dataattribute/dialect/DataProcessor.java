package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import org.attoparser.util.TextUtil;
import org.thymeleaf.context.ITemplateContext;
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
import org.thymeleaf.standard.expression.NoOpToken;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.EscapedAttributeUtils;
import org.unbescape.html.HtmlEscape;

public class DataProcessor extends AbstractProcessor implements IElementTagProcessor {

	public static final int PRECEDENCE = Integer.MAX_VALUE;

	private final String dialectPrefix;
	private final MatchingAttributeName matchingAttributeName;

	public DataProcessor(TemplateMode templateMode, String dialectPrefix) {
		super(templateMode, PRECEDENCE);
		this.dialectPrefix = dialectPrefix;
		matchingAttributeName = MatchingAttributeName.forAllAttributesWithPrefix(getTemplateMode(), dialectPrefix);
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
	public void process(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
		TemplateMode templateMode = getTemplateMode();
		IAttribute[] attributes = tag.getAllAttributes();

		for (IAttribute attribute : attributes) {
			AttributeName attributeName = attribute.getAttributeDefinition().getAttributeName();
			if (attributeName.isPrefixed()) {
				if (TextUtil.equals(templateMode.isCaseSensitive(), attributeName.getPrefix(), dialectPrefix)) {
					processDataAttribute(context, tag, attribute, structureHandler);
				}
			}
		}
	}

	private void processDataAttribute(ITemplateContext context, IProcessableElementTag tag, IAttribute attribute, IElementTagStructureHandler structureHandler) {
		try {
			AttributeName attributeName = attribute.getAttributeDefinition().getAttributeName();
			String attributeValue = EscapedAttributeUtils.unescapeAttribute(context.getTemplateMode(), attribute.getValue());

			String originalCompleteAttributeName = attribute.getAttributeCompleteName();
			String canonicalAttributeName = attributeName.getAttributeName();

			String newAttributeName = "data-";
			if (TextUtil.endsWith(true, originalCompleteAttributeName, canonicalAttributeName)) {
				newAttributeName += canonicalAttributeName;
			} else {
				newAttributeName += originalCompleteAttributeName.substring(originalCompleteAttributeName.length() - canonicalAttributeName.length());
			}

			IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());

			Object expressionResult;
			if (attributeValue != null) {
				IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
				expressionResult = expression.execute(context);
			} else {
				expressionResult = null;
			}

			if (expressionResult == NoOpToken.VALUE) {
				structureHandler.removeAttribute(attributeName);
				return;
			}

			String newAttributeValue = HtmlEscape.escapeHtml4Xml(expressionResult == null ? null : expressionResult.toString());
			if (newAttributeValue == null || newAttributeValue.length() == 0) {
				structureHandler.removeAttribute(newAttributeName);
				structureHandler.removeAttribute(attributeName);
			} else {
				structureHandler.replaceAttribute(attributeName, newAttributeName, newAttributeValue == null? "" : newAttributeValue);
			}
		} catch (TemplateProcessingException e) {
			if (!e.hasTemplateName()) {
				e.setTemplateName(tag.getTemplateName());
			}

			if (!e.hasLineAndCol()) {
				e.setLineAndCol(attribute.getLine(), attribute.getCol());
			}

			throw e;
		} catch (Exception e) {
			throw new TemplateProcessingException("Error during execution of processor '" + getClass().getName() + "'", tag.getTemplateName(), attribute.getLine(), attribute.getCol(), e);
		}
	}

}
