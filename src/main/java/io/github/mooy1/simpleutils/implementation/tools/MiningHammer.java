package io.github.mooy1.simpleutils.implementation.tools;

import java.util.Locale;
import java.util.Locale.Category;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

@Deprecated
public final class MiningHammer extends SimpleSlimefunItem<ToolUseHandler> implements NotPlaceable {

    private final int radius;
    private final int blocks;

    public MiningHammer(ItemGroup category, Material material, String name, int size, int eff) {
        super(category, new SlimefunItemStack(
                ChatUtils.removeColorCodes(name).toUpperCase(Locale.ROOT) + "_MINING_HAMMER",
                material,
                name + " 挖矿榔头",
                "&4即将弃用，使用爆炸稿子代替"
        ), RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                null, null, null,
                null, new ItemStack(Material.BARRIER), null,
                null, null, null
        });

        getItem().addUnsafeEnchantment(Enchantment.DIG_SPEED, eff);

        // # of extra blocks that will be mined
        this.blocks = size * size - 1;
        this.radius = (size - 1) >> 1;

        setHidden(true);
    }

    @Nonnull
    @Override
    public ToolUseHandler getItemHandler() {
        return (e, item, fortune, drops) -> {
            Player p = e.getPlayer();

            if (ThreadLocalRandom.current().nextInt(10) == 0) {
                p.sendMessage("&4This item is deprecated. It will be removed soon. Use the explosive pick instead.");
            }

            if (p.isSneaking() || !Slimefun.getProtectionManager().hasPermission(p, e.getBlock(), Interaction.BREAK_BLOCK)) {
                return;
            }

            for (Block b : getBlocks(e.getBlock(), p.getFacing(), p.getLocation().getPitch())) {
                if (canBreak(p, b)) {
                    breakBlock(p, item, b, fortune);
                }
            }

            if (ThreadLocalRandom.current().nextInt(3 - this.radius + item.getEnchantmentLevel(Enchantment.DURABILITY)) == 0) {
                Damageable damageable = (Damageable) item.getItemMeta();
                damageable.setDamage(damageable.getDamage() + 1);
                if (damageable.getDamage() >= item.getType().getMaxDurability()) {
                    p.playSound(p.getEyeLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                    item.setAmount(0);
                } else {
                    item.setItemMeta((ItemMeta) damageable);
                }
            }
        };
    }

    private static boolean canBreak(Player p, Block b) {
        return !b.isEmpty()
                && !b.isLiquid()
                && !SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(b.getType())
                && Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.BREAK_BLOCK)
                && !BlockStorage.hasBlockInfo(b);
    }

    private static void breakBlock(Player p, ItemStack item, Block b, int fortune) {
        Slimefun.getProtectionManager().logAction(p, b, Interaction.BREAK_BLOCK);
        b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

        Material type = b.getType();

        if (type == Material.PLAYER_HEAD || SlimefunTag.SHULKER_BOXES.isTagged(type)) {
            b.breakNaturally(item);
        } else {
            boolean applyFortune = SlimefunTag.MINER_TALISMAN_TRIGGERS.isTagged(type);

            for (ItemStack drop : b.getDrops(item)) {
                b.getWorld().dropItemNaturally(b.getLocation(), applyFortune ? new CustomItemStack(drop, fortune) : drop);
            }

            b.setType(Material.AIR);
        }
    }

    private Block[] getBlocks(Block middle, BlockFace face, float pitch) {
        Block[] arr = new Block[this.blocks];
        int index = 0;
        if (pitch > 45 || pitch < -45) {
            for (int x = -this.radius ; x <= this.radius ; x++) {
                for (int z = -this.radius ; z <= this.radius ; z++) {
                    if (x != 0 || z != 0) {
                        arr[index++] = middle.getRelative(x, 0, z);
                    }
                }
            }
        } else if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            for (int x = -this.radius ; x <= this.radius ; x++) {
                for (int y = -this.radius ; y <= this.radius ; y++) {
                    if (y != 0 || x != 0) {
                        arr[index++] = middle.getRelative(x, y, 0);
                    }
                }
            }
        } else if (face == BlockFace.EAST || face == BlockFace.WEST) {
            for (int z = -this.radius ; z <= this.radius ; z++) {
                for (int y = -this.radius ; y <= this.radius ; y++) {
                    if (z != 0 || y != 0) {
                        arr[index++] = middle.getRelative(0, y, z);
                    }
                }
            }
        }
        return arr;
    }

}
