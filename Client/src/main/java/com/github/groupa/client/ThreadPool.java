package com.github.groupa.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	ExecutorService executor = Executors.newFixedThreadPool(10);

	public void add(Runnable job) {
		synchronized (this) {
			executor.execute(job);
		}
	}

	public void shutdown() throws InterruptedException {
		synchronized (this) {
			executor.shutdown();
			executor.awaitTermination(0, null);
		}
	}
}
