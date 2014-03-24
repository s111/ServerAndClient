package controllers;

import models.Image;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import utils.HibernateUtil;

public class ImageRater extends Controller {
	public static Result rate(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();

		int rating = 0;

		try {
			rating = Integer.parseInt(requestData.get("value"));
		} catch (NumberFormatException exception) {
			return badRequest();
		}

		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		if (rating < 1 || rating > 5 || image == null)
			return badRequest();

		queryImage.rateImage(id, rating);

		return ok();
	}
}
