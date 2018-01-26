package betterwithmods.module.gameplay;

import betterwithmods.module.Feature;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;


/**
 * Created by primetoxinz on 6/25/17.
 */
public class NetherGrowth extends Feature {

    @Override
    public void init(FMLInitializationEvent event) {
        //TODO
//        addCauldronRecipe(new ItemStack(BWMBlocks.NETHER_GROWTH), new ItemStack[]{
//                new ItemStack(Blocks.BROWN_MUSHROOM),
//                new ItemStack(Blocks.RED_MUSHROOM),
//                new ItemStack(Blocks.MYCELIUM),
//                new ItemStack(Items.NETHER_WART),
//                new ItemStack(Items.ROTTEN_FLESH),
//                BlockUrn.getStack(BlockUrn.EnumType.FULL, 8),
//        });
    }

    @Override
    public String getFeatureDescription() {
        return "Adds Nether Growth, a fungus that will *infest* the Nether and stop all mobs from spawning. Be sure before placing it!";
    }
}
