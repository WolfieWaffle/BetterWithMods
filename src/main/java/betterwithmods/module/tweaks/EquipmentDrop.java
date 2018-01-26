package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class EquipmentDrop extends Feature {

    @SubscribeEvent
    public void setDropChance(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) e.getEntity();
            if (entity instanceof EntityZombie) {
                for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                    entity.setDropChance(slot, 1);
                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public String getFeatureDescription() {
        return "Zombies have a 100% chance to drop any equipment";
    }
}
