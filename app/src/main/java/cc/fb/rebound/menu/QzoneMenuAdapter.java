package cc.fb.rebound.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.fb.rebound.R;


/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2016-02-19
 * Time: 16:30
 * Version 1.0
 */

public class QzoneMenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<QzoneMenuItem> mList;

    public QzoneMenuAdapter(Context context, List<QzoneMenuItem> list){
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyView mv = null;
        if(convertView == null){
            mv = new MyView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_qzone_menu_layout, parent, false);
            convertView.setTag(mv);

            mv.qImg = (ImageView)convertView.findViewById(R.id.icon);
            mv.qTitle = (TextView)convertView.findViewById(R.id.title);

        } else {
            mv = (MyView) convertView.getTag();
        }

        QzoneMenuItem menu = mList.get(position);
        if(menu != null){
            mv.qImg.setImageResource(menu.icon);
            mv.qTitle.setText(menu.title);
        }

        return convertView;
    }

    class MyView{
        ImageView qImg;
        TextView qTitle;
    }
}
