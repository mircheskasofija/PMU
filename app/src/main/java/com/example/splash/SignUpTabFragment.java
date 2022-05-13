package com.example.splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpTabFragment  extends Fragment{

    EditText email, username, password, confirmpassword;
    Spinner spinner;
    Button signup;
    String _email, _username, _password, _confirmpassword, _type;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    User user;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab, container, false);

        email = root.findViewById(R.id.emailS);
        username = root.findViewById(R.id.usernameS);
        password = root.findViewById(R.id.passwordS);
        confirmpassword = root.findViewById(R.id.confirmpassword);
        signup = (Button) root.findViewById(R.id.signup_button);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();
            }
        });

        return root;
    }

    private void SignUp(){
        _email = email.getText().toString();
        _username = username.getText().toString();
        _password = password.getText().toString();
        _confirmpassword = confirmpassword.getText().toString();
        _type = (String) spinner.getSelectedItem();

        if(!_email.matches(emailPattern)){
            email.setError("Въведете коректен емаил!");
        }
        else if(_password.isEmpty() || _password.length() < 6){
            password.setError("Паролата не може да бъде по-малка от 6 знака!");
        }
        else if(!_password.equals(_confirmpassword)){
            confirmpassword.setError("Паролите не съвпадат!");
        }
        else{
            progressDialog.setMessage("Моля изчакайте...");
            progressDialog.setTitle("Регистрация");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        user = new User(_username, _email, _password, _type);
                        FirebaseUser newUser = mAuth.getCurrentUser();
                        FirebaseDatabase.getInstance().getReference("users").child(newUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    //sendUserToNextActivity();
                                    Toast.makeText(getActivity(), "Успешна Регистрация!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(), "Плъзнете надясно, за да влезете в акаунта си!", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Неуспещно", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getActivity(), "Неуспешно", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
