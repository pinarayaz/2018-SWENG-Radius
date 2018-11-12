/** Represents an interface to communicate with the Firebase Realtime Database
 * @usage : 1 . Instantiate class by providing the table we need to access in the DB
 *              ("user", "message", ...)
 *          2 . Read objects of this table by providing an object with the id we want to match.
 *          3 . Write/Update by providing the object we need to store
 */
package ch.epfl.sweng.radius.database;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtility extends Database{
    public FirebaseUtility(){}

    @Override
    public String getCurrent_user_id() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void readObjOnce(final DatabaseObject obj,
                            final Tables tableName,
                            final CallBackDatabase callback) {
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .child(obj.getID())
                .addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void  onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    writeInstanceObj(obj, tableName);
                    callback.onFinish(obj);
                }
                else
                    callback.onFinish(dataSnapshot.getValue(obj.getClass()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    @Override
    public void readObj(final DatabaseObject obj,
                        final Tables tableName,
                        final CallBackDatabase callback) {
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .child(obj.getID())
                .addValueEventListener( new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(obj.getID())) {
                    writeInstanceObj(obj, tableName);
                    callback.onFinish(obj);
                }
                else
                    callback.onFinish(dataSnapshot.getValue(obj.getClass()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    @Override
    public void readListObjOnce(final List<String> ids,
                            final Tables tableName,
                            final CallBackDatabase callback) {
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DatabaseObject> allItems = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    DatabaseObject snap = (DatabaseObject)postSnapshot
                            .getValue(tableName.getTableClass());
                    if (ids.contains(snap.getID())) {
                        allItems.add((DatabaseObject)postSnapshot
                                .getValue(tableName.getTableClass()));
                    }
                }
                callback.onFinish(allItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);

            }
        });
    }

    @Override
    public void readAllTableOnce(final Tables tableName, final CallBackDatabase callback) {
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<DatabaseObject> allItems = new ArrayList<>();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            DatabaseObject snap = (DatabaseObject)postSnapshot
                                    .getValue(tableName.getTableClass());
                                allItems.add((DatabaseObject)postSnapshot
                                        .getValue(tableName.getTableClass()));
                        }
                        callback.onFinish(allItems);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(databaseError);

                    }
                });
    }

    @Override
    public void writeInstanceObj(final DatabaseObject obj, final Tables tableName){
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .child(obj.getID()).setValue(obj);
    }
}


