package com.vbs.irmenergy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.vbs.irmenergy.R;
import com.vbs.irmenergy.utilities.Utility;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    private static final String TAG = "Scan";
    BarcodeReader barcodeReader;
    private RelativeLayout rl_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        rl_scan = (RelativeLayout) findViewById(R.id.rl_scan);
//        rl_scan.setLayoutParams(new RelativeLayout.LayoutParams(width,height));

        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();
        Log.v("Barcode", barcode.displayValue);
        parseXml(barcode.displayValue);
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void parseXml(String data) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(data)); // pass input whatever xml you have
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(TAG, "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    Log.d(TAG, "Start tag " + xpp.getName());
                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d(TAG, "End tag " + xpp.getName());
                    Utility.writeSharedPreferences(this, "uid", xpp.getAttributeValue(0));
                    finish();
                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d(TAG, "Text " + xpp.getText()); // here you get the text from xml
                }
                eventType = xpp.next();
            }
            Log.d(TAG, "End document");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
