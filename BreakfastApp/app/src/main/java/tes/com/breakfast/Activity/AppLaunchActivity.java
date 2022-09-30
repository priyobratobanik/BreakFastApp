package tes.com.breakfast.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import tes.com.breakfast.DatabaseManagement.UserStatus;
import tes.com.breakfast.Exception.BreakfastException;
import tes.com.breakfast.R;
import tes.com.breakfast.Util.MessageDisplay;
import tes.com.breakfast.admin.Activity.AdminLoginActivity;

public class AppLaunchActivity extends AppCompatActivity {

    private UserStatus mStatus;

    /**
     * All initializations are done here
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_launch);

        //Variables to be initialized
        //Objects to be created here
        doVariableInitialization();

        //Check for Logged In User
        //Check if User is Already logged In
        try
        {
            if(mStatus.hasLogin())
            {
                MessageDisplay.successMessage("You are Already Logged In !!!", AppLaunchActivity.this);

                //Transfer the control to the User Home Screen
                Intent transferIntent=new Intent(AppLaunchActivity.this,HomeActivity.class);
                startActivity(transferIntent);
                finish();
            }
        }catch (BreakfastException ex)
        {
            MessageDisplay.errorMessage(ex.getMessage(),AppLaunchActivity.this);
        }
    }


    /**
     * Member variables initialized here
     * Objects are created
     */
    private void doVariableInitialization() {
        mStatus=new UserStatus(AppLaunchActivity.this);
    }



    /**
     * On Login button click method
     *
     * @param view
     */
    public void goToLoginFromView(View view)
    {
        Intent transferIntent=new Intent(AppLaunchActivity.this,LoginActivity.class);
        startActivity(transferIntent);
        finish();
    }
    /**
     * On Register  button click method
     *
     * @param view
     */
    public void goToRegisterFromView(View view)
    {
        Intent transferIntent=new Intent(AppLaunchActivity.this,SignUpActivity.class);
        startActivity(transferIntent);
        finish();
    }


    /**
     * Go to Admin Login Page
     * @param view
     */
    public void doAdminAccess(View view)
    {
        Intent transferIntent=new Intent(AppLaunchActivity.this,AdminLoginActivity.class);
        startActivity(transferIntent);
        finish();
    }
}