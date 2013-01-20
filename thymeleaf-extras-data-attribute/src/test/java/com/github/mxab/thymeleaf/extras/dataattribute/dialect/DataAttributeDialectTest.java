package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;

public class DataAttributeDialectTest {

	@Test
	public void testPrefix() {
		DataAttributeDialect dialect = new DataAttributeDialect();
		assertThat(dialect.getPrefix(), is(equalTo("data")));
	}

	@Test
	public void testLenient() {
		DataAttributeDialect dialect = new DataAttributeDialect();
		assertThat(dialect.isLenient(), equalTo(false));
	}

	@Test
	public void testProcessors() {
		DataAttributeDialect dialect = new DataAttributeDialect();
		assertThat("processors", dialect.getProcessors(), is(notNullValue()));
		assertThat(dialect.getProcessors().size(), is(equalTo(1)));
	}
}
