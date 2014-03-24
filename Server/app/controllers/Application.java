package controllers;

import java.util.List;

import models.Image;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import utils.HibernateUtil;
import views.html.index;

public class Application extends Controller {
	public static Result index() {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		List<Image> images = queryImage.getImages();

		return ok(index.render(images));
	}
}
