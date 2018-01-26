package betterwithmods.common.potion;
/*
The MIT License (MIT)
Copyright (c) 2014-2016 Samson Bassett (Lothrazar)
 */


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PotionSlowfall extends BWPotion {
    public static final float slowfallSpeed = 0.41F;

    public PotionSlowfall(String name, boolean b, int potionColor) {
        super(name, b, potionColor);
    }

    @Override
    public void tick(EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) entityLiving;
            if (p.isSneaking()) {
                return;
            }
        }
        // else: so we are either a non-sneaking player, or a non player
        // entity
        // a normal fall seems to go up to 0, -1.2, -1.4, -1.6, then
        // flattens out at -0.078
        if (entityLiving.motionY < 0) {
            entityLiving.motionY *= slowfallSpeed;
            entityLiving.fallDistance = 0f; // for no fall damage
        }
    }
}