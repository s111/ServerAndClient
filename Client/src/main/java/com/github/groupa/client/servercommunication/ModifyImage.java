package com.github.groupa.client.servercommunication;

import java.awt.Rectangle;
import java.util.List;

import com.github.groupa.client.Callback;
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
	public ModifyImage(ServerConnection serverConnection, ThreadPool threadPool, Library library) {
		this.serverConnection = serverConnection;
		this.threadPool = threadPool;
		this.library = library;
	}

	public void rotate(final Callback<ImageObject> callback,
			final ImageObject image, final int angle) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.rotate(image.getId(), angle))
					success = true;
			}

			public void success() {
				image.refreshImages();
			}
		};
		threadPool.add(new WorkerThread<ImageObject>(callback, image, job));
	}

	public void crop(final Callback<ImageObject> callback,
			final ImageObject image, final Rectangle rectangle) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.crop(image.getId(), rectangle))
					success = true;
			}

			public void success() {
				image.refreshImages();
			}
		};
		threadPool.add(new WorkerThread<ImageObject>(callback, image, job));
	}

	public void rate(final Callback<ImageObject> callback,
			final ImageObject image, final int rating) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.rate(image.getId(), rating))
					success = true;
			}

			public void success() {
				image.rate(rating);
			}
		};
		threadPool.add(new WorkerThread<ImageObject>(callback, image, job));
	}

	public void describe(final Callback<ImageObject> callback,
			final ImageObject image, final String description) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.describe(image.getId(), description))
					success = true;
			}

			public void success() {
				image.describe(description);
			}
		};
		threadPool.add(new WorkerThread<ImageObject>(callback, image, job));
	}

	public void addTag(final Callback<ImageObject> callback,
			final ImageObject image, final String tag) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.addTag(image.getId(), tag))
					success = true;
			}

			public void success() {
				image.addTag(tag);
			}
		};
		threadPool.add(new WorkerThread<ImageObject>(callback, image, job));
	}

	public void deleteTag(final Callback<ImageObject> callback,
			final ImageObject image, final String tag) {
		Job job = new Job() {

			public void run() {
				if (serverConnection.deleteTag(image.getId(), tag)) {
					success = true;
				}
			}

			public void success() {
				// TODO need a image.deleteTag(tag) (like image.addTag(tag) but
				// for removing a tag)
			}
		};
		threadPool.add(new WorkerThread<ImageObject>(callback, image, job));
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

		threadPool.add(new WorkerThread<Void>(null, null, job));
	}

	private class WorkerThread<T> implements Runnable {
		private Callback<T> callback;
		private T param;
		private Job job;

		public WorkerThread(Callback<T> callback, T param, Job job) {
			this.callback = callback;
			this.param = param;
			this.job = job;
		}

		public void run() {
			job.run();
			if (job.success) {
				job.success();
				if (callback != null) {
					callback.success(param);
				}
			} else {
				job.failure();
				if (callback != null)
					callback.failure();
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
