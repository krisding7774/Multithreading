package com;

public class DuplicateScopusThreadMain {

	private static final String root = "D:\\workspace\\change\\example\\storehouse";

	public static void main(String[] args) {
		DuplicateScopusThread thread_0 = new DuplicateScopusThread(root, "mId_0.txt", "thread_0", "url_0.txt");
		DuplicateScopusThread thread_1 = new DuplicateScopusThread(root, "mId_1.txt", "thread_1", "url_1.txt");
		DuplicateScopusThread thread_2 = new DuplicateScopusThread(root, "mId_2.txt", "thread_2", "url_2.txt");
		DuplicateScopusThread thread_3 = new DuplicateScopusThread(root, "mId_3.txt", "thread_3", "url_3.txt");
		DuplicateScopusThread thread_4 = new DuplicateScopusThread(root, "mId_4.txt", "thread_4", "url_4.txt");
		DuplicateScopusThread thread_5 = new DuplicateScopusThread(root, "mId_5.txt", "thread_5", "url_5.txt");
		DuplicateScopusThread thread_6 = new DuplicateScopusThread(root, "mId_6.txt", "thread_6", "url_6.txt");
		DuplicateScopusThread thread_7 = new DuplicateScopusThread(root, "mId_7.txt", "thread_7", "url_7.txt");
		DuplicateScopusThread thread_8 = new DuplicateScopusThread(root, "mId_8.txt", "thread_8", "url_8.txt");
		DuplicateScopusThread thread_9 = new DuplicateScopusThread(root, "mId_9.txt", "thread_9", "url_9.txt");

		thread_0.start();
		thread_1.start();
		thread_2.start();
		thread_3.start();
		thread_4.start();
		thread_5.start();
		thread_6.start();
		thread_7.start();
		thread_8.start();
		thread_9.start();
	}
}
