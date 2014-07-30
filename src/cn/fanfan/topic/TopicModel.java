package cn.fanfan.topic;

public class TopicModel {
	private String topicId;
	private String imageUrl;
	private String topicTitle;
	private String topicSummary;

	public TopicModel(String topicId, String imageUrl, String topicTitle,
			String topicSummary) {
		super();
		this.topicId = topicId;
		this.imageUrl = imageUrl;
		this.topicTitle = topicTitle;
		this.topicSummary = topicSummary;
	}

	public TopicModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public String getTopicSummary() {
		return topicSummary;
	}

	public void setTopicSummary(String topicSummary) {
		this.topicSummary = topicSummary;
	}

	@Override
	public String toString() {
		return "TopicModel [topicId=" + topicId + ", imageUrl=" + imageUrl
				+ ", topicTitle=" + topicTitle + ", topicSummary="
				+ topicSummary + "]";
	}

}