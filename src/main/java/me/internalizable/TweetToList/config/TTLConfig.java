package me.internalizable.TweetToList.config;

import lombok.Getter;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TTLConfig {

    @Getter
    public YamlFile configFile;

    public void init() throws Exception {

        configFile = new YamlFile("data/config.yml");

        if (!getConfigFile().exists()) {
            System.out.println("New file has been created: " + getConfigFile().getFilePath() + "\n");
            getConfigFile().createNewFile(true);

            getConfigFile().set("userList", "");
            getConfigFile().set("login.hasLogin", false);
            getConfigFile().set("login.oauthp", "");
            getConfigFile().set("login.oauths", "");
            getConfigFile().set("login.token", "");
            getConfigFile().set("login.tokenSecret", "");
            getConfigFile().set("data.listID", "");

            saveConfig();

            System.exit(0);
        } else {
            System.out.println(getConfigFile().getFilePath() + " already exists, loading configurations...\n");
        }

        getConfigFile().loadWithComments();
        initList();
    }

    public void saveConfig() {
        try {
            getConfigFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Getter public List<Long> userList = new ArrayList<Long>();

    private void initList() {
        userList = configFile.getLongList("userList");
    }

    public boolean hasLogin() {
        return configFile.getBoolean("login.hasLogin");
    }

    public String getOAuthPublic() { return configFile.getString("login.oauthp"); }

    public String getOAuthSecret() { return configFile.getString("login.oauths"); }

    public String getToken() {
        return configFile.getString("login.token");
    }

    public String getTokenSecret() {
        return configFile.getString("login.tokenSecret");
    }

    public Long getListID() { return configFile.getLong("data.listID"); }

}
