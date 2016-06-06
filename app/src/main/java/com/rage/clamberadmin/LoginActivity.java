package com.rage.clamberadmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Allows admin user to enter their username
 */
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    @Bind(R.id.login_activity_username_edit_text)
    protected EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_activity_submit_button)
    public void onSubmitClicked(){
        String username = usernameEditText.getText().toString();

        //Check for network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Call<Boolean> userCall = ApiManager.getClamberService().getAdminUser(username);
            userCall.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.body()){
                        Intent intent = new Intent(LoginActivity.this, AppActivity.class);
                        startActivity(intent);
                      }
                    else {
                        Log.d(TAG, getString(R.string.user_does_not_exist));
                        Toast.makeText(LoginActivity.this, R.string.user_does_not_exist, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.d(TAG, "Failure");
                }
            });

        }
    }
}
