package com.myblue;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TabHost;

/**
 * 带有动画效果的TabHost
 * 
 * @Project App_Bluetooth
 * @Package com.android.bluetooth
 * @author chenlin
 * @version 1.0
 * @Date 2013年6月2日
 * @Note TODO
 */
public class AnimationTabHost extends TabHost {

	private int mCurrentTabID = 0;//当前的tabId
	private final long mDuration = 400;//动画时间

	public AnimationTabHost(Context context) {
		this(context, null);
	}

	public AnimationTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 切换动画
	 */
	@Override
	public void setCurrentTab(int index) {
		//向右平移 
		if (index > mCurrentTabID) {
			TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					-1.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
			translateAnimation.setDuration(mDuration);
			getCurrentView().startAnimation(translateAnimation);
			//向左平移
		} else if (index < mCurrentTabID) {
			TranslateAnimation translateAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0f,
					Animation.RELATIVE_TO_SELF, 0f);
			translateAnimation.setDuration(mDuration);
			getCurrentView().startAnimation(translateAnimation);
		} 
		
		super.setCurrentTab(index);

		//-----方向平移------------------------------
		if (index > mCurrentTabID) {
			TranslateAnimation translateAnimation = new TranslateAnimation( //
					Animation.RELATIVE_TO_PARENT, 1.0f,// RELATIVE_TO_SELF
					Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f);
			translateAnimation.setDuration(mDuration);
			getCurrentView().startAnimation(translateAnimation);
		} else if (index < mCurrentTabID) {
			TranslateAnimation translateAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f,
					Animation.RELATIVE_TO_PARENT, 0f);
			translateAnimation.setDuration(mDuration);
			getCurrentView().startAnimation(translateAnimation);
		}
		mCurrentTabID = index;
	}
}
