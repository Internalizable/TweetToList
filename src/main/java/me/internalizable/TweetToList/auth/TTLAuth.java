package me.internalizable.TweetToList.auth;

import me.internalizable.TweetToList.config.TTLConfig;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TTLAuth {

    public void authInit(Twitter twitter, TTLConfig config) throws Exception {

        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if available) or just hit enter. [PIN]:");
            String pin = br.readLine();
            try{
                if(pin.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }

        config.getConfigFile().set("login.token", accessToken.getToken());
        config.getConfigFile().set("login.tokenSecret", accessToken.getTokenSecret());
        config.getConfigFile().set("login.hasLogin", true);

        config.saveConfig();
    }

}
