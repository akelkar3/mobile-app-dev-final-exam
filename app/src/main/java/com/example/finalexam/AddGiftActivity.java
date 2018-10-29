package com.example.finalexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.finalexam.utils.FirebaseApi;
import com.example.finalexam.utils.Gift;
import com.example.finalexam.utils.GiftsAdapter;
import com.example.finalexam.utils.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddGiftActivity extends AppCompatActivity {
    final String TAG = "demo";
    ListView listView;
    GiftsAdapter giftsAdapter;
    ArrayList<Gift> gifts = new ArrayList<>();
    final FirebaseApi caller = new FirebaseApi(this);
    Person person=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);
        setTitle(R.string.add_gift);
        person= (Person) getIntent().getSerializableExtra(ChristmasListActivity.PERSON_KEY);

        listView = findViewById(R.id.listview);
        giftsAdapter = new GiftsAdapter(this, R.layout.gift_item, gifts);
        listView.setAdapter(giftsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                caller.addGift(gifts.get(position),person);
                person.totalBought=person.totalBought+gifts.get(position).price;
                person.totalBought=person.giftCount+1;
                Intent intent = new Intent(AddGiftActivity.this, PersonGiftsActivity.class);

                intent.putExtra(ChristmasListActivity.PERSON_KEY,person);
                startActivity(intent);
                finish();
            }
        });



        DatabaseReference threadRef= caller.mDatabase.getReference("gifts");
        threadRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                gifts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Gift post = postSnapshot.getValue(Gift.class);
                    if (post.price<=person.totalBudget-person.totalBought)
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
}
