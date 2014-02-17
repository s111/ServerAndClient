package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class ImageModel extends Model {
	private static final long serialVersionUID = 6508928253062924228L;
	
	public static Finder<Long, ImageModel> find = new Finder<>(Long.class, ImageModel.class);

	@Id
	public long id;
	
	public String filename;
	
	public ImageModel(String filename) {
		
	}
}
