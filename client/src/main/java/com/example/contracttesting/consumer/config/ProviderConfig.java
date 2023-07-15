package com.example.contracttesting.consumer.config;

import static java.lang.String.format;

public class ProviderConfig {

    public static String allSpellsUrl() {
        return "/spells";
    }

    public static String getSpellByNameUrl(String name) {
        return format("/spells/search?name=%s", name.toLowerCase());
    }

    public static String addSpellUrl() {
        return "/spells";
    }
}
