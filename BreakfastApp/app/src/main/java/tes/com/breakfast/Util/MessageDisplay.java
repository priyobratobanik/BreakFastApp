package tes.com.breakfast.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import tes.com.breakfast.R;

/**
 * Created by Aashijit on 17/10/2018.
 */

public class MessageDisplay
{
    public MessageDisplay()
    {
    }

    /**
     * Display error message via toast
     * @param message message to be displayed
     */
    public static void errorMessage(String message, final Context context)
    {
        final AlertDialog.Builder alert=new AlertDialog.Builder(context);
        alert.setIcon(R.drawable.error_icon);
        alert.setTitle("Error !!! ");
        alert.setMessage(message);

        alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

    /**
     * Display a message after a successful job
     * @param message message to be displayed
     */
    public static void successMessage(String message,Context context)
    {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=inflater.inflate(R.layout.success_message_layout,null);
        TextView numberOfHospitals=(TextView)layout.findViewById(R.id.successMessageText);
        numberOfHospitals.setText(message);
        final Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setView(layout);
        toast.show();
    }

    /**
     * Display a message after a request has been made
     * @param message message to be displayed
     */
    public static void requestMessage(String message,Context context)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

}