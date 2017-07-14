package com.myblue;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;


public class DeviceActivity extends Activity {
	private ListView mListView;
	//数据
	private ArrayList<DeviceBean> mDatas;
	private Button mBtnSearch, mBtnService;
	private ChatListAdapter mAdapter;
	//蓝牙适配器
	private BluetoothAdapter mBtAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devices);
		initDatas();
		initViews();
		registerBroadcast();
		init();
	}
	
	private void initDatas() {
		mDatas = new ArrayList<DeviceBean>();
		mAdapter = new ChatListAdapter(this, mDatas);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * 列出所有的蓝牙设备
	 */
	private void init() {
		Log.i("tag", "mBtAdapter=="+ mBtAdapter);
		//根据适配器得到所有的设备信息
		Set<BluetoothDevice> deviceSet = mBtAdapter.getBondedDevices();
		if (deviceSet.size() > 0) {
			for (BluetoothDevice device : deviceSet) {
				mDatas.add(new DeviceBean(device.getName() + "\n"
						+device.getAddress(), true));
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mDatas.size() - 1);
			}
		} else {
			mDatas.add(new DeviceBean("没有配对的设备", true));
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mDatas.size() - 1);
		}
	}

	/**
	 * 注册广播
	 */
	private void registerBroadcast() {
		//设备被发现广播
		IntentFilter discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, discoveryFilter);

		// 设备发现完成
		IntentFilter foundFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(mReceiver, foundFilter);
	}

	/**
	 * 初始化视图
	 */
	private void initViews() {
		mListView = (ListView) findViewById(R.id.list);
		mListView.setAdapter(mAdapter);
		mListView.setFastScrollEnabled(true);
		
		
		mListView.setOnItemClickListener(mDeviceClickListener);
		
		mBtnSearch = (Button) findViewById(R.id.start_seach);
		mBtnSearch.setOnClickListener(mSearchListener);

		
		mBtnService = (Button) findViewById(R.id.start_service);
		mBtnService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				BluetoothActivity.mType = BluetoothActivity.Type.SERVICE;
				BluetoothActivity.mTabHost.setCurrentTab(1);
			}
		});
		
	}
	

	/**
	 * 搜索监听
	 */
	private OnClickListener mSearchListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if (mBtAdapter.isDiscovering()) {
				mBtAdapter.cancelDiscovery();
				mBtnSearch.setText("重新搜索");
			} else {
				mDatas.clear();
				mAdapter.notifyDataSetChanged();

				init();
				
				/* 开始搜索 */
				mBtAdapter.startDiscovery();
				mBtnSearch.setText("ֹͣ停止搜索");
			}
		}
	};

	/**
	 * 点击设备监听
	 */
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			DeviceBean bean = mDatas.get(position);
			String info = bean.message;
			String address = info.substring(info.length() - 17);
			BluetoothActivity.BlueToothAddress = address;

			AlertDialog.Builder stopDialog = new AlertDialog.Builder(DeviceActivity.this);
			stopDialog.setTitle("连接");//标题
			stopDialog.setMessage(bean.message);
			stopDialog.setPositiveButton("连接", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					mBtAdapter.cancelDiscovery();
					mBtnSearch.setText("重新搜索");

					BluetoothActivity.mType = BluetoothActivity.Type.CILENT;
					BluetoothActivity.mTabHost.setCurrentTab(1);
					
					dialog.cancel();
				}
			});
			stopDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					BluetoothActivity.BlueToothAddress = null;
					dialog.cancel();
				}
			});
			stopDialog.show();
		}
	};
	
	/**
	 * 发现设备广播
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// 获得设备信息
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 如果绑定的状态不一样
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					mDatas.add(new DeviceBean(device.getName() + "\n" + device.getAddress(), false));
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mDatas.size() - 1);
				}
				// 如果搜索完成了
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				if (mListView.getCount() == 0) {
					mDatas.add(new DeviceBean("没有发现蓝牙设备", false));
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mDatas.size() - 1);
				}
				mBtnSearch.setText("重新搜索");
			}
		}
	};
	
	@Override
	public void onStart() {
		super.onStart();
		if (!mBtAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, 3);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBtAdapter != null) {
			mBtAdapter.cancelDiscovery();
		}
		this.unregisterReceiver(mReceiver);
	}
}