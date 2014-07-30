package cn.fanfan.welcome;

public class LoginModel {
	private String uid;
	private String email;

	public LoginModel(String uid, String email) {
		super();
		this.uid = uid;
		this.email = email;
	}

	public LoginModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "LoginModel [uid=" + uid + ", email=" + email + "]";
	}

}
