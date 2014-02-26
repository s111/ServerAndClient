import java.io.File;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;

import models.ImageModel;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {
		File directory = new File("../../images");

		if (!directory.isDirectory())
			return;

		File[] listOfFiles = directory.listFiles();

		for (File image : listOfFiles) {
			String filename = image.getName();

			String filenameInDatabase = "../../images/" + filename;

			if (filename.matches("^(.+).png$")) {
				if (ImageModel.find.where().eq("filename", filenameInDatabase)
						.findUnique() == null) {
					ImageModel.create(filenameInDatabase);
				}

			}
		}
	}

	@Override
	public void onStop(Application app) {
		String serverName = "default";

		EbeanServer server = Ebean.getServer(serverName);
		ServerConfig config = new ServerConfig();

		DdlGenerator ddl = new DdlGenerator();
		ddl.setup((SpiEbeanServer) server, new H2Platform(), config);
		ddl.runScript(false, ddl.generateDropDdl());
		ddl.runScript(false, ddl.generateCreateDdl());
	}
}