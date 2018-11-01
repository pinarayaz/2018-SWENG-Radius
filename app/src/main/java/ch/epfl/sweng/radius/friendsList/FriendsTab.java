package ch.epfl.sweng.radius.friendsList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.FirebaseUtility;
import ch.epfl.sweng.radius.database.User;


public class FriendsTab extends Fragment {


    public FriendsTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.friends_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.friendsList);
        //mock data for testing purposes

        FriendsListItem items[] = { new FriendsListItem("John Doe",R.drawable.image1),
                new FriendsListItem("Jane Doe",R.drawable.image2),
                new FriendsListItem("Alison Star",R.drawable.image3),
                new FriendsListItem("Mila Noon",R.drawable.image4),
                new FriendsListItem("David Doyle",R.drawable.image5)};


        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUtility database = new FirebaseUtility("users");
        User currentUser = new User(userUID);
        database.readObjOnce(currentUser, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                User userStoredInTheDB = (User)value;
                System.out.println(userStoredInTheDB.getNickname());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        FriendsListAdapter adapter = new FriendsListAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Inflate the layout for this fragment
        return view;
    }
}
