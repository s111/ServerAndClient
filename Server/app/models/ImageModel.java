package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ImageModel extends Model {
	private static final long serialVersionUID = 6508928253062924228L;

	public static Finder<Long, ImageModel> find = new Finder<>(Long.class,
			ImageModel.class);

	@Id
	public long id;

	@Required
	@JsonIgnore
	public String filename;

	public ImageModel(String filename) {
		this.filename = filename;
	}

	public static ImageModel create(String filename) {
		ImageModel imageModel = new ImageModel(filename);
		imageModel.save();

		return imageModel;
	}

	public static ImageModel getImageModel(long id) {
		return find.byId(id);
	}

	public static List<ImageModel> getAll() {
		return find.all();
	}
	
	public static int getRowCount() {
		return find.all().size();
	}
	
	public static List<ImageModel> getSubList(int offset, int limit) {
		if (offset < 0 || limit < 0)
			return new ArrayList<ImageModel>();
		
		List<ImageModel> imageModels = find.all();
		
		int size = imageModels.size();
		
		int newLimit = offset + limit;
		
		if (newLimit >= size) {
			newLimit = size - 1;
		}
		
		return find.all().subList(offset, newLimit);
	}
}
