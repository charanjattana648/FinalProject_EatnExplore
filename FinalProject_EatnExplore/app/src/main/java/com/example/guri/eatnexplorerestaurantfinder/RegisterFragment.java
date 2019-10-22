package com.example.guri.eatnexplorerestaurantfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private View rootView;
    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnRegisterUser, btnLoginLink, btnBackRegister;
    private String userEmail, userPassword, userConfirmPassword;
    private boolean uppercaseText = false, lowercaseText = false, numberText = false;
    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {

       /* Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();*/
        //fragment.setArguments(args);
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_register, container, false);
        etEmail = rootView.findViewById(R.id.etEnterEmail);
        etPassword = rootView.findViewById(R.id.etEnterPassword);
        etConfirmPassword = rootView.findViewById(R.id.etConfirmPassword);
        btnLoginLink = rootView.findViewById(R.id.btnLoginLink);
        btnBackRegister = rootView.findViewById(R.id.buttonBackRegister);
        mAuth = FirebaseAuth.getInstance();
        btnRegisterUser = rootView.findViewById(R.id.btnRegisterUser);
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        btnLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        btnBackRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HomeScreenMainActivity.class));
            }
        });

        return rootView;
    }

    public void openLoginActivity(){
        FragmentManager fragmentManager = getFragmentManager();
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RegisterFragment registerFragment = (RegisterFragment) fragmentManager.findFragmentById(R.id.fragment_register_container);
        //LoginFragment loginFragment1 = (LoginFragment) fragmentManager.findFragmentById(R.id.fragment_login_container);
        if(registerFragment != null) {


        }
            fragmentTransaction.replace(R.id.fragment_login_container, loginFragment);
            fragmentTransaction.addToBackStack(null).commit();

    }

    public void registerUser(){
        userEmail = etEmail.getText().toString().trim();
        userPassword = etPassword.getText().toString().trim();
        userConfirmPassword = etConfirmPassword.getText().toString().trim();
        try{
            if(userEmail.isEmpty()){
                Toast.makeText(getContext(), "Enter user email", Toast.LENGTH_SHORT).show();
            }
            else if(userPassword.isEmpty()){
                Toast.makeText(getContext(), "Enter password", Toast.LENGTH_SHORT).show();
            }
            else if(!userPassword.equals(userConfirmPassword)){
                Toast.makeText(getContext(), "Password does not match", Toast.LENGTH_SHORT).show();
            }
            else if(userPassword.length() < 8){
                Toast.makeText(getContext(), "Password should be 8 characters", Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getContext(), HomeScreenMainActivity.class));
                        }
                        else {
                            Toast.makeText(getContext(), "Try with a different email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.registerUser();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void registerUser();
    }
}
