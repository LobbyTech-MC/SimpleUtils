package io.github.mooy1.simpleutils.blocks.shapedrecipe;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * An output with the amount of each item that was inputted
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public final class ShapedOutput {

    private final ItemStack output;
    private final int[] inputAmounts;
	public ShapedOutput(ItemStack output, int[] inputAmounts) {
		this.output = output;
		this.inputAmounts = inputAmounts;
	}
	public int[] getInputAmounts() {
		return inputAmounts;
	}
	public ItemStack getOutput() {
		return output;
	}
    
}
