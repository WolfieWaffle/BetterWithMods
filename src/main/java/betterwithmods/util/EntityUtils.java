package betterwithmods.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.Optional;

public class EntityUtils {

    public static void removeAI(EntityLiving entity, Class<? extends EntityAIBase> clazz) {
        entity.tasks.taskEntries.removeIf(entityAITaskEntry -> clazz.isAssignableFrom(entityAITaskEntry.action.getClass()));
    }

    public  static <T extends EntityAIBase> Optional<T> findFirst(EntityLiving entity, Class<? extends EntityAIBase> clazz) {
        return entity.tasks.taskEntries.stream().filter( t -> clazz.isAssignableFrom(t.getClass())).map( t -> (T)t.action).findFirst();
    }
}
