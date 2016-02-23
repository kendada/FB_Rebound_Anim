package cc.fb.rebound.menu;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringChain;
import com.facebook.rebound.SpringSystem;

import java.util.List;

import cc.fb.rebound.R;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-22
 * Time: 16:36
 * Version 1.0
 */

public class QzoneMenu {

    private int sprinIndex = 0;

    private final static double INIT_CURRENT_VALUE = 135; // 旋转角度默认值
    private final static double INIT_END_VALUE = 0; // 旋转结束角度默认值

    private double currentValue = INIT_CURRENT_VALUE;
    private double endValue = INIT_END_VALUE;

    private List<QzoneMenuItem> menuItems;

    private Activity mActivity;

    private QzoneMenuAdapter adapter = null;

    private View menuView = null;
    private GridView menu_grid_view = null;
    private ImageView menu_btn = null;

    private SpringSystem mSpringSystem = SpringSystem.create();

    private OnMenuItemClickListener onMenuItemClickListener;

    private boolean isShow = true; // 是否正在显示

    private String tag = QzoneMenu.class.getSimpleName();

    public QzoneMenu(Activity activity, List<QzoneMenuItem> qzoneMenuItems){
        this(activity, qzoneMenuItems, null);
    }

    public QzoneMenu(Activity activity, List<QzoneMenuItem> qzoneMenuItems, OnMenuItemClickListener onMenuItemClickListener){
        mActivity = activity;
        menuItems = qzoneMenuItems;
        this.onMenuItemClickListener = onMenuItemClickListener;

        setMenuContentView(R.layout.adapter_test_fb_rebound_layout);

        menu_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setMenuItemForRebound(parent, view, position, id);
            }
        });

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置旋转动画角度
                if(currentValue == INIT_CURRENT_VALUE){
                    currentValue = INIT_END_VALUE;
                    endValue = INIT_CURRENT_VALUE;
                } else {
                    currentValue = INIT_CURRENT_VALUE;
                    endValue = INIT_END_VALUE;
                }
                setMenuBtnRotation(menu_btn, currentValue, endValue); // 测试旋转
                if(isShow){ // 根据文字判断
                    closeMenu();
                } else {
                    showMenu();
                }
            }
        });
    }

    public boolean isShow() {
        return isShow;
    }

    /**
     * 多个动画连锁动画：展示菜单
     * */
    public void showMenu(){
        isShow = true;
        show();
    }

    /**
     * 关闭菜单
     * */
    public void closeMenu(){
        isShow = false;
        hide();
    }

    /**
     * 给GridView 的每个Item设置点击动画
     * */
    private void setMenuItemForRebound(final AdapterView<?> parent, final View view, final int position, final long id){
        Spring mSpring = mSpringSystem.createSpring();
        mSpring.setCurrentValue(-0.5); // 设置位移值
        Log.i(tag, " ***** ");
        mSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                }
            }

            @Override
            public void onSpringAtRest(Spring spring) { // 监听spring状态的改变
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onItemClick(parent, view, position, id);
                }
                Log.i(tag, " *** onSpringAtRest() *** ");
            }
        });

        mSpring.setEndValue(0f);
    }

    /**
     * 设置View的旋转
     * */
    private void setMenuBtnRotation(final View view, double currentValue, double endValue){
        Spring mSpring = mSpringSystem.createSpring();
        mSpring.setCurrentValue(currentValue);
        mSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    view.setRotation((float) spring.getCurrentValue());
                }
            }
        });

        mSpring.setEndValue(endValue);
    }

    /**
     * 显示菜单
     * */
    private void show(){
        SpringChain mSpringChain = SpringChain.create(40, 8, 50, 7);
        // 子View总数
        int childCount = menu_grid_view.getChildCount();
        Log.i(tag, " **** childCount = " + childCount + " *** ");
        for(int i=0; i<childCount; i++){
            final View view = menu_grid_view.getChildAt(i);
            mSpringChain.addSpring(new SimpleSpringListener(){
                @Override
                public void onSpringUpdate(Spring spring) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        view.setTranslationY((float) spring.getCurrentValue());
                    }
                }
            });
        }

        List<Spring> springs = mSpringChain.getAllSprings();

        for(Spring spring : springs){
            spring.setCurrentValue(500); // 设定位移值
        }

        mSpringChain.setControlSpringIndex(2).getControlSpring().setEndValue(0f);
    }

    /**
     * 关闭菜单
     * */
    private void hide(){
        SpringChain mSpringChain = SpringChain.create(40, 8, 50, 7);
        final int childCount = menu_grid_view.getCount();
        for(int i=0; i<childCount; i++){
            final View view = menu_grid_view.getChildAt(i);
            mSpringChain.addSpring(new SimpleSpringListener(){
                @Override
                public void onSpringUpdate(Spring spring) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        view.setTranslationY((float)spring.getCurrentValue());
                    }
                }

                @Override
                public void onSpringAtRest(Spring spring) {
                    if(sprinIndex==0){ // 只有等于0时执行
                        Toast.makeText(mActivity, "动画执行完毕。。。", Toast.LENGTH_SHORT).show();
                    }
                    sprinIndex++; // 自增
                    if(sprinIndex==childCount){
                        sprinIndex = 0; // 恢复
                    }
                    Log.i(tag, " *** onSpringAtRest() ***" + sprinIndex);
                }
            });
        }

        List<Spring> springs = mSpringChain.getAllSprings();
        for(Spring spring : springs){
            spring.setCurrentValue(0);
        }
        mSpringChain.setControlSpringIndex(1).getControlSpring().setEndValue(850f);
    }

    /**
     * 填充布局XML
     * */
    private void setMenuContentView(int resLayout){
        menuView = LayoutInflater.from(mActivity).inflate(resLayout, null);
        menu_grid_view = (GridView)menuView.findViewById(R.id.menu_grid_layout);
        menu_btn = (ImageView)menuView.findViewById(R.id.menu_btn);

        ViewGroup decorView = (ViewGroup)mActivity.getWindow().getDecorView();
        decorView.addView(menuView);

        adapter = new QzoneMenuAdapter(mActivity, menuItems);
        menu_grid_view.setAdapter(adapter);

        Log.i(tag, " &&&& " + adapter.getCount() + " &&&& " + menuItems.size() + " &&&& ");
    }

    public OnMenuItemClickListener getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    /**
     * 每个Item点击回调方法
     * */
    public interface OnMenuItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

}
