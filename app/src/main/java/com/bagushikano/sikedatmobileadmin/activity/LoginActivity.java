package com.bagushikano.sikedatmobileadmin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bagushikano.sikedatmobileadmin.R;
import com.bagushikano.sikedatmobileadmin.api.ApiRoute;
import com.bagushikano.sikedatmobileadmin.api.RetrofitClient;
import com.bagushikano.sikedatmobileadmin.model.AuthResponse;
import com.bagushikano.sikedatmobileadmin.model.ResponseGeneral;
import com.bagushikano.sikedatmobileadmin.util.CloseKeyboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, forgotPassButton;
    private TextInputLayout loginUsernameLayout, loginPasswordLayout;
    private TextInputEditText loginUsernameField, loginPasswordField;
    private LinearLayout loginProgressLayout;

    SharedPreferences loginPreferences, userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        forgotPassButton = findViewById(R.id.forgot_pass_button);
        loginUsernameLayout = findViewById(R.id.username_form);
        loginUsernameField = findViewById(R.id.username_text_field);
        loginPasswordLayout = findViewById(R.id.password_form);
        loginPasswordField = findViewById(R.id.password_text_field);
        loginProgressLayout = findViewById(R.id.login_progress_layout);

        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        userPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        // handler for snackbar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(
                        getWindow().getDecorView().findViewById(android.R.id.content),
                        R.string.login_screen_welcome_string,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }, 1000);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });

        forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassActivity = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(forgotPassActivity);
            }
        });

        if (loginPreferences.getInt("login_status", 0) == 1) {
            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);
            finish();
        }
    }

    public void submitData() {
        CloseKeyboard.CloseKeyboard(getCurrentFocus(), getApplicationContext());
        loginButton.setVisibility(View.GONE);
        loginProgressLayout.setVisibility(View.VISIBLE);
        ApiRoute submitData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<AuthResponse> authResponseCall = submitData.loginUser(loginUsernameField.getText().toString(), loginPasswordField.getText().toString());
        authResponseCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                loginButton.setVisibility(View.VISIBLE);
                loginProgressLayout.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getStatusCode() == 200 && response.body().getMessage().equals("Login berhasil")) {
                    SharedPreferences.Editor loginPrefEditor = loginPreferences.edit();
                    loginPrefEditor.putInt("login_status", 1);
                    loginPrefEditor.putString("token", response.body().getToken());
                    loginPrefEditor.apply();
                    sendFirebaseToken(response.body().getToken());
                    Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(homeActivity);
                    finishAffinity();
                } else if (response.code() == 200 && response.body().getStatusCode() == 401 && response.body().getMessage().equals("Username/Email/No HP atau password salah")) {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Username/email/No hp atau password salah", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loginButton.setVisibility(View.VISIBLE);
                loginProgressLayout.setVisibility(View.GONE);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.server_fail, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void sendFirebaseToken(String tokenUser) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("fcm", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.d("fcm", token);
                        ApiRoute storeToken = RetrofitClient.buildRetrofit().create(ApiRoute.class);
                        Call<ResponseGeneral> fcmTokenResponseCall = storeToken.sendFcmToken("Bearer " + tokenUser, token);
                        fcmTokenResponseCall.enqueue(new Callback<ResponseGeneral>() {
                            @Override
                            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                                if (response.code() == 200 && response.body().getMessage().equals("Berhasil memperbarui token")) {
                                    Log.d("fcm", "token fcm dikirim");
                                    FirebaseMessaging.getInstance().subscribeToTopic("all");
                                } else {
                                    Log.d("fcm", "token fcm gagal dikirim");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                                Log.d("fcm", "token fcm gagal dikirim");
                            }
                        });

                    }
                });
    }
}