package betterwithmods.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by primetoxinz on 4/22/17.
 */
public class EntityAIEatFood extends EntityAIBase {
    private EntityCreature entity;
    private Predicate<ItemStack> validItem;
    private Optional<EntityItem> targetItem;
    private int timeoutCounter;

    public EntityAIEatFood(EntityCreature creature, Predicate<ItemStack> validItem) {
        this.entity = creature;
        this.validItem = validItem;
    }

    public Optional<EntityItem> getTargetItem(List<EntityItem> items) {
        if (items.isEmpty())
            return Optional.empty();
        EntityItem target = null;
        for (EntityItem item : items) {
            if (validItem.test(item.getItem())) {
                target = item;
                break;
            }
        }
        return Optional.ofNullable(target);
    }

    @Override
    public void startExecuting() {
        if (targetItem.isPresent()) {
            EntityItem item = targetItem.get();
            this.entity.getNavigator().tryMoveToXYZ(item.posX, item.posY, item.posZ, 1.0F);
        }
    }

    @Override
    public boolean shouldExecute() {
        if (entity.isChild())
            return false;
        BlockPos entityPos = entity.getPosition();
        if (targetItem == null) {
            List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
            targetItem = getTargetItem(entityItems);
        }
        if (targetItem.isPresent()) {
            EntityItem item = targetItem.get();
            BlockPos targetPos = item.getPosition();
            if (entityPos.getDistance(targetPos.getX(), targetPos.getY(), targetPos.getZ()) <= 2D && item.getItem().getCount() > 0) {
                processItemEating();
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {

        if (!targetItem.map(EntityItem::isEntityAlive).orElse(false))
            return false;
        if (targetItem.map(e -> e.getItem().getCount() < 1).orElse(false)) {
            BlockPos entityPos = entity.getPosition();
            List<EntityItem> entityItems = entity.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1)).expand(5, 5, 5));
            targetItem = getTargetItem(entityItems);
        }

        if (timeoutCounter > 1200)
            return false;
        if (!this.entity.getNavigator().noPath()) {

            if (targetItem.isPresent()) {
                EntityItem item = targetItem.get();
                double sqDistToPos = this.entity.getDistanceSq(item.posX, item.posY, item.posZ);
                if (sqDistToPos > 2.0D)
                    return true;
            }

        }
        return false;
    }


    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        if(targetItem.isPresent()) {
            EntityItem item = targetItem.get();
            if (entity.getDistanceSq(item.posX, item.posY, item.posZ) <= 2.0D && item.getItem().getCount() > 0) {
                processItemEating();
            } else {
                ++timeoutCounter;
                if (timeoutCounter % 40 == 0) {
                    this.entity.getNavigator().tryMoveToXYZ(item.posX, item.posY, item.posZ, 1.0F);
                }
            }
        }

    }

    private void processItemEating() {
        if(targetItem.isPresent()) {
            ItemStack foodStack = targetItem.get().getItem().splitStack(1);
            entity.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0F, (entity.world.rand.nextFloat() - entity.world.rand.nextFloat()) * 0.2F + 1.0F);
            foodStack.shrink(1);
        }
    }
}
