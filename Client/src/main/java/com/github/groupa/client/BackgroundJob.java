package com.github.groupa.client;

public interface BackgroundJob extends Runnable {
	public static final int HIGH_PRIORITY = 1;
	public static final int MEDIUM_PRIORITY = 2;
	public static final int LOW_PRIORITY = 3;
	public void setPriority(int priority);
	public int getPriority();
}
