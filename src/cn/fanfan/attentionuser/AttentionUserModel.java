package cn.fanfan.attentionuser;

public class AttentionUserModel {
	private String userImageUrl;
	private String userName;
	private String singnature;
	private String uid;

	public AttentionUserModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AttentionUserModel(String userImageUrl, String userName,
			String singnature, String uid) {
		super();
		this.userImageUrl = userImageUrl;
		this.userName = userName;
		this.singnature = singnature;
		this.uid = uid;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSingnature() {
		return singnature;
	}

	public void setSingnature(String singnature) {
		this.singnature = singnature;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "AttentionUserModel [userImageUrl=" + userImageUrl
				+ ", userName=" + userName + ", singnature=" + singnature
				+ ", uid=" + uid + "]";
	}

}