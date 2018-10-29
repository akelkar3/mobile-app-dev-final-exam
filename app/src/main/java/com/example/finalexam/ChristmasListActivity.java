package com.example.finalexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.finalexam.utils.FirebaseApi;
import com.example.finalexam.utils.Person;
import com.example.finalexam.utils.PersonsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChristmasListActivity extends AppCompatActivity {
    final String TAG = "demo";
    public static String PERSON_KEY="person";
    ListView listView;
    PersonsAdapter personsAdapter;
    ArrayList<Person> persons = new ArrayList<>();
    final FirebaseApi caller = new FirebaseApi(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_christmas_list);
        setTitle(R.string.main_name);
        getPersons();

        listView = findViewById(R.id.listview);
        personsAdapter = new PersonsAdapter(this, R.layout.person_item, persons);
        listView.setAdapter(personsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent( ChristmasListActivity.this, PersonGiftsActivity.class);
                intent.putExtra(PERSON_KEY,persons.get(position));
                startActivity(intent);

            }
        });


        DatabaseReference threadRef= caller.mDatabase.getReference("persons/"+caller.mAuth.getCurrentUser().getDisplayName());
        threadRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                persons.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Person post = postSnapshot.getValue(Person.class);
                    persons.add(post);
                }
                //     Log.d(TAG, "getThread: "+ newtread);
                Log.d(TAG, "Value is: " + persons.size());
                //threads.add(newtread);

                personsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.christmas_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_person_menu_item:
                Log.d(TAG, "onOptionsItemSelected: add person");
                Intent intent = new Intent(this, AddPersonActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout_menu_item:
                //logout
                // go to the login activity
                caller.logout();
                Intent intent1=new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                // finish this activity
                Log.d(TAG, "onOptionsItemSelected: logout");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getPersons()
    {
    }

}
