package betterwithmods.module.compat;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.blocks.mini.*;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.AnvilRecipes;
import betterwithmods.module.gameplay.SawRecipes;
import betterwithmods.module.hardcore.crafting.HCSaw;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static betterwithmods.common.BWMBlocks.registerBlock;
import static betterwithmods.common.BWMBlocks.setInventoryModel;
import static betterwithmods.common.BWOreDictionary.registerOre;

/**
 * Created by primetoxinz on 5/27/17.
 */
@SuppressWarnings("unused")
public class Rustic extends CompatFeature {
    public static final Block SIDING = new BlockSiding(Material.ROCK) {
        @Override
        public int getUsedTypes() {
            return 3;
        }

        @Override
        public Material getMaterial(IBlockState state) {
            int type = state.getValue(BlockMini.TYPE);
            return type < 2 ? BlockMini.MINI : Material.ROCK;
        }
    }.setRegistryName("rustic_compat_siding");
    public static final Block MOULDING = new BlockMoulding(Material.ROCK) {
        @Override
        public int getUsedTypes() {
            return 3;
        }

        @Override
        public Material getMaterial(IBlockState state) {
            int type = state.getValue(BlockMini.TYPE);
            return type < 2 ? BlockMini.MINI : Material.ROCK;
        }
    }.setRegistryName("rustic_compat_moulding");
    public static final Block CORNER = new BlockCorner(Material.ROCK) {
        @Override
        public int getUsedTypes() {
            return 3;
        }

        @Override
        public Material getMaterial(IBlockState state) {
            int type = state.getValue(BlockMini.TYPE);
            return type < 2 ? BlockMini.MINI : Material.ROCK;
        }
    }.setRegistryName("rustic_compat_corner");
    public String[] woods = {"oak", "spruce", "birch", "jungle", "acacia", "big_oak", "olive", "ironwood"};

    public Rustic() {
        super("rustic");
        recipeCondition = true;
    }


    @Override
    public void setupConfig() {
        super.setupConfig();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerBlock(SIDING, new ItemBlockMini(SIDING));
        registerBlock(MOULDING, new ItemBlockMini(MOULDING));
        registerBlock(CORNER, new ItemBlockMini(CORNER));
        ItemBark.barks.add("olive");
        ItemBark.barks.add("ironwood");

        BWMRecipes.removeRecipe(new ItemStack(getBlock(new ResourceLocation("rustic", "rope"))));
        Block plank = getBlock("rustic:planks");
        Block log = getBlock("rustic:log");
        for (int i = 0; i < 2; i++) {
            SawRecipes.addSawRecipe(log, i, new ItemStack(plank, 4, i), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 2), ItemBark.getStack(woods[i + 6], 1));
            SawRecipes.addSawRecipe(plank, i, new ItemStack(SIDING, 2, i));
            SawRecipes.addSawRecipe(SIDING, i, new ItemStack(MOULDING, 2, i));
            SawRecipes.addSawRecipe(MOULDING, i, new ItemStack(CORNER, 2, i));
            SawRecipes.addSawRecipe(CORNER, i, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR, 2));
            addHardcoreRecipe(new ShapelessOreRecipe(null, new ItemStack(plank, 1, i), new ItemStack(SIDING, 1, i), new ItemStack(SIDING, 1, i)).setRegistryName(new ResourceLocation("betterwithmods", "rustic_" + woods[i + 6] + "_plank_recover")));
            addHardcoreRecipe(new ShapelessOreRecipe(null, new ItemStack(SIDING, 1, i), new ItemStack(MOULDING, 1, i), new ItemStack(MOULDING, 1, i)).setRegistryName(new ResourceLocation("betterwithmods", "rustic_" + woods[i + 6] + "_siding_recover")));
            addHardcoreRecipe(new ShapelessOreRecipe(null, new ItemStack(MOULDING, 1, i), new ItemStack(CORNER, 1, i), new ItemStack(CORNER, 1, i)).setRegistryName(new ResourceLocation("betterwithmods", "rustic_" + woods[i + 6] + "_moulding_recover")));
        }
        boolean isHCSawEnabled = ModuleLoader.isFeatureEnabled(HCSaw.class);
        if (isHCSawEnabled) {
            BWMRecipes.removeRecipe("rustic:olive_door");
            BWMRecipes.removeRecipe("rustic:ironwood_door");
            BWMRecipes.removeRecipe("rustic:crop_stake");
        }

        for (int i = 0; i < woods.length; i++) {
            String chair_loc = "rustic:" + woods[i] + "_chair";
            String table_loc = "rustic:" + woods[i] + "_table";
            if (isHCSawEnabled) {
                BWMRecipes.removeRecipe(chair_loc);
                BWMRecipes.removeRecipe(table_loc);
                if (i >= 6) {
                    String fence_gate = "rustic:" + woods[i] + "_fence_gate";
                    String fence = "rustic:" + woods[i] + "_fence";
                    BWMRecipes.removeRecipe(fence_gate);
                    BWMRecipes.removeRecipe(fence);
                }
            }
        }
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation("rustic_siding"), new ItemStack(SIDING, 8, 2), "SSSS", 'S', getBlock("rustic:slate"));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation("rustic_moulding"), new ItemStack(MOULDING, 8, 2), "SSSS", 'S', new ItemStack(SIDING, 1, 2));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation("rustic_corner"), new ItemStack(CORNER, 8, 2), "SSSS", 'S', new ItemStack(MOULDING, 1, 2));

        registerOre("sidingWood", new ItemStack(SIDING, 1, 0), new ItemStack(SIDING, 1, 1));
        registerOre("mouldingWood", new ItemStack(MOULDING, 1, 0), new ItemStack(MOULDING, 1, 1));
        registerOre("cornerWood", new ItemStack(CORNER, 1, 0), new ItemStack(CORNER, 1, 1));

    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        setInventoryModel(SIDING);
        setInventoryModel(MOULDING);
        setInventoryModel(CORNER);

    }

}

