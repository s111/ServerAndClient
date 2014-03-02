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

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.Page;
import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	public List<TagModel> tags = new ArrayList<>();
	
	@OneToMany(mappedBy = "image")
	public Map<Integer, ThumbnailModel> thumbnails = new HashMap<>();

	public ImageModel(String filename) {
		this.filename = filename;
	}
	
	public void tag(TagModel tagModel) {
		if (tags.contains(tagModel)) return;
		
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

	public static ImageModel getNext(long id) {
		List<ImageModel> imageModels = find.where().gt("id", id).findList();

		if (imageModels.size() == 0) {
			return null;
		}

		return imageModels.get(0);
	}

	public static ImageModel getPrevious(long id) {
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
}
