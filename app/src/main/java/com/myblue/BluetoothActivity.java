package com.myblue;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

/**
 * 主页
 * 
 * @Project App_Bluetooth
 * @Package com.android.bluetooth
 * @author chenlin
 * @version 1.0
 * @Date 2013年6月2日
 */
@SuppressWarnings("deprecation")
public class BluetoothActivity extends TabActivity {
	static AnimationTabHost mTabHost;//动画tabhost
	static String BlueToothAddress;//蓝牙地址
	static Type mType = Type.NONE;//类型
	static boolean isOpen = false;

	//类型：
	enum Type {
		NONE, SERVICE, CILENT
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initTab();
	}

	private void initTab() {
		//初始化
		mTabHost = (AnimationTabHost) getTabHost();
		//添加tab
		mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("设备列表", getResources().getDrawable(android.R.drawable.ic_menu_add))
				.setContent(new Intent(this, DeviceActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("操作列表", getResources().getDrawable(android.R.drawable.ic_menu_add))
				.setContent(new Intent(this, ChatActivity.class)));
		//添加监听
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				if (tabId.equals("Tab1")) {
					//TODO
				}
			}
		});
		//默认在第一个tabhost上面
		mTabHost.setCurrentTab(0);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, "address:", Toast.LENGTH_SHORT).show();
	}
	
}