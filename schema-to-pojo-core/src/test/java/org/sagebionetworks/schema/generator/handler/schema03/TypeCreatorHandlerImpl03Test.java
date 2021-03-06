package org.sagebionetworks.schema.generator.handler.schema03;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sagebionetworks.schema.FORMAT;
import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDeclaration;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;

public class TypeCreatorHandlerImpl03Test {
	
	JCodeModel codeModel;
	JPackage _package;
	JType type;
	ObjectSchema schema;

	@Before
	public void before() throws JClassAlreadyExistsException,
			ClassNotFoundException {
		codeModel = new JCodeModel();
		_package = codeModel._package("org.sample");
//		sampleClass = codeModel._class("Sample");
		schema = new ObjectSchema();
		schema.setType(TYPE.OBJECT);
		// give it a name
		schema.setName("Sample");
		schema.setId("org.sample"+schema.getName());
	}
	
	@Test
	public void testCreateNewClass() throws ClassNotFoundException{
		// Add a title and description
		String title = "This is the title";
		String description = "Add a description";
		schema.setTitle(title);
		schema.setDescription(description);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		// Make sure we can call this twice with the same class
		JType second = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertEquals(clazz, second);
		assertTrue(clazz instanceof JDefinedClass);
		JDefinedClass sampleClass = (JDefinedClass)clazz;
		// Write to a string
		String classString = declareToString(sampleClass);
//		System.out.println(classString);
		assertTrue(classString.indexOf(title) > 0);
		assertTrue(classString.indexOf(description) > 0);
		assertTrue(classString.indexOf(TypeCreatorHandlerImpl03.AUTO_GENERATED_MESSAGE) > 0);
	}
	
	@Test
	public void testCreateNewInterface() throws ClassNotFoundException, JClassAlreadyExistsException{
		// Add a title and description
		String title = "This is the title";
		String description = "Add a description";
		schema.setTitle(title);
		schema.setDescription(description);
		schema.setType(TYPE.INTERFACE);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JDefinedClass parentInterface = _package._interface("ParentInterface");
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null,
				new JType[] { parentInterface });
		assertNotNull(clazz);
		// Make sure we can call this twice with the same class
		JType second = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null,
				new JType[] { parentInterface });
		assertEquals(clazz, second);
		assertTrue(clazz instanceof JDefinedClass);
		JDefinedClass sampleClass = (JDefinedClass)clazz;
		assertTrue(sampleClass.isInterface());
		JClass parent = sampleClass.getBaseClass(parentInterface);
		assertNotNull(parent);
		assertTrue(parent.isInterface());
		// Write to a string
		String classString = declareToString(sampleClass);
//		System.out.println(classString);
		assertTrue(classString.indexOf(title) > 0);
		assertTrue(classString.indexOf(description) > 0);
		assertTrue(classString.indexOf(TypeCreatorHandlerImpl03.AUTO_GENERATED_MESSAGE) > 0);
	}
	
	@Test
	public void testClassExtendsClass() throws ClassNotFoundException, JClassAlreadyExistsException {
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		JDefinedClass parentClass = _package._class("ParentClass");
		JType clazz = handler.handelCreateType(codeModel, schema, parentClass, null, null, null, null);
		assertNotNull(clazz);
		assertTrue(clazz instanceof JDefinedClass);
		JDefinedClass sampleClass = (JDefinedClass)clazz;
		String classString = declareToString(sampleClass);
//		System.out.println(classString);
		assertTrue(classString.indexOf("extends org.sample.ParentClass") > 0);
	}
	
	@Test
	public void testClassImplementsInterfances() throws ClassNotFoundException, JClassAlreadyExistsException{
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		JDefinedClass parentInternace = _package._interface("ParentInterface");
		JDefinedClass parentInternace2 = _package._interface("ParentInterface2");
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, new JType[] {
				parentInternace, parentInternace2 });
		assertNotNull(clazz);
		assertTrue(clazz instanceof JDefinedClass);
		JDefinedClass sampleClass = (JDefinedClass)clazz;
		String classString = declareToString(sampleClass);
//		System.out.println(classString);
		assertTrue(classString.indexOf("implements org.sagebionetworks.schema.adapter.JSONEntity, org.sample.ParentInterface, org.sample.ParentInterface2") > 0);
	}
	
	@Test
	public void testInterfanceExtendsNull()throws ClassNotFoundException, JClassAlreadyExistsException{
		schema.setType(TYPE.INTERFACE);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertTrue(clazz instanceof JDefinedClass);
		JDefinedClass sampleClass = (JDefinedClass)clazz;
		String classString = declareToString(sampleClass);
//		System.out.println(classString);
		assertTrue(classString.indexOf("public interface Sample") > 0);
		assertTrue(classString.indexOf("extends org.sagebionetworks.schema.adapter.JSONEntity") > 0);
	}
	
	@Test
	public void testInterfanceExtendsInterfance() throws ClassNotFoundException, JClassAlreadyExistsException{
		schema.setType(TYPE.INTERFACE);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		JDefinedClass parentInternace = _package._interface("ParentInterface");
		JDefinedClass parentInternace2 = _package._interface("ParentInterface2");
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, new JType[] {
				parentInternace, parentInternace2 });
		assertNotNull(clazz);
		assertTrue(clazz instanceof JDefinedClass);
		JDefinedClass sampleClass = (JDefinedClass)clazz;
		String classString = declareToString(sampleClass);
