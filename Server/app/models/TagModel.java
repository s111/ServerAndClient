package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

import com.google.common.base.Optional;

@Entity
public class TagModel extends Model {
	private static final long serialVersionUID = -5432412894736352438L;

	@Id
	public String name;

	@ManyToMany
	public List<ImageModel> images = new ArrayList<>();

	public static Finder<String, TagModel> find = new Finder<>(String.class,
			TagModel.class);

	public TagModel(String name) {
		this.name = name;
	}

	public static TagModel create(String name) {
		TagModel tagModel = new TagModel(name);
		tagModel.save();

		return tagModel;
	}

	public static Optional<TagModel> get(String name) {
		TagModel tagModel = find.where().eq("name", name).findUnique();

		return Optional.fromNullable(tagModel);
	}

	public static List<TagModel> getAll() {
		return find.all();
	}
}
