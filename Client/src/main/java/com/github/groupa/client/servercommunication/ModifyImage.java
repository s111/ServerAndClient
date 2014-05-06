package com.github.groupa.client.servercommunication;

import java.awt.Rectangle;
import java.util.List;

import com.github.groupa.client.ImageObject;
import com.github.groupa.client.ThreadPool;
import com.github.groupa.client.jsonobjects.ImagesUpdate;
import com.github.groupa.client.library.Library;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;

public class ModifyImage {
	private ServerConnection serverConnection;
	private ThreadPool threadPool;
	private Library library;

	@Inject
	public ModifyImage(ServerConnection serverConnection,
			ThreadPool threadPool, Library library) {
		this.serverConnection = serverConnection;
		this.threadPool = threadPool;
		this.library = library;
	}

	public void rotate(final ImageObject image, final int angle) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.rotate(image.getId(), angle))
					success = true;
			}

			public void success() {
				image.refreshImages();
			}
		};
		threadPool.add(new WorkerThread(job));
	}

	public void crop(final ImageObject image, final Rectangle rectangle) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.crop(image.getId(), rectangle))
					success = true;
			}

			public void success() {
				image.refreshImages();
			}
		};
		threadPool.add(new WorkerThread(job));
	}

	public void rate(final ImageObject image, final int rating) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.rate(image.getId(), rating))
					success = true;
			}

			public void success() {
				image.rate(rating);
			}
		};
		threadPool.add(new WorkerThread(job));
	}

	public void describe(final ImageObject image, final String description) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.describe(image.getId(), description))
					success = true;
			}

			public void success() {
				image.describe(description);
			}
		};
		threadPool.add(new WorkerThread(job));
	}

	public void addTag(final ImageObject image, final String tag) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.addTag(image.getId(), tag))
					success = true;
			}

			public void success() {
				image.addTag(tag);
			}
		};
		threadPool.add(new WorkerThread(job));
	}

	public void deleteTag(final ImageObject image, final String tag) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.deleteTag(image.getId(), tag)) {
					success = true;
				}
			}

			public void success() {
				image.deleteTag(tag);
			}
		};
		threadPool.add(new WorkerThread(job));
	}

	public void updateMultipleImages(final ImagesUpdate imagesUpdate) {
		Job job = new Job() {
			public void run() {
				JsonObject jsonObject = imagesUpdateToJson(imagesUpdate);

				if (serverConnection.updateMultipleImages(jsonObject)) {
					success = true;
				}
			}

			public void success() {
				List<Long> ids = imagesUpdate.getIds();

				List<ImageObject> images = library.getAllImages();

				for (ImageObject image : images) {
					if (ids.contains(image.getId())) {
						image.reloadImageInfo();
					}
				}
			}

			private JsonObject imagesUpdateToJson(ImagesUpdate imagesUpdate) {
				Gson gson = new Gson();

				String json = gson.toJson(imagesUpdate);

				JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
				JsonObject metadataWithoutId = jsonObject.get("metadata")
						.getAsJsonObject();
				metadataWithoutId.remove("id");

				jsonObject.remove("metadata");
				jsonObject.add("metadata", metadataWithoutId);

				return jsonObject;
			}
		};

		threadPool.add(new WorkerThread(job));
	}

	private class WorkerThread implements Runnable {
		private Job job;

		public WorkerThread(Job job) {
			this.job = job;
		}

		public void run() {
			job.run();
			if (job.success) {
				job.success();
			} else {
				job.failure();
			}
		}
	}

	private class Job {
		public boolean success = false;

		public void run() {
		}

		public void success() {
		}

		public void failure() {
		}
	}
}
