package betterwithmods.module.hardcore.corruption;

import betterwithmods.api.util.ITickEffect;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.util.EntityUtils;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HopperCorruption implements ITickEffect {
    protected static final HopperCorruption INSTANCE = new HopperCorruption();

    @Override
    public void tick(World world, BlockPos pos, int level) {
        TileEntityFilteredHopper tile = (TileEntityFilteredHopper) world.getTileEntity(pos);
        if (tile != null) {
            ITickEffect.forEachEntityAround(EntityVillager.class, world, pos, 4, villager -> {
                if (tile.soulsRetained > 0) {
                    EntityUtils.replaceEntity(villager, EntityWitch.class);
                    tile.soulsRetained--;
                }
            });

            ITickEffect.forEachEntityAround(EntityCow.class, world, pos, 4, cow -> {
                if (tile.soulsRetained > 0) {
                    int used = EntityUtils.spawnEntity(world, EntityBat.class, cow.getPositionVector(), 5);
                    tile.soulsRetained -= used;
                }
            });
            ITickEffect.forEachEntityAround(EntitySheep.class, world, pos, 4, sheep -> {
                if (tile.soulsRetained > 0) {
                    if (!sheep.isPotionActive(MobEffects.LEVITATION)) {
                        sheep.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 4000,3,false,false));
                        tile.soulsRetained--;
                    }
                }
            });
        }

    }
}
