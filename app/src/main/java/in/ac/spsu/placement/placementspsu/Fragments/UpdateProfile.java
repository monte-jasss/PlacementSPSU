package in.ac.spsu.placement.placementspsu.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import in.ac.spsu.placement.placementspsu.Activities.Activation;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfile extends Fragment {
    private TextInputLayout name, fname, course, semester, _10, _12, cgpa, skill, project, training, certificate;
    private Button update;
    private RadioGroup updateIntGrp;
    RadioButton rb;
    private Server server;
    private ProgressDialog progressDialog;

    public UpdateProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        name = view.findViewById(R.id.updateName);
        fname = view.findViewById(R.id.updateFName);
        course = view.findViewById(R.id.updateCourse);
        semester = view.findViewById(R.id.updateSemester);
        _10 = view.findViewById(R.id.update10);
        _12 = view.findViewById(R.id.update12);
        cgpa = view.findViewById(R.id.updateCGPA);
        skill = view.findViewById(R.id.updateSkill);
        project = view.findViewById(R.id.updateProject);
        training = view.findViewById(R.id.updateTraining);
        certificate = view.findViewById(R.id.updateCertificate);
        updateIntGrp = view.findViewById(R.id.updateIntGrp);
        update = view.findViewById(R.id.updateSave);
        server = Client.getClient().create(Server.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = SharedPref.getUserName(getContext());
                String na = name.getEditText().getText().toString();
                String fn = fname.getEditText().getText().toString();
                String co = course.getEditText().getText().toString();
                String sm = semester.getEditText().getText().toString();
                String _0 = _10.getEditText().getText().toString();
                String _2 = _12.getEditText().getText().toString();
                String cg = cgpa.getEditText().getText().toString();
                String sk = skill.getEditText().getText().toString();
                String pr = project.getEditText().getText().toString();
                String tr = training.getEditText().getText().toString();
                String ce = certificate.getEditText().getText().toString();
                rb = view.findViewById(updateIntGrp.getCheckedRadioButtonId());
                String in = rb.getText().toString();
                if(na.equals("")||fn.equals("")||co.equals("")||sm.equals("")||_0.equals("")||_2.equals("")||cg.equals("")||sk.equals("")||pr.equals("")||tr.equals("")||ce.equals("")||in.equals("")){
                    Toast.makeText(getContext(), getString(R.string.enter_all), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    if(CheckNetwork.isNetworkConnected(getActivity())){
                        Call<String> call = server.updateStudentProfile(em,na,fn,co,sm,_0,_2,cg,in,sk,pr,tr,ce);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.body().equals(getString(R.string.success))) {
                                    name.getEditText().setText("");
                                    fname.getEditText().setText("");
                                    course.getEditText().setText("");
                                    semester.getEditText().setText("");
                                    _10.getEditText().setText("");
                                    _12.getEditText().setText("");
                                    cgpa.getEditText().setText("");
                                    skill.getEditText().setText("");
                                    project.getEditText().setText("");
                                    training.getEditText().setText("");
                                    certificate.getEditText().setText("");
                                    hideKeyboard(getActivity());
                                    Toast.makeText(getContext(), getString(R.string.prof_created), Toast.LENGTH_SHORT).show();
                                } else if(response.body().equals(getString(R.string.updated))) {
                                    name.getEditText().setText("");
                                    fname.getEditText().setText("");
                                    course.getEditText().setText("");
                                    semester.getEditText().setText("");
                                    _10.getEditText().setText("");
                                    _12.getEditText().setText("");
                                    cgpa.getEditText().setText("");
                                    skill.getEditText().setText("");
                                    project.getEditText().setText("");
                                    training.getEditText().setText("");
                                    certificate.getEditText().setText("");
                                    hideKeyboard(getActivity());
                                    Toast.makeText(getContext(), getString(R.string.prof_updated), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    hideKeyboard(getActivity());
                                    Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getContext(), getString(R.string.something)+t.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
