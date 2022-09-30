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
import tes.com.breakfast.DatabaseManagement.ForgotPassword;
import tes.com.breakfast.DatabaseManagement.Login;
import tes.com.breakfast.DatabaseManagement.UserStatus;
import tes.com.breakfast.Exception.BreakfastException;
import tes.com.breakfast.R;
import tes.com.breakfast.Util.DataValidation;
import tes.com.breakfast.Util.MessageDisplay;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mUsername,mPassword,mNewPassword,mConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Initialize member Variable
        doVariableInitialization();



    }

    /**
     * Member variables initialized here
     * Objects are created
     */
    private void doVariableInitialization() {
        mUsername = (EditText) findViewById(R.id.forgotPasswordPageUsername);
        mPassword = (EditText) findViewById(R.id.forgotPasswordPageOldPassword);
        mNewPassword=(EditText) findViewById(R.id.forgotPasswordPageNewPassword);
        mConfirmNewPassword=(EditText)findViewById(R.id.forgotPasswordPageConfirmNewPassword);
    }


    /**
     * On Change Password button click method
     *
     * @param view
     */
    public void doChangePasswordFromView(View view)
    {
        //Fetch Data
        String username=mUsername.getText().toString();
        String oldPassword=mPassword.getText().toString();
        String newPassword=mNewPassword.getText().toString();
        String confirmPassword=mConfirmNewPassword.getText().toString();


        //Data Validation
        if(!DataValidation.validateUserName(username))
            MessageDisplay.errorMessage("Username Cannot be Empty !!!",ForgotPasswordActivity.this);
        else if(!DataValidation.validatePassword(oldPassword))
            MessageDisplay.errorMessage("Old Password Invalid !!!",ForgotPasswordActivity.this);
        else if(!DataValidation.validatePassword(newPassword))
            MessageDisplay.errorMessage("New Password Invalid !!!",ForgotPasswordActivity.this);
        else if(!DataValidation.validateConfirmPassword(newPassword,confirmPassword))
            MessageDisplay.errorMessage("Passwords do not match !!!",ForgotPasswordActivity.this);
        else
        {
            //Data Validated
            //Creating a new thread to do Login
            new AsyncForgotPasswordTask().execute(username,oldPassword,newPassword);

        }
    }


    /**
     * Go back to Login Screen
     * @param view
     */
    public void goBackFromView(View view)
    {
        //Transfer control to Login Screen
        Intent transferIntent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
        startActivity(transferIntent);
        finish();
    }


    /**
     * Parallel Thread to do Login in Background
     */
    private  class AsyncForgotPasswordTask extends AsyncTask<String,String,String>
    {

        private ProgressDialog progress=new ProgressDialog(ForgotPasswordActivity.this);

        @Override
        protected void onPreExecute()
        {
            progress.setMessage("Updating Database ... ");
            progress.show();

            //Stop User Interaction
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }

        @Override
        protected String doInBackground(String... payload)
        {
            String status="";
            try {
                status=ForgotPassword.doChangePassword(payload[0],payload[1],payload[2],ForgotPasswordActivity.this);
            } catch (BreakfastException ex) {
                Log.v("BreakfastException",ex.getMessage());
            } catch (InterruptedException ex) {
                Log.v("InterruptedException",ex.getMessage());
            }
            return status;
        }

        @Override
        protected void onProgressUpdate(String ...values)
        {
            progress.setMessage(values[values.length-1]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            progress.dismiss();
            //Start User Interaction
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            if(result!=null)
            {
                if(result.equals("Password Change Successful"))
                {
                    MessageDisplay.successMessage(result,ForgotPasswordActivity.this);

                    //Transfer control to Login Screen
                    Intent transferIntent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                    startActivity(transferIntent);
                    finish();
                }
                else if(result.equals("User Not Found !!!"))
                {
                    MessageDisplay.errorMessage(result,ForgotPasswordActivity.this);
                }
                else if(result.equals("Server Failure !!!"))
                    MessageDisplay.errorMessage("Fatal Error !!!",ForgotPasswordActivity.this);
            }
            else
            MessageDisplay.errorMessage("Fatal Error !!!",ForgotPasswordActivity.this);

        }
    }
}