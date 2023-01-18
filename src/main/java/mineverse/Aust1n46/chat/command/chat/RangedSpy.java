package mineverse.Aust1n46.chat.command.chat;

import mineverse.Aust1n46.chat.api.MineverseChatAPI;
import mineverse.Aust1n46.chat.api.MineverseChatPlayer;
import mineverse.Aust1n46.chat.localization.LocalizedMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RangedSpy extends Command {
    public RangedSpy() {
        super("rangedspy");
    }

    @Override
    public boolean execute(CommandSender sender, String command, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getServer().getConsoleSender().sendMessage(LocalizedMessage.COMMAND_MUST_BE_RUN_BY_PLAYER.toString());
            return true;
        }
        MineverseChatPlayer mcp = MineverseChatAPI.getOnlineMineverseChatPlayer((Player) sender);
        if (mcp.getPlayer().hasPermission("venturechat.rangedspy")) {
            if (!mcp.getRangedSpy()) {
                mcp.setRangedSpy(true);
                mcp.getPlayer().sendMessage(LocalizedMessage.RANGED_SPY_ON.toString());
                return true;
            }
            mcp.setRangedSpy(false);
            mcp.getPlayer().sendMessage(LocalizedMessage.RANGED_SPY_OFF.toString());
            return true;
        }
        mcp.getPlayer().sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
