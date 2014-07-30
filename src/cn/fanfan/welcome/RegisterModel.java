package cn.fanfan.welcome;

public class RegisterModel {
	private String userName;
	private String mail;
	private String passwd;
	private String confirmPasswd;

	
	public RegisterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RegisterModel(String userName, String mail, String passwd,
			String confirmPasswd) {
		super();
		this.userName = userName;
		this.mail = mail;
		this.passwd = passwd;
		this.confirmPasswd = confirmPasswd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getConfirmPasswd() {
		return confirmPasswd;
	}

	public void setConfirmPasswd(String confirmPasswd) {
		this.confirmPasswd = confirmPasswd;
	}

}
