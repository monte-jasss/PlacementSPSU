package in.ac.spsu.placement.placementspsu.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.ac.spsu.placement.placementspsu.Adapters.FeedJobAdapter;
import in.ac.spsu.placement.placementspsu.Adapters.RequestAdapter;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.Model.FeedJobData;
import in.ac.spsu.placement.placementspsu.Model.RequestData;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Request extends Fragment {
    private ListView listView;
    private TextView tv;
    private Server server;
    private ProgressDialog progressDialog;
    private RequestAdapter requestAdapter;
    private SwipeRefreshLayout swipeReq;

    public Request() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        swipeReq = view.findViewById(R.id.swipeReq);
        server = Client.getClient().create(Server.class);
        listView = view.findViewById(R.id.requestList);
        tv = view.findViewById(R.id.noReq);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);
        requestAdapter = new RequestAdapter(getContext(),R.layout.request_style);
        listView.setAdapter(requestAdapter);

        swipeReq.setColorSchemeColors(getResources().getColor(R.color.blueDark),getResources().getColor(R.color.colorPrimaryDark));

        swipeReq.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createRequestList();
                swipeReq.setRefreshing(false);
            }
        });

        createRequestList();

        return view;
    }

    private void createRequestList() {
        if(CheckNetwork.isNetworkConnected(getActivity())){
            progressDialog.show();
            Call<List<RequestData>> call = server.getAppliedJobList(SharedPref.getUserName(getContext()));
            call.enqueue(new Callback<List<RequestData>>() {
                @Override
                public void onResponse(Call<List<RequestData>> call, Response<List<RequestData>> response) {
                    requestAdapter.clear();
                    List<RequestData> data = response.body();
                    if(data.size()==0){
                        listView.setVisibility(View.GONE);
                        tv.setText(getString(R.string.you_haven_t_applied_yet));
                        tv.setVisibility(View.VISIBLE);
                    } else {
                        tv.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        for(RequestData item : data){
                            requestAdapter.add(item);
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<RequestData>> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

}
