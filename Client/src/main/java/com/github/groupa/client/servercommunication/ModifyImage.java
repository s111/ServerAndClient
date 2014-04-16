package com.github.groupa.client.servercommunication;

import java.awt.Rectangle;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.github.groupa.client.ThreadPool;
import com.google.gson.JsonObject;
import com.google.inject.Inject;

public class ModifyImage {
	private ServerConnection serverConnection;
	private ThreadPool threadPool;

	@Inject
	public ModifyImage(ServerConnection serverConnection, ThreadPool threadPool) {
		this.serverConnection = serverConnection;
		this.threadPool = threadPool;
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

	public void updateMultipleImages(final JsonObject jsonObject) {
		Runnable job = new Runnable() {
			@Override
			public void run() {
				serverConnection.updateMultipleImages(jsonObject);
			}
		};

		threadPool.add(job);
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
