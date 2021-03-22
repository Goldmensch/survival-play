package de.nick.survivalplay.storage;

public enum YamlStoragePaths {

    SPAWN("server.spawn"),
    PRIVATE_HOME("homes.private"),
    PUBLIC_HOME("homes.public"),
    PLAYER_DATA_NAME("player.name"),
    PLAYER_DATA_UUID("player.uuid");

    final String path;

    YamlStoragePaths(String s) {
        path = s;
    }

    public String getPath() {
        return path;
    }
}
