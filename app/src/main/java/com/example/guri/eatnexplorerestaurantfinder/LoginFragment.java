package com.example.guri.eatnexplorerestaurantfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private LoginFragment.OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private View rootView;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegisterLink, btnBackLogin;
    private String userEmail, userPassword;

    Button googleSignBtn;
    private static final int RC_SIGN_IN = 2;
    private GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mAccount;

    @Override
    public void onStart() {
        super.onStart();
        mAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        mAuth.addAuthStateListener(mAuthListener);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {

        /*Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);*/
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_login, container, false);
        etEmail = rootView.findViewById(R.id.etEnterUserEmail);
        etPassword = rootView.findViewById(R.id.etEnterUserPassword);
        btnLogin = rootView.findViewById(R.id.btnUserLogin);
        btnBackLogin = rootView.findViewById(R.id.buttonBackLogin);
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();
        btnRegisterLink = rootView.findViewById(R.id.btnRegisterLink);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onLoginFragmentInteraction();
            }
        });

        btnRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });

        googleSignBtn = rootView.findViewById(R.id.btnGoogleSignIn);

        final FirebaseUser user = mAuth.getCurrentUser();
        mAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    googleSignBtn.setText("Sign Out");
                }
                else {
                    Toast.makeText(getContext(), "Sign Out successfull", Toast.LENGTH_SHORT).show();
                    googleSignBtn.setText(R.string.google);
                }
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.oAuthClientId))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null){
                    signOut();
                }
                else if(mAuth.getCurrentUser() == null){
                    /** Session management with sharedPref*/
                    SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharePref.edit();
                    editor.putBoolean("isLoggedInKey", true);
                    editor.commit();
                    signIn();
                }
            }
        });

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HomeScreenMainActivity.class));
            }
        });

        if (mAuth.getCurrentUser() != null){
            etEmail.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
        }

        return rootView;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mAuth.signOut();
                    if(mAuth.getCurrentUser() == null) {
                        etEmail.setVisibility(View.VISIBLE);
                        etPassword.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.VISIBLE);
                    }
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            Toast.makeText(getContext(), "Welcome " + account.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), HomeScreenMainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    public void onLoginFragmentInteraction()
    {
        userEmail = etEmail.getText().toString().trim();
        userPassword = etPassword.getText().toString().trim();

        try {
            if (userEmail.isEmpty()) {
                Toast toast = Toast.makeText(getContext(), "UserName cannot be Empty", Toast.LENGTH_SHORT);
                toast.show();
            } else if (userPassword.isEmpty()) {
                Toast toast = Toast.makeText(getContext(), "Password cannot be Empty", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            /** Session management with sharedPref*/
                            SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            SharedPreferences.Editor editor = sharePref.edit();
                            editor.putBoolean("isLoggedInKey", true);
                            editor.commit();
                            startActivity(new Intent(getContext(), HomeScreenMainActivity.class));
                            getActivity().finish();
                        } else {
                            Toast toast = Toast.makeText(getContext(), "Email or Password is Wrong", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void openRegisterActivity(){
        FragmentManager fragmentManager = getFragmentManager();
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment fragment = (LoginFragment) fragmentManager.findFragmentById(R.id.fragment_login_container);
        if(fragment != null){
            fragmentTransaction.replace(R.id.fragment_login_container, registerFragment);
            fragmentTransaction.addToBackStack(null).commit();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onLoginFragmentInteraction();
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
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
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
        void onLoginFragmentInteraction();
    }
}
