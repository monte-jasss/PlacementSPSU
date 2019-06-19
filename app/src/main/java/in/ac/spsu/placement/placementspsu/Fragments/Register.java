package in.ac.spsu.placement.placementspsu.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Register extends Fragment {
    private TextInputLayout registerName, registerEmail, registerMobile, registerPassword;
    private Button register;
    private Server server;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        registerName = view.findViewById(R.id.registerName);
        registerEmail = view.findViewById(R.id.registerEmail);
        registerMobile = view.findViewById(R.id.registerMobile);
        registerPassword = view.findViewById(R.id.registerPassword);
        register = view.findViewById(R.id.register2);
        server = Client.getClient().create(Server.class);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = registerName.getEditText().getText().toString();
                final String email = registerEmail.getEditText().getText().toString();
                String mobile = registerMobile.getEditText().getText().toString();
                String password = registerPassword.getEditText().getText().toString();
                if(name.equals("")||email.equals("")||mobile.equals("")||password.equals("")){
                    Toast.makeText(getContext(), getString(R.string.enter_all), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    if(CheckNetwork.isNetworkConnected(getActivity())){
                        Call<String> call = server.doUserRegistration(name,email,Long.parseLong(mobile),password,0);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.body().equals(getString(R.string.success))) {
                                    Toast.makeText(getContext(), getString(R.string.reg_cmp), Toast.LENGTH_SHORT).show();
                                    registerName.getEditText().setText("");
                                    registerMobile.getEditText().setText("");
                                    registerEmail.getEditText().setText("");
                                    registerPassword.getEditText().setText("");
                                    Intent i = new Intent(getContext(), Activation.class);
                                    i.putExtra("email", email);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                                else
                                    Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
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
