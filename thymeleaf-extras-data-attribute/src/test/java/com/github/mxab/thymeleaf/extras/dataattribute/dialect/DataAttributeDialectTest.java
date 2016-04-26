package com.github.mxab.thymeleaf.extras.dataattribute.dialect;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;

public class DataAttributeDialectTest {

	private static final TestExecutor executor = new TestExecutor();

	@BeforeClass
	public static void configureSequence() {
		executor.setDialects(Arrays.asList(new StandardDialect(),
				new DataAttributeDialect()));
	}

	@Before
	public void configureTest() {
		executor.reset();
	}

	@Test
	public void testExisting() {
		executor.execute("classpath:existing.thtest");
		Assert.assertTrue(executor.isAllOK());
	}

	@Test
	public void testMessage() {
		executor.execute("classpath:message.thtest");
		Assert.assertTrue(executor.isAllOK());
	}

	@Test
	public void testMissing() {
		executor.execute("classpath:missing.thtest");
		Assert.assertTrue(executor.isAllOK());
	}

	@Test
	public void testNoOp() {
		executor.execute("classpath:noop.thtest");
		Assert.assertTrue(executor.isAllOK());
	}

	@Test
	public void testSimple() {
		executor.execute("classpath:simple.thtest");
		Assert.assertTrue(executor.isAllOK());
	}

}
