package cn.fanfan.detailquestion;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ComListView extends ListView {

	public ComListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	 public ComListView(Context context, AttributeSet attrs) { 
	        super(context, attrs); 
	        // TODO Auto-generated constructor stub 
	    } 
	    public ComListView(Context context, AttributeSet attrs, int defStyle) { 
	        super(context, attrs, defStyle); 
	        // TODO Auto-generated constructor stub 
	    } 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// TODO Auto-generated method stub
    	int expandSpec = MeasureSpec.makeMeasureSpec(  
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
         super.onMeasure(widthMeasureSpec, expandSpec); 
    }
}
