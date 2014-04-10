package com.github.groupa.client.servercommunication;

import java.awt.Rectangle;
import java.net.ConnectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.client.Response;

import com.github.groupa.client.Callback;
import com.github.groupa.client.ImageObject;
import com.google.inject.Inject;

public class ModifyImage {
	private static final Logger logger = LoggerFactory
			.getLogger(ModifyImage.class);
	private RESTService restService;

	@Inject
	public ModifyImage(RESTService restService) {
		this.restService = restService;
	}

	public void rotate(final Callback<ImageObject> callback,
			final ImageObject image, final int angle) {
		Job job = new Job("rotate image " + image.getId()) {
			public void run() throws ConnectException {
				Response response = restService.rotateImage(image.getId(),
						angle);
				if (response != null && response.getStatus() == 200)
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
		Job job = new Job("crop image " + image.getId()) {
			public void run() throws ConnectException {
				Response response = restService.cropImage(image.getId(),
						rectangle.x, rectangle.y, rectangle.width,
						rectangle.height);

				if (response != null && response.getStatus() == 200) {
					success = true;
				}
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
			try {
				job.run();
			} catch (ConnectException e) {
				logger.warn("Could not connect to server(" + job.jobType + ") "
						+ e.getMessage());
			} catch (Exception e) {
				logger.warn("Unknown server error(" + job.jobType + ") "
						+ e.getMessage());
			}
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
		public String jobType;

		public Job(String jobType) {
			this.jobType = jobType;
		}

		public void run() throws Exception {
		}

		public void success() {
		}

		public void failure() {
		}
	}
}
