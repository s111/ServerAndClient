package generators;

import models.ImageModel;
import play.mvc.Http.Request;

import com.google.common.base.Optional;

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
		Optional<ImageModel> next = imageModel.getNext();
		Optional<ImageModel> last = ImageModel.getLast();

		if (!next.isPresent() || !last.isPresent()
				|| next.get().id == last.get().id) {
			return null;
		}

		return getImageInfoURL(next.get().id);
	}

	public String getPreviousURL() {
		Optional<ImageModel> previous = imageModel.getPrevious();

		if (!previous.isPresent()) {
			return null;
		}

		return getImageInfoURL(previous.get().id);
	}

	public String getFirstURL() {
		Optional<ImageModel> first = ImageModel.getFirst();

		if (first.isPresent()) {
			return getImageInfoURL(first.get().id);
		}

		return null;
	}

	public String getLastURL() {
		Optional<ImageModel> last = ImageModel.getLast();

		if (!last.isPresent()) {
			return null;
		}

		return getImageInfoURL(last.get().id);
	}

	private String getImageInfoURL(long id) {
		String url = routes.GetImage.info(id).absoluteURL(request);

		return url;
	}
}
