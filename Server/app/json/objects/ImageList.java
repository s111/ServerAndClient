package json.objects;

import java.util.List;

public class ImageList {
	private String href;
	private String first;
	private String next;
	private String previous;
	private String last;
	
	private int offset;
	private int limit;
	
	private List<ImageShort> images;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public List<ImageShort> getImages() {
		return images;
	}

	public void setImages(List<ImageShort> images) {
		this.images = images;
	}
}
