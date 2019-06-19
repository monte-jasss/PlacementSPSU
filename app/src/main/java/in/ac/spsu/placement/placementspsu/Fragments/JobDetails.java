package in.ac.spsu.placement.placementspsu.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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

public class JobDetails extends Fragment {
    private int id;
    private Server server;
    private ProgressDialog progressDialog;
    private TextView title, comp, decs, req, opening, lastDate;
    private Button apply;

    public JobDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_details, container, false);

        title = view.findViewById(R.id.detailTitle);
        comp = view.findViewById(R.id.detailCompany);
        decs = view.findViewById(R.id.detailDesc);
        req = view.findViewById(R.id.detailReq);
        apply = view.findViewById(R.id.jobApply);
        opening = view.findViewById(R.id.detailOpen);
        lastDate = view.findViewById(R.id.detailLastDate);
        server = Client.getClient().create(Server.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);

        if(CheckNetwork.isNetworkConnected(getActivity())){
            progressDialog.show();
            Call<FeedJobData> call = server.getJobDetails(id);
            call.enqueue(new Callback<FeedJobData>() {
                @Override
                public void onResponse(Call<FeedJobData> call, Response<FeedJobData> response) {
                    FeedJobData data = response.body();
                    if(data!=null){
                        title.setText(data.getName());
                        comp.setText(data.getTitle());
                        decs.setText(data.getDescription());
                        req.setText(data.getRequirement());
                        opening.setText(data.getOpening());
                        lastDate.setText(data.getLastdate());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<FeedJobData> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<String> call = server.doApplyForJob(id, SharedPref.getUserName(getContext()));
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String data = response.body();
                        if(data.equals(getString(R.string.success))){
                            Toast.makeText(getContext(), getString(R.string.application), Toast.LENGTH_SHORT).show();
                        } else if(data.equals(getString(R.string.applied))) {
                            Toast.makeText(getContext(), getString(R.string.already), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    public void setJobId(int id){
        this.id = id;
    }
}
