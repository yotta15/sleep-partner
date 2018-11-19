package com.example.gzy.test3.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.gzy.test3.R;
import com.example.gzy.test3.UI.ILauncherView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LauncherPagerAdapter extends PagerAdapter implements OnClickListener {
	private ILauncherView launcherView;

	private List<View> views;
	private int[] images = new int[] { R.drawable.tutorial_1, R.drawable.tutorial_2, R.drawable.tutorial_3,
			R.drawable.tutorial_4 };

	public LauncherPagerAdapter(Context context, ILauncherView launcherView) {
		views = new ArrayList<View>();
		this.launcherView = launcherView;
		for (int i = 0; i < images.length; i++) {
			View item = LayoutInflater.from(context).inflate(R.layout.activity_luancher_pager_item, null);
			ImageView imageview = (ImageView) item.findViewById(R.id.imageview);
			imageview.setImageResource(images[i]);
			item.findViewById(R.id.tv_start_headlines).setOnClickListener(this);
			views.add(item);
		}
	}

	public List<View> getViews() {
		return views;
	}

	@Override
	public int getCount() {
		return views == null ? 0 : views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(views.get(position), 0);
		return views.get(position);
	}

	@Override
	public void onClick(View v) {
		launcherView.gotoMain();
	}

}
