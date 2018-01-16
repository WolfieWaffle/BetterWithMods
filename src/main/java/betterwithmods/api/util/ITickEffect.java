package betterwithmods.api.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by primetoxinz on 7/17/17.
 */
public interface ITickEffect {



    void tick(World world, BlockPos pos, int level);

    static void forEachPlayersAround(World world, BlockPos pos, int radius, Consumer<? super EntityLivingBase> player) {
        forEachEntityAround(EntityPlayer.class, world, pos, radius, player);
    }

    static void forEachEntityAround(Class<? extends EntityLivingBase> clazz, World world, BlockPos pos, int radius, Consumer<? super EntityLivingBase> consumer) {
        AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(radius);
        List<? extends EntityLivingBase> entities = world.getEntitiesWithinAABB(clazz, box);
        entities.forEach(consumer);
    }

    default boolean processInteractions(World world, BlockPos pos, int level, EntityPlayer player, ItemStack stack) {
        return false;
    }

    default void breakBlock(World world, BlockPos pos, int level) {
        
    }

    default int getTickSpeed() {
        return 120;
    }
}
