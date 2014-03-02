package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class TagModel extends Model {
	private static final long serialVersionUID = -5432412894736352438L;
	
	@Id
	public long id;
	
	@Required
	public String name;
}
