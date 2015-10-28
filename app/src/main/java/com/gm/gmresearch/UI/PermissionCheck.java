package com.gm.gmresearch.UI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.gm.gmresearch.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionCheck extends AppCompatActivity implements View.OnClickListener {

    Button single_button, multiple_button;
    PercentRelativeLayout percentRelativeLayout;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        initViews();


    }

    public void initViews() {
        //initiate views
        percentRelativeLayout = (PercentRelativeLayout) findViewById(R.id.mainLayout);
        single_button = (Button) findViewById(R.id.btn_sp);
        multiple_button = (Button) findViewById(R.id.btn_mp);

        //initiate listener's
        single_button.setOnClickListener(this);
        multiple_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_sp:
                singlePermission();
                break;
            case R.id.btn_mp:
                multiplePermission();
                break;
        }
    }

    /**
     *
     */
    public void singlePermission() {
        singlePermissionWrapper();

    }

    /**
     *
     */
    public void multiplePermission() {
        MultiplePermissionWrapper();
        //snachBar();
    }


    private void singleDummypurmission() {
        Toast.makeText(PermissionCheck.this, "WRITE_CONTACTS will worked now !!!!", Toast.LENGTH_SHORT)
                .show();
    }

    private void multipleDummypurmission() {
        Toast.makeText(PermissionCheck.this, "All Permissions will worked now !!!!", Toast.LENGTH_SHORT)
                .show();
    }


    private void singlePermissionWrapper() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(PermissionCheck.this,
                Manifest.permission.WRITE_CONTACTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(PermissionCheck.this,
                    Manifest.permission.WRITE_CONTACTS)) {
                showMessageOKCancel("You need to allow access to Contacts",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(PermissionCheck.this,
                                        new String[]{Manifest.permission.WRITE_CONTACTS},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(PermissionCheck.this,
                    new String[]{Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        singleDummypurmission();
    }


    private void MultiplePermissionWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

         final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(PermissionCheck.this, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(PermissionCheck.this,permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        multipleDummypurmission();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(PermissionCheck.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(PermissionCheck.this,permission))
                return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    singleDummypurmission();
                } else {
                    // Permission Denied
                    Toast.makeText(PermissionCheck.this, "WRITE_CONTACTS Denied \n Open Permission and enable WRITE_CONTACTS", Toast.LENGTH_SHORT)
                            .show();

                    gotoAppSetting();
                }
                break;

            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    multipleDummypurmission();
                } else {
                    // Permission Denied
                    Toast.makeText(PermissionCheck.this, "Some Permission is Denied \n Open Permission and enable all ", Toast.LENGTH_SHORT)
                            .show();
                    gotoAppSetting();
                }
            }
            break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void gotoAppSetting(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        this.startActivity(intent);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PermissionCheck.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void snachBar() {
        Snackbar snackbar = Snackbar
                .make(percentRelativeLayout, "Welcome to SinglePermission", Snackbar.LENGTH_LONG)
                .setAction("Setting", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Snackbar snackbar1 = Snackbar.make(percentRelativeLayout, "Message is restored!", Snackbar.LENGTH_SHORT);
//                        snackbar1.show();
                        gotoAppSetting();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }
}
