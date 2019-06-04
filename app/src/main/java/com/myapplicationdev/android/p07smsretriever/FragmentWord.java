package com.myapplicationdev.android.p07smsretriever;


import android.Manifest;
import android.content.ContentResolver;
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

public class FragmentWord extends Fragment {

    EditText etWord;
    TextView tvWord;
    Button btnRetrieveWord;

    public FragmentWord() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_word, container, false);
        etWord = view.findViewById(R.id.etSearchWord);
        tvWord = view.findViewById(R.id.tvRetrieveWord);
        btnRetrieveWord = view.findViewById(R.id.btnRetrieveWord);

        btnRetrieveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchWord = etWord.getText().toString();
                String[] splitStr = searchWord.trim().split("\\s+");

                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};

                ContentResolver cr = getActivity().getContentResolver();
                String smsBody = "";

                for (int i = 0; i < splitStr.length; i++){
                    String filter = "body LIKE ?";
                    String[] filterArgs = {"%" + splitStr[i] +"%"};
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
                }
                tvWord.setText(smsBody);
                etWord.setText("");

            }
        });

        if (savedInstanceState != null){
            tvWord.setText(savedInstanceState.getString("text"));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("text", tvWord.getText().toString());
    }

}
