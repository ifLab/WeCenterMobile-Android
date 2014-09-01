package cn.fanfan.detail.question;

public class CommentModel {
     
	private String username;
	private String backname;
	private String comcontent;
	private String time;
   	private String uid;
   	private String backuid;

   	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getComcontent() {
		return comcontent;
	}
	public void setComcontent(String comcontent) {
		this.comcontent = comcontent;
	}
	public String getBackname() {
		return backname;
	}
	public void setBackname(String backname) {
		this.backname = backname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getBackuid() {
		return backuid;
	}
	public void setBackuid(String backuid) {
		this.backuid = backuid;
	}
	
	
}
