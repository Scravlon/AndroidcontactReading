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
import android.provider.ContactsContract;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Telephone;

public class AllVCFActivity extends AppCompatActivity {

    public static final int REQUEST_READ_CONTACTS = 79;
    public static final int REQUEST_EXTERNAL_STORAGE = 90;
    private static String READ_CON = android.Manifest.permission.READ_CONTACTS;
    private static String READ_EXT = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    private ArrayList<Integer> list_item;
    ContactAdapter ctAdapter;
    ListView list;
    ArrayList<Contact> ct_Array;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_vcf);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermission(READ_EXT,REQUEST_EXTERNAL_STORAGE);
        }
        list_item = new ArrayList<>();
        ct_Array = new ArrayList<>();
        list = findViewById(R.id.list);
        readVCF();
        ctAdapter = new ContactAdapter(this, ct_Array,null);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

    /**
     * VCF reading on the file specified in the external storage /test.vcf
     * Example test file is attached in the project folder
     */
    public void readVCF(){
        try{
            File file = new File(Environment.getExternalStorageDirectory()+"/test.vcf");
            List<VCard> vcards = Ezvcard.parse(file).all();
            for (VCard vcard : vcards){
                System.out.println("Name: " + vcard.getFormattedName().getValue());
                String Name = vcard.getFormattedName().getValue();
                String add = vcard.getAddresses().isEmpty()?"Null":vcard.getAddresses().get(0).getStreetAddress();
                for (Telephone tel : vcard.getTelephoneNumbers()){
                    ct_Array.add(new Contact(Name,tel.getText(), add));
                }
            }
            ctAdapter.notifyDataSetChanged();
        }catch(Exception e){e.printStackTrace();}
    }

}
