package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class RenewableEndstone extends Feature {

    @SubscribeEvent
    public void giveEndermenEndStone(LivingSpawnEvent evt) {

        EntityLivingBase entity = evt.getEntityLiving();

        if (evt.getWorld().provider.getDimensionType() == DimensionType.THE_END) {
            if (entity instanceof EntityEnderman) {
                if (evt.getWorld().rand.nextInt(2000) == 0)
                    ((EntityEnderman) entity).setHeldBlockState(Blocks.END_STONE.getDefaultState());
            }
        }
    }
    @Override
    public String getFeatureDescription() {
        return "Endermen spawn with endstone in the End";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
