package controllers;

import models.Image;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import queryDB.QueryTag;
import utils.HibernateUtil;

public class ImageTagger extends Controller {
	public static Result tag(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();

		String tagData = requestData.get("value");

		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		if (tagData == null || image == null)
			return badRequest();

		String[] tags = tagData.split(",");

		QueryTag queryTag = new QueryTag(HibernateUtil.getSessionFactory());

		for (String tag : tags) {
			queryTag.tagImage(id, tag);
		}

		return ok();
	}
}
