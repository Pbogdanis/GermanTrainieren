package Helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.gerproject.germantrainieren.JsonPlaceHolderApi;
import com.gerproject.germantrainieren.MainActivity;
import com.gerproject.germantrainieren.R;
import com.gerproject.germantrainieren.SaveNewWord;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.LENGTH_SHORT;
import static com.gerproject.germantrainieren.MainActivity.mContext;

public class DialogBuilder {

    public static String _username, _password;
    private Call<Boolean> _call;
    public static Boolean isAuth;



    public DialogBuilder(){
        isAuth = false;
    }


    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_signin, null);
        final EditText txtUser = v.findViewById(R.id.username);
        final EditText txtPwd = v.findViewById(R.id.password);
        builder.setTitle(R.string.app_name);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                        _username = txtUser.getText().toString();
                        _password = txtPwd.getText().toString();

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
                            Snackbar.make(v, getString(R.string.wrongCreds), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });
        return builder.create();
    }*/

    public void ShowDialog(final Activity _activity) {
        try {

            final Dialog dialog = new Dialog(_activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_signin);

            //TextView message = dialog.findViewById(R.id.text_dialog);
            final EditText txtUser = dialog.findViewById(R.id.username);
            final EditText txtPwd = dialog.findViewById(R.id.password);

            Button signinButton = dialog.findViewById(R.id.signin);
            Button cancelButton = dialog.findViewById(R.id.cancel);

            cancelButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });
            signinButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    // sign in the user ...

                    _username = txtUser.getText().toString();
                    _password = txtPwd.getText().toString();

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
                                    Snackbar.make(v, mContext.getString(R.string.wrongCreds), Snackbar.LENGTH_SHORT).show();
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
                        Snackbar.make(v, mContext.getString(R.string.wrongCreds), Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//Controlling width and height.

            //Align OK button in the center of the dialog
            signinButton.setTextAppearance(mContext, R.style.DialogCustomTextView);
            ConstraintLayout.LayoutParams neutralButtonLL = (ConstraintLayout.LayoutParams) signinButton.getLayoutParams();
            neutralButtonLL.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            signinButton.setLayoutParams(neutralButtonLL);

            //Changing dialog message appearance
            //message.setGravity(Gravity.CENTER);
            //message.setTextAppearance(mContext, R.style.DialogCustomTextView);
        }
        catch (Exception e){
            Toast.makeText(mContext,"Error in showDialog",Toast.LENGTH_SHORT).show();
        }
    }
}
