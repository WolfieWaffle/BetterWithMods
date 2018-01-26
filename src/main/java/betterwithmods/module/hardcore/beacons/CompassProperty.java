package betterwithmods.module.hardcore.beacons;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class CompassProperty implements IItemPropertyGetter {
    private double rotation;
    private double rota;
    private long lastUpdateTick;

    @SideOnly(Side.CLIENT)
    public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
        if (entityIn == null && !stack.isOnItemFrame()) {
            return 0.0F;
        } else {
            boolean flag = entityIn != null;
            Entity entity = flag ? entityIn : stack.getItemFrame();

            if (worldIn == null) {
                worldIn = entity.world;
            }
            double d0;

            if (worldIn.provider.isSurfaceWorld()) {
                double d1 = flag ? (double) entity.rotationYaw : this.getFrameRotation((EntityItemFrame) entity);
                d1 = MathHelper.positiveModulo(d1 / 360.0D, 1.0D);

                CapabilityBeacon beacons = worldIn.getCapability(CapabilityBeacon.BEACON_CAPABILITY, EnumFacing.UP);
                double angle = 0;
                if (beacons != null) {
                    angle = this.getAngleToPos(beacons.getClosest(worldIn, entity), entity);
                } else {
                    angle = this.getSpawnToAngle(worldIn, entity);
                }
                d0 = 0.5D - (d1 - 0.25D - angle);
            } else {
                d0 = Math.random();
            }

            if (flag) {
                d0 = this.wobble(worldIn, d0);
            }

            return MathHelper.positiveModulo((float) d0, 1.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    private double wobble(World worldIn, double p_185093_2_) {
        if (worldIn.getTotalWorldTime() != this.lastUpdateTick) {
            this.lastUpdateTick = worldIn.getTotalWorldTime();
            double d0 = p_185093_2_ - this.rotation;
            d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
            this.rota += d0 * 0.1D;
            this.rota *= 0.8D;
            this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
        }

        return this.rotation;
    }

    @SideOnly(Side.CLIENT)
    private double getFrameRotation(EntityItemFrame p_185094_1_) {
        return (double) MathHelper.wrapDegrees(180 + p_185094_1_.facingDirection.getHorizontalIndex() * 90);
    }

    @SideOnly(Side.CLIENT)
    private double getSpawnToAngle(World world, Entity entity) {
        return getAngleToPos(world.getSpawnPoint(), entity);
    }

    private double getAngleToPos(BlockPos pos, Entity entity) {
        return Math.atan2((double) pos.getZ() - entity.posZ, (double) pos.getX() - entity.posX) / (Math.PI * 2D);
    }
}
