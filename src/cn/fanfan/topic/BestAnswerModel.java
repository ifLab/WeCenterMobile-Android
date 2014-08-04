package cn.fanfan.topic;

public class BestAnswerModel {
	private int question_id;
	private String question_content;
	private int answer_id;
	private String answer_content;
	private int agree_count;
	private int uid;
	private String avatar_file;

	public BestAnswerModel(int question_id, String question_content,
			int answer_id, String answer_content, int agree_count, int uid,
			String avatar_file) {
		super();
		this.question_id = question_id;
		this.question_content = question_content;
		this.answer_id = answer_id;
		this.answer_content = answer_content;
		this.agree_count = agree_count;
		this.uid = uid;
		this.avatar_file = avatar_file;
	}

	public BestAnswerModel() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public int getAnswer_id() {
		return answer_id;
	}

	public void setAnswer_id(int answer_id) {
		this.answer_id = answer_id;
	}

	public String getAnswer_content() {
		return answer_content;
	}

	public void setAnswer_content(String answer_content) {
		this.answer_content = answer_content;
	}

	public int getAgree_count() {
		return agree_count;
	}

	public void setAgree_count(int agree_count) {
		this.agree_count = agree_count;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getAvatar_file() {
		return avatar_file;
	}

	public void setAvatar_file(String avatar_file) {
		this.avatar_file = avatar_file;
	}

	@Override
	public String toString() {
		return "BestAnswerModel [question_id=" + question_id
				+ ", question_content=" + question_content + ", answer_id="
				+ answer_id + ", answer_content=" + answer_content
				+ ", agree_count=" + agree_count + ", uid=" + uid
				+ ", avatar_file=" + avatar_file + "]";
	}

}
