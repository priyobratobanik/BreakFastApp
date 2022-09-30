package tes.com.breakfast.admin.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import tes.com.breakfast.R;
import tes.com.breakfast.Util.MessageDisplay;
import tes.com.breakfast.admin.DTO.AdminItem;

public class AdminUploadNewItem extends AppCompatActivity {

    private EditText mItemName;
    private EditText mItemPrice;
    private static final int PICK_IMAGE_REQUEST = 111;
    private Uri mFilePath;
    private DatabaseReference mDataReference;
    private StorageReference imageReference;
    private StorageReference fileRef;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload_new_item);
        doVariableInitialization();
    }

    /**
     * Member variables initialized here
     * Objects are created
     */
    private void doVariableInitialization() {
        mItemName = (EditText) findViewById(R.id.itemName);
        mItemPrice = (EditText) findViewById(R.id.itemPrice);
        mDataReference = FirebaseDatabase.getInstance().getReference("AdminItem");
        imageReference = FirebaseStorage.getInstance().getReference().child("AdminItem");
        fileRef = null;
        progressDialog = new ProgressDialog(this);
    }

    public void doCaptureFromView(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select a single Image"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.v("File Path COntrol","Control Here");
            mFilePath = data.getData();
            Log.v("File Path",mFilePath.toString());


            uploadFile();

        }

    }

    private void uploadFile()
    {
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        fileRef = imageReference.child(mItemName.getText().toString()+ "_" +mItemPrice.getText().toString()+"."+getFileExtension(mFilePath));
        fileRef.putFile(mFilePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        String name = taskSnapshot.getMetadata().getName();
                        String url = taskSnapshot.getDownloadUrl().toString();

                        Log.e("Check", "Uri: " + url);
                        Log.e("check", "Name: " + name);

                        writeNewImageInfoToDB(name, url);

                        MessageDisplay.successMessage("File Uploaded",AdminUploadNewItem.this);
                        mItemName.setText("");
                        mItemPrice.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();

                MessageDisplay.errorMessage("File Not Uploaded\n" + exception.getMessage(),AdminUploadNewItem.this);
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        // progress percentage
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        // percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                })
                .addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        MessageDisplay.requestMessage("Uploaded Paused",AdminUploadNewItem.this);
                    }
                });

    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void writeNewImageInfoToDB(String name, String url) {
        AdminItem info = new AdminItem();
        info.setItemName(name);
        info.setDownloadUri(url);

        String key = mDataReference.push().getKey();
        mDataReference.child(key).setValue(info);
    }
}