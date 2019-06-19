package in.ac.spsu.placement.placementspsu.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.Helper.SwitchFragment;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    Context context;
    private TextView name, fname, course, semester, _10, _12, cgpa, skill, project, training, certificate, interest;
    private Server server;
    private ProgressDialog progressDialog;
    private LinearLayout profTrue, profFalse;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private SwipeRefreshLayout swipeProf;

    public Profile() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        swipeProf = view.findViewById(R.id.swipeProf);
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
        server = Client.getClient().create(Server.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);
        builder = new AlertDialog.Builder(getContext());

        builder.setCancelable(false);
        builder.setTitle(getString(R.string.not_found));
        builder.setMessage(getString(R.string.do_create));
        builder.setPositiveButton(getString(R.string.create),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SwitchFragment.replaceFragment(new UpdateProfile(),getFragmentManager());
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();

        swipeProf.setColorSchemeColors(getResources().getColor(R.color.blueDark),getResources().getColor(R.color.colorPrimaryDark));

        swipeProf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createProfile();
                swipeProf.setRefreshing(false);
            }
        });

        progressDialog.show();
        createProfile();

        return view;
    }

    private void createProfile() {
        if(CheckNetwork.isNetworkConnected(getActivity())){
            Call<List<String>> call = server.getStudentProfile(SharedPref.getUserName(getContext()));
            call.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    List<String> list = response.body();
                    if(list.get(0).equals(getString(R.string.not))){
                        profFalse.setVisibility(View.VISIBLE);
                        profTrue.setVisibility(View.GONE);
                        dialog.show();
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
                    dialog.show();
                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }
}