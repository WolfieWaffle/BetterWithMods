package betterwithmods.proxy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
    default void preInit(FMLPreInitializationEvent event) {
    }

    default void init(FMLInitializationEvent event) {
    }

    default void postInit(FMLPostInitializationEvent event) {
    }

    default void addResourceOverride(String space, String dir, String file, String ext) {
    }

    default void addResourceOverride(String space, String domain, String dir, String file, String ext) {
    }

    default void syncHarness(int entityId, ItemStack harness) {
    }
}
