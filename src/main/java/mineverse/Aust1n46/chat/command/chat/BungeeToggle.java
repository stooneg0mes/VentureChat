package mineverse.Aust1n46.chat.command.chat;

import mineverse.Aust1n46.chat.MineverseChat;
import mineverse.Aust1n46.chat.api.MineverseChatAPI;
import mineverse.Aust1n46.chat.api.MineverseChatPlayer;
import mineverse.Aust1n46.chat.localization.LocalizedMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BungeeToggle extends Command {
    public BungeeToggle() {
        super("bungeetoggle");
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getServer().getConsoleSender().sendMessage(LocalizedMessage.COMMAND_MUST_BE_RUN_BY_PLAYER.toString());
            return true;
        }
        MineverseChatPlayer mcp = MineverseChatAPI.getOnlineMineverseChatPlayer((Player) sender);
        if (mcp.getPlayer().hasPermission("venturechat.bungeetoggle")) {
            if (!mcp.getBungeeToggle()) {
                mcp.setBungeeToggle(true);
                mcp.getPlayer().sendMessage(LocalizedMessage.BUNGEE_TOGGLE_ON.toString());
                MineverseChat.synchronize(mcp, true);
                return true;
            }
            mcp.setBungeeToggle(false);
            mcp.getPlayer().sendMessage(LocalizedMessage.BUNGEE_TOGGLE_OFF.toString());
            MineverseChat.synchronize(mcp, true);
            return true;
        }
        mcp.getPlayer().sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
