package me.char321.sfadvancements.core.gui;

import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import me.char321.sfadvancements.SFAdvancements;
import me.char321.sfadvancements.api.Advancement;
import me.char321.sfadvancements.api.AdvancementGroup;
import me.char321.sfadvancements.api.criteria.Criterion;
import me.char321.sfadvancements.core.registry.AdvancementsRegistry;
import me.char321.sfadvancements.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpenGUI {
    private final Inventory inventory;
    private final AdvancementsRegistry registry = SFAdvancements.getRegistry();
    private final UUID player;
    private int page = 1;
    private int groupIndex = 0;
    private int scroll = 0;

    public OpenGUI(Player player) {
        this(player.getUniqueId());
    }

    public OpenGUI(UUID player) {
        this.inventory = Bukkit.createInventory(null, 54, ChatColor.BLUE + "進度");
        this.player = player;
        refresh();
    }

    public Inventory getInventory() {
        refresh();
        return inventory;
    }

    public void click(int slot) {
        if (slot == 1 && page > 1) {
            page--;
        } else if (slot == 7) {
            int maxPage = (registry.getAdvancementGroups().size() - 1) / 5 + 1;
            if (page + 1 <= maxPage) {
                page++;
            }
        } else if (slot > 1 && slot < 7) {
            int possibleIndex = 5 * (page - 1) + (slot - 2);
            if (registry.getAdvancementGroups().size() > possibleIndex) {
                groupIndex = possibleIndex;
                scroll = 0;
            }
        } else if (slot == 17 && scroll > 0) {
            scroll--;
        } else if (slot == 53) {
            AdvancementGroup group = registry.getAdvancementGroups().get(groupIndex);
            //make better
            int size = group.getVisibleAdvancements(player).size();
            int maxScroll = (size - 1) / 8 - 4;
            if (scroll + 1 <= maxScroll) {
                scroll++;
            }
        }
        refresh();
    }

    public void refresh() {
        refreshBorders();
        refreshStats();
        refreshArrows();
        refreshGroups();
        refreshScroll();
        refreshAdvancements();
    }

    private void refreshBorders() {
        inventory.setItem(0, MenuItems.GRAY);
    }

    private void refreshStats() {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player));
        meta.setDisplayName(ChatColor.YELLOW + "狀態");
        StringBuilder completedadvancements = new StringBuilder();
        completedadvancements.append(ChatColor.GRAY).append("已完成進度: ");
        int completed = SFAdvancements.getAdvManager().getProgress(player).getCompletedAdvancements().size();
        int total = SFAdvancements.getRegistry().getAdvancements().size();
        if(completed == total) {
            completedadvancements.append(ChatColor.YELLOW);
        } else {
            completedadvancements.append(ChatColor.WHITE);
        }
        completedadvancements.append(completed).append(ChatColor.GRAY).append("/").append(total);
        List<String> lore = new ArrayList<>();
        lore.add(completedadvancements.toString());
        meta.setLore(lore);
        head.setItemMeta(meta);
        inventory.setItem(8, head);
    }

    private void refreshArrows() {
        int maxPage = (registry.getAdvancementGroups().size() - 1) / 5 + 1;
        String pageLore = "&7(" + page + " / " + maxPage + ")";

        ItemStack leftArrow;
        if (page == 1) {
            leftArrow = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&7上一頁", pageLore);
        } else {
            leftArrow = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&e上一頁", pageLore);
        }
        inventory.setItem(1, leftArrow);

        ItemStack rightArrow;
        if (page == maxPage) {
            rightArrow = new CustomItemStack(Material.BLACK_STAINED_GLASS_PANE, "&7下一頁", pageLore);
        } else {
            rightArrow = new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&e下一頁", pageLore);
        }
        inventory.setItem(7, rightArrow);
    }

    private void refreshGroups() {
        for (int i=0;i<5;i++) {
            int slot = i+2;
            int dispIndex = 5 * (page - 1) + i;
            ItemStack display;
            if (registry.getAdvancementGroups().size() > dispIndex) {
                display = registry.getAdvancementGroups().get(dispIndex).getDisplayItem();
                if (dispIndex == groupIndex) {
                    display = Utils.makeShiny(display);
                }
            } else {
                display = MenuItems.GRAY;
            }
            inventory.setItem(slot, display);
        }
    }

    private void refreshScroll() {
        AdvancementGroup group = registry.getAdvancementGroups().get(groupIndex);
        inventory.setItem(26, MenuItems.YELLOW);
        inventory.setItem(35, MenuItems.YELLOW);
        inventory.setItem(44, MenuItems.YELLOW);

        ItemStack scrollUp;
        if (scroll == 0) {
            scrollUp = MenuItems.YELLOW;
        } else {
            scrollUp = new CustomItemStack(Material.ARROW, "&e向上");
        }
        inventory.setItem(17, scrollUp);

        ItemStack scrollDown;
        int size = group.getVisibleAdvancements(player).size();
        int maxScroll = (size - 1) / 8 - 4;
        if (scroll >= maxScroll) {
            scrollDown = MenuItems.YELLOW;
        } else {
            scrollDown = new CustomItemStack(Material.ARROW, "&e向下");
        }
        inventory.setItem(53, scrollDown);
    }

    private void refreshAdvancements() {
        AdvancementGroup group = registry.getAdvancementGroups().get(groupIndex);
        List<Advancement> advancements = group.getVisibleAdvancements(player);
        for (int i = 0; i < 40; i++) {
            int row = i / 8 + 1;
            int col = i % 8;
            int slot = row * 9 + col;

            int advindex = i + 8 * scroll;
            ItemStack display = null;
            if (advindex < advancements.size()) {
                Advancement adv = advancements.get(advindex);
                display = adv.getDisplay().clone();
                ItemMeta displayim = display.getItemMeta();

                if (SFAdvancements.getAdvManager().isCompleted(player, adv)) {
                    Utils.makeShiny(displayim);
                }

                List<String> lore = displayim.getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                }
                lore.add("");

                for (Criterion criterion : adv.getCriteria()) {
                    String criterionName = criterion.getName();
                    int progress = SFAdvancements.getAdvManager().getCriterionProgress(player, criterion);
                    int max = criterion.getCount();
                    boolean cridone = progress >= max;
                    lore.add(ChatColor.GRAY + criterionName + ": " + (cridone? ChatColor.YELLOW : ChatColor.WHITE) + progress + "/" + max);
                }
                displayim.setLore(lore);
                display.setItemMeta(displayim);
            }

            inventory.setItem(slot, display);
        }
    }
}
