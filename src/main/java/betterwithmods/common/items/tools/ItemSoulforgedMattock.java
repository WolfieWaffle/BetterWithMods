package betterwithmods.common.items.tools;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.module.hardcore.creatures.HCEnchanting;
import betterwithmods.util.item.ToolsManager;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Set;

public class ItemSoulforgedMattock extends ItemTool {
    private static final Set<Block> EFFECTIVE = Sets.union(ToolsManager.getEffectiveBlocks((ItemTool) BWMItems.STEEL_PICKAXE), ToolsManager.getEffectiveBlocks((ItemTool) BWMItems.STEEL_SHOVEL));

    public ItemSoulforgedMattock() {
        super(BWMItems.SOULFORGED_STEEL, EFFECTIVE);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return BWOreDictionary.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel")) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (PlayerHelper.isCurrentToolEffectiveOnBlock(stack, state))
            return efficiency;
        return EFFECTIVE.contains(state.getBlock()) ? this.efficiency : 1.0F;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("bwmmattock", "pickaxe", "shovel");
    }

    public boolean canHarvestBlock(IBlockState blockIn) {
        Block block = blockIn.getBlock();
        return toolMaterial.getHarvestLevel() >= block.getHarvestLevel(blockIn);
    }


    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return HCEnchanting.canEnchantSteel();
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return toolMaterial.getHarvestLevel();
    }
}
