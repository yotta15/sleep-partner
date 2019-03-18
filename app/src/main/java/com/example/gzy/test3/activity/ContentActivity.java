package com.example.gzy.test3.activity;

/**
 * Created by w on 2018/12/2.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.gzy.test3.R;
import com.example.gzy.test3.fragment.FragmentOne;
import com.example.gzy.test3.fragment.FragmentThree;
import com.example.gzy.test3.fragment.FragmentTwo;

public class ContentActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    BottomNavigationBar mBottomNavigationBar;
    private FragmentOne mFragmentOne;
    private FragmentTwo mFragmentTwo;
    private FragmentThree mFragmentThree;
    private Fragment[] fragments;
    private int lastShowFragment = 0;
    private FrameLayout f1;
    private TextView textView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;

    private  Fragment  currentFragment=new Fragment();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyPermission();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.tabbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        toolbar.setTitle("Toolbar");//设置Toolbar标题
        toolbar.setTitleTextColor(getResources().getColor(R.color.holo_blue_light)); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        f1 = (FrameLayout) findViewById(R.id.content);
        textView = (TextView) findViewById(R.id.message);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.draw);

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

        /**
         * the setting for BottomNavigationBar
         * TODO 设置图片与文字的距离太近
         **/

//        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
//        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBottomNavigationBar.setBarBackgroundColor(R.color.white);//set background color for navigation bar
        mBottomNavigationBar.setInActiveColor(R.color.black);//unSelected icon color
        //   BadgeItem badgeItem = new BadgeItem();//实现数值添加
//        badgeItem.setHideOnSelect(false)
//                .setBackgroundColorResource(R.color.holo_blue_light)
//                .setBorderWidth(0);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.firstpg, R.string.tab_one)
                .setActiveColorResource(R.color.holo_blue_light))
                .addItem(new BottomNavigationItem(R.drawable.secondpg, R.string.tab_two)
                        .setActiveColorResource(R.color.holo_blue_light))
                .addItem(new BottomNavigationItem(R.drawable.thirdpg, R.string.tab_three)
                        .setActiveColorResource(R.color.holo_blue_light))
                //依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化
        mBottomNavigationBar.setTabSelectedListener(this);
        //初始化fragments及设置默认fragment
        initFragments();
    }

    //动态申请权限
    public void applyPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

    }

    @Override
    public void onTabSelected(int position) {
      // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {

            case 0:
                if (lastShowFragment != 0) {
                    switchFragment(mFragmentOne).commit();
                    lastShowFragment = 0;
                }
                break;
            case 1:
                if (lastShowFragment != 1) {
                    switchFragment(mFragmentTwo).commit();
                    lastShowFragment = 1;
                }
                break;
            case 2:
                if (lastShowFragment != 2) {
                    switchFragment(mFragmentThree).commit();
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
  //
    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下    
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.content, targetFragment);
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        } currentFragment = targetFragment;
        return transaction;
    }

    /**
     * 切换Fragment
     * TODO 切换fragment时状态未能保存
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     *                  NOTICE : replace 会在fragment切换时进行重建，这里应该使用add
     */
//    public void switchFrament(int lastIndex, int index) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.hide(fragments[lastIndex]);
//        if (!fragments[index].isAdded()) {
//            transaction.replace(R.id.content, fragments[index]);
//        }
//        transaction.show(fragments[index]).commitAllowingStateLoss();
//    }

    //第一次init把fragment全部添加进来，这样所有Fragment只会被实例化一次（优化）
    private void initFragments() {
        mFragmentOne = new FragmentOne();
        mFragmentTwo = new FragmentTwo();
        mFragmentThree = new FragmentThree();
        fragments = new Fragment[]{mFragmentOne, mFragmentTwo, mFragmentThree};
        lastShowFragment = 0;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mFragmentOne)
                .add(R.id.content, mFragmentTwo)
                .add(R.id.content, mFragmentThree)
                .hide(mFragmentTwo)
                .hide(mFragmentThree)
              //  .show(mFragmentOne)
                .commit();
    }
}

