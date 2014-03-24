package controllers;

import models.Image;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import utils.HibernateUtil;

public class ImageDescriber extends Controller {
	public static Result describe(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();

		String description = requestData.get("value");

		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		if (description == null || description.length() > 255 || image == null) {
			return badRequest();
		}

		queryImage.describeImage(id, description);

		return ok();
	}
}
