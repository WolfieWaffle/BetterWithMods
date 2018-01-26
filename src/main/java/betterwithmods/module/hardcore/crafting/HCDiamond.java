package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.CrucibleRecipes;
import betterwithmods.module.gameplay.MetalReclaming;
import betterwithmods.module.hardcore.needs.HCTools;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCDiamond extends Feature {
    public HCDiamond() {
        recipeCondition = true;
    }

    @Override
    public String getFeatureDescription() {
        return "Makes it so diamonds have to me made into an ingot alloy to be used in certain recipes";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_axe"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_hoe"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_pickaxe"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_sword"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_shovel"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_helmet"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_chestplate"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_leggings"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_boots"));
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if (ModuleLoader.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            if (HCTools.changeAxeRecipe) {
                CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2), new Object[]{new ItemStack(Items.DIAMOND_AXE, 1, OreDictionary.WILDCARD_VALUE)});
            } else {
                CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3), new Object[]{new ItemStack(Items.DIAMOND_AXE, 1, OreDictionary.WILDCARD_VALUE)});
            }
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2),new Object[]{new ItemStack(Items.DIAMOND_HOE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3),new Object[]{new ItemStack(Items.DIAMOND_PICKAXE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 1),new Object[]{new ItemStack(Items.DIAMOND_SHOVEL, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2),new Object[]{new ItemStack(Items.DIAMOND_SWORD, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 5),new Object[]{new ItemStack(Items.DIAMOND_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 8),new Object[]{new ItemStack(Items.DIAMOND_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 7),new Object[]{new ItemStack(Items.DIAMOND_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
            CrucibleRecipes.addStokedCrucibleRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 4),new Object[]{new ItemStack(Items.DIAMOND_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
        }
    }


    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }
}
