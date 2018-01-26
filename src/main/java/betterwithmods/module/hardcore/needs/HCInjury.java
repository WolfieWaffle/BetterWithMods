package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWSounds;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.needs.hunger.HCHunger;
import betterwithmods.util.player.HealthPenalty;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 5/24/17.
 */
public class HCInjury extends Feature {


    @SubscribeEvent
    public void attack(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (PlayerHelper.isSurvival(player)) {
                HealthPenalty healthPenalty = PlayerHelper.getHealthPenalty(player);
                double mod = healthPenalty.getModifier();
                if (mod <= 0.75) {
                    player.playSound(BWSounds.OOF, 0.75f, 1f);
                    event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public void penalty(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote)
            return;
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!PlayerHelper.isSurvival(player))
                return;
            if (player != null) {
                if (!ModuleLoader.isFeatureEnabled(HCHunger.class)) {
                    PlayerHelper.changeSpeed(player, "Health speed penalty", PlayerHelper.getHealthPenalty(player).getModifier(), PlayerHelper.PENALTY_SPEED_UUID);
                }
                HealthPenalty healthPenalty = PlayerHelper.getHealthPenalty(player);
                double mod = healthPenalty.getModifier();
                if (mod <= 0.25) {
                    if (player.world.getWorldTime() % 60 == 0) {
                        player.playSound(BWSounds.OOF, 0.75f, 1f);
                        if (mod <= 0.20)
                            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 80, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    public String getFeatureDescription() {
        return "Add Penalties to lower health levels.";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
