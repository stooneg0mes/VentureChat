package mineverse.Aust1n46.chat.api.events;

import mineverse.Aust1n46.chat.MineverseChat;
import mineverse.Aust1n46.chat.api.MineverseChatPlayer;
import mineverse.Aust1n46.chat.channel.ChatChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

/**
 * Event called when a message has been sent to a channel.
 * This event can not be cancelled.
 *
 * @author Aust1n46
 */
public class VentureChatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final MineverseChatPlayer mcp;
    private final String username;
    private final String nickname;
    private final String playerPrimaryGroup;
    private final ChatChannel channel;
    private final Set<Player> recipients;
    private final int recipientCount; //For not counting vanished players
    private final String chat;
    private final String firstFormat, secondFormat;
    private final String firstGlobalJSON, secondGlobalJSON;
    private final int firstHash, secondHash;
    private final boolean bungee;

    public VentureChatEvent(MineverseChatPlayer mcp, String username, String nickname, String playerPrimaryGroup, ChatChannel channel, Set<Player> recipients, int recipientCount, String chat, String firstFormat, String firstGlobalJSON, int firstHash, String secondFormat, String secondGlobalJSON, int secondHash, boolean bungee) {
        super(MineverseChat.ASYNC);
        this.mcp = mcp;
        this.username = username;
        this.nickname = nickname;
        this.playerPrimaryGroup = playerPrimaryGroup;
        this.channel = channel;
        this.recipients = recipients;
        this.recipientCount = recipientCount;
        this.chat = chat;
        this.firstFormat = firstFormat;
        this.firstGlobalJSON = firstGlobalJSON;
        this.firstHash = firstHash;
        this.secondFormat = secondFormat;
        this.secondGlobalJSON = secondGlobalJSON;
        this.secondHash = secondHash;
        this.bungee = bungee;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public MineverseChatPlayer getMineverseChatPlayer() {
        return this.mcp;
    }

    public String getUsername() {
        return this.username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getPlayerPrimaryGroup() {
        return this.playerPrimaryGroup;
    }

    public ChatChannel getChannel() {
        return this.channel;
    }

    public Set<Player> getRecipients() {
        return this.recipients;
    }

    //Could be lower than the total number of recipients because vanished players are not counted
    public int getRecipientCount() {
        return this.recipientCount;
    }

    public String getChat() {
        return this.chat;
    }

    public String getFirstFormat() {
        return this.firstFormat;
    }

    public String getSecondFormat() {
        return this.secondFormat;
    }

    public String getConsoleChat() {
        return this.firstFormat + " " + this.secondFormat + this.chat;
    }

    public String getFirstGlobalJSON() {
        return this.firstGlobalJSON;
    }

    public String getSecondGlobalJSON() {
        return this.secondGlobalJSON;
    }

    public int getFirstHash() {
        return this.firstHash;
    }

    public int getSecondHash() {
        return this.secondHash;
    }

    public boolean isBungee() {
        return this.bungee;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}