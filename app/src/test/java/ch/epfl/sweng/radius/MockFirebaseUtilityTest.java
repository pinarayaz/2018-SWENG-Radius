package ch.epfl.sweng.radius;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ MockFirebaseUtility.class, FirebaseDatabase.class})
public class MockFirebaseUtilityTest {

    private static MockFirebaseUtility mockedDB;

    @BeforeClass
    public static void before() throws IOException {

        mockedDB = new MockFirebaseUtility();
        System.out.println("MockedFirebaseUtility instantiated");
    }

    @Before     
    public void clearPath(){
        mockedDB.clearPath();
    }


    @Test
    public void filesShouldBeGenerated() {


        File userFile = new File(mockedDB.mockUserDBPath);
        File chatFile = new File(mockedDB.mockChatLogDBPath);
        File msgFile = new File(mockedDB.mockMsgDBPath);
        if ((!userFile.exists())) {
            System.out.println("Users file not generated !");
            throw new AssertionError();
        }
        if (!chatFile.exists()) {
            System.out.println("ChatLogs file not generated !");
            throw new AssertionError();
        }
        if (!msgFile.exists()) {
            System.out.println("Messages file not generated !");
            throw new AssertionError();
        }


    }

    @Test
    public void writeAndReadUserDB() {

        // Create new user
        User user = new User();
        user.setNickname("Arthy");
        user.setStatus("Testing the MockUtilityDatabase !");

        try {
            mockedDB.writeUser(user);
            User readUser = mockedDB.getUser(user.getUserID());

            if ((!readUser.getNickname().equals(user.getNickname()))) {
                System.out.println("User Nickname was not written/read correctly !");
                throw new AssertionError();
            }
            if ((!readUser.getStatus().equals(user.getStatus()))) {
                System.out.println("User Status was not written/read correctly !");
                throw new AssertionError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    @Test
    public void getSignedInUserProfileTest() {
      //  when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object val = mockedDataSnapshot.getValue();
                if (val == null){
                    System.out.println("User data is null!");
                    return;
                }
                System.out.println("User data is changed : " + val);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        System.out.println("Path : " + path);
        mockedDatabaseReference.child("arthur").addListenerForSingleValueEvent(listener);
        // check preferences are updated
    }


    @Test
    public void writeToDB() {

        path = "";
        User user = new User();
        user.setNickname("Archie");
        // Try writing to existing user
        mockedDatabaseReference.child("user").setValue(user);


        ValueEventListener listener;

        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user2 = dataSnapshot.getValue(User.class);

                System.out.println("User : " + user2.getNickname());
                Log.e("Firebase", "User data has been read.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read user", databaseError.toException());

            }
        };

        mockedDatabaseReference.child("0").addListenerForSingleValueEvent(listener);

        path = "";
        Date date = new Date();

        Message msg = new Message(10, new User(), "Coucou" + Integer.toString(0), date);
        // Try writing to existing user
        mockedDatabaseReference.child("messages");


        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Message msg2 = dataSnapshot.getValue(Message.class);

                System.out.println("User : " + msg2.getContentMessage());
                Log.e("Firebase", "Message data has been read.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read user", databaseError.toException());

            }
        };

        mockedDatabaseReference.child("0").addListenerForSingleValueEvent(listener);
        path = "";
        mockedDatabaseReference.child("chatlogs");

        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Participants : " + path);

                ChatLogs msg2 = dataSnapshot.getValue(ChatLogs.class);

                System.out.println("Participants : " + msg2.getParticipants().get(0).getUserID());
                Log.e("Firebase", "Message data has been read.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read user", databaseError.toException());

            }
        };

        mockedDatabaseReference.child("40").addListenerForSingleValueEvent(listener);


        return;

    }

}
    */
}