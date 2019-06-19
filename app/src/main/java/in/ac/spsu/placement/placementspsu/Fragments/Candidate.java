package in.ac.spsu.placement.placementspsu.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.ac.spsu.placement.placementspsu.Adapters.ListJobAdapter;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Candidate extends Fragment {
    private ListView listView;
    private TextView tv, name, fname, course, semester, _10, _12, cgpa, skill, project, training, certificate, interest;
    private Server server;
    Button select, reject;
    private LinearLayout profTrue, profFalse;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeProf;
    private int std_id,id;

    public Candidate() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candidate, container, false);

        swipeProf = view.findViewById(R.id.swipeProf);
        server = Client.getClient().create(Server.class);
        listView = view.findViewById(R.id.listJobs);
        tv = view.findViewById(R.id.noJob);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);

        select = view.findViewById(R.id.select);
        reject = view.findViewById(R.id.reject);

        profTrue = view.findViewById(R.id.isProfileTrue);
        profFalse = view.findViewById(R.id.isProfileFalse);
        name = view.findViewById(R.id.profName);
        fname = view.findViewById(R.id.profFather);
        course = view.findViewById(R.id.profCourse);
        semester = view.findViewById(R.id.profSemester);
        _10 = view.findViewById(R.id.prof_10);
        _12 = view.findViewById(R.id.prof_12);
        cgpa = view.findViewById(R.id.profCGPA);
        skill = view.findViewById(R.id.profSkill);
        project = view.findViewById(R.id.profProject);
        training = view.findViewById(R.id.profTraining);
        certificate = view.findViewById(R.id.profCertificate);
        interest = view.findViewById(R.id.profInterest);

        swipeProf.setColorSchemeColors(getResources().getColor(R.color.blueDark),getResources().getColor(R.color.colorPrimaryDark));

        swipeProf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createProfile();
                swipeProf.setRefreshing(false);
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetwork.isNetworkConnected(getContext())){
                    Call<String> call = server.sendStatus(1,id);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String data = response.body();
                            if (data.equals(getString(R.string.success))){
                                Toast.makeText(getContext(), name.getText()+" "+getString(R.string.is_short), Toast.LENGTH_SHORT).show();
                            } else if(data.equals(getString(R.string.not))) {
                                Toast.makeText(getContext(), name.getText()+" "+getString(R.string.is_alre), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetwork.isNetworkConnected(getContext())){
                    Call<String> call = server.sendStatus(2,id);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String data = response.body();
                            if (data.equals(getString(R.string.success))){
                                Toast.makeText(getContext(), name.getText()+" "+getString(R.string.is_rej), Toast.LENGTH_SHORT).show();
                            } else if(data.equals(getString(R.string.not))) {
                                Toast.makeText(getContext(), name.getText()+" "+getString(R.string.is_arej), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        createProfile();

        return view;
    }

    private void createProfile() {
        if(CheckNetwork.isNetworkConnected(getActivity())){
            Call<List<String>> call = server.getStudentProfileById(std_id);
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    List<String> list = response.body();
                    if(list.get(0).equals(getString(R.string.not))){
                        profFalse.setVisibility(View.VISIBLE);
                        profTrue.setVisibility(View.GONE);
                    } else {
                        profFalse.setVisibility(View.GONE);
                        profTrue.setVisibility(View.VISIBLE);
                        name.setText(list.get(0));
                        fname.setText(list.get(1));
                        course.setText(list.get(2));
                        semester.setText(list.get(3));
                        _10.setText(list.get(4));
                        _12.setText(list.get(5));
                        cgpa.setText(list.get(6));
                        interest.setText(list.get(7));
                        skill.setText(list.get(8));
                        project.setText(list.get(9));
                        training.setText(list.get(10));
                        certificate.setText(list.get(11));
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    profFalse.setVisibility(View.VISIBLE);
                    profTrue.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void setId(int std_id,int id) {
        this.std_id = std_id;
        this.id = id;
    }

}
