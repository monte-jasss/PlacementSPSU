package in.ac.spsu.placement.placementspsu.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.ac.spsu.placement.placementspsu.Adapters.FeedJobAdapter;
import in.ac.spsu.placement.placementspsu.Adapters.ListJobAdapter;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.Model.FeedJobData;
import in.ac.spsu.placement.placementspsu.Model.ListJobData;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobFeed extends Fragment {
    private ListView listView;
    private TextView tv;
    private Server server;
    private ProgressDialog progressDialog;
    private FeedJobAdapter feedJobAdapter;
    private OnShowJobDetails onShowJobDetails;
    private SwipeRefreshLayout swipeFeed;

    public JobFeed() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        swipeFeed = view.findViewById(R.id.swipeFeed);
        server = Client.getClient().create(Server.class);
        listView = view.findViewById(R.id.listStdJobs);
        tv = view.findViewById(R.id.noJobFeed);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);
        feedJobAdapter = new FeedJobAdapter(getContext(),R.layout.feed_style);
        listView.setAdapter(feedJobAdapter);

        swipeFeed.setColorSchemeColors(getResources().getColor(R.color.blueDark),getResources().getColor(R.color.colorPrimaryDark));

        swipeFeed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createFeed();
                swipeFeed.setRefreshing(false);
            }
        });

        createFeed();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = view.findViewById(R.id.listId);
                onShowJobDetails.showJobDetail(Integer.parseInt(temp.getText().toString()));
            }
        });

        return view;
    }

    private void createFeed() {
        if(CheckNetwork.isNetworkConnected(getActivity())){
            progressDialog.show();
            Call<List<FeedJobData>> call = server.getAllCompanyJob(SharedPref.getUserName(getContext()));
            call.enqueue(new Callback<List<FeedJobData>>() {
                @Override
                public void onResponse(Call<List<FeedJobData>> call, Response<List<FeedJobData>> response) {
                    feedJobAdapter.clear();
                    List<FeedJobData> data = response.body();
                    if(data.size()==0){
                        listView.setVisibility(View.GONE);
                        tv.setText(getString(R.string.no_jobs));
                        tv.setVisibility(View.VISIBLE);
                    } else {
                        tv.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        for(FeedJobData item : data){
                            feedJobAdapter.add(item);
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<FeedJobData>> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public interface OnShowJobDetails{
        void showJobDetail(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onShowJobDetails = (OnShowJobDetails) activity;
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
