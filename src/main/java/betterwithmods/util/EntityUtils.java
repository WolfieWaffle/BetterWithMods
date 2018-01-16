package betterwithmods.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class EntityUtils {

    public static void removeAI(EntityLiving entity, Class<? extends EntityAIBase> clazz) {
        entity.tasks.taskEntries.removeIf(entityAITaskEntry -> clazz.isAssignableFrom(entityAITaskEntry.action.getClass()));
    }

    public static <T extends EntityAIBase> Optional<T> findFirst(EntityLiving entity, Class<? extends EntityAIBase> clazz) {
        return entity.tasks.taskEntries.stream().filter(t -> clazz.isAssignableFrom(t.getClass())).map(t -> (T) t.action).findFirst();
    }

    public static <T extends Entity> T replaceEntity(Entity oldEntity, Class<T> clazz) {
        World world = oldEntity.world;
        T newEntity = null;
        try {
            newEntity = clazz.getConstructor(World.class).newInstance(world);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (newEntity != null) {
            copyEntityInfo(oldEntity, newEntity);
            oldEntity.setDead();
            world.spawnEntity(newEntity);
            return newEntity;
        }
        return null;
    }


    public static int spawnEntity(World world, Class<? extends Entity> clazz, Vec3d pos, int count) {
        int s = 0;
        for (int i = 0; i < count; i++) {
            s += spawnEntity(world, clazz, pos) ? 1 : 0;
        }
        return 0;
    }

    public static boolean spawnEntity(World world, Class<? extends Entity> clazz, Vec3d pos) {
        try {
            Entity entity = clazz.getConstructor(World.class).newInstance(world);
            entity.setPosition(pos.x, pos.y, pos.z);
            return world.spawnEntity(entity);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void copyEntityInfo(Entity copyFrom, Entity copyTo) {
        if (copyFrom instanceof EntityLiving && copyTo instanceof EntityLiving) {
            EntityLiving e1 = ((EntityLiving) copyTo), e2 = ((EntityLiving) copyFrom);
            e1.setHealth(e2.getHealth());
        }
        copyTo.setPositionAndRotation(copyFrom.posX, copyFrom.posY, copyFrom.posZ, copyFrom.rotationYaw, copyFrom.rotationPitch);
        copyTo.setRotationYawHead(copyFrom.getRotationYawHead());
    }
}
