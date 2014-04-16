package controllers;

import java.io.IOException;
import java.util.List;

import json.objects.ImageFull;
import json.objects.ImagesUpdate;
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
		ImagesUpdate imagesUpdate = mapper.convertValue(jsonData,
				ImagesUpdate.class);

		QueryImage imageQueries = new QueryImage(
				HibernateUtil.getSessionFactory());
		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		for (Long id : imagesUpdate.getIds()) {
			if (id == null || id == 0) {
				continue;
			}

			ImageFull imageFull = imagesUpdate.getMetadata();

			String description = imageFull.getDescription();

			int rating = imageFull.getRating();

			List<String> tags = imageFull.getTags();

			if (description != null) {
				imageQueries.describeImage(id, description);
			}

			if (rating != 0) {
				imageQueries.rateImage(id, rating);
			}

			if (tags != null) {
				for (String tag : tags) {
					queryTag.tagImage(id, tag);
				}
			}
		}

		return ok();
	}
}
