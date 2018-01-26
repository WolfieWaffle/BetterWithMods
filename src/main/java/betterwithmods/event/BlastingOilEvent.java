package betterwithmods.event;

import betterwithmods.common.items.ItemMaterial;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 11/26/16
 */
@Mod.EventBusSubscriber
public class BlastingOilEvent {
    @SubscribeEvent
    public static void onPlayerTakeDamage(LivingHurtEvent e) {
        DamageSource BLAST_OIL = new DamageSource("blastingoil");
        EntityLivingBase living = e.getEntityLiving();
        if (living.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler inventory = living.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            int count = 0;
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);

                if (!stack.isEmpty() && stack.isItemEqual(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL))) {
                    count += stack.getCount();
                    inventory.extractItem(i, stack.getCount(), false);
                }
            }
            if (count > 0) {
                living.attackEntityFrom(BLAST_OIL, Float.MAX_VALUE);
                living.getEntityWorld().createExplosion(null, living.posX, living.posY + living.height / 16, living.posZ, (float) (Math.sqrt(count / 5) / 2.5 + 1), true);
            }
        }
    }
}
