package generators;

import models.ImageModel;
import play.mvc.Http.Request;
import controllers.routes;

public class AbsoluteURLGenerator {
	private ImageModel imageModel;

	private Request request;

	public AbsoluteURLGenerator(ImageModel imageModel, Request request) {
		this.imageModel = imageModel;
		this.request = request;
	}

	public String getURL() {
		return getImageInfoURL(imageModel.id);
	}

	public String getNextURL() {
		ImageModel next = imageModel.getNext();

		if (next == null) {
			return null;
		}

		if (next.id >= ImageModel.getLast().id) {
			return null;
		}

		return getImageInfoURL(next.id);
	}

	public String getPreviousURL() {
		ImageModel previous = imageModel.getPrevious();

		if (previous == null) {
			return null;
		}

		return getImageInfoURL(previous.id);
	}

	public String getFirstURL() {
		ImageModel first = ImageModel.getFirst();

		return getImageInfoURL(first.id);
	}

	public String getLastURL() {
		ImageModel last = ImageModel.getLast();

		return getImageInfoURL(last.id);
	}

	private String getImageInfoURL(long id) {
		String url = routes.GetImage.info(id).absoluteURL(request);

		return url;
	}
}
