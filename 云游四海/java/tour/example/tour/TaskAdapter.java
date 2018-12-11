package tour.example.tour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class TaskAdapter extends ArrayAdapter{
    private final int resourceId;

    public TaskAdapter(Context context, int textViewResourceId, List<Task> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = (Task) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView Name = (TextView) view.findViewById(R.id.task_name);
        TextView Time = (TextView) view.findViewById(R.id.task_time);
        Name.setText(task.getContent().toString());
        Time.setText(task.getTime());


        return view;
    }
}
