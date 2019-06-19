package in.ac.spsu.placement.placementspsu.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.ac.spsu.placement.placementspsu.Activities.MainActivity;
import in.ac.spsu.placement.placementspsu.Adapters.ListJobAdapter;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.Model.ListJobData;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListJob extends Fragment {
    private ListView listView;
    private TextView tv;
    private Server server;
    private ProgressDialog progressDialog,progress;
    private ListJobAdapter listJobAdapter;
    private SwipeRefreshLayout swipeList;

    public ListJob() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_job, container, false);

        swipeList = view.findViewById(R.id.swipeList);
        server = Client.getClient().create(Server.class);
        listView = view.findViewById(R.id.listJobs);
        tv = view.findViewById(R.id.noJob);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);
        progress = new ProgressDialog(getContext());
        progress.setMessage(getString(R.string.dele));
        progress.setCancelable(false);
        listJobAdapter = new ListJobAdapter(getContext(),R.layout.feed_style);
        listView.setAdapter(listJobAdapter);
        registerForContextMenu(listView);

        swipeList.setColorSchemeColors(getResources().getColor(R.color.blueDark),getResources().getColor(R.color.colorPrimaryDark));

        swipeList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createList();
                swipeList.setRefreshing(false);
            }
        });

        createList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = view.findViewById(R.id.listId);
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("id",temp.getText());
                startActivity(i);
            }
        });

        return view;
    }

    private void createList() {
        if(CheckNetwork.isNetworkConnected(getActivity())){
            progressDialog.show();
            Call<List<ListJobData>> call = server.getAllCurrentCompanyJob(SharedPref.getUserName(getContext()));
            call.enqueue(new Callback<List<ListJobData>>() {
                @Override
                public void onResponse(Call<List<ListJobData>> call, Response<List<ListJobData>> response) {
                    listJobAdapter.clear();
                    List<ListJobData> data = response.body();
                    if(data.size()==0){
                        listView.setVisibility(View.GONE);
                        tv.setText(getString(R.string.no_jobs));
                        tv.setVisibility(View.VISIBLE);
                    } else {
                        tv.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        for(ListJobData item : data){
                            listJobAdapter.add(item);
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<ListJobData>> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void deleteSelectedJob(final String title, int id, final int pos) {
        if(CheckNetwork.isNetworkConnected(getActivity())){
            progress.show();
            Call<String> call = server.doRemoveJob(id);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String data = response.body();
                    if(data.equals(getString(R.string.success))){
                        listJobAdapter.remove(pos);
                        listJobAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), title+" "+getString(R.string.removed), Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.company, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.action_delete:
                TextView tv = info.targetView.findViewById(R.id.listId);
                TextView tm = info.targetView.findViewById(R.id.listTitle);
                deleteSelectedJob(tm.getText().toString(),Integer.parseInt(tv.getText().toString()),info.position);
                break;
        }

        return super.onContextItemSelected(item);
    }
}
