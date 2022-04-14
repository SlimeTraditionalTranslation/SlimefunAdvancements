package me.char321.sfadvancements.core.command;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.char321.sfadvancements.SFAdvancements;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DumpItemCommand implements SubCommand {
    @Override
    public boolean onExecute(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("你必須是玩家才能執行此指令");
            return false;
        }

        Player p = (Player) sender;

        sender.sendMessage("正在將你手上的物品寫成yml檔案...");
        ItemStack item = p.getInventory().getItemInMainHand();
        SFAdvancements.info("物品表示 " + item);

        if(!item.hasItemMeta()) {
            SFAdvancements.info("這個物品可以只用字串來表示 \n" + item.getType().name());
        }

        ItemMeta im = item.getItemMeta();

        String type = item.getType().name();

        Optional<String> itemData = Slimefun.getItemDataService().getItemData(im);
        if (itemData.isPresent()) {
            String id = itemData.get();
            if (SlimefunUtils.isItemSimilar(item, SlimefunItem.getById(id).getItem(), true)) {
                SFAdvancements.info("這個物品可以只用字串來表示 \n" + id);
            }
            type = id;
        }

        StringBuilder representation = new StringBuilder();
        representation.append("type: ").append(type).append("\n");
        representation.append("name: ").append(ItemUtils.getItemName(item).replace(ChatColor.COLOR_CHAR, '&').replaceAll("[\\[\\]]", "")).append("\n");
        if(im.hasLore()) {
            representation.append("lore: ").append("\n");
            for (String s : im.getLore()) {
                representation.append("  - ").append(s.replace(ChatColor.COLOR_CHAR, '&')).append("\n");
            }
        }
        SFAdvancements.info("如果你只關心外觀, 你可以用以下方式表示該物品 \n" + representation);

        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("item", item);
        SFAdvancements.info("完整的yml檔案表示: \n" + configuration.saveToString());

        sender.sendMessage("完成! 查看後台的詳細資訊.");
        return true;
    }

    @Nonnull
    @Override
    public String getCommandName() {
        return "dumpitem";
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
