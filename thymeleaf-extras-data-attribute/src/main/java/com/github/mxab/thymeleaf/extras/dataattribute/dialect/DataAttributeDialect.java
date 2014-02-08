package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

public class DataAttributeDialect extends AbstractDialect {

	@Override
	public String getPrefix() {
		return "data";
	}

	@Override
	public Set<IProcessor> getProcessors() {
		HashSet<IProcessor> processors = new HashSet<IProcessor>();
		processors.add(new DataProcessor());
		return processors;
	}

}
