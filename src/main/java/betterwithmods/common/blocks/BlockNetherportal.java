package betterwithmods.common.blocks;

import betterwithmods.module.hardcore.corruption.CorruptEvent;
import betterwithmods.module.hardcore.corruption.Corruption;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

public class BlockNetherportal extends BlockPortal {
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!Corruption.disablePortalPigmen)
            super.updateTick(world, pos, state, rand);
        MinecraftForge.EVENT_BUS.post(new CorruptEvent(world, state, pos));
    }
}
