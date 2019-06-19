package in.ac.spsu.placement.placementspsu.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import in.ac.spsu.placement.placementspsu.Activities.Activation;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.DateFormatter;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddJob extends Fragment {
    private DatePickerDialog.OnDateSetListener mdate;
    private TextInputLayout titleJob, descriptionJob, requirementJob, openingJob;
    private Button addJob;
    private TextView date;
    private Server server;
    private ProgressDialog progressDialog;

    public AddJob() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_job, container, false);

        date = view.findViewById(R.id.datepickerJob);
        titleJob = view.findViewById(R.id.jobTitle);
        descriptionJob = view.findViewById(R.id.jobDescription);
        requirementJob = view.findViewById(R.id.jobRequirment);
        openingJob = view.findViewById(R.id.jobOpening);
        addJob = view.findViewById(R.id.addJob);
        server = Client.getClient().create(Server.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,mdate,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mdate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(DateFormatter.dateToString(dayOfMonth,month,year));
            }
        };

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleJob.getEditText().getText().toString();
                String desc = descriptionJob.getEditText().getText().toString();
                String reqr = requirementJob.getEditText().getText().toString();
                String open = openingJob.getEditText().getText().toString();
                String lastdate = date.getText().toString();
                String email = SharedPref.getUserName(getContext());
                if(title.equals("")||desc.equals("")||reqr.equals("")||open.equals("")||lastdate.equals(R.string.select_date)){
                    Toast.makeText(getContext(), getString(R.string.enter_all), Toast.LENGTH_SHORT).show();
                } else {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    String today = DateFormatter.dateToString(day,month,year);
                    if(DateFormatter.stringToDate(lastdate).before(DateFormatter.stringToDate(today))){
                        Toast.makeText(getContext(), getString(R.string.enter_date), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        if(CheckNetwork.isNetworkConnected(getActivity())){
                            Call<String> call = server.doAddJob(email,title,desc,reqr,open,lastdate);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.body().equals(getString(R.string.success))) {
                                        titleJob.getEditText().setText("");
                                        descriptionJob.getEditText().setText("");
                                        requirementJob.getEditText().setText("");
                                        openingJob.getEditText().setText("");
                                        date.setText(R.string.select_date);
                                        SharedPref.getUserName(getContext());
                                        Toast.makeText(getContext(), getString(R.string.job_crtd), Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(getContext(), getString(R.string.job_fld), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(getContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }
                }
            }
        });

        return view;
    }
}
