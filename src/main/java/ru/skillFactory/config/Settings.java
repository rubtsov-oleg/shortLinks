package ru.skillFactory.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Settings {
    public static final String TRANSITION_LIMIT;
    public static final String LIVE_HOURS;

    static {
        Dotenv dotenv = Dotenv.load();
        TRANSITION_LIMIT = dotenv.get("TRANSITION_LIMIT");
        LIVE_HOURS = dotenv.get("LIVE_HOURS");
    }
}
