package generators;

import models.ImageModel;
import play.mvc.Http.Request;

import com.google.common.base.Optional;

import controllers.routes;

public class ImageInfoURLGenerator {
	private Request request;

	public ImageInfoURLGenerator(Request request) {
		this.request = request;
	}

	public String getURL(ImageModel imageModel) {
		return getImageInfoURL(imageModel.id);
	}

	public String getNextURL(ImageModel imageModel) {
		Optional<ImageModel> next = imageModel.getNext();

		if (!next.isPresent()) {
			return null;
		}

		return getImageInfoURL(next.get().id);
	}

	public String getPreviousURL(ImageModel imageModel) {
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
