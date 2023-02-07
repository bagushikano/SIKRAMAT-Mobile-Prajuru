package com.bagushikano.sikedatmobileadmin.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

public class FileOpenUtil {
    Context context;
    public FileOpenUtil(Context context) {
        this.context = context;
    }

    public void openFile(Uri uriFile) {
        // Get URI and MIME type of file
        Uri uri = uriFile;
        String mime = context.getContentResolver().getType(uri);
        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }
}
