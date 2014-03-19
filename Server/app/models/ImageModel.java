package models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import metadata.ExifWriter;
import metadata.PrepareImageModel;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;

import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.google.common.base.Optional;

@Entity
public class ImageModel extends Model {
	private static final long serialVersionUID = 6508928253062924228L;

	public static Finder<Long, ImageModel> find = new Finder<>(Long.class,
			ImageModel.class);

	@Id
	public long id;

	public int rating;

	/**
	 * Should be incremented each time the model info is changed (rating,
	 * description, tags), before save() is called
	 */
	@Column(columnDefinition = "integer default 0")
	public Integer infoChangeCount = 0;

	/**
	 * Should be incremented each time a model is created or deleted
	 */
	@Transient
	public static Integer listChangeCount = 0;

	/**
	 * Should be incremented each time there is changes to the actual image
	 */
	@Column(columnDefinition = "integer default 0")
	public Integer imageChangeCount = 0;

	@Required
	public String filename;
	public String description;

	@ManyToMany(mappedBy = "images")
	public List<TagModel> tags = new ArrayList<>();

	@OneToMany(mappedBy = "image")
	public Map<Integer, ThumbnailModel> thumbnails = new HashMap<>();

	private ImageModel(String filename) {
		this.filename = filename;
	}

	public static ImageModel create(String filename) {
		ImageModel imageModel = new ImageModel(filename);
		imageModel.description = "";
		imageModel.save();

		ImageModel.listChangeCount++;

		return imageModel;
	}

	public static int getRowCount() {
		return find.all().size();
	}

	public static Optional<ImageModel> get(long id) {
		return Optional.fromNullable(find.byId(id));
	}

	public static Optional<ImageModel> getFirst() {
		ImageModel imageModel = find.orderBy("id ASC").setMaxRows(1)
				.findUnique();

		return Optional.fromNullable(imageModel);
	}

	public static Optional<ImageModel> getLast() {
		ImageModel imageModel = find.orderBy("id DESC").setMaxRows(1)
				.findUnique();

		return Optional.fromNullable(imageModel);
	}

	public static List<ImageModel> getAll() {
		List<ImageModel> imageModels = find.orderBy("id ASC").findList();

		return imageModels;
	}

	public static List<ImageModel> getList(int offset, int limit) {
		List<ImageModel> imageModels = find.orderBy("id ASC")
				.setFirstRow(offset).setMaxRows(limit).findList();

		return imageModels;
	}

	public Optional<ImageModel> getNext() {
		ImageModel imageModel = find.where().gt("id", id).orderBy("id ASC")
				.setMaxRows(1).findUnique();

		return Optional.fromNullable(imageModel);
	}

	public Optional<ImageModel> getPrevious() {
		ImageModel imageModel = find.where().lt("id", id).orderBy("id DESC")
				.setMaxRows(1).findUnique();

		return Optional.fromNullable(imageModel);
	}

	public void setDescription(String description) {
		if (this.description.equals(description)) {
			return;
		}

		this.description = description;

		infoChangeCount++;

		save();
		writeMetadata();
	}

	public void setRating(int rating) {
		if (this.rating == rating) {
			return;
		}

		this.rating = rating;

		infoChangeCount++;

		save();
		writeMetadata();
	}

	public void addTag(TagModel tagModel) {
		Optional<TagModel> retrievedTagModel = TagModel.get(tagModel.name);

		if (retrievedTagModel.isPresent()
				&& tags.contains(retrievedTagModel.get())) {
			return;
		}

		tags.add(tagModel);

		tagModel.images.add(this);

		infoChangeCount++;

		save();
		writeMetadata();
	}

	private void writeMetadata() {
		File image = new File(filename);

		Optional<TiffImageMetadata> retrievedExif = PrepareImageModel
				.getMetadataTable(image);

		if (!retrievedExif.isPresent()) {
			Logger.warn("Could not read exif metadata from: "
					+ image.getAbsolutePath());

			return;
		}

		TiffImageMetadata exif = retrievedExif.get();

		try {
			ExifWriter exifWriter = new ExifWriter(image, exif);
			exifWriter.setDescription(description);
			exifWriter.setRating(rating);

			String tagList = "";

			for (TagModel tag : tags) {
				tagList += tag.name + ",";
			}

			exifWriter.setTags(tagList);

			exifWriter.save();
		} catch (ImageReadException | ImageWriteException | IOException exception) {
			Logger.warn("Could not write metadata to image: "
					+ image.getAbsolutePath() + "\nException: "
					+ exception.getMessage());
		}
	}
}