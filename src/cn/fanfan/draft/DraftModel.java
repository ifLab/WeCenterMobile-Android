package cn.fanfan.draft;

public class DraftModel {
	private String Title;
	private String yourDraft;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getYourDraft() {
		return yourDraft;
	}

	public void setYourDraft(String yourDraft) {
		this.yourDraft = yourDraft;
	}

	public DraftModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DraftModel(String title, String yourDraft) {
		super();
		Title = title;
		this.yourDraft = yourDraft;
	}
}
