package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class MossGeneration extends Feature {

    @SubscribeEvent
    public void generateMossNearSpawner(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote || event.phase != TickEvent.Phase.END || event.side != Side.SERVER)
            return;
        Random rand = event.world.rand;
        try {
	        List<BlockPos> positions = event.world.loadedTileEntityList.stream().filter(t -> t instanceof TileEntityMobSpawner).map(TileEntity::getPos).collect(Collectors.toList());
            positions.forEach(pos -> {
                int x = rand.nextInt(9) - 4;
                int y = rand.nextInt(5) - 1;
                int z = rand.nextInt(9) - 4;
                BlockPos check = pos.add(x, y, z);
                IBlockState state = event.world.getBlockState(check);
                if ((state.getBlock() == Blocks.COBBLESTONE || (state.getBlock() == Blocks.STONEBRICK && state.getBlock().getMetaFromState(state) == 0)) && rand.nextInt(30) == 0) {
                    IBlockState changeState = state.getBlock() == Blocks.COBBLESTONE ? Blocks.MOSSY_COBBLESTONE.getDefaultState() : Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
                    event.world.setBlockState(check, changeState);
                }
            });
        } catch (ConcurrentModificationException ignored) {
        	ignored.printStackTrace();
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public String getFeatureDescription() {
        return "Cobblestone or Stonebrick within the spawning radius of a Mob Spawner will randomly grow into the Mossy version.";
    }
}
