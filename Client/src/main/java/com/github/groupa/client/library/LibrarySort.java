package com.github.groupa.client.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.github.groupa.client.ImageObject;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class LibrarySort {

	private LibrarySort() {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Comparator<ImageObject> SORT_RATING_ASC = new Comparator() {
		public int compare(Object o1, Object o2) {
			ImageObject i1 = (ImageObject) o1;
			ImageObject i2 = (ImageObject) o2;
			return ComparisonChain
					.start()
					.compare(i1.getRating(), i2.getRating())
					.compare(i1.getId(), i2.getId(),
							Ordering.natural().nullsLast()).result();
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Comparator<ImageObject> SORT_RATING_DESC = new Comparator() {
		public int compare(Object o1, Object o2) {
			ImageObject i1 = (ImageObject) o1;
			ImageObject i2 = (ImageObject) o2;
			return ComparisonChain
					.start()
					.compare(i2.getRating(), i1.getRating())
					.compare(i1.getId(), i2.getId(),
							Ordering.natural().nullsLast()).result();
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Comparator<ImageObject> SORT_UPLOAD_DATE_ASC = new Comparator() {
		public int compare(Object o1, Object o2) {
			ImageObject i1 = (ImageObject) o1;
			ImageObject i2 = (ImageObject) o2;
			Date d1 = i1.getDateTaken();
			Date d2 = i2.getDateTaken();
			int ret = 0;
			if (d1 != null) {
				if (d2 != null)
					ret = d1.compareTo(d2);
				else
					ret = -1;
			} else
				ret = 1;

			if (ret == 0)
				ret = (i1.getId() < i2.getId()) ? -1 : 1;

			return ret;
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Comparator<ImageObject> SORT_UPLOAD_DATE_DESC = new Comparator() {
		public int compare(Object o1, Object o2) {
			ImageObject i1 = (ImageObject) o1;
			ImageObject i2 = (ImageObject) o2;
			Date d1 = i1.getDateTaken();
			Date d2 = i2.getDateTaken();
			int ret = 0;
			if (d1 != null) {
				if (d2 != null)
					ret = d2.compareTo(d1);
				else
					ret = -1;
			} else
				ret = 1;

			if (ret == 0)
				ret = (i1.getId() < i2.getId()) ? -1 : 1;

			return ret;
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Comparator<ImageObject> SORT_ID_ASC = new Comparator() {
		public int compare(Object o1, Object o2) {
			ImageObject i1 = (ImageObject) o1;
			ImageObject i2 = (ImageObject) o2;
			return ComparisonChain
					.start()
					.compare(i1.getId(), i2.getId(),
							Ordering.natural().nullsLast()).result();
		}
	};

	public static boolean sort(List<ImageObject> images,
			Comparator<ImageObject> comparator) {
		List<ImageObject> old = new ArrayList<>();
		old.addAll(images);
		Collections.sort(images, comparator);
		int s = old.size();
		if (s == images.size()) {
			for (int i = 0; i < s; i++) {
				if (!(old.get(i).equals(images.get(i)))) {
					return true;
				}
			}
		}
		return false;
	}
}
