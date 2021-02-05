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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

public class Login extends AppCompatActivity {

    private static final String TAG = "Jelzi.log.info";
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button btLogin;
    private TextView notMember, forgotPass, signUp;
    private final Utils utils = new Utils();
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        loadingDialog = new LoadingDialog(Login.this);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        notMember = findViewById(R.id.tvNotMember);
        forgotPass = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvSignUp);

        if(mAuth.getCurrentUser() != null){
            loadingDialog.startLoadingDialog();
            hasTracing();
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()){
                    if (utils.isConnected(Login.this)){
                        doLogin(email, pass);
                    }else{
                        TastyToast.makeText(Login.this, getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle(getString(R.string.nodata));
                    builder.setMessage(getString(R.string.nodataloginerror));
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

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doForgotPass();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });

        notMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });
    }

    private void doLogin(String email, String pass) {
        loadingDialog.startLoadingDialog();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "userLogin:success");
                            hasTracing();
                        } else {
                            Log.w(TAG, "userLogin:failure", task.getException());
                            loadingDialog.endLoadingDialog();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle(getString(R.string.errorlogin));
                            builder.setMessage(getString(R.string.loginerror));
                            builder.setPositiveButton(getString(R.string.agree), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    etEmail.setText("");
                                    etPassword.setText("");
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(false);
                        }
                    }
                });
    }

    public void goMain(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        loadingDialog.endLoadingDialog();
        startActivity(intent);
        finish();
    }

    public void hasTracing(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("tracing")){
                    goTracing();
                }
                else{
                    goMain();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void goTracing(){
        Intent intent = new Intent(Login.this, Tracing.class);
        loadingDialog.endLoadingDialog();
        startActivity(intent);
        finish();
    }

    public void goRegister(){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void doForgotPass(){
        final EditText forgotpassemail = new EditText(Login.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle(getString(R.string.resetpass));
        builder.setMessage(getString(R.string.resetpassdesc));
        builder.setView(forgotpassemail);
        builder.setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = forgotpassemail.getText().toString();
                if(!email.isEmpty()){
                    if (utils.isConnected(Login.this)){
                        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                TastyToast.makeText(Login.this, getString(R.string.emailsent), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                TastyToast.makeText(Login.this, getString(R.string.emailsenterror), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        });
                    }else{
                        TastyToast.makeText(Login.this, getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }
                }
            }
        });
        builder.setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
}