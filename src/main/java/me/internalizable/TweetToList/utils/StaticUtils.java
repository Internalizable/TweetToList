package me.internalizable.TweetToList.utils;

import me.internalizable.TweetToList.Core;
import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

public class StaticUtils {

    public static List<Long> getFollowersIDs(Twitter twitter) {
        List<Long> followingList = new ArrayList<Long>();

        long cursor = -1L;

        IDs ids;

        try {
            do {
                ids = twitter.getFollowersIDs(twitter.getId(), cursor);

                for(long userID : ids.getIDs()){
                    followingList.add(userID);
                }

                cursor = ids.getNextCursor();
                handleRateLimit(ids.getRateLimitStatus());
            } while(cursor != 0);

        } catch (TwitterException exception) {
            exception.printStackTrace();
        }

        return followingList;
    }

    public static void handleRateLimit(RateLimitStatus rateLimitStatus) {

        if(rateLimitStatus == null)
            return;

        int remaining = rateLimitStatus.getRemaining();
        if (remaining == 0) {
            int resetTime = rateLimitStatus.getSecondsUntilReset() + 5;
            int sleep = (resetTime * 1000);
            try {
                Thread.sleep(sleep > 0 ? sleep : 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
