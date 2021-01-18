package me.internalizable.TweetToList.runtime;

import me.internalizable.TweetToList.config.TTLConfig;
import me.internalizable.TweetToList.utils.StaticUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UserList;

import java.util.List;
import java.util.TimerTask;

public class TTLRuntime extends TimerTask {

    private Twitter twitter;
    private TTLConfig config;

    public TTLRuntime(Twitter twitter, TTLConfig config) {
        this.twitter = twitter;
        this.config = config;
    }

    @Override
    public void run() {

        System.out.println("Fetching followers...");

        List<Long> currentFollowers = StaticUtils.getFollowersIDs(twitter);

        System.out.println("Fetched followers succesfully.");

        for(Long userInList : config.getUserList()) {
            if(currentFollowers.contains(userInList))
                currentFollowers.remove(userInList);
        }

        try {

            for(Long userToAdd : currentFollowers) {
                System.out.println("Adding user with ID " + userToAdd + " to list " + config.getListID());

                UserList list = twitter.createUserListMember(config.getListID(), userToAdd);
                config.getUserList().add(userToAdd);
                StaticUtils.handleRateLimit(list.getRateLimitStatus());
            }

        } catch (TwitterException exc) {
            if(exc.getErrorCode() != 104 && exc.getStatusCode() != 104)
                exc.printStackTrace();
        }

    }

}
