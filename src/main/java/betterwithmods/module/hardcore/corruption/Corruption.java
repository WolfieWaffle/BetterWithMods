package betterwithmods.module.hardcore.corruption;

import betterwithmods.common.blocks.BlockNetherportal;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.module.Feature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Corruption extends Feature {


    private static Block NETHER_PORTAL = new BlockNetherportal().setRegistryName("portal");

    public static boolean disablePortalPigmen;

    @Override
    public void setupConfig() {
        disablePortalPigmen = loadPropBool("Disable Pigmen spawn from portal", "Disables the ability for pigmen to spawn from netherportals.", true);
    }


    @Override
    public void preInit(FMLPreInitializationEvent event) {
//        BWMBlocks.registerBlock(NETHER_PORTAL);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {


    }

    @Override
    public String getFeatureDescription() {
        return "Often the player messes with forces they do not understand and can cause many things that are not expected.";
    }


    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @SubscribeEvent
    public void onCorruptEvent(CorruptEvent event) {
        if (event.getWorld().getTileEntity(event.getPos()) instanceof TileEntityFilteredHopper) {
            HopperCorruption.INSTANCE.tick(event.getWorld(),event.getPos(),0);
        } else if (event.getState().getBlock() instanceof BlockPortal) {

        }
    }


}
