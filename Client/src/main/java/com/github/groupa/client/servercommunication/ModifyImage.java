package com.github.groupa.client.servercommunication;

import java.awt.Rectangle;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.google.inject.Inject;

public class ModifyImage {
	private ServerConnection serverConnection;

	@Inject
	public ModifyImage(ServerConnection serverConnection) {
		this.serverConnection = serverConnection;
	}

	public void rotate(final Callback<ImageObject> callback,
			final ImageObject image, final int angle) {
		Job job = new Job() {
			
			public void run() {
				if (serverConnection.rotate(image.getId(), angle))
					success = true;
			}

			public void success() {
				image.refreshImage();
			}
		};
		addJob(callback, image, job);
	}

	public void crop(final Callback<ImageObject> callback,
			final ImageObject image, final Rectangle rectangle) {
		Job job = new Job() {
			
			public void run() {
				if (serverConnection.crop(image.getId(), rectangle))
					success = true;
			}

			public void success() {
				image.refreshImage();
			}
		};
		addJob(callback, image, job);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> void addJob(Callback<T> callback, ImageObject image, Job job) {
		Thread workerThread = new WorkerThread(callback, image, job);
		workerThread.start();
	}

	private class WorkerThread<T> extends Thread {
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
