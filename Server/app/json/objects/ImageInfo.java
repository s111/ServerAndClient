package json.objects;

public class ImageInfo {
	private String href;
	private String first;
	private String next;
	private String previous;
	private String last;
	
	private ImageFull image;

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

	public ImageFull getImage() {
		return image;
	}

	public void setImage(ImageFull image) {
		this.image = image;
	}
}
