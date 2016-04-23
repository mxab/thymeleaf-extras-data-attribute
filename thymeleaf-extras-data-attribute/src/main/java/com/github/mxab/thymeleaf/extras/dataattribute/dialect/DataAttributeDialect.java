package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

public class DataAttributeDialect extends AbstractProcessorDialect {

	public static final String NAMESPACE = "http://www.thymeleaf.org/extras/data";
	public static final String PREFIX = "data";

	public static final int PRECEDENCE = 1000;

	public DataAttributeDialect() {
		super(NAMESPACE, PREFIX, PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		HashSet<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
		processors.add(new DataProcessor(TemplateMode.HTML, dialectPrefix));
		return processors;
	}

}
