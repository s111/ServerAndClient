package com.github.groupa.client.jsonobjects;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ImagesUpdateTest {
	@Test
	public void convert_imagesUpdate_to_json() {
		List<String> tags = new ArrayList<>();
		tags.add("tag1");
		tags.add("tag2");

		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);

		ImagesUpdate imagesUpdate = new ImagesUpdate();
		imagesUpdate.setIds(ids);
		imagesUpdate.setMetadata(new ImageFull());

		ImageFull imageFull = imagesUpdate.getMetadata();

		imageFull.setDescription("asd");
		imageFull.setRating(5);
		imageFull.setTags(tags);
		Gson gson = new Gson();

		String json = gson.toJson(imagesUpdate);

		JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

		assertEquals(
				"{\"ids\":[1,2],\"metadata\":{\"id\":0,\"rating\":5,\"description\":\"asd\",\"tags\":[\"tag1\",\"tag2\"]}}",
				jsonObject.toString());
	}
}
