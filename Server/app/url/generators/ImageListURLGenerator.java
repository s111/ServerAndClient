package url.generators;

import play.mvc.Http.Request;
import queryDB.QueryImage;
import utils.HibernateUtil;
import controllers.routes;

public class ImageListURLGenerator {
	private int offset;
	private int nextOffset;
	private int previousOffset;
	private int lastOffset;
	private int limit;
	private int rowCount;

	private Request request;

	public ImageListURLGenerator(int offset, int limit, Request request) {
		this.offset = offset;
		this.limit = limit;
		this.request = request;

		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		rowCount = queryImage.getImages().size();

		calculateOffsets();
	}

	public int getOffset() {
		return offset;
	}

	public int getLimit() {
		return limit;
	}

	public String getURL() {
		return getImageListURL(offset, limit);
	}

	public String getNextURL() {
		return getImageListURL(nextOffset, limit);
	}

	public String getPreviousURL() {
		return getImageListURL(previousOffset, limit);
	}

	public String getFirstURL() {
		return getImageListURL(0, limit);
	}

	public String getLastURL() {
		return getImageListURL(lastOffset, limit);
	}

	private String getImageListURL(int offset, int limit) {
		String url = routes.ImageController.getImages(offset, limit)
				.absoluteURL(request);

		return url;
	}

	private void calculateOffsets() {
		/* last offset must be calculated first */
		calculateLastOffset();
		calculateNextOffset();
		calculatePreviousOffset();
	}

	private void calculateLastOffset() {
		int pages = 0;

		if (limit != 0) {
			pages = rowCount / limit;
		}

		int lastOffset = pages * limit;

		this.lastOffset = lastOffset;
	}

	private void calculateNextOffset() {
		int nextOffset = offset + limit;

		if (nextOffset > rowCount - 1) {
			nextOffset = lastOffset;
		}

		this.nextOffset = nextOffset;
	}

	private void calculatePreviousOffset() {
		int previousOffset = offset - limit;

		if (previousOffset < 0) {
			previousOffset = 0;
		}

		this.previousOffset = previousOffset;
	}
}