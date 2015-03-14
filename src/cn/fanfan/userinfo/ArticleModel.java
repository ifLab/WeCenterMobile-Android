package cn.fanfan.userinfo;

public class ArticleModel {
	private String id;
	private String title;
	private String message;

	public ArticleModel(String id, String title, String message) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
	}

	public ArticleModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ArticleModel [id=" + id + ", title=" + title + ", message="
				+ message + "]";
	}

}