package in.ac.spsu.placement.placementspsu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.ac.spsu.placement.placementspsu.Model.FeedJobData;
import in.ac.spsu.placement.placementspsu.R;

public class FeedJobAdapter extends ArrayAdapter {
    List<FeedJobData> list = new ArrayList<FeedJobData>();
    Holder holder;

    public FeedJobAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void clear() {
        super.clear();
        this.list.clear();
    }

    public void add(FeedJobData object) {
        super.add(object);
        this.list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row==null){
            LayoutInflater in = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = in.inflate(R.layout.feed_style, parent, false);

            holder = new Holder();
            holder.t1 = row.findViewById(R.id.listId);
            holder.t2 = row.findViewById(R.id.listTitle);
            holder.t3 = row.findViewById(R.id.listDate);
            row.setTag(holder);
        } else holder = (Holder) row.getTag();

        holder.t1.setText(""+list.get(position).getId());
        holder.t2.setText(list.get(position).getTitle());
        holder.t3.setText(list.get(position).getLastdate());
        return row;
    }

    private static class Holder {
        TextView t1,t2,t3;
    }
}
