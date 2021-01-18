package me.internalizable.TweetToList.config;

import lombok.Getter;
import me.internalizable.TweetToList.Core;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;


public class TTLConfig {

    @Getter
    private Core core;

    @Getter
    public YamlFile configFile;

    public TTLConfig(Core core) {
        this.core = core;
    }

    public void init() throws Exception {

        configFile = new YamlFile("data/config.yml");

        if (!getConfigFile().exists()) {
            System.out.println("New file has been created: " + getConfigFile().getFilePath() + "\n");
            getConfigFile().createNewFile(true);
            getConfigFile().options().copyDefaults(true);
        } else {
            System.out.println(getConfigFile().getFilePath() + " already exists, loading configurations...\n");
        }

        getConfigFile().loadWithComments();
    }

    public void saveConfig() {
        try {
            getConfigFile().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
