package ch.epfl.sweng.radius.home;

import java.util.List;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.GroupLocationFetcher;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.customGroups.CustomGroupTab;

public class GroupTab extends CustomGroupTab {
    public GroupTab(){}

    @Override
    protected List<String> getIds(User current_user) {
        final String userId = current_user.getID();
        final Database database = Database.getInstance();
        //  Get user Radius value and set listener for updates
        //  If it was already fetched, no need to read again, there is a listener

        GroupLocationFetcher groupLocationFetcher = new GroupLocationFetcher();
        database.readAllTableOnce(Database.Tables.LOCATIONS, groupLocationFetcher);

        for(String s :groupLocationFetcher.getGroupLocationsIds()) {
            System.out.println(s);
        }
        return groupLocationFetcher.getGroupLocationsIds();
    }
}
