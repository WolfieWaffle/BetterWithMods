package betterwithmods.module.tweaks;

import betterwithmods.common.entity.ai.EntityAIFlee;
import betterwithmods.module.Feature;
import betterwithmods.util.EntityUtils;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class ImprovedFlee extends Feature {

    private static boolean blockPlace, blockBreak, groupFlee;


    @Override
    public void setupConfig() {
        blockPlace = loadPropBool("Animals Flee from Block Place", "When a block is placed in the vicinity of an animal it will get scared and run.", true);
        blockBreak = loadPropBool("Animals Flee from Block Break", "When a non-replaceable block is broken in the vicinity of an animal it will get scared and run.", true);
//        groupFlee = loadPropBool("Animals Group Flee", "When an animal is attacked in the vicinity of another animal it will get scared and run.", true);
    }

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) evt.getEntity();
            if (entity instanceof EntityAnimal && !(entity instanceof EntityTameable)) {
                float speed = 1.25F;
                if (entity instanceof EntityCow)
                    speed = 2.0F;
                else if (entity instanceof EntityChicken)
                    speed = 1.4F;
                EntityUtils.removeAI(entity, EntityAIPanic.class);
                entity.tasks.addTask(0, new EntityAIFlee((EntityCreature) entity, speed));
            }
        }
    }


    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.PlaceEvent event) {
        if (!blockPlace)
            return;
        if (event.getPlayer() != null) {
            AxisAlignedBB box = event.getPlacedBlock().getBoundingBox(event.getWorld(), event.getPos()).offset(event.getPos()).grow(10);
            for (EntityAnimal animal : event.getWorld().getEntitiesWithinAABB(EntityAnimal.class, box)) {
                animal.setRevengeTarget(event.getPlayer());
            }
        }
    }

    @SubscribeEvent
    public void onPlaceBreak(BlockEvent.BreakEvent event) {
        if (!blockBreak)
            return;
        if (event.getPlayer() != null && !event.getState().getMaterial().isReplaceable()) {
            AxisAlignedBB box = event.getState().getBoundingBox(event.getWorld(), event.getPos()).offset(event.getPos()).grow(10);
            for (EntityAnimal animal : event.getWorld().getEntitiesWithinAABB(EntityAnimal.class, box)) {
                animal.setRevengeTarget(event.getPlayer());
            }
        }
    }

    @SubscribeEvent
    public void onGroupFlee(LivingSetAttackTargetEvent event) {
        if (!groupFlee)
            return;
        if (event.getEntityLiving() instanceof EntityAnimal) {

            EntityAnimal a = (EntityAnimal) event.getEntityLiving();
            AxisAlignedBB box = new AxisAlignedBB(a.posX, a.posY, a.posZ, a.posX + 1, a.posY + 1, a.posZ + 1).grow(10);
            for (EntityAnimal animal : a.getEntityWorld().getEntitiesWithinAABB(EntityAnimal.class, box, entity ->
                    entity != null && entity != a && entity.getRevengeTarget() == null)) {
                System.out.println(animal.getRevengeTimer());
                animal.setRevengeTarget(event.getTarget());
            }
        }
    }

    @Override
    public String getFeatureDescription() {
        return "Improve fleeing AI for attacked animals";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
