package com.example.splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.splash.guide.GuideActivity;
import com.example.splash.tourist.TouristActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginTabFragment extends Fragment {

    EditText email, password;
    TextView message;
    Button login;
    String _email, _password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    User user;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference databaseReference;

    float v = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab, container, false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        message = root.findViewById(R.id.signupmessage);
        login = root.findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn();
            }
        });

        email.setTranslationX(800);
        password.setTranslationX(800);
        message.setTranslationY(200);
        login.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        message.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        message.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(1300).start();

        return root;
    }

    private void LogIn() {
        _email = email.getText().toString();
        _password = password.getText().toString();

        if(!_email.matches(emailPattern)){
            email.setError("Въведете коректен емаил!");
        }
        else if(_password.isEmpty() || _password.length() < 6){
            password.setError("Грешна парола!");
        }
        else{
            progressDialog.setMessage("Моля изчакайте...");
            progressDialog.setTitle("Влизане в акаунта...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(_email, _password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Успешно!", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+user.getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User value = snapshot.getValue(User.class);
                                if(value.getType().equals("Турист")){
                                    sendTouristToNextActivity();
                                }
                                else if (value.getType().equals("Водач")){
                                    sendGuideToNextActivity();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Неуспешно", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else{
                        Toast.makeText(getActivity(), "Неуспешно", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendTouristToNextActivity() {
        Intent intent = new Intent(getActivity(), TouristActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendGuideToNextActivity() {
        Intent intent = new Intent(getActivity(), GuideActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
