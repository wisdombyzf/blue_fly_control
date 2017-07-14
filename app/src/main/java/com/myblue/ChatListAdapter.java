package com.myblue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * 会话适配器
 * @Project    App_Bluetooth
 * @Package    com.android.bluetooth
 * @author     chenlin
 * @version    1.0
 * @Date       2013年6月2日
 * @Note       TODO
 */
public class ChatListAdapter extends BaseAdapter {
	private ArrayList<DeviceBean> mDatas;
	private LayoutInflater mInflater;

	public ChatListAdapter(Context context, ArrayList<DeviceBean> datas) {
		this.mDatas = datas;
		this.mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return mDatas.size();
	}

	public Object getItem(int position) {
		return mDatas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		DeviceBean item = mDatas.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			mHolder = new ViewHolder((View) convertView.findViewById(R.id.list_child),
					(TextView) convertView.findViewById(R.id.chat_msg));
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		if (item.isReceive) {
			mHolder.child.setBackgroundResource(R.drawable.msgbox_rec);
		} else {
			mHolder.child.setBackgroundResource(R.drawable.msgbox_send);
		}
		mHolder.msg.setText(item.message);

		return convertView;
	}

	class ViewHolder {
		protected View child;
		protected TextView msg;

		public ViewHolder(View child, TextView msg) {
			this.child = child;
			this.msg = msg;

		}
	}
}
