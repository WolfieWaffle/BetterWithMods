package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.BlockWindmill;
import betterwithmods.util.DirUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWindmillHorizontal extends TileEntityBaseWindmill {

    public TileEntityWindmillHorizontal() {
        super();
        this.radius = 7;
        this.bladeMeta = new int[]{0, 0, 0, 0};
    }

    @Override
    public void verifyIntegrity() {
        boolean valid = true;
        if (getBlockWorld().getBlockState(pos).getBlock() == BWMBlocks.WINDMILL) {
            EnumFacing.Axis axis = getBlockWorld().getBlockState(pos).getValue(DirUtils.AXIS);
            for (int vert = -6; vert <= 6; vert++) {
                for (int i = -6; i <= 6; i++) {
                    int xP = (axis == EnumFacing.Axis.Z ? i : 0);
                    int zP = (axis == EnumFacing.Axis.X ? i : 0);
                    BlockPos offset = pos.add(xP, vert, zP);
                    if (xP == 0 && vert == 0 && zP == 0)
                        continue;
                    else
                        valid = getBlockWorld().isAirBlock(offset);
                    if (!valid)
                        break;
                }
                if (!valid)
                    break;
            }
        }
        isValid = valid && this.getBlockWorld().canBlockSeeSky(pos) && !isNether() && !isEnd();
    }

    //Extend the bounding box if the TESR is bigger than the occupying block.
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (getBlockWorld().getBlockState(pos).getBlock() instanceof BlockWindmill) {
            EnumFacing.Axis axis = getBlockWorld().getBlockState(pos).getValue(DirUtils.AXIS);
            int xP = axis == EnumFacing.Axis.Z ? radius : 0;
            int yP = radius;
            int zP = axis == EnumFacing.Axis.X ? radius : 0;
            return new AxisAlignedBB(x - xP - 1, y - yP - 1, z - zP - 1, x + xP + 1, y + yP + 1, z + zP + 1);
        } else {
            return super.getRenderBoundingBox();
        }
    }
}
