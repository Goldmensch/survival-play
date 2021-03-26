package de.nick.survivalplay;

import de.nick.smartclans.api.SmartclansAPI;
import de.nick.survivalplay.commands.*;
import de.nick.survivalplay.commands.home.DelhomeCommand;
import de.nick.survivalplay.commands.home.HomeCommand;
import de.nick.survivalplay.commands.home.SethomeCommand;
import de.nick.survivalplay.commands.msg.MsgCommand;
import de.nick.survivalplay.commands.msg.RCommand;
import de.nick.survivalplay.listeners.*;
import de.nick.survivalplay.sleepskip.NightSkipHandler;
import de.nick.survivalplay.storage.YamlStorage;
import de.nick.survivalplay.text.RankHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class SurvivalPlay extends JavaPlugin {

    private TpaCommand tpaCommand;
    private GamemodeCommand gamemodeCommand;
    private SpawnCommand spawnCommand;
    private MsgCommand msgCommand;
    private RCommand rCommand;
    private DiscordCommand discordCommand;
    private RtpCommand rtpCommand;
    private SurvivalplayCommand survivalplayCommand;
    private SethomeCommand sethomeCommand;
    private OnlinePlayersCommand onlinePlayersCommand;
    private DelhomeCommand delhomeCommand;
    private HomeCommand homeCommand;

    private PlayerMoveListener playerMoveListener;
    private EntitySpawnListener entitySpawnListener;
    private EntityDeathListener entityDeathListener;
    private PlayerLeaveListener playerLeaveListener;
    private PlayerBedEnterListener playerBedEnterListener;
    private ChatListener chatListener;
    private SmartClansListeners smartClansListeners;
    private PlayerJoinListener playerJoinListener;

    private ConfigHandler configHandler;
    private YamlStorage storage;
    private NightSkipHandler nightSkipHandler;

    private File storageFile;

    private TextComponent prefix;

    private SmartclansAPI smartclansAPI;

    private Broadcaster broadcaster;

    @Override
    public void onEnable() {
        // init variables
        initVariables();
        // register commands
        registerCommands();
        //register tab completer
        registerTabCompleter();
        // register listener
        registerListener();
        // check and build config
        configHandler.checkAndBuild();
        // sets all players tablist name
        RankHandler rankHandler = new RankHandler(smartclansAPI);
        for (Player player : Bukkit.getOnlinePlayers()) {
            rankHandler.updateTabName(player);
        }
        // start broadcasts
        broadcaster.start();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    // regsiter commands
    private void registerCommands() {
        Objects.requireNonNull(getCommand("tpa")).setExecutor(tpaCommand);
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(gamemodeCommand);
        Objects.requireNonNull(getCommand("spawn")).setExecutor(spawnCommand);
        Objects.requireNonNull(getCommand("msg")).setExecutor(msgCommand);
        Objects.requireNonNull(getCommand("r")).setExecutor(rCommand);
        Objects.requireNonNull(getCommand("discord")).setExecutor(discordCommand);
        Objects.requireNonNull(getCommand("rtp")).setExecutor(rtpCommand);
        Objects.requireNonNull(getCommand("survivalplay")).setExecutor(survivalplayCommand);
        Objects.requireNonNull(getCommand("survivalplay")).setTabCompleter(survivalplayCommand);
        Objects.requireNonNull(getCommand("sethome")).setExecutor(sethomeCommand);
        Objects.requireNonNull(getCommand("onlineplayers")).setExecutor(onlinePlayersCommand);
        Objects.requireNonNull(getCommand("delhome")).setExecutor(delhomeCommand);
        Objects.requireNonNull(getCommand("home")).setExecutor(homeCommand);
    }

    //register TabCompleter
    private void registerTabCompleter() {
        Objects.requireNonNull(getCommand("home")).setTabCompleter(homeCommand);
        Objects.requireNonNull(getCommand("delhome")).setTabCompleter(delhomeCommand);
        Objects.requireNonNull(getCommand("sethome")).setTabCompleter(sethomeCommand);

    }

    //register listener
    private void registerListener() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(playerMoveListener, this);
        pm.registerEvents(entitySpawnListener, this);
        pm.registerEvents(entityDeathListener, this);
        pm.registerEvents(playerBedEnterListener, this);
        pm.registerEvents(playerLeaveListener, this);
        pm.registerEvents(chatListener, this);
        pm.registerEvents(smartClansListeners, this);
        pm.registerEvents(playerJoinListener, this);
    }

    //init variables
    private void initVariables() {
        //apis
        smartclansAPI = new SmartclansAPI();
        //files
        storageFile = new File(getDataFolder(), "data.yml");
        //data
        configHandler = new ConfigHandler(this);
        storage = new YamlStorage(storageFile);
        //handlers
        nightSkipHandler = new NightSkipHandler(this);
        // prefix
        prefix = new TextComponent("[Survivalplay-Plugin] ");
        prefix.setColor(ChatColor.GOLD);
        // commands
        tpaCommand = new TpaCommand(this);
        gamemodeCommand = new GamemodeCommand();
        spawnCommand = new SpawnCommand(this);
        msgCommand = new MsgCommand();
        rCommand = new RCommand(this);
        discordCommand = new DiscordCommand(this);
        rtpCommand = new RtpCommand(this);
        survivalplayCommand = new SurvivalplayCommand(this);
        sethomeCommand = new SethomeCommand(this);
        onlinePlayersCommand = new OnlinePlayersCommand();
        delhomeCommand = new DelhomeCommand(this);
        homeCommand = new HomeCommand(this);
        // listener
        playerMoveListener = new PlayerMoveListener(this);
        entitySpawnListener = new EntitySpawnListener(this);
        entityDeathListener = new EntityDeathListener(this);
        playerBedEnterListener = new PlayerBedEnterListener(this);
        playerLeaveListener = new PlayerLeaveListener(this);
        chatListener = new ChatListener(smartclansAPI);
        smartClansListeners = new SmartClansListeners(smartclansAPI);
        playerJoinListener = new PlayerJoinListener(this);
        //others
        broadcaster = new Broadcaster(this);
    }

    public TpaCommand getTpaCommand() {
        return tpaCommand;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public YamlStorage getStorage() {
        return storage;
    }

    public MsgCommand getMsgCommand() {
        return msgCommand;
    }

    public RtpCommand getRtpCommand() {
        return rtpCommand;
    }

    public TextComponent getPrefix() {
        return prefix;
    }

    public NightSkipHandler getNightSkipHandler() {
        return nightSkipHandler;
    }

    public SpawnCommand getSpawnCommand() {
        return spawnCommand;
    }

    public SmartclansAPI getSmartclansAPI() {
        return smartclansAPI;
    }

    public HomeCommand getHomeCommand() {
        return homeCommand;
    }
}

