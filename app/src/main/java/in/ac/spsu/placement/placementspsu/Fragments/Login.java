package in.ac.spsu.placement.placementspsu.Fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import in.ac.spsu.placement.placementspsu.Activities.Admin;
import in.ac.spsu.placement.placementspsu.Activities.Company;
import in.ac.spsu.placement.placementspsu.Activities.Home;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {
    private TextInputLayout loginEmail, loginPassword;
    private Button login;
    private Server server;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);
        login = view.findViewById(R.id.login);
        server = Client.getClient().create(Server.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);

        login.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                final String email = loginEmail.getEditText().getText().toString();
                String password = loginPassword.getEditText().getText().toString();
                if(email.equals("")||password.equals("")){
                    Toast.makeText(getContext(), getString(R.string.enter_all), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    if(CheckNetwork.isNetworkConnected(getActivity())){
                        Call<String> call = server.getAuthentication(email,password);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.body().equals(getString(R.string.not))){
                                    Toast.makeText(getContext(), getString(R.string.user_not), Toast.LENGTH_SHORT).show();
                                }
                                else if(response.body().equals(getString(R.string.wrong))){
                                    Toast.makeText(getContext(), getString(R.string.wrong_pass), Toast.LENGTH_SHORT).show();
                                }
                                else if(response.body().equals(getString(R.string.active))){
                                    Toast.makeText(getContext(), getString(R.string.activate_acc), Toast.LENGTH_SHORT).show();
                                }
                                else if(response.body().equals(getString(R.string.student))) {
                                    if(SharedPref.setUserName(getContext(),email) && SharedPref.setUserType(getContext(),0)){
                                        startActivity(new Intent(getContext(), Home.class));
                                        getActivity().finish();
                                    }
                                }
                                else if(response.body().equals(getString(R.string.company))) {
                                    if(SharedPref.setUserName(getContext(),email) && SharedPref.setUserType(getContext(),1)) {
                                        startActivity(new Intent(getContext(), Company.class));
                                        getActivity().finish();
                                    }
                                }
                                else if(response.body().equals(getString(R.string.adm))) {
                                    if(SharedPref.setUserName(getContext(),email) && SharedPref.setUserType(getContext(),2)) {
                                        startActivity(new Intent(getContext(), Admin.class));
                                        getActivity().finish();
                                    }
                                }
                                else
                                    Toast.makeText(getContext(), getString(R.string.log_fld), Toast.LENGTH_SHORT).show();
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
        });

        return view;
    }
}
