package tour.example.tour.Utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tour.example.tour.R;
import tour.example.tour.bean.Msg;


/**
 * Created by dream on 2017/6/1.
 */

public class MsgAdapter extends ArrayAdapter<Msg> {

    private int resourceId;

    public MsgAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Msg> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Msg msg = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftlayout = (LinearLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightlayout = (LinearLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftImg = (ImageView) view.findViewById(R.id.left_img);
            viewHolder.rightImg = (ImageView) view.findViewById(R.id.right_img);
            viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);

            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if(msg.getType()==Msg.TYPE_RECEIVED){
            viewHolder.leftlayout.setVisibility(View.VISIBLE);
            viewHolder.rightlayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
            viewHolder.leftMsg.setAutoLinkMask(Linkify.WEB_URLS);
            viewHolder.leftMsg.setMovementMethod(LinkMovementMethod.getInstance());
        }else if(msg.getType()==Msg.TYPE_SENT){
            viewHolder.leftlayout.setVisibility(View.GONE);
            viewHolder.rightlayout.setVisibility(View.VISIBLE);
            viewHolder.rightMsg.setText(msg.getContent());
        }
        return view;
    }

    class ViewHolder {
        LinearLayout leftlayout;
        LinearLayout rightlayout;
        ImageView leftImg;
        ImageView rightImg;
        TextView leftMsg;
        TextView rightMsg;
    }
}
