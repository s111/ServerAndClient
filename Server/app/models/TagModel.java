package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class TagModel extends Model {
	private static final long serialVersionUID = -5432412894736352438L;

	@Id
	public String name;

	public static Finder<Long, TagModel> find = new Finder<>(Long.class,
			TagModel.class);

	public TagModel(String name) {
		this.name = name;
	}

	public static TagModel create(String name) {
		TagModel tagModel = new TagModel(name);
		tagModel.save();

		return tagModel;
	}
}
