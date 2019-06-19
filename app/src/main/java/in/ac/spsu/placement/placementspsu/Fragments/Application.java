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

import in.ac.spsu.placement.placementspsu.Adapters.CandidateAdapter;
import in.ac.spsu.placement.placementspsu.Adapters.FeedJobAdapter;
import in.ac.spsu.placement.placementspsu.Adapters.ListJobAdapter;
import in.ac.spsu.placement.placementspsu.Adapters.RequestAdapter;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.Model.CandidateData;
import in.ac.spsu.placement.placementspsu.Model.RequestData;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Application extends Fragment {
    private ListView listView;
    private TextView tv;
    private Server server;
    private ProgressDialog progressDialog,progress;
    private CandidateAdapter listAdapter;
    private SwipeRefreshLayout swipe;
    private int id;
    private OnShowCandidateProfile onShowCandidateProfile;

    public Application() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, container, false);

        swipe = view.findViewById(R.id.swipe);
        server = Client.getClient().create(Server.class);
        listView = view.findViewById(R.id.candidateList);
        tv = view.findViewById(R.id.noApp);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);
        progress = new ProgressDialog(getContext());
        progress.setMessage(getString(R.string.dele));
        progress.setCancelable(false);
        listAdapter = new CandidateAdapter(getContext(),R.layout.request_style);
        listView.setAdapter(listAdapter);
        //registerForContextMenu(listView);

        id = getArguments().getInt("id");

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createApplicationList();
                swipe.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = view.findViewById(R.id.item_no);
                TextView temp2 = view.findViewById(R.id.item_no_2);
                onShowCandidateProfile.showCandidateProfile(Integer.parseInt(temp.getText().toString()),
                        Integer.parseInt(temp2.getText().toString()));
            }
        });

        createApplicationList();

        return view;
    }

    private void createApplicationList() {
        if(CheckNetwork.isNetworkConnected(getActivity())){
            progressDialog.show();
            Call<List<CandidateData>> call = server.getApplicationList(id);
            call.enqueue(new Callback<List<CandidateData>>() {
                @Override
                public void onResponse(Call<List<CandidateData>> call, Response<List<CandidateData>> response) {
                    listAdapter.clear();
                    List<CandidateData> data = response.body();
                    if(data.size()==0){
                        listView.setVisibility(View.GONE);
                        tv.setText(getString(R.string.no_application_recieved_yet));
                        tv.setVisibility(View.VISIBLE);
                    } else {
                        tv.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        for(CandidateData item : data){
                            listAdapter.add(item);
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<CandidateData>> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public interface OnShowCandidateProfile{
        void showCandidateProfile(int std_id, int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onShowCandidateProfile = (OnShowCandidateProfile) activity;
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
