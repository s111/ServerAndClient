package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.google.common.base.Optional;

@Entity
public class ThumbnailModel extends Model {
	private static final long serialVersionUID = 6264824175207421757L;

	public static Finder<Long, ThumbnailModel> find = new Finder<>(Long.class,
			ThumbnailModel.class);

	public static final int X_SMALL = 0;
	public static final int SMALL = 1;
	public static final int MEDIUM = 2;
	public static final int LARGE = 3;
	public static final int X_LARGE = 4;
	public static final int COMPRESSED = 5;

	@Id
	public long id;

	@ManyToOne
	public ImageModel image;

	@Required
	public String filename;

	@Required
	public int size;

	private ThumbnailModel(ImageModel imageModel, String filename, int size) {
		this.image = imageModel;
		this.filename = filename;
		this.size = size;
	}

	public static ThumbnailModel create(ImageModel imageModel, String filename,
			int size) {
		ThumbnailModel thumbnailModel = new ThumbnailModel(imageModel,
				filename, size);

		thumbnailModel.save();

		return thumbnailModel;
	}

	public static Optional<ThumbnailModel> get(long id, int size) {
		ThumbnailModel thumbnailModel = find.where().eq("image.id", id)
				.eq("size", size).findUnique();

		return Optional.fromNullable(thumbnailModel);
	}
}
