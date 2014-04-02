package controllers;

import java.io.IOException;
import java.util.List;

import json.objects.ImageFull;
import json.objects.ImageFullList;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import queryDB.QueryTag;
import utils.HibernateUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImageUpdateMultiple extends Controller {
	public static Result update() throws IOException {
		JsonNode jsonData = request().body().asJson();

		ObjectMapper mapper = new ObjectMapper();
		ImageFullList imageFullList = mapper.convertValue(jsonData,
				ImageFullList.class);

		QueryImage imageQueries = new QueryImage(
				HibernateUtil.getSessionFactory());
		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		for (ImageFull image : imageFullList.getImages()) {
			if (image.getId() == 0) {
				continue;
			}

			String description = image.getDescription();

			int rating = image.getRating();

			List<String> tags = image.getTags();

			if (description != null) {
				imageQueries.describeImage(image.getId(), description);
			}

			if (rating != 0) {
				imageQueries.rateImage(image.getId(), rating);
			}

			if (tags != null) {
				for (String tag : tags) {
					queryTag.tagImage(image.getId(), tag);
				}
			}
		}

		return ok();
	}
}
