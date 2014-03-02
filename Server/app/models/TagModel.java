package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.ebean.Model;

@Entity
public class TagModel extends Model {
	private static final long serialVersionUID = -5432412894736352438L;

	@Id
	public String name;
	
	@ManyToMany
	@JsonIgnore
	public List<ImageModel> images = new ArrayList<>();

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

	public static TagModel get(String name) {
		TagModel tagModel = find.where().eq("name", name).findUnique();
		
		if (tagModel == null) tagModel = create(name);
		
		return tagModel;
	}

	public static List<TagModel> getAll() {
		return find.all();
	}
}
