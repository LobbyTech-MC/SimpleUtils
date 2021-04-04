package io.github.mooy1.simpleutils;

import io.github.mooy1.infinitylib.core.PluginUtils;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import org.bukkit.Material;

import java.util.Locale;

@UtilityClass
public final class Items {

    public static final Category CATEGORY = new Category(PluginUtils.getKey("main"), new CustomItem(Material.COMPOSTER, "&6简易工具"), 0);
    
    // dusts
    public static final SlimefunItemStack NICKEL_DUST = dust(Material.SUGAR, "&7镍");
    public static final SlimefunItemStack COBALT_DUST = dust(Material.SUGAR, "&9钴");

    private static SlimefunItemStack dust(Material material, String  name) {
        return new SlimefunItemStack(
                name.substring(2).toUpperCase(Locale.ROOT) + "_DUST",
                material,
                name + "粉"
        );
    }
    
    // misc
    public static final SlimefunItemStack AUTOMATON_CORE = new SlimefunItemStack(
      "AUTOMATON_CORE",
      Material.POLISHED_GRANITE,
      "&6自动化核心",
      "&7简易自动化机器核心部件"      
    );
    
    public static final SlimefunItemStack HAMMER_ROD = new SlimefunItemStack(
            "HAMMER_ROD",
            Material.BLAZE_ROD,
            "&6榔头零件",
            "&7挖矿榔头的核心部件"
    );
    
}
