package tes.com.breakfast.admin.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tes.com.breakfast.Activity.AppLaunchActivity;
import tes.com.breakfast.DTO.User;
import tes.com.breakfast.R;
import tes.com.breakfast.Util.DataValidation;
import tes.com.breakfast.Util.MessageDisplay;

public class AdminLoginActivity extends AppCompatActivity {

        private EditText mUsername,mPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //Initialize the variables
        doVariableInitialization();

    }

    /**
     * Member variables initialized here
     * Objects are created
     */
    private void doVariableInitialization() {
        mUsername = (EditText) findViewById(R.id.adminLoginPageUsername);
        mPassword = (EditText) findViewById(R.id.adminLoginPagePassword);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Remove from the Screen
     *
     * @param view
     */
    public void goBackFromView(View view) {
        //Transfer Control to Launcher Screen
        Intent transferIntent = new Intent(AdminLoginActivity.this, AppLaunchActivity.class);
        startActivity(transferIntent);
        finish();
    }

    /**
     * Do Login From View
     * @param view
     */
    public void doLoginFromView(View view)
    {
        //Fetch Data
        String username=mUsername.getText().toString();
        String password=mPassword.getText().toString();

        //Data Validation
        if(!DataValidation.validateUserName(username))
            MessageDisplay.errorMessage("Username Cannot be Empty !!",AdminLoginActivity.this);
        else if(!DataValidation.validatePassword(password))
            MessageDisplay.errorMessage("Password Invalid !!",AdminLoginActivity.this);
        else
        {
            //Data Validated
            //Creating a new thread to do Login
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null) {
                                    MessageDisplay.successMessage("Logged In", AdminLoginActivity.this);
                                    //Transfer Control to Home Activity
                                    Intent trasnferIntent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                                    startActivity(trasnferIntent);
                                    finish();
                                }
                                else
                                    MessageDisplay.errorMessage("Could not Find Admin !!",AdminLoginActivity.this);

                            } else {
                                // If sign in fails, display a message to the user.
                                MessageDisplay.errorMessage("Authentication Failed !!!",AdminLoginActivity.this);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Transfer Control to Home Activity
        if(currentUser!=null)
        {
            Intent trasnferIntent=new Intent(AdminLoginActivity.this,AdminHomeActivity.class);
            startActivity(trasnferIntent);
            finish();
        }
    }
}