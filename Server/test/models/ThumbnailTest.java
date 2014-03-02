package models;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

public class ThumbnailTest {
	@BeforeClass
	public static void startApp() {
		start(fakeApplication());
	}

	@AfterClass
	public static void stopApp() {
		stop(fakeApplication());
	}

	@Before
	public void resetDB() {
		String serverName = "default";

		EbeanServer server = Ebean.getServer(serverName);
		ServerConfig config = new ServerConfig();

		DdlGenerator ddl = new DdlGenerator();
		ddl.setup((SpiEbeanServer) server, new H2Platform(), config);
		ddl.runScript(false, ddl.generateDropDdl());
		ddl.runScript(false, ddl.generateCreateDdl());
	}
	
	@Test
	public void add_thumbnail_to_image_expect_image_to_contain_one_thumbnail() {
		String filename = "../../images/01.png";
		int thumbnailSize = 1;
		
		ImageModel imageModel = new ImageModel(filename);
		ThumbnailModel thumbnailModel = new ThumbnailModel("../../images/01s.png", thumbnailSize);
		
		imageModel.addThumbnail(thumbnailModel);
		
		assertEquals(1, imageModel.thumbnails.size());
		assertEquals(imageModel, thumbnailModel.image);
	}
	
	@Test
	public void add_three_thumbnails_to_image_expect_image_to_contain_three_thumbnails() {
		String filename = "../../images/01.png";
		int xs = 0;
		int s = 1;
		int m = 2;
		
		ImageModel imageModel = new ImageModel(filename);
		ThumbnailModel thumbnailModel1 = new ThumbnailModel("../../images/01s.png", xs);
		ThumbnailModel thumbnailModel2 = new ThumbnailModel("../../images/02s.png", s);
		ThumbnailModel thumbnailModel3 = new ThumbnailModel("../../images/03s.png", m);
		
		imageModel.addThumbnail(thumbnailModel1);
		imageModel.addThumbnail(thumbnailModel2);
		imageModel.addThumbnail(thumbnailModel3);
		
		assertEquals(3, imageModel.thumbnails.size());
		assertEquals(imageModel, thumbnailModel1.image);
		assertEquals(imageModel, thumbnailModel2.image);
		assertEquals(imageModel, thumbnailModel3.image);
	}
}
