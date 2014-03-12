package collection;

import java.util.ArrayList;
import java.util.List;

import jsonobjects.ImageList;
import jsonobjects.ImageShort;
import models.ImageModel;
import play.mvc.Http.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.routes;

public class ImageModelList {
	List<ImageModel> imageModels;

	Request request;

	int offset;
	int limit;

	public ImageModelList(Request request, int offset, int limit) {
		this.request = request;
		this.offset = offset;
		this.limit = limit;

		imageModels = ImageModel.getPageList(offset, limit);
	}

	public JsonNode generateJSON() {
		int lastOffset = calculateLastOffset();
		int previousOffset = calculatePreviousOffset();
		int nextOffset = calculateNextOffset();

		String next = getAbsoluteURLToImageListOrNull(nextOffset);
		String href = getAbsoluteURLToImageList(offset);
		String first = getAbsoluteURLToImageList(0);
		String previous = getAbsoluteURLToImageListOrNull(previousOffset);
		String last = getAbsoluteURLToImageList(lastOffset);

		List<ImageShort> images = new ArrayList<>();

		for (ImageModel imageModel : imageModels) {
			ImageShort imageShort = new ImageShort();

			long id = imageModel.id;

			imageShort.setId(id);
			imageShort.setHref(routes.GetImage.info(id).absoluteURL(request));

			images.add(imageShort);
		}

		ImageList imageList = new ImageList();
		imageList.setHref(href);
		imageList.setOffset(offset);
		imageList.setLimit(limit);
		imageList.setNext(next);
		imageList.setPrevious(previous);
		imageList.setFirst(first);
		imageList.setLast(last);
		imageList.setImages(images);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode imageListNode = mapper.convertValue(imageList, JsonNode.class);

		return imageListNode;
	}

	private int calculateLastOffset() {
		int numRows = ImageModel.getRowCount();
		int pages = 0;

		if (pages != 0) {
			pages = numRows / limit;
		}

		int lastOffset = pages * limit;

		return lastOffset;
	}

	private int calculateNextOffset() {
		int numRows = ImageModel.getRowCount();

		int nextOffset = offset + limit;

		if (nextOffset > numRows - 1) {
			nextOffset = -1;
		}

		return nextOffset;
	}

	private int calculatePreviousOffset() {
		int previousOffset = offset - limit;

		if (previousOffset < 0) {
			previousOffset = 0;
		}

		return previousOffset;
	}

	private String getAbsoluteURLToImageListOrNull(int offset) {
		return offset >= 0 ? getAbsoluteURLToImageList(offset) : null;
	}

	private String getAbsoluteURLToImageList(int offset) {
		return routes.ImageController.getImages(offset, limit).absoluteURL(
				request);
	}
}
