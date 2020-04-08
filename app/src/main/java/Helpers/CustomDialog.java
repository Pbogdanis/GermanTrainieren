package Helpers;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gerproject.germantrainieren.JsonPlaceHolderApi;
import com.gerproject.germantrainieren.MainActivity;
import com.gerproject.germantrainieren.R;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gerproject.germantrainieren.MainActivity.mContext;

public class CustomDialog extends Activity implements View.OnClickListener {

    public static String _username, _password;
    private Call<Boolean> _call;
    public static Boolean isAuth;
    private EditText _txtUser, _txtPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signin);



        _txtUser = findViewById(R.id.username);
        _txtPwd = findViewById(R.id.password);

        Button signinButton = findViewById(R.id.signin);
        Button cancelButton = findViewById(R.id.cancel);

        //Dialog size
        //get Screen sizes
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        /*//Change layout size
        View view = findViewById(R.id.layout);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height/2;
        view.setLayoutParams(layoutParams);*/

        //Align OK button in the center of the dialog
        /*signinButton.setTextAppearance(mContext, R.style.DialogCustomTextView);
        ConstraintLayout.LayoutParams neutralButtonLL = (ConstraintLayout.LayoutParams) signinButton.getLayoutParams();
        neutralButtonLL.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        signinButton.setLayoutParams(neutralButtonLL);*/
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {

            case R.id.cancel:
                finish();
                break;

            case R.id.signin:
                // sign in the user ...

                _username = _txtUser.getText().toString();
                _password = _txtPwd.getText().toString();

                if(!_username.isEmpty() && !_password.isEmpty()){
                    //Call LOGIN api method
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new BasicAuthInterceptor(_username, _password))
                            .build();

                    //Make post call to API
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(mContext.getResources().getString(R.string.api_url))
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    JsonPlaceHolderApi _jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                    _call = _jsonPlaceHolderApi.GetLogin();

                    _call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(!response.isSuccessful()){
                                //Not successfull
                                Snackbar.make(v, getString(R.string.wrongCreds), Snackbar.LENGTH_SHORT).show();
                                isAuth = false;
                            } else {
                                isAuth = true;
                                MainActivity.saveCred();
                                MainActivity.startAc();
                                MainActivity.dismissDialog();

                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            //Not successfull
                            isAuth = false;
                        }
                    });

                } else {
                    Snackbar.make(v, this.getString(R.string.wrongCreds), Snackbar.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
