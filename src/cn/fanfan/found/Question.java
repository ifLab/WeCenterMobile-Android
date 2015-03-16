package cn.fanfan.found;

public class Question extends FoundItem {
	private int question_id;
	private String question_content;
	private long add_time;
	private long update_time;
	private int published_uid;
	private int answer_count;
	private int answer_users;
	private int view_count;
	private int focus_count;
	private String post_type;

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getQuestion_content() {
		return question_content;
	}

	public void setQuestion_content(String question_content) {
		this.question_content = question_content;
	}

	public long getAdd_time() {
		return add_time;
	}

	public void setAdd_time(long add_time) {
		this.add_time = add_time;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}

	public int getPublished_uid() {
		return published_uid;
	}

	public void setPublished_uid(int published_uid) {
		this.published_uid = published_uid;
	}

	public int getAnswer_count() {
		return answer_count;
	}

	public void setAnswer_count(int answer_count) {
		this.answer_count = answer_count;
	}

	public int getAnswer_users() {
		return answer_users;
	}

	public void setAnswer_users(int answer_users) {
		this.answer_users = answer_users;
	}

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public int getFocus_count() {
		return focus_count;
	}

	public void setFocus_count(int focus_count) {
		this.focus_count = focus_count;
	}

	public String getPost_type() {
		return post_type;
	}

	public void setPost_type(String post_type) {
		this.post_type = post_type;
	}

}
