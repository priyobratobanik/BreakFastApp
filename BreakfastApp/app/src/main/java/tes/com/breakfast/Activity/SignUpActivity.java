package tes.com.breakfast.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import tes.com.breakfast.DTO.Order;
import tes.com.breakfast.DTO.User;
import tes.com.breakfast.DatabaseManagement.Login;
import tes.com.breakfast.DatabaseManagement.Registration;
import tes.com.breakfast.Exception.BreakfastException;
import tes.com.breakfast.Location.LocationService;
import tes.com.breakfast.Manifest;
import tes.com.breakfast.R;
import tes.com.breakfast.Util.DataValidation;
import tes.com.breakfast.Util.MessageDisplay;

public class SignUpActivity extends AppCompatActivity {


    private EditText mUsername, mPassword, mConfirmPassword, mAddress;
    private static final int RequestLocationPermissionCode = 1;
    private Location mLocation;
    private String mLatitudeStr, mLongitudeStr;
    private ImageView mLocationUpdateButton;
    private TextView mPasswordInstruction;

    /**
     * All initializations are done here
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Variables to be initialized
        //Objects to be created here
        doVariableInitialization();

        //Permission Request Made Here
        checkAndRequestLocationPermission();


        //Implement the Location Finder Button
        mLocationUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationUpdates();
            }
        });

        //Implement the Password Edit Text Listener
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mPasswordInstruction.setVisibility(View.VISIBLE);
            }
        });

        //Implement the Confirm Password Edit Text Listener
        mConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mPasswordInstruction.setVisibility(View.INVISIBLE);
            }
        });

    }

    /**
     * Method to get the Location Update from the Location Service Class
     */
    private void requestLocationUpdates() {
        LocationService locationService = new LocationService(SignUpActivity.this);
        if (locationService.canGetLocation()) {
            mLocation = locationService.getLocation();
            if (mLocation != null) {
                mLatitudeStr = mLocation.getLatitude() + "";
                mLongitudeStr = mLocation.getLongitude() + "";
                if (mLatitudeStr != "" && mLongitudeStr != "") {
                    storeAddress();
                } else {
                    MessageDisplay.errorMessage("Switch On GPS and Restart App !!", SignUpActivity.this);
                }
            }
        } else
            locationService.showSettingsAlert();
    }

    /**
     * Class to store the corresponding Address in the Address Bar
     */
    private void storeAddress() {
        try {

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(Double.parseDouble(mLatitudeStr), Double.parseDouble(mLongitudeStr), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            mAddress.setText(address);
            if (address.length() > 0)
                mLocationUpdateButton.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
            mAddress.setEnabled(true);
            mLocationUpdateButton.setVisibility(View.INVISIBLE);
            mAddress.requestFocus();
            mAddress.setHint(R.string.SignUpTextAddressAlternate);
        }
    }


    /**
     * Member variables initialized here
     * Objects are created
     */
    private void doVariableInitialization() {
        mUsername = (EditText) findViewById(R.id.signUpPageUsername);
        mPassword = (EditText) findViewById(R.id.signUpPagePassword);
        mConfirmPassword = (EditText) findViewById(R.id.signUpPageConfirmPassword);
        mAddress = (EditText) findViewById(R.id.signUpPageAddress);
        mLocationUpdateButton = (ImageView) findViewById(R.id.signUpPageLocationFinderButton);
        mPasswordInstruction = (TextView) findViewById(R.id.signUpPagePasswordInstruction);
    }

    /**
     * Method to check and request for location permission
     */
    private void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            //request for Location Permission Here
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]
                    {
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                    }, RequestLocationPermissionCode);
        }
    }

    /**
     * Callback method for granting permissions
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RequestLocationPermissionCode:
                if (grantResults.length == 1 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //permission was granted do nothing and carry on

                } else
                    //permission denied
                    MessageDisplay.errorMessage("Access Denied !!! Exiting App !!!", SignUpActivity.this);
        }
    }


    /**
     * On register button click method
     *
     * @param view
     */
    public void doRegisterFromView(View view) {
        //Data Fetching
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        String address = mAddress.getText().toString();

        //Data Validation
        if (!DataValidation.validateUserName(username))
            MessageDisplay.errorMessage("Username cannot be empty !!!", SignUpActivity.this);
        else if (!DataValidation.validatePassword(password))
            MessageDisplay.errorMessage("Please follow password Criteria !!!", SignUpActivity.this);
        else if (!DataValidation.validateConfirmPassword(password, confirmPassword))
            MessageDisplay.errorMessage("Password and Confirm Password does not Match !!!", SignUpActivity.this);
        else if (!DataValidation.validateAddress(address)) {
            MessageDisplay.requestMessage("Requesting Updates !!!", SignUpActivity.this);
            requestLocationUpdates();
            storeAddress();
        } else {
            //Data Validated
            //Create User
            User currentUser = new User();
            currentUser.setmUserName(username);
            currentUser.setmPassword(password);
            currentUser.setmAddress(address);

            //Take Current Time Stamp
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            currentUser.setmLastLoginTime(currentTime.toString());


            //Registration object to be made
            Registration registration = new Registration(currentUser, SignUpActivity.this);

            new RegisterTask().execute(registration);

        }
    }

    /**
     * Transfer Control to Launcher Screen
     *
     * @param view
     */
    public void goBackFromView(View view) {
        //Transfer Control to Launcher Screen
        Intent transferIntent = new Intent(SignUpActivity.this, AppLaunchActivity.class);
        startActivity(transferIntent);
        finish();
    }


    public class RegisterTask extends AsyncTask<Registration, String, String> {

        private ProgressDialog dialog=new ProgressDialog(SignUpActivity.this);
        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Searching Database !!!");
            dialog.show();
        }

        @Override
        protected String doInBackground(Registration... registration) {
            try {
                registration[0].register();
            } catch (BreakfastException ex) {
                return ex.getMessage();
            } catch (InterruptedException ex1) {
                return ex1.getMessage();
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String res) {

            dialog.dismiss();
            if(res.equals("Exists"))
            {
                MessageDisplay.errorMessage("Username Exists !!!", SignUpActivity.this);
                return;
            }
            //Successfully Created User
            MessageDisplay.successMessage("User Created", SignUpActivity.this);


            //Transfer control to Home Screen
            Intent transferIntent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(transferIntent);
            finish();
        }
    }


}