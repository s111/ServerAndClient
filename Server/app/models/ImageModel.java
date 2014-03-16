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

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.Page;
import com.avaje.ebean.PagingList;

@Entity
public class ImageModel extends Model implements Comparable<ImageModel> {
	private static final long serialVersionUID = 6508928253062924228L;

	public static Finder<Long, ImageModel> find = new Finder<>(Long.class,
			ImageModel.class);

	@Id
	public long id;
	public int rating;

	@Required
	public String filename;
	public String description;

	@ManyToMany(mappedBy = "images")
	public List<TagModel> tags = new ArrayList<>();

	@OneToMany(mappedBy = "image")
	public Map<Integer, ThumbnailModel> thumbnails = new HashMap<>();

	public ImageModel(String filename) {
		this.filename = filename;
	}

	@Override
	public int compareTo(ImageModel imageModel) {
		if (imageModel.id < id) {
			return 1;
		} else if (imageModel.id > id) {
			return -1;
		}

		return 0;
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

		Logger.debug("Added Image to database. {id=" + imageModel.id + "}");

		return imageModel;
	}

	public static ImageModel get(long id) {
		return find.byId(id);
	}

	public static ImageModel getFirst() {
		List<ImageModel> imageModels = getAll();

		if (imageModels.size() == 0) {
			return null;
		}

		return imageModels.get(0);
	}

	public static ImageModel getLast() {
		List<ImageModel> imageModels = getAll();

		if (imageModels.size() == 0) {
			return null;
		}

		Collections.reverse(imageModels);

		return imageModels.get(0);
	}

	public ImageModel getNext() {
		List<ImageModel> imageModels = getAll();

		int size = imageModels.size();
		int index = imageModels.indexOf(this) + 1;

		if (size == 0 || index >= size) {
			return null;
		}

		return imageModels.get(index);
	}

	public ImageModel getPrevious() {
		List<ImageModel> imageModels = getAll();

		int size = imageModels.size();
		int index = imageModels.indexOf(this) - 1;

		if (size == 0 || index < 0) {
			return null;
		}

		return imageModels.get(index);
	}

	public static List<ImageModel> getAll() {
		List<ImageModel> all = find.all();

		Collections.sort(all);

		return all;
	}

	public static int getRowCount() {
		return find.all().size();
	}

	public static List<ImageModel> getPageList(int offset, int limit) {
		if (offset < 0 || limit < 0)
			return new ArrayList<ImageModel>();

		PagingList<ImageModel> imageModels = find.findPagingList(limit);

		int page = 0;
		if (limit != 0) {
			page = offset / limit;
		}

		Page<ImageModel> imagesOnPage = imageModels.getPage(page);

		List<ImageModel> list = imagesOnPage.getList();

		Collections.sort(list);

		return list;
	}

	public void addThumbnail(ThumbnailModel thumbnailModel) {
		thumbnails.put(thumbnailModel.size, thumbnailModel);

		thumbnailModel.image = this;

		save();
	}
}
