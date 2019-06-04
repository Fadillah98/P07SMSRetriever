package com.myapplicationdev.android.p07smsretriever;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentNumber extends Fragment {

    EditText etNum;
    TextView tvNum;
    Button btnRetrieveNum, btnEmail;
    String smsBody = "";

    public FragmentNumber() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number, container, false);

        etNum = view.findViewById(R.id.etSearchNumber);
        tvNum = view.findViewById(R.id.tvRetrieveNumber);
        btnRetrieveNum = view.findViewById(R.id.btnRetrieveNum);
        btnEmail = view.findViewById(R.id.btnEmail);

        btnRetrieveNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchNum = etNum.getText().toString();

                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};

                ContentResolver cr = getActivity().getContentResolver();
                String filter = "address LIKE ?";
                String[] filterArgs = {"%" + searchNum +"%"};
                Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                if (cursor.moveToFirst()) {
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat.format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox:";
                        } else {
                            type = "Sent:";
                        }
                        smsBody += type + " " + address + "\n at " + date
                                + "\n\"" + body + "\"\n\n";
                    } while (cursor.moveToNext());
                }
                tvNum.setText(smsBody);
                etNum.setText("");

            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"17010176@myrp.edu.sg"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Email SMS Content");
                email.putExtra(Intent.EXTRA_TEXT, smsBody);

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        return view;
    }

}
