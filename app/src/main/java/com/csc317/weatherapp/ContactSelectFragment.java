package com.csc317.weatherapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactSelectFragment extends Fragment {

    private static List<String> contactList = null;
    private static ArrayAdapter<String> contactListAdapter = null;
    private List<String> clickedContactEmail = null;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String URI_STRING = "uriString";
    private String uriString;   //the uri of the saved drawing file

    public ContactSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ContactFragment.
     */
    public static ContactSelectFragment newInstance(String param1) {
        ContactSelectFragment fragment = new ContactSelectFragment();
        Bundle args = new Bundle();
        args.putString(URI_STRING, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uriString = getArguments().getString(URI_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);


        //Step 1: Use cursor to get a list of names
        contactList = new ArrayList<>();
        Cursor cursor = getActivity().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // Do something with the contactId and the name
            contactList.add(name + "::" +contactId);
        }
        cursor.close();

        //Step2: setup an list view
        String[] contactArray = contactList.toArray(new String[0]); //string list to string array
        contactListAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_row_item, R.id.list_row_item, contactArray);
        ListView listView = view.findViewById(R.id.contact_list_view);
        listView.setAdapter(contactListAdapter);


        //Step3: add onclick function to it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = (String) parent.getAdapter().getItem(position);
                int contactId = Integer.parseInt(text.substring(text.indexOf("::") + 2));
                //call this once the user choose which contact, the query by ID
                //use a content provider to get the email associated with the contact id
                clickedContactEmail = new ArrayList<>();
                Cursor emails = getActivity().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
                if (emails.moveToNext()) {
                    String email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    // Do something with the email address
                    clickedContactEmail.add(email);
                }
                emails.close();

                //System.out.println(clickedContactEmail);

                //Setup an intent to share via email
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("vnd.android.cursor.dir/email");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, clickedContactEmail.toArray(new String[0]));
                intent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse(uriString)); //the uri is passing in as parameter
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        });

        return view;
    }


}