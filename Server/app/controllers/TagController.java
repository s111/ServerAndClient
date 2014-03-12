package controllers;

import java.util.ArrayList;
import java.util.List;

import jsonobjects.ImageList;
import jsonobjects.ImageShort;
import models.ImageModel;
import models.TagModel;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TagController extends Controller {
	public static Result getImages(String tag, int offset, int limit) {
		if (isNotWithinBoundaries(tag, offset, limit))
			return badRequest();

		List<ImageModel> imageModels = TagModel.get(tag).images;

		int lastOffset = calculateLastOffset(tag, limit);
		int previousOffset = calculatePreviousOffset(offset, limit);
		int nextOffset = calculateNextOffset(offset, limit);

		String next = getAbsoluteURLToImageListOrNull(nextOffset, limit);
		String href = getAbsoluteURLToImageList(offset, limit);
		String first = getAbsoluteURLToImageList(0, 25);
		String previous = getAbsoluteURLToImageListOrNull(previousOffset, limit);
		String last = getAbsoluteURLToImageList(lastOffset, limit);

		List<ImageShort> images = getImagesShort(imageModels);

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

		return ok(imageListNode);
	}

	private static List<ImageShort> getImagesShort(List<ImageModel> imageModels) {
		List<ImageShort> images = new ArrayList<>();

		for (ImageModel imageModel : imageModels) {
			ImageShort imageShort = new ImageShort();

			long id = imageModel.id;

			imageShort.setId(id);
			imageShort.setHref(routes.GetImage.info(id).absoluteURL(request()));

			images.add(imageShort);
		}

		return images;
	}

	private static int calculateLastOffset(String tag, int limit) {
		int numRows = TagModel.get(tag).images.size();
		int pages = numRows / limit;
		int lastOffset = pages * limit;

		return lastOffset;
	}

	private static int calculateNextOffset(int offset, int limit) {
		int numRows = ImageModel.getRowCount();

		int nextOffset = offset + limit;

		if (nextOffset > numRows - 1) {
			nextOffset = -1;
		}

		return nextOffset;
	}

	private static int calculatePreviousOffset(int offset, int limit) {
		int previousOffset = offset - limit;

		if (previousOffset < 0) {
			previousOffset = 0;
		}

		return previousOffset;
	}

	private static String getAbsoluteURLToImageListOrNull(int offset, int limit) {
		return offset >= 0 ? getAbsoluteURLToImageList(offset, limit) : null;
	}

	private static String getAbsoluteURLToImageList(int offset, int limit) {
		return routes.ImageController.getImages(offset, limit).absoluteURL(
				request());
	}

	private static boolean isNotWithinBoundaries(String tag, int offset,
			int limit) {
		int numRows = TagModel.get(tag).images.size();

		return (offset < 0 || limit < 0 || offset > numRows);
	}
}
