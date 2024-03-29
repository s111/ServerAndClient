package com.github.groupa.client;

import java.util.LinkedList;

public class BackgroundImageFetcher {
	private LinkedList<BackgroundJob> hiJobs = new LinkedList<>();
	private LinkedList<BackgroundJob> midJobs = new LinkedList<>();
	private LinkedList<BackgroundJob> lowJobs = new LinkedList<>();

	private Thread workThread = null;

	public BackgroundImageFetcher() {
	}

	public void addJob(BackgroundJob job) {
		if (hiJobs.contains(job) || midJobs.contains(job)
				|| lowJobs.contains(job))
			return;
		if (job.getPriority() == BackgroundJob.HIGH_PRIORITY)
			hiJobs.add(job);
		else if (job.getPriority() == BackgroundJob.MEDIUM_PRIORITY)
			midJobs.add(job);
		else
			lowJobs.add(job);

		if (workThread == null)
			startWorker();
	}

	private void startWorker() {
		workThread = new Thread() {
			long timeStep = 200l;
			long timeLimit = 10000000000l;
			long lastJob = System.nanoTime();

			public void run() {
				while (true) {
					BackgroundJob job;

					job = hiJobs.poll();
					if (job == null)
						job = midJobs.poll();

					if (job == null)
						job = lowJobs.poll();

					if (job != null) {
						job.run();
						lastJob = System.nanoTime();
					} else {
						try {
							sleep(timeStep);
						} catch (InterruptedException e) {
						}
					}
					if (lastJob + timeLimit < System.nanoTime()) {
						workThread = null;
						break;
					}
				}
			}
		};
		workThread.start();
	}

	public BackgroundJob getJob(BackgroundImageFetch job) {
		if (hiJobs.contains(job)) {
			int idx = hiJobs.indexOf(job);
			return hiJobs.get(idx);
		} else if (midJobs.contains(job)) {
			int idx = midJobs.indexOf(job);
			return midJobs.get(idx);
		} else if (lowJobs.contains(job)) {
			int idx = lowJobs.indexOf(job);
			return lowJobs.get(idx);
		}
		return null;
	}
}
