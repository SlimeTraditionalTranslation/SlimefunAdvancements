package me.char321.sfadvancements.core.command;

import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class SaveCommand implements SubCommand {

    @Override
    public boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
        try {
            SFAdvancements.getAdvManager().save();
            sender.sendMessage("成功儲存進度");
            return true;
        } catch(IOException e) {
            sender.sendMessage("儲存進度時出現錯誤!");
            sender.sendMessage("查看後台以獲取詳細資訊");
            SFAdvancements.logger().log(Level.SEVERE, e, () -> "Could not save advancements");
            return false;
        }
    }

    @Override
    public @Nonnull String getCommandName() {
        return "save";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
