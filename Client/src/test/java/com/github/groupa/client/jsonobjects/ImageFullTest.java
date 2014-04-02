package com.github.groupa.client.jsonobjects;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ImageFullTest {
	@Test
	public void convert_imageFullList_to_json() {
		List<String> tags = new ArrayList<>();
		tags.add("tag1");
		tags.add("tag2");

		List<Long> ids = new ArrayList<>();
		ids.add(1L);
		ids.add(2L);

		ImageFull imageFull = new ImageFull();
		imageFull.setDescription("asd");
		imageFull.setRating(5);
		imageFull.setIds(ids);
		imageFull.setTags(tags);
		Gson gson = new Gson();

		String json = gson.toJson(imageFull);

		JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
		// The id is not used when updating multiple images so it can be removed
		jsonObject.remove("id");

		assertEquals(
				"{\"ids\":[1,2],\"rating\":5,\"description\":\"asd\",\"tags\":[\"tag1\",\"tag2\"]}",
				jsonObject.toString());
	}
}
