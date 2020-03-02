package org.sagebionetworks.schema.pets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.EntityFactory;
import org.sagebionetworks.schema.adapter.org.json.JSONObjectAdapterImpl;

public class PetsTest {

	@Test
	public void testPet() throws IOException, JSONObjectAdapterException {
		ObjectSchema schema = loadSchema("Pet.json");
		EntityFactory.createJSONStringForEntity(schema);
		System.out.println(schema);
	}

	public static ObjectSchema loadSchema(String fileName) throws IOException, JSONObjectAdapterException {
		try (InputStream in = PetsTest.class.getClassLoader().getResourceAsStream(fileName)) {
			if (in == null) {
				throw new IllegalArgumentException("Cannot find: " + fileName + " on the classpath");
			}
			String jsonString = IOUtils.toString(in, StandardCharsets.UTF_8);
			JSONObjectAdapterImpl adapter = new JSONObjectAdapterImpl(jsonString);
			return new ObjectSchema(adapter);
		}
	}
}
