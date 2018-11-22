package ch.epfl.sweng.radius.home;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.GroupLocationFetcher;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.customGroups.CustomGroupTab;

public class GroupTab extends CustomGroupTab implements DBLocationObserver {
    public GroupTab(){
        OthersInfo.getInstance().addLocationObserver(this);
    }

    @Override
    protected List<String> getIds(User current_user) {
        //  Get user Radius value and set listener for updates
        //  If it was already fetched, no need to read again, there is a listener

        GroupLocationFetcher groupLocationFetcher = new GroupLocationFetcher();
        database.readAllTableOnce(Database.Tables.LOCATIONS, groupLocationFetcher);

        List<String> returnList = new ArrayList<String>();
        returnList.add("1");
        returnList.add("2");
        Log.e("MessageList", "Size of groupLoc is :" + Integer.toString(groupLocationFetcher.getGroupLocationsIds().size()));
        return new ArrayList<String>(OthersInfo.getInstance().getGroupsPos().keySet());
}

    @Override
    public void onLocationChange(String id) {

    }
}
