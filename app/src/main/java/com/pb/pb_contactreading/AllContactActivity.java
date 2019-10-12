package com.pb.pb_contactreading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Telephone;

public class AllContactActivity extends AppCompatActivity {
    public static final int REQUEST_READ_CONTACTS = 79;
    private static String READ_CON = android.Manifest.permission.READ_CONTACTS;
    private ArrayList<Contact> list_item;
    ContactAdapter ctAdapter;
    ListView list;
    ArrayList<Contact> mobileArray;
    ArrayList<ArrayList<Contact>> contact_groups;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contact);
        mobileArray = new ArrayList<>();
        list_item = new ArrayList<>();
        contact_groups = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            //Read all contact
            mobileArray = getAllContacts();
        } else {
            requestPermission(READ_CON,REQUEST_READ_CONTACTS);
        }

        list = findViewById(R.id.list);
      //  Parcelable state = list.onSaveInstanceState();

        ctAdapter = new ContactAdapter(this, mobileArray, this);
        list.setAdapter(ctAdapter);
    }

    private void requestPermission(String perm_req, int req_code) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm_req)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{perm_req},
                    req_code);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm_req)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{perm_req},
                    req_code);
        }
    }
    public void addCont(int i){
        list_item.add(mobileArray.get(i));
    }

    public void remCont(int i){
        list_item.remove(mobileArray.get(i));
    }

    public boolean conCont(int i)
    {
        return list_item.contains(mobileArray.get(i));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mobileArray = getAllContacts();
                } else {
                }
                return;
            }
        }
    }

    /**
     * Read all the contacts, only read the name and number
     * @return ArrayList of contacts
     */
    private ArrayList getAllContacts() {
        ArrayList<Contact> nameList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                String street = "test";
                Cursor addrCursor = getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?", new String[] {
                        id
                }, null);
                try {
                    if (addrCursor != null) {
                        while (addrCursor.moveToNext()) {
                            street = addrCursor.getString(addrCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        }
                    }
                } catch (Exception e){
                    continue;
                }
                if(street== null||street.equals("test")){
                    continue;
                }

                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Contact ctx = new Contact(name,phoneNo,street);
                        nameList.add(ctx);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return nameList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_group:
                if(list_item.isEmpty()){
                    Toast.makeText(this, "Select at least 1 contact to add to group!", Toast.LENGTH_SHORT).show();
                } else{
                    contact_groups.add((ArrayList<Contact>) list_item.clone());
                    mobileArray.removeAll(list_item);
                    list_item = new ArrayList<>();
                    mobileArray.add(new Contact("--------Group " + (contact_groups.size()-1) + "--------","---",""));
                    mobileArray.addAll(contact_groups.get(contact_groups.size()-1));
                    ctAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Group " + (contact_groups.size()-1) + "added", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
