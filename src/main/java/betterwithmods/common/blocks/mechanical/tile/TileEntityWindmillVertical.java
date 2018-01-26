package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.BlockWindmill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWindmillVertical extends TileEntityBaseWindmill {

    public TileEntityWindmillVertical() {
        this.bladeMeta = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    }

    public boolean isSlaveValid(int offset) {
        int airCounter = 0;
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                BlockPos offPos = pos.add(x, offset, z);
                if (x == 0 && z == 0)
                    continue;
                if (getBlockWorld().isAirBlock(offPos)) {
                    airCounter++;
                } else {
                    return false;
                }

            }
        }
        return airCounter > 25;
    }

    @Override
    public void verifyIntegrity() {
        boolean valid = false;
        if (getBlockWorld().getBlockState(pos).getBlock() == BWMBlocks.WINDMILL) {
            for (int i = -3; i <= 3; i++) {
                if (i != 0) {
                    if (isSlaveValid(i)) {
                        valid = true;
                    } else {
                        valid = false;
                        break;
                    }
                }
            }
        }

        isValid = valid && !isEnd();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
        IBlockState state = getBlockWorld().getBlockState(this.pos);
        this.getBlockWorld().notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (getBlockWorld().getBlockState(pos).getBlock() instanceof BlockWindmill)
            return new AxisAlignedBB(x - 4, y - 4, z - 4, x + 4, y + 4, z + 4);
        else
            return super.getRenderBoundingBox();
    }

}
