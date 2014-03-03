package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class ThumbnailModel extends Model {
	private static final long serialVersionUID = 6264824175207421757L;
	
	public static final int X_SMALL = 0;
	public static final int SMALL = 1;
	public static final int MEDIUM = 2;
	public static final int LARGE = 3;
	public static final int X_LARGE = 4;
	

	@Id
	public long id;
	
	@ManyToOne
	public ImageModel image;
	
	@Required
	public String filename;
	
	@Required
	public int size;
	
	public ThumbnailModel(String filename, int size) {
		this.filename = filename;
		this.size = size;
	}
	
	public static ThumbnailModel create(String filename, int size) {
		ThumbnailModel thumbnailModel = new ThumbnailModel(filename, size);
		
		thumbnailModel.save();
		
		return thumbnailModel;
	}
}
