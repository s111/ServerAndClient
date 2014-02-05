package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class ImageModel extends Model {
	@Id
	public Long id;

	@Required
	public String filename;

	public int rating;

	@DateTime(pattern = "dd/MM/yyyy HH:mm:ss")
	public Date added;

	@DateTime(pattern = "dd/MM/yyyy HH:mm:ss")
	public Date modified;

	public static Finder<Long, ImageModel> find = new Finder<>(Long.class,
			ImageModel.class);

	public ImageModel(String filename) {
		this.filename = filename;

		added = new Date();
		modified = added;
	}

	public static ImageModel create(String filename) {
		ImageModel image = new ImageModel(filename);
		image.save();

		return image;
	}

	public static List<ImageModel> getAll() {
		return find.all();
	}

	public static ImageModel getImage(Long imageId) {
		return find.byId(imageId);
	}
}
