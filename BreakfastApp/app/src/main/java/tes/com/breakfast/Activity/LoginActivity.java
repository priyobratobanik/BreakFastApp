package tes.com.breakfast.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import tes.com.breakfast.DTO.User;
import tes.com.breakfast.DatabaseManagement.Login;
import tes.com.breakfast.DatabaseManagement.UserStatus;
import tes.com.breakfast.Exception.BreakfastException;
import tes.com.breakfast.R;
import tes.com.breakfast.Util.DataValidation;
import tes.com.breakfast.Util.MessageDisplay;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername,mPassword;
    private TextView mForgotPassword;
    private UserStatus mStatus;
    private User mCurrentUser;

    /**
     * All initializations are done here
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Variables to be initialized
        //Objects to be created here
        doVariableInitialization();

        //Check if User is Already logged In
        try
        {
            if(mStatus.hasLogin())
            {
                MessageDisplay.successMessage("You are Already Logged In !!!", LoginActivity.this);

                //Transfer the control to the User Home Screen
                Intent transferIntent=new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(transferIntent);
                //finish();
            }
        }catch (BreakfastException ex)
        {
            MessageDisplay.errorMessage(ex.getMessage(),LoginActivity.this);
        }

    }

    /**
     * Member variables initialized here
     * Objects are created
     */
    private void doVariableInitialization() {
        mUsername = (EditText) findViewById(R.id.loginPageUsername);
        mPassword = (EditText) findViewById(R.id.loginPagePassword);
        mForgotPassword=(TextView)findViewById(R.id.loginPageForgotPassword);
        mStatus=new UserStatus(LoginActivity.this);
    }

    /**
     * On Login button click method
     *
     * @param view
     */
    public void doLoginFromView(View view)
    {
        //Fetch Data
        String username=mUsername.getText().toString();
        String password=mPassword.getText().toString();

        //Data Validation
        if(!DataValidation.validateUserName(username))
            MessageDisplay.errorMessage("Username Cannot be Empty !!",LoginActivity.this);
        else if(!DataValidation.validatePassword(password))
            MessageDisplay.errorMessage("Password Invalid !!",LoginActivity.this);
        else
        {
            //Data Validated
            //Creating a new thread to do Login
            new AsyncLoginTask().execute(username,password);

        }
    }

    /**
     * Remove from the Screen
     * @param view
     */
    public void goBackFromView(View view)
    {
        //Transfer Control to Launcher Screen
        Intent transferIntent=new Intent(LoginActivity.this,AppLaunchActivity.class);
        startActivity(transferIntent);
        finish();
    }

    /**
     * Transfer Control to Forgot Password Screen
     * @param view
     */
    public void forgotPasswordFromView(View view)
    {
        Intent transferIntent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
        startActivity(transferIntent);
        finish();
    }

    /**
     * Parallel Thread to do Login in Background
     */
    private  class AsyncLoginTask extends AsyncTask<String,String,User>
    {
        private Login userLogin;
        private ProgressDialog progress=new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute()
        {
            progress.setMessage("Searching Database ... ");
            progress.show();

            //Stop User Interaction
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            userLogin=new Login(LoginActivity.this);
        }

        @Override
        protected User doInBackground(String... payload)
        {
            try {
                try {
                    return userLogin.doLogin(payload[0], payload[1]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (BreakfastException ex) {
                publishProgress(ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String ...values)
        {
            progress.setMessage(values[values.length-1]);
        }

        @Override
        protected void onPostExecute(User result)
        {
            progress.dismiss();
            //Start User Interaction
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if(result.getmUserName().equals("NotPresent"))
            {
                MessageDisplay.errorMessage("Invalid Username or Password !!!",LoginActivity.this);
            }
            else
            {
                mCurrentUser=result;
                UserStatus userStatus=new UserStatus(LoginActivity.this);
                try {
                    userStatus.setLogin("LoggedIn");
                } catch (BreakfastException ex) {
                    MessageDisplay.errorMessage(ex.getMessage(),LoginActivity.this);
                }
                MessageDisplay.successMessage("Successful Login !!!",LoginActivity.this);
                //Transfer Control to Home Screen
                Intent transferIntent=new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(transferIntent);
                finish();
            }
        }
    }
}