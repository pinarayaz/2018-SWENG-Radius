package ch.epfl.sweng.radius.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.ChatLogDbUtility;
import ch.epfl.sweng.radius.utils.UserInfos;

/**
 * Activity that hosts messages between two users
 * MessageListActivity and many layout file comes in part from https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public class MessageListActivity extends AppCompatActivity {
    private RecyclerView myMessageRecycler;
    private MessageListAdapter myMessageAdapter;
    private EditText messageZone;
    private Firebase chatReference;
    private ChatLogs chatLogs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        messageZone = (EditText) findViewById(R.id.edittext_chatbox);



        /*

        // Test the chat view
        //User alfred = new User(UserInfos.getChatWithID());
        User alfred = new User();
        User mika = new User(UserInfos.getUserId());

        ArrayList<String> participantsId = new ArrayList<>();

        participantsId.add(alfred.getUserID());
        participantsId.add(mika.getUserID());

        Message m1 = new Message( alfred, "Hello", new Date());
        Message m2 = new Message( mika,"Hello alfred", new Date());
        Message m3 = new Message( alfred,"how are you ?", new Date());

        List messageList= new ArrayList();
        messageList.add(m1);
        messageList.add(m2);
        messageList.add(m3);
        */

        /*sort by date
        Collections.sort(messageList, new Comparator<UserMessage>() {
            @Override
            public int compare(UserMessage o1, UserMessage o2) {
                return (int) (o1.getCreatedAt() - o2.getCreatedAt());
            }
        });
        */

        // End Test

        //get chatlogs from db
        chatLogs = ChatLogDbUtility.getChatLogs(someChatLogsId);

        myMessageRecycler = findViewById(R.id.reyclerview_message_list);
        myMessageAdapter = new MessageListAdapter(this, chatLogs.getAllMessages());
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        myMessageRecycler.setAdapter(myMessageAdapter);


        Firebase.setAndroidContext(this);
        chatReference = new Firebase("https://radius-1538126456577.firebaseio.com/messages/" + UserInfos.getchatList().getChatId(receiver.getUserId()));

        findViewById(R.id.button_chatbox_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageZone.getText().toString();

                if (!message.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", message);
                    map.put("user", UserInfos.getUsername());
                    chatReference.push().setValue(map);
                    messageZone.setText("");
                }
            }
        });
        chatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String senderId = map.get("senderId").toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                Date sendingTime = null;
                try {
                    sendingTime = simpleDateFormat.parse(map.get("sendingTime").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                chatLogs.addMessage(new Message(senderId,message,sendingTime));
                myMessageAdapter.notify();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
