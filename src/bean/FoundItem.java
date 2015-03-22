package bean;

public class FoundItem {
	public UserInfo user_info;

	public UserInfo getUser_info() {
		return user_info;
	}

	public void setUser_info(UserInfo user_info) {
		this.user_info = user_info;
	}

	public class UserInfo {
		private String uid;
		private String user_name;
		private String avatar_file;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getAvatar_file() {
			return avatar_file;
		}

		public void setAvatar_file(String avatar_file) {
			this.avatar_file = avatar_file;
		}
	}

}
