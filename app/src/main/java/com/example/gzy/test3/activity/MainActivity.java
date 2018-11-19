package com.example.gzy.test3.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gzy.test3.R;
import com.example.gzy.test3.UI.ILauncherView;
import com.example.gzy.test3.adapter.LauncherPagerAdapter;

public class MainActivity extends FragmentActivity implements ILauncherView {
	private ViewPager viewpagerLauncher;
	private LauncherPagerAdapter adapter;

	private ImageView[] tips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		if (!isFirst()) {
			gotoMain();
		}
		viewpagerLauncher = (ViewPager) findViewById(R.id.viewpager_launcher);
		adapter = new LauncherPagerAdapter(this, this);
		viewpagerLauncher.setOffscreenPageLimit(2);
		viewpagerLauncher.setCurrentItem(0);
		viewpagerLauncher.setOnPageChangeListener(changeListener);
		viewpagerLauncher.setAdapter(adapter);
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);// ??????????????
		tips = new ImageView[4];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			tips[i] = imageView;
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;// ???????view??????
			layoutParams.rightMargin = 10;// ???????view??????
			group.addView(imageView, layoutParams);
		}
	}

	private OnPageChangeListener changeListener = new OnPageChangeListener() {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			setImageBackground(index);

			TextView tvStartHeadlines = (TextView) adapter.getViews().get(index).findViewById(R.id.tv_start_headlines);
			if (index == tips.length - 1) {
				tvStartHeadlines.setVisibility(View.VISIBLE);
			} else {
				tvStartHeadlines.setVisibility(View.INVISIBLE);
			}
		}
	};

	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	@Override
	public void gotoMain() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private boolean isFirst() {
		SharedPreferences setting = getSharedPreferences("headlines", 0);
		Boolean user_first = setting.getBoolean("FIRST", true);
		if (user_first) {
			return true;
		} else {
			return false;
		}
	}
}
