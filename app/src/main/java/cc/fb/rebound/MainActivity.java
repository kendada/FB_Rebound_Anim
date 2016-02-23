package cc.fb.rebound;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.fb.rebound.menu.QzoneMenu;
import cc.fb.rebound.menu.QzoneMenuItem;

public class MainActivity extends AppCompatActivity {

    private List<QzoneMenuItem> list = new ArrayList<>();

    private QzoneMenu mQzoneMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
        mQzoneMenu = new QzoneMenu(this, list);
        mQzoneMenu.setOnMenuItemClickListener(new QzoneMenu.OnMenuItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, " *** " + position + " *** ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getData(){
        list.add(new QzoneMenuItem(R.mipmap.skin_tabbar_btn_popup_talk, "说说"));
        list.add(new QzoneMenuItem(R.mipmap.skin_tabbar_btn_popup_transferphotos, "照片"));
        list.add(new QzoneMenuItem(R.mipmap.skin_tabbar_btn_popup_pastercamera, "贴纸相机"));
        list.add(new QzoneMenuItem(R.mipmap.skin_tabbar_btn_popup_video, "视频"));

        list.add(new QzoneMenuItem(R.mipmap.skin_tabbar_btn_popup_registration, "签到"));
        list.add(new QzoneMenuItem(R.mipmap.skin_tabbar_btn_popup_watermarkcamera, "动感影集"));
        list.add(new QzoneMenuItem(R.mipmap.skin_tabbar_btn_popup_journal, "日志"));
        list.add(new QzoneMenuItem(R.mipmap.skin_tabbar_btn_popup_addmore, "添加应用"));
    }
}
