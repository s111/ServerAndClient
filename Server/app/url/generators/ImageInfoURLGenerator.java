package url.generators;

import models.Image;
import play.mvc.Http.Request;
import queryDB.QueryImage;
import utils.HibernateUtil;
import controllers.routes;

public class ImageInfoURLGenerator {
	private Request request;

	public ImageInfoURLGenerator(Request request) {
		this.request = request;
	}

	public String getURL(long id) {
		return getImageInfoURL(id);
	}

	public String getNextURL(long id) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());
		Image next = queryImage.getNextImage(id);

		if (next == null) {
			return null;
		}

		return getImageInfoURL(next.getId());
	}

	public String getPreviousURL(long id) {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());
		Image previous = queryImage.getPreviousImage(id);

		if (previous == null) {
			return null;
		}

		return getImageInfoURL(previous.getId());
	}

	public String getFirstURL() {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image first = queryImage.getFirstImage();

		if (first != null) {
			return getImageInfoURL(first.getId());
		}

		return null;
	}

	public String getLastURL() {
		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image last = queryImage.getLastImage();

		if (last != null) {
			return getImageInfoURL(last.getId());
		}

		return null;
	}

	private String getImageInfoURL(long id) {
		String url = routes.GetImage.info(id).absoluteURL(request);

		return url;
	}
}
