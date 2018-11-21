package ch.epfl.sweng.radius.home;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.MapUtility;
import ch.epfl.sweng.radius.utils.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;

// TODO : On activity end, clear myUser empty Chaltogs (no message) and repush do
    // TODO     the same for userIDs

public class PeopleTab extends CustomUserTab {
    private MLocation myLocation;
    private double myRadius = UserInfo.getInstance().getCurrentUser().getRadius();
    private String radiusListener;
    List<String> userIDs = new ArrayList<>();

    private final CallBackDatabase radiusCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            myRadius = ((User) value).getRadius();
            // Call method to refresh userList
        }

        @Override
        public void onError(DatabaseError error) {

        }
    };

    private final CallBackDatabase locationCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            myLocation = (MLocation) value;

        }

        @Override
        public void onError(DatabaseError error) {
            Log.e("PeopleTab", "Database read error on my Location");
        }
    };

    private CallBackDatabase locationsCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            ArrayList<MLocation> locations = (ArrayList<MLocation>) value;
            for(MLocation loc : locations){
                // TODO Fix for non-user locations by checking TBD location type
                if(isInRadius(loc) && loc.isVisible() && (loc.getIsGroupLocation() != 1)){
                    userIDs.add(loc.getID());
                }

            }
        }

        @Override
        public void onError(DatabaseError error) {
            Log.e("PeopleTab", "Database read error all locations");
        }
    };

    public PeopleTab() {
    }
    protected  List<String> getIds(User current_user){
        final String userId = UserInfo.getInstance().getCurrentUser().getID();
        final Database database = Database.getInstance();
        radiusListener = userId + "_radiusListener";
        //  Get user Radius value and set listener for updates
        //  If it was already fetched, no need to read again, there is a listener

        // Get my Location
        // TODO Add listener like for radius
        database.readObjOnce(new MLocation(userId), Database.Tables.LOCATIONS, locationCallback);

        // Get all other locations in Radius and add corresponding user to List
        // TODO Setup a Listener instead of reading once
        database.readAllTableOnce(Database.Tables.LOCATIONS, locationsCallback);

        return userIDs;
    }

    private boolean isInRadius(MLocation loc) {

        return MapUtility.findDistance(myLocation, loc) < myRadius*1000;
    }

}
