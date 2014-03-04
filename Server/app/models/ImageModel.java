package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import jsonserializers.TagSerializer;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.libs.Json;
import play.mvc.Http.Request;

import com.avaje.ebean.Page;
import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.routes;

@Entity
public class ImageModel extends Model {
	private static final long serialVersionUID = 6508928253062924228L;

	public static Finder<Long, ImageModel> find = new Finder<>(Long.class,
			ImageModel.class);

	@Id
	public long id;
	public int rating;

	@Required
	@JsonIgnore
	public String filename;
	public String description;

	@ManyToMany(mappedBy = "images")
	@JsonSerialize(using = TagSerializer.class)
	public List<TagModel> tags = new ArrayList<>();

	@OneToMany(mappedBy = "image")
	@JsonIgnore
	public Map<Integer, ThumbnailModel> thumbnails = new HashMap<>();

	public ImageModel(String filename) {
		this.filename = filename;
	}

	public void setDescription(String description) {
		this.description = description;

		save();
	}

	public void setRating(int rating) {
		this.rating = rating;

		save();
	}

	public void tag(TagModel tagModel) {
		if (tags.contains(tagModel))
			return;

		tags.add(tagModel);

		tagModel.images.add(this);

		save();
	}

	public static ImageModel create(String filename) {
		ImageModel imageModel = new ImageModel(filename);
		imageModel.save();

		return imageModel;
	}

	public static ImageModel get(long id) {
		return find.byId(id);
	}

	public static ImageModel getFirst() {
		List<ImageModel> imageModels = find.all();

		if (imageModels.size() == 0) {
			return null;
		}

		return imageModels.get(0);
	}

	public static ImageModel getLast() {
		List<ImageModel> imageModels = find.all();

		if (imageModels.size() == 0) {
			return null;
		}

		Collections.reverse(imageModels);

		return imageModels.get(0);
	}

	@JsonIgnore
	public ImageModel getNext() {
		List<ImageModel> imageModels = find.where().gt("id", id).findList();

		if (imageModels.size() == 0) {
			return null;
		}

		return imageModels.get(0);
	}

	@JsonIgnore
	public ImageModel getPrevious() {
		List<ImageModel> imageModels = find.where().lt("id", id).findList();

		if (imageModels.size() == 0) {
			return null;
		}

		Collections.reverse(imageModels);

		return imageModels.get(0);
	}

	public static List<ImageModel> getAll() {
		return find.all();
	}

	public static int getRowCount() {
		return find.all().size();
	}

	public static List<ImageModel> getPageList(int offset, int limit) {
		if (offset < 0 || limit < 0)
			return new ArrayList<ImageModel>();

		PagingList<ImageModel> imageModels = find.findPagingList(limit);

		int page = offset / limit;

		Page<ImageModel> imagesOnPage = imageModels.getPage(page);

		return imagesOnPage.getList();
	}

	public void addThumbnail(ThumbnailModel thumbnailModel) {
		thumbnails.put(thumbnailModel.size, thumbnailModel);

		thumbnailModel.image = this;

		save();
	}

	public static ArrayNode generateImageArrayJSON(Request request, List<ImageModel> imageModels) {
		ObjectMapper mapper = new ObjectMapper();

		ArrayNode images = mapper.convertValue(imageModels, ArrayNode.class);

		for (JsonNode image : images) {
			long id = image.get("id").asLong();

			((ObjectNode) image).removeAll();
			((ObjectNode) image).put("id", id);
			((ObjectNode) image).put("href", routes.GetImage.info(id)
					.absoluteURL(request));
		}

		return images;
	}

	public ObjectNode generateImageInfoJSON(Request request) {
		ObjectMapper mapper = new ObjectMapper();

		ObjectNode rootNode = Json.newObject();
		rootNode.put("href", routes.GetImage.info(id).absoluteURL(request));

		ImageModel nextImageModel = getNext();
		ImageModel previousImageModel = getPrevious();
		ImageModel firstImageModel = getFirst();
		ImageModel lastImageModel = getLast();

		long nextId = -1;
		long previousId = -1;
		long firstId = -1;
		long lastId = -1;

		if (nextImageModel != null) {
			nextId = nextImageModel.id;
		}

		if (previousImageModel != null) {
			previousId = previousImageModel.id;
		}

		if (firstImageModel != null) {
			firstId = firstImageModel.id;
		}

		if (lastImageModel != null) {
			lastId = lastImageModel.id;
		}

		rootNode.put("next", getAbsoluteURLToImageOrNull(request, nextId));
		rootNode.put("previous",
				getAbsoluteURLToImageOrNull(request, previousId));
		rootNode.put("first", getAbsoluteURLToImageOrNull(request, firstId));
		rootNode.put("last", getAbsoluteURLToImageOrNull(request, lastId));

		ObjectNode image = mapper.convertValue(this, ObjectNode.class);

		rootNode.put("image", image);

		return rootNode;
	}

	private static String getAbsoluteURLToImageOrNull(Request request, long id) {
		return id > 0 ? routes.GetImage.info(id).absoluteURL(request) : null;
	}
}
