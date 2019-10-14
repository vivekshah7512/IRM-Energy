package com.vbs.irmenergy.utilities;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vbs.irmenergy.R;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utility {
    private Context context;

    public Utility(Context context) {
        this.context = context;
    }

    public static void toast(String toastMessage, Context context) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
    }

    public static void snackBarShow(View container, String msg) {
        Snackbar.make(container, msg, 0).show();
    }

    public static void writeSharedPreferences(Context mContext, String key, String value) {
        try {
            Editor editor = mContext.getSharedPreferences(Constant.PREFS_NAME, 0).edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAppPrefString(Context mContext, String key) {
        try {
            return mContext.getSharedPreferences(Constant.PREFS_NAME, 0).getString(key, "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static String parseDateToddMMyyyy(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        try {
            return new SimpleDateFormat("dd MMM, yyyy hh:mm a").format(inputFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").
                    format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFTPPath(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            return new SimpleDateFormat("yyyy/MMMM").
                    format(inputFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Current time => ");
        stringBuilder.append(c.getTime());
        printStream.println(stringBuilder.toString());
        return new SimpleDateFormat("yyyy/dd/mm").format(c.getTime());
    }

    public static String getCurrentDateTime1() {
        Calendar c = Calendar.getInstance();
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Current time => ");
        stringBuilder.append(c.getTime());
        printStream.println(stringBuilder.toString());
        return new SimpleDateFormat("dd/mm/yyyy").format(c.getTime());
    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(0) != null && connectivityManager.getNetworkInfo(0).getState() == State.CONNECTED) || (connectivityManager.getNetworkInfo(1) != null && connectivityManager.getNetworkInfo(1).getState() == State.CONNECTED);
    }

    public static boolean isValidEmail1(CharSequence target) {
        if (target == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches();
    }

    public static String getDateTime() {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault()).format(new Date());
    }

    public static String getDateTime3() {
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String getDateTime2() {
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
    }

    public static String getDateTime1() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -10);
        Date teenMinutesFromNow = now.getTime();

        SimpleDateFormat spf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        return spf.format(teenMinutesFromNow);
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(new Date());
    }

    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setDoInput(true);
            connection.connect();
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        int orientation = new ExifInterface(image_absolute_path).getAttributeInt("Orientation", 1);
        if (orientation == 6) {
            return rotate(bitmap, 90.0f);
        }
        if (orientation == 8) {
            return rotate(bitmap, 270.0f);
        }
        switch (orientation) {
            case 2:
                return flip(bitmap, true, false);
            case 3:
                return rotate(bitmap, 180.0f);
            case 4:
                return flip(bitmap, false, true);
            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        float f = 1.0f;
        float f2 = horizontal ? -1.0f : 1.0f;
        if (vertical) {
            f = -1.0f;
        }
        matrix.preScale(f2, f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void showDialogOK(Context c, String message, OnClickListener okListener) {
        new Builder(c).setTitle("IMR Energy").setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", okListener).create().show();
    }

    public static void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("appTitle.txt", 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("File write failed: ");
            stringBuilder.append(e.toString());
            Log.e("Exception", stringBuilder.toString());
        }
    }

    public static String readFromFile(Context context) {
        StringBuilder stringBuilder;
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("appTitle.txt");
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String receiveString = "";
                StringBuilder stringBuilder2 = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    receiveString = readLine;
                    if (readLine == null) {
                        break;
                    }
                    stringBuilder2.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder2.toString();
            }
        } catch (FileNotFoundException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("File not found: ");
            stringBuilder.append(e.toString());
            Log.e("login activity", stringBuilder.toString());
        } catch (IOException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Can not read file: ");
            stringBuilder.append(e2.toString());
            Log.e("login activity", stringBuilder.toString());
        }
        return ret;
    }

    public static String postRequest(final Context c, String url, List<NameValuePair> data) {
        String result = "";
        System.out.println("Url " + url + " Data is " + data);
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
            httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
            HttpConnectionParams.setSoTimeout(httpParameters, 30000);
            HttpEntity entity = ((BasicHttpResponse) new DefaultHttpClient(httpParameters).execute(httpPost)).getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity).trim();
            }
        } catch (Exception e) {
            ((Activity) c).runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText((Activity) c, "unable to reach server, please try again later", Toast.LENGTH_LONG).show();
                }
            });
            e.printStackTrace();
        }
        return result;
    }

    public static String getRequest(final Context c, String url, List<NameValuePair> data) {
        String result = "";
        System.out.println("Url " + url + " Data is " + data);
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
            HttpConnectionParams.setSoTimeout(httpParameters, 30000);
            HttpEntity entity = ((BasicHttpResponse) new DefaultHttpClient(httpParameters).execute(httpGet)).getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity).trim();
            }
        } catch (Exception e) {
            ((Activity) c).runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText((Activity) c, "unable to reach server, please try again later", Toast.LENGTH_LONG).show();
                }
            });
            e.printStackTrace();
        }
        return result;
    }

    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory().getPath() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void setTitle(final Context context, String title) {
        ((TextView) ((Activity) context).findViewById(R.id.toolbar_title)).setText(title);
    }

}