package com.project.adersh.middleearpathology;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class PathUtil {
    public static String getPathFromUri(Context context, Uri uri) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // If the URI is a Document URI
            String documentId = DocumentsContract.getDocumentId(uri);
            if (uriAuthorityContainsMedia(documentId)) {
                // MediaStore (Gallery, Video, Audio and Download folder)
                String id = documentId.split(":")[1];
                String[] column = {MediaStore.Video.Media.DATA};
                String sel = MediaStore.Video.Media._ID + "=?";
                Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // If the URI is a content URI
            String[] projection = {MediaStore.Video.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // If the URI is a file URI
            filePath = uri.getPath();
        }
        return filePath;
    }

    private static boolean uriAuthorityContainsMedia(String documentId) {
        return documentId.contains("video") || documentId.contains("audio") || documentId.contains("image") || documentId.contains("download");
    }
}
