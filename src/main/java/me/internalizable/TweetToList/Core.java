package me.internalizable.TweetToList;

import java.net.URL;

public class Core {

    public static void main(String[] args) {

    }


    public URL getResource(final String file) {
        return Core.class.getClassLoader().getResource(file);
    }
}
