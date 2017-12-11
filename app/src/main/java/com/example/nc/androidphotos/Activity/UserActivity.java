package com.example.nc.androidphotos.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Neil on 12/11/17.
 */

public class UserActivity extends AppCompatActivity{
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private User theUser;
    private Album selectedAlbum;

    private static final String SEARCH_RESULTS = "SEARCH_RESULTS_ALBUM";


}
