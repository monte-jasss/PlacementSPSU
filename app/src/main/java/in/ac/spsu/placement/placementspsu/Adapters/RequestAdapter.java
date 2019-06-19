package in.ac.spsu.placement.placementspsu.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.ac.spsu.placement.placementspsu.Model.RequestData;
import in.ac.spsu.placement.placementspsu.R;

public class RequestAdapter extends ArrayAdapter {
    List<RequestData> list = new ArrayList<RequestData>();
    Holder holder;

    public RequestAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void clear() {
        super.clear();
        this.list.clear();
    }

    public void add(RequestData object) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row==null){
            LayoutInflater in = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = in.inflate(R.layout.request_style, parent, false);

            holder = new Holder();
            holder.t1 = row.findViewById(R.id.item_1);
            holder.t2 = row.findViewById(R.id.item_2);
            holder.t3 = row.findViewById(R.id.item_3);
            row.setTag(holder);
        } else holder = (Holder) row.getTag();

        holder.t1.setText(list.get(position).getTitle());
        holder.t2.setText(list.get(position).getName());
        int tmp = list.get(position).getStatus();
        if(tmp==0){
            holder.t3.setBackground(row.getResources().getDrawable(R.drawable.status_p));
            holder.t3.setText(R.string.pending);
        } else if(tmp==1){
            holder.t3.setBackground(row.getResources().getDrawable(R.drawable.status_s));
            holder.t3.setText(R.string.selected);
        } else if(tmp==2){
            holder.t3.setBackground(row.getResources().getDrawable(R.drawable.status_r));
            holder.t3.setText(R.string.rejected);
        }
        return row;
    }

    private static class Holder {
        TextView t1,t2,t3;
    }
}
