package jsonserializers;

import java.io.IOException;
import java.util.List;

import models.TagModel;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TagSerializer extends JsonSerializer<Object> {
	@SuppressWarnings("unchecked")
	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		List<TagModel> tags = (List<TagModel>) value;

		if (tags.isEmpty()) {
			return;
		}

		jgen.writeStartArray();

		for (TagModel tagModel : tags) {
			jgen.writeString(tagModel.name);
		}

		jgen.writeEndArray();
	}

}
