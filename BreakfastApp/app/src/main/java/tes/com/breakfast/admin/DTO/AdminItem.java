package tes.com.breakfast.admin.DTO;

import android.net.Uri;

/**
 * Created by Aashijit on 27/10/2018.
 */

public class AdminItem
{
    private String itemName;

    private String downloadUri;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }
}
