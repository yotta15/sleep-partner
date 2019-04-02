package com.example.gzy.test3.activity;

/**
 * Created by w on 2018/12/2.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.gzy.test3.R;
import com.example.gzy.test3.fragment.FragmentMonitor;
import com.example.gzy.test3.fragment.FragmentPersonal;
import com.example.gzy.test3.fragment.FragmentMusic;
import com.example.gzy.test3.fragment.FragmentReport;
import com.githang.statusbar.StatusBarCompat;

import java.lang.reflect.Field;

import cn.bmob.v3.Bmob;

public class ContentActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    BottomNavigationBar mBottomNavigationBar;
    private FragmentMonitor mFragmentMonitor;
    private FragmentReport mFragmentReport;
    private FragmentMusic mFragmentMusic;
    private FragmentPersonal mFragmentPersonal;
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

    private Fragment currentFragment = new Fragment();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //第一：默认初始化
        Bmob.initialize(this, "5a5dcb5264e14c4fa9886e8511707ac0");
        //设置BmobConfig
//        BmobConfig config =new BmobConfig.Builder()
//                //请求超时时间（单位为秒）：默认15s
//                .setConnectTimeout(30)
//                //文件分片上传时每片的大小（单位字节），默认512*1024
//                .setBlockSize(500*1024)
//                .build();
//        Bmob.getInstance().initConfig(config);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.holo_blue_light));
        //初始化fragments及设置默认fragment
        initFragments();
        switchFragment(mFragmentMonitor);
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
         **/


        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.setBarBackgroundColor(R.color.white);//set background color for navigation bar
        mBottomNavigationBar.setInActiveColor(R.color.gray);//unSelected icon color

        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.firstpg, getResources().getString(R.string.tab_one))
                .setActiveColorResource(R.color.holo_blue_light))
                .addItem(new BottomNavigationItem(R.drawable.secondpg,getResources().getString(R.string.tab_three))
                        .setActiveColorResource(R.color.holo_blue_light))
                .addItem(new BottomNavigationItem(R.drawable.thirdpg,getResources().getString(R.string.tab_three))
                        .setActiveColorResource(R.color.holo_blue_light))
                .addItem(new BottomNavigationItem(R.drawable.fourthpg, getResources().getString(R.string.tab_four))
                        .setActiveColorResource(R.color.holo_blue_light))
                //依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .initialise();//初始化
        mBottomNavigationBar.setTabSelectedListener(this);
        setBottomNavigationItem(mBottomNavigationBar, 6, 26, 10);
    }

    //动态申请权限,录音权限，读写权限
    public void applyPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
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
        switch (position) {
            case 0:
                if (lastShowFragment != 0) {
                    switchFragment(mFragmentMonitor).commit();
                    lastShowFragment = 0;
                }
                break;
            case 1:
                if (lastShowFragment != 1) {
                    switchFragment(mFragmentReport).commit();
                    lastShowFragment = 1;
                }
                break;
            case 2:
                if (lastShowFragment != 2) {
                    switchFragment(mFragmentMusic).commit();
                    lastShowFragment = 2;
                }
                break;
            case 3:
                if (lastShowFragment != 3) {
                    switchFragment(mFragmentPersonal).commit();
                    lastShowFragment = 3;
                }
                break;
            default:
                break;
        }
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
        }
        currentFragment = targetFragment;
        return transaction;
    }


    //第一次init把fragment全部添加进来，这样所有Fragment只会被实例化一次（优化）
    private void initFragments() {
        mFragmentMonitor = new FragmentMonitor();
        mFragmentReport=new FragmentReport();
        mFragmentMusic = new FragmentMusic();
        mFragmentPersonal = new FragmentPersonal();
        fragments = new Fragment[]{mFragmentMonitor,mFragmentReport, mFragmentMusic, mFragmentPersonal};
        lastShowFragment = 0;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, mFragmentMonitor)
                .add(R.id.content, mFragmentReport)
                .add(R.id.content, mFragmentMusic)
                .add(R.id.content, mFragmentPersonal)
                .hide(mFragmentReport)
                .hide(mFragmentMusic)
                .hide(mFragmentPersonal)
              //  .show(mFragmentMonitor)
                .commit();
    }

    /**
     * @param bottomNavigationBar，需要修改的 BottomNavigationBar
     * @param space                     图片与文字之间的间距
     * @param imgLen                    单位：dp，图片大小，应 <= 36dp
     * @param textSize                  单位：dp，文字大小，应 <= 20dp
     */
    private void setBottomNavigationItem(BottomNavigationBar bottomNavigationBar, int space, int imgLen, int textSize) {
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try {
                    //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //获取到容器内的各个Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(56));
                        FrameLayout container = (FrameLayout) view.findViewById(R.id.fixed_bottom_navigation_container);
                        container.setLayoutParams(params);
                        container.setPadding(dip2px(12), dip2px(0), dip2px(12), dip2px(0));

                        //获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                        labelView.setIncludeFontPadding(false);
                        labelView.setPadding(0, 0, 0, dip2px(20 - textSize - space / 2));

                        //获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        params = new FrameLayout.LayoutParams(dip2px(imgLen), dip2px(imgLen));
                        params.setMargins(0, 0, 0, space / 2);
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