//		System.out.println(classString);
		assertTrue(classString.indexOf("public interface Sample") > 0);
		assertTrue(classString.indexOf("extends org.sagebionetworks.schema.adapter.JSONEntity, org.sample.ParentInterface, org.sample.ParentInterface2") > 0);
	}
	
	@Test
	public void testStringFormatedDateTime() throws ClassNotFoundException{
		// String formated as date-time
		schema.setType(TYPE.STRING);
		schema.setFormat(FORMAT.DATE_TIME);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertEquals(codeModel._ref(Date.class), clazz);
	}
	
	@Test
	public void testStringFormatedDate() throws ClassNotFoundException{
		// String formated as date
		schema.setType(TYPE.STRING);
		schema.setFormat(FORMAT.DATE);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertEquals(codeModel._ref(Date.class), clazz);
	}
	
	@Test
	public void testStringFormatedTime() throws ClassNotFoundException{
		// String formated as date
		schema.setType(TYPE.STRING);
		schema.setFormat(FORMAT.TIME);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertEquals(codeModel._ref(Date.class), clazz);
	}
	
	
	@Test
	public void testIntegerFormatedDateTime() throws ClassNotFoundException{
		// String formated as date-time
		schema.setType(TYPE.INTEGER);
		schema.setFormat(FORMAT.DATE_TIME);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertEquals(codeModel._ref(Date.class), clazz);
	}
	
	@Test
	public void testItegerFormatedDate() throws ClassNotFoundException{
		// String formated as date
		schema.setType(TYPE.INTEGER);
		schema.setFormat(FORMAT.DATE);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertEquals(codeModel._ref(Date.class), clazz);
	}
	
	@Test
	public void testIntegerFormatedTime() throws ClassNotFoundException{
		// String formated as date
		schema.setType(TYPE.STRING);
		schema.setFormat(FORMAT.TIME);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertEquals(codeModel._ref(Date.class), clazz);
	}
	
	@Test
	public void testIntegerFormatedUTC_MILLISEC() throws ClassNotFoundException{
		// String formated as date
		schema.setType(TYPE.INTEGER);
		schema.setFormat(FORMAT.UTC_MILLISEC);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertEquals(codeModel._ref(Date.class), clazz);
	}
	
	/**
	 * Enumerations must have a type of string.
	 * @throws ClassNotFoundException
	 */
	@Test (expected=IllegalArgumentException.class)
	public void testCreateEnumerationNotString() throws ClassNotFoundException{
		schema.setType(TYPE.BOOLEAN);
		schema.setEnum(new String[]{"one","two","three"});
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testCreateEnumerationNullName() throws ClassNotFoundException{
		schema.setType(TYPE.STRING);
		schema.setEnum(new String[]{"one","two","three"});
		schema.setName(null);
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
	}
	
	@Test
	public void testCreateEnumeration() throws ClassNotFoundException{
		String title = "This is the title";
		String description = "Add a description";
		schema.setTitle(title);
		schema.setDescription(description);
		schema.setType(TYPE.STRING);
		schema.setEnum(new String[]{"one","two","three"});
		schema.setName("SampleEnum");
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertTrue(clazz instanceof JDefinedClass);
		JDefinedClass sampleClass = (JDefinedClass)clazz;
		String classString = declareToString(sampleClass);
//		System.out.println(classString);
		assertTrue(classString.indexOf("one,") > 0);
		assertTrue(classString.indexOf("two,") > 0);
		assertTrue(classString.indexOf("three;") > 0);
		assertTrue(classString.indexOf(title) > 0);
		assertTrue(classString.indexOf(description) > 0);
		assertTrue(classString.indexOf(TypeCreatorHandlerImpl03.AUTO_GENERATED_MESSAGE) > 0);
	}

	@Test
	public void testEffectiveSchema() throws ClassNotFoundException{
		String title = "This is the title";
		String description = "Add a description";
		schema.setTitle(title);
		schema.setDescription(description);
		schema.setType(TYPE.OBJECT);
		schema.setName("Sample");
		schema.putProperty("someProperty", new  ObjectSchema(TYPE.STRING));
		TypeCreatorHandlerImpl03 handler = new TypeCreatorHandlerImpl03();
		// Create the class
		JType clazz = handler.handelCreateType(codeModel, schema, codeModel._ref(Object.class), null, null, null, null);
		assertNotNull(clazz);
		assertTrue(clazz instanceof JDefinedClass);
		JDefinedClass sampleClass = (JDefinedClass)clazz;
		String classString = declareToString(sampleClass);
		assertTrue(classString.indexOf("public final static java.lang.String EFFECTIVE_SCHEMA") > 0);
	}
	
	
	/**
	 * Helper to declare a model object to string.
	 * @param toDeclare
	 * @return
	 */
	public String declareToString(JDeclaration toDeclare){
		StringWriter writer = new StringWriter();
		JFormatter formatter = new JFormatter(writer);
		toDeclare.declare(formatter);
		return writer.toString();
	}

}
