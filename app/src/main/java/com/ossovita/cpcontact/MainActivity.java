package com.ossovita.cpcontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab = null;
    ListView li;
    View viewum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        li = findViewById(R.id.listView);
        fab = findViewById(R.id.fab);
        //açılır açılmaz izin yoksa izin iste
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            //izin iste
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }
        loadData();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.viewum = view;
              loadDataAndShow(view);

            }
        });
    }

    private void loadDataAndShow(View view) {
        //izin verilmişse
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            ContentResolver contentResolver = getContentResolver();
            //Rehberdeki display name'leri al, display name'e göre sırala
            String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME);

            if(cursor!=null){
                ArrayList<String> contactList = new ArrayList<>();
                //columnIx = hangi sütunu tarayacağımız
                String columnIx = ContactsContract.Contacts.DISPLAY_NAME;
                //Display name sütununda sonuncu satıra kadar olan verileri al listeye at
                while(cursor.moveToNext()){//gidebildiğin kadar git
                    contactList.add(cursor.getString(cursor.getColumnIndex(columnIx)));
                }
                cursor.close();
                //Liste ile listView'ı bağlayacak olan adapter
                ArrayAdapter<String> aa = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,contactList);
                li.setAdapter(aa);

            }

        }else{
            Snackbar.make(view,"Permission needed",Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK",new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            //don't ask again'e basmadıysa direkt sor
                            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CONTACTS)){
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                            }else{//rehber erişimini tekrar sorma dediyse
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                                intent.setData(uri);
                                MainActivity.this.startActivity(intent);
                            }

                        }
                    }).show();
                     /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                           .setAction("Action", null).show();*/
        }
    }

    private void loadData() {
        //izin verilmişse
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            ContentResolver contentResolver = getContentResolver();
            //Rehberdeki display name'leri al, display name'e göre sırala
            String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME);

            if (cursor != null) {
                ArrayList<String> contactList = new ArrayList<>();
                //columnIx = hangi sütunu tarayacağımız
                String columnIx = ContactsContract.Contacts.DISPLAY_NAME;
                //Display name sütununda sonuncu satıra kadar olan verileri al listeye at
                while (cursor.moveToNext()) {//gidebildiğin kadar git
                    contactList.add(cursor.getString(cursor.getColumnIndex(columnIx)));
                }
                cursor.close();
                //Liste ile listView'ı bağlayacak olan adapter
                ArrayAdapter<String> aa = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, contactList);
                li.setAdapter(aa);

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //izni cevabına göre; dönen sonuç varsa ve onaylamışsa
        if(requestCode==1 && grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            loadDataAndShow(viewum);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}