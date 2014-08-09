package cn.fanfan.found;

import org.json.JSONObject;

public class Founditem {
	private String name;
	private String question;
	private int focus_count;
	private int answer_count;
	private int view_count;
	private String avatar_file;
	private String question_id;
	private String uid;
	private int inttag;

	public Founditem() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getFocus_count() {
		return focus_count;
	}

	public void setFocus_count(int focus_count) {
		this.focus_count = focus_count;
	}

	public int getAnswer_count() {
		return answer_count;
	}

	public void setAnswer_count(int answer_count) {
		this.answer_count = answer_count;
	}

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public String getAvatar_file() {
		return avatar_file;
	}

	public void setAvatar_file(String avatar_file) {
		this.avatar_file = avatar_file;
	}
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getInttag() {
		return inttag;
	}
	public void setInttag(int inttag) {
		this.inttag = inttag;
	}

}
