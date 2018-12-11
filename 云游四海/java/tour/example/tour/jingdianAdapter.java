package tour.example.tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class jingdianAdapter extends ArrayAdapter<Spots> {
    private int resourceId;

    public jingdianAdapter(Context context, int textViewResourceId,
                           List<Spots> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Spots spots = getItem(position); // 获取当前项的Fruit实例

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView fruitjing = (TextView) view.findViewById(R.id.jingdian);
        TextView fruitjie = (TextView) view.findViewById(R.id.jieshao);
        fruitjing.setText(spots.getJingdian());
        fruitjie.setText(spots.getJieshao());
        return view;
    }
}