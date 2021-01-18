package me.internalizable.TweetToList;

import me.internalizable.TweetToList.auth.TTLAuth;
import me.internalizable.TweetToList.config.TTLConfig;
import me.internalizable.TweetToList.runtime.TTLRuntime;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Core {

    public static void main(String[] args) {
        TTLConfig config = new TTLConfig();

        try {
            config.init();
        } catch (Exception exc) {
            //todo log
        }

        Twitter twitter = TwitterFactory.getSingleton();

        if(config.getOAuthPublic() == "" || config.getOAuthSecret() == "") {
            System.out.println("Fatal OAuth error, please configure the file.");
            System.exit(0);
        }

        twitter.setOAuthConsumer(config.getOAuthPublic(),config.getOAuthSecret());

        if(!config.hasLogin()) {
            TTLAuth auth = new TTLAuth();

            try {
                auth.authInit(twitter, config);
            } catch (Exception exc) {
                //todo log
            }

        } else {
            twitter.setOAuthAccessToken(new AccessToken(config.getToken(), config.getTokenSecret()));
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TTLRuntime(twitter, config), 0L, TimeUnit.MINUTES.toMillis(5));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String exitString = br.readLine();

            System.out.print("Saving config with exit " + exitString);
            config.getConfigFile().set("userList", config.getUserList());
            config.saveConfig();

            System.exit(0);

        } catch(IOException exception) {
            exception.printStackTrace();
        }

    }

}
