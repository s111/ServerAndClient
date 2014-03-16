package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.Page;
import com.avaje.ebean.PagingList;

@Entity
public class ImageModel extends Model {
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

	public static ImageModel create(String filename) {
		ImageModel imageModel = new ImageModel(filename);
		imageModel.save();

		return imageModel;
	}

	public static ImageModel get(long id) {
		return find.byId(id);
	}

	public static ImageModel getFirst() {
		ImageModel imageModel = find.orderBy("id ASC").setMaxRows(1)
				.findUnique();

		return imageModel;
	}

	public static ImageModel getLast() {
		ImageModel imageModel = find.orderBy("id DESC").setMaxRows(1)
				.findUnique();

		return imageModel;
	}

	public ImageModel getNext() {
		ImageModel imageModel = find.where().gt("id", id).orderBy("id ASC")
				.setMaxRows(1).findUnique();

		return imageModel;
	}

	public ImageModel getPrevious() {
		ImageModel imageModel = find.where().lt("id", id).orderBy("id DESC")
				.setMaxRows(1).findUnique();

		return imageModel;
	}

	public static List<ImageModel> getAll() {
		List<ImageModel> all = find.orderBy("id ASC").findList();

		return all;
	}

	public static int getRowCount() {
		return find.all().size();
	}

	public static List<ImageModel> getPageList(int offset, int limit) {
		if (offset < 0 || limit < 0)
			return new ArrayList<ImageModel>();

		PagingList<ImageModel> imageModels = find.orderBy("id ASC")
				.findPagingList(limit);

		int page = 0;
		if (limit != 0) {
			page = offset / limit;
		}

		Page<ImageModel> imagesOnPage = imageModels.getPage(page);

		List<ImageModel> list = imagesOnPage.getList();

		return list;
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
}