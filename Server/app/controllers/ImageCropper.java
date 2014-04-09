package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import models.Image;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import queryDB.QueryImage;
import queryDB.QueryThumbnail;
import utils.HibernateUtil;

import com.google.common.io.Files;

public class ImageCropper extends Controller {
	public static Result crop(long id) {
		DynamicForm requestData = Form.form().bindFromRequest();

		int x, y, width, height;

		try {
			x = Integer.parseInt(requestData.get("x"));
			y = Integer.parseInt(requestData.get("y"));
			width = Integer.parseInt(requestData.get("width"));
			height = Integer.parseInt(requestData.get("height"));
		} catch (NumberFormatException exception) {
			return badRequest();
		}

		QueryImage queryImage = new QueryImage(
				HibernateUtil.getSessionFactory());

		Image image = queryImage.getImage(id);

		if (x < 0 || y < 0 || (width < 0 && height < 0) || image == null) {
			return badRequest();
		}

		File file = new File(image.getFilename());

		try {
			BufferedImage originalImage = ImageIO.read(file);

			if (x + width > originalImage.getWidth()
					|| y + height > originalImage.getHeight()) {
				return badRequest();
			}

			BufferedImage croppedImage = originalImage.getSubimage(x, y, width,
					height);

			String extension = Files.getFileExtension(image.getFilename());

			ImageIO.write(croppedImage, extension, file);
		} catch (IOException exception) {
			Logger.warn("Could not crop image: " + file.getAbsolutePath());

			return internalServerError();
		}

		QueryThumbnail queryThumbnail = new QueryThumbnail(
				HibernateUtil.getSessionFactory());
		queryThumbnail.deleteAllThumbnails(image.getId());

		return ok();
	}
}
