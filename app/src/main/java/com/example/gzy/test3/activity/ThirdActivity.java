package com.example.gzy.test3.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.gzy.test3.R;
import com.example.gzy.test3.fragment.FragmentOne;
import com.example.gzy.test3.fragment.FragmentThree;
import com.example.gzy.test3.fragment.FragmentTwo;

/**
 * Created by gzy on 2018/3/17.
 */

public class ThirdActivity  extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    BottomNavigationBar mBottomNavigationBar;
    private FragmentOne mFragmentOne;
    private FragmentTwo mFragmentTwo;
    private FragmentThree mFragmentThree;
    private Fragment[] fragments;
    private int lastShowFragment = 0;
    private FrameLayout f1;
    private TextView textView;

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.tabbar);
        f1=(FrameLayout) findViewById(R.id.content);
        textView=(TextView) findViewById(R.id.message);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        /*** the setting for BottomNavigationBar ***/

//        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBottomNavigationBar.setBarBackgroundColor(R.color.green);//set background color for navigation bar
       mBottomNavigationBar.setInActiveColor(R.color.white);//unSelected icon color
       BadgeItem badgeItem = new BadgeItem();//实现数值添加
        badgeItem.setHideOnSelect(false)
                .setText("10")
                .setBackgroundColorResource(R.color.orange)
                .setBorderWidth(0);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.firstpg, R.string.tab_one)
                .setActiveColorResource(R.color.lime).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(R.drawable.secondpg, R.string.tab_two)
                        .setActiveColorResource(R.color.lime))
                .addItem(new BottomNavigationItem(R.drawable.thirdpg, R.string.tab_three)
                        .setActiveColorResource(R.color.lime))
                //依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化
        mBottomNavigationBar.setTabSelectedListener(this);
        //初始化fragments及设置默认fragment
        initFragments();
    }

    @Override
    public void onTabSelected(int position) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {

            case 0:
                if (lastShowFragment != 0) {
                    switchFrament(lastShowFragment, 0);
                    lastShowFragment = 0;
                }
                break;
            case 1:
                if (lastShowFragment != 1) {
                    switchFrament(lastShowFragment, 1);
                    lastShowFragment = 1;
                }
                break;
            case 2:
                if (lastShowFragment != 2) {
                    switchFrament(lastShowFragment, 2);
                    lastShowFragment = 2;
                }
                break;

            default:

                break;
        }
//        transaction.commit();


    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
    /**
     * 切换Fragment
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    public void switchFrament(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.replace(R.id.content, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }
//fragment初始化有问题
    private void initFragments() {
        mFragmentOne = new FragmentOne();
        mFragmentTwo = new FragmentTwo();
        mFragmentThree = new FragmentThree();
        fragments = new Fragment[]{mFragmentOne, mFragmentTwo, mFragmentThree};
        lastShowFragment = 0;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, mFragmentOne)
                .show(mFragmentOne)
                .commit();
    }
}

