package cn.fanfan.homepage;

public class HomePageItemModel {
	public static final int LAYOUT_TYPE_SIMPLE = 0;
	public static final int LAYOUT_TYPE_COMPLEX = 1;
	private String userName, mState, itemTitle, bestAnswer, agreeCount;
	private int avatarImageId, layoutType;

	public HomePageItemModel(int avatarImageId, int layoutType,
			String userName, String mState, String itemTitle,
			String bestAnswer, String agreeCount) {
		this.avatarImageId = avatarImageId;
		this.userName = userName;
		this.mState = mState;
		this.itemTitle = itemTitle;
		this.bestAnswer = bestAnswer;
		this.agreeCount = agreeCount;
		this.layoutType = layoutType;
	}

	public String getUserName() {
		return userName;
	}

	public String getmState() {
		return mState;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public String getBestAnswer() {
		return bestAnswer;
	}

	public String getAgreeCount() {
		return agreeCount;
	}

	public int getAvatarImageId() {
		return avatarImageId;
	}

	public int getLayoutType() {
		return layoutType;
	}

}
