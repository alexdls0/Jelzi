package com.example.jelzi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jelzi.model.LoadingDialog;
import com.example.jelzi.model.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

public class Register extends AppCompatActivity {

    private static final String TAG = "Jelzi.log.info";
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText etUsername, etEmail, etPassword;
    private Button btRegister;
    private TextView alreadyMember, singIn;
    private final Utils utils = new Utils();
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        loadingDialog = new LoadingDialog(Register.this);
        mAuth = FirebaseAuth.getInstance();
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btRegister = findViewById(R.id.btRegister);
        alreadyMember = findViewById(R.id.tvAlreadyMember);
        singIn = findViewById(R.id.tvSignIn);

        if(mAuth.getCurrentUser() != null){
            goLogin();
        }

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString();

                if(!username.isEmpty() && !email.isEmpty() && !pass.isEmpty()){
                    if(utils.checkUsername(username)){
                        if(pass.length() >= 6){
                            if(utils.checkEmail(email)){
                                if (utils.isConnected(Register.this)){
                                    doRegister(email, pass, username);
                                }else{
                                    TastyToast.makeText(Register.this, getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setTitle(getString(R.string.invalidemail));
                                builder.setMessage(getString(R.string.emailerror));
                                builder.setPositiveButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        etEmail.setText("");
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                dialog.setCanceledOnTouchOutside(false);
                            }
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                            builder.setTitle(getString(R.string.shortpasserror));
                            builder.setMessage(getString(R.string.passerror));
                            builder.setPositiveButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etPassword.setText("");
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(false);
                        }
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                        builder.setTitle(getString(R.string.invalidusername));
                        builder.setMessage(getString(R.string.usernameerror));
                        builder.setPositiveButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etUsername.setText("");
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle(getString(R.string.nodata));
                    builder.setMessage(getString(R.string.nodataerror));
                    builder.setPositiveButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }

            }
        });

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();
            }
        });

        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();
            }
        });
    }

    private void doRegister(String email, String pass,final String name) {
        loadingDialog.startLoadingDialog();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "userRegistration:success");
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("users/"+mAuth.getCurrentUser().getUid()+"/userName").setValue(name);
                    loadingDialog.endLoadingDialog();
                    goLogin();
                } else {
                    Log.w(TAG, "userRegistration:failure", task.getException());
                    loadingDialog.endLoadingDialog();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle(getString(R.string.errorregistration));
                    builder.setMessage(getString(R.string.registrationerror));
                    builder.setPositiveButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etEmail.setText("");
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }
            }
        });
    }

    public void goLogin(){
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}