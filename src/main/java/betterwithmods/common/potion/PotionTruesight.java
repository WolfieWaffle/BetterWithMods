package betterwithmods.common.potion;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class PotionTruesight extends BWPotion {
    public PotionTruesight(String name, boolean b, int potionColor) {
        super(name, b, potionColor);
    }

    @Override
    public void tick(EntityLivingBase entity) {
        World world = entity.getEntityWorld();
        if (world.isRemote) {
            Minecraft mc = Minecraft.getMinecraft();
            int var3 = mc.gameSettings.particleSetting;
            if (!mc.isGamePaused()
                    && (world.provider.getDimension() == 0 || world.provider.getDimension() == 1)) {

                int var4 = MathHelper.floor(entity.posX);
                int var5 = MathHelper.floor(entity.posY);
                int var6 = MathHelper.floor(entity.posZ);
                int radius = 10;
                for (int x = var4 - radius; x <= var4 + radius; ++x) {
                    for (int y = var5 - radius; y <= var5 + radius; ++y) {
                        for (int z = var6 - radius; z <= var6 + radius; ++z) {
                            if (canMobsSpawnHere(world, new BlockPos(x, y, z))
                                    && (var3 == 0 || world.rand.nextInt(12) <= 2 - var3 << 1)) {

                                double i = (double) x + world.rand.nextDouble();
                                double j = (double) y + world.rand.nextDouble() * 0.25D;
                                double k = (double) z + world.rand.nextDouble();
                                world.spawnParticle(EnumParticleTypes.SPELL_MOB, i, j, k, 0, 0, 0);
                            }
                        }
                    }
                }
            }
        }

    }

    public static boolean canMobsSpawnHere(World world, BlockPos pos) {
        if (!world.isSideSolid(pos.down(), EnumFacing.UP)) {
            return false;
        } else if (!world.isBlockNormalCube(pos, false) && !world.isBlockNormalCube(pos.up(), false)
                && !world.getBlockState(pos).getMaterial().isLiquid()) {
            IBlockState state = world.getBlockState(pos);
            if (state == Blocks.BEDROCK.getDefaultState()) {
                return false;
            } else if (world.getWorldTime() < 11615 && world.getLightFor(EnumSkyBlock.SKY, pos) >= 15) {
                return false;
            } else {
                int lightLevel = world.getLightFor(EnumSkyBlock.BLOCK, pos);
                return lightLevel < 8 && (world.isAirBlock(pos) || state.getCollisionBoundingBox(world, pos) == null);
            }
        } else {
            return false;
        }

    }
}
