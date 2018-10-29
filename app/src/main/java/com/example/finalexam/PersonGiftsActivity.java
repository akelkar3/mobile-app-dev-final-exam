package com.example.finalexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finalexam.utils.FirebaseApi;
import com.example.finalexam.utils.Gift;
import com.example.finalexam.utils.GiftsAdapter;
import com.example.finalexam.utils.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonGiftsActivity extends AppCompatActivity {
    final String TAG = "demo";
    ListView listView;
    GiftsAdapter giftsAdapter;
    ArrayList<Gift> gifts = new ArrayList<>();
    Person person= null;
    final FirebaseApi caller = new FirebaseApi(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_gifts);

        person= (Person) getIntent().getSerializableExtra(ChristmasListActivity.PERSON_KEY);
        setTitle(person.name);
        listView = findViewById(R.id.listview);
        giftsAdapter = new GiftsAdapter(this, R.layout.gift_item, gifts);
        listView.setAdapter(giftsAdapter);



        DatabaseReference threadRef= caller.mDatabase.getReference("personGifts/"+caller.mAuth.getCurrentUser().getDisplayName()+"/"+person.name);
        threadRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                gifts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Gift post = postSnapshot.getValue(Gift.class);
                    //if (post.price<=person.totalBudget-person.totalBought)
                    gifts.add(post);
                }
                //     Log.d(TAG, "getThread: "+ newtread);
                Log.d(TAG, "gift Value is: " + gifts.size());
                //threads.add(newtread);

                giftsAdapter.notifyDataSetChanged();

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
        inflater.inflate(R.menu.person_gifts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_gift_menu_item:
                Log.d(TAG, "onOptionsItemSelected: ");
                if (person.totalBought==person.totalBudget) {
                    Toast.makeText(this, "You have no budget left", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(this, AddGiftActivity.class);
                    intent.putExtra(ChristmasListActivity.PERSON_KEY, person);
                    startActivity(intent);
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
