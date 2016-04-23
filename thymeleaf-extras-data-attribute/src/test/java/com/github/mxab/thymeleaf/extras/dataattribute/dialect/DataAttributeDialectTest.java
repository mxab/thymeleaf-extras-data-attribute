package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

public class DataAttributeDialectTest {

	@Test
	public void testPrefix() {
		DataAttributeDialect dialect = new DataAttributeDialect();
		assertThat(dialect.getPrefix(), is(equalTo(DataAttributeDialect.PREFIX)));
	}

	@Test
	public void testProcessors() {
		DataAttributeDialect dialect = new DataAttributeDialect();
		assertThat("processors", dialect.getProcessors(dialect.getPrefix()), is(notNullValue()));
		assertThat(dialect.getProcessors(dialect.getPrefix()).size(), is(equalTo(2)));
	}
}
