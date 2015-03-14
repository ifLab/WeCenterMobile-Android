package cn.fanfan.homepage;

public class HomePageItemModel {
	public static final int LAYOUT_TYPE_SIMPLE = 0;
	public static final int LAYOUT_TYPE_COMPLEX = 1;
	private String avatarUrl, userName, action;
	private String itemTitle;
	private String bestAnswer;
	private int layoutType, itemTitleUid,bestAnswerUid,agreeCount,userUid;

	public HomePageItemModel(int layoutType, String avatarUrl, String userName,
			int userUid, String action, String itemTitle, int itemTitleUid,
			String bestAnswer, int bestAnswerUid, int agreeCount) {
		this.layoutType = layoutType;
		this.avatarUrl = avatarUrl;
		this.userName = userName;
		this.userUid = userUid;
		this.action = action;
		this.itemTitle = itemTitle;
		this.itemTitleUid = itemTitleUid;
		this.bestAnswer = bestAnswer;
		this.bestAnswerUid = bestAnswerUid;
		this.agreeCount = agreeCount;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public String getUserName() {
		return userName;
	}

	public int getUserUid() {
		return userUid;
	}

	public String getAction() {
		return action;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public int getItemTitleUid() {
		return itemTitleUid;
	}

	public String getBestAnswer() {
		return bestAnswer;
	}

	public int getBestAnswerUid() {
		return bestAnswerUid;
	}

	public int getAgreeCount() {
		return agreeCount;
	}

	public int getLayoutType() {
		return layoutType;
	}

}
