package cn.fanfan.detilques;

import java.util.ArrayList;

public class AnswerItem {

	private String answer_id;
	private String answer_content;
	private String agree_count;
	private String uid;
	private String name;
	private String avatar_file;
	private ArrayList<String> urls;

	public String getAnswer_id() {
		return answer_id;
	}

	public void setAnswer_id(String answer_id) {
		this.answer_id = answer_id;
	}

	public String getAnswer_content() {
		return answer_content;
	}

	public void setAnswer_content(String answer_content) {
		this.answer_content = answer_content;
	}

	public String getAgree_count() {
		return agree_count;
	}

	public void setAgree_count(String agree_count) {
		this.agree_count = agree_count;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar_file() {
		return avatar_file;
	}

	public void setAvatar_file(String avatar_file) {
		this.avatar_file = avatar_file;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}

}
