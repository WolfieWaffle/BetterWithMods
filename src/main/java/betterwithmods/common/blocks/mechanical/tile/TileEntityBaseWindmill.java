package betterwithmods.common.blocks.mechanical.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityBaseWindmill extends TileAxleGenerator implements IColor {
    protected int[] bladeMeta;
    private boolean rain, thunder;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < 4; i++) {
            if (tag.hasKey("Color_" + i))
                bladeMeta[i] = tag.getInteger("Color_" + i);
        }
        rain = tag.getBoolean("rain");
        thunder = tag.getBoolean("thunder");

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        for (int i = 0; i < 4; i++) {
            t.setInteger("Color_" + i, bladeMeta[i]);
        }
        t.setByte("DyeIndex", this.dyeIndex);
        t.setBoolean("thunder",this.thunder);
        t.setBoolean("rain",this.rain);
        return t;
    }

    @Override
    public boolean dyeBlade(int dyeColor) {
        boolean dyed = false;
        if (bladeMeta[dyeIndex] != dyeColor) {
            bladeMeta[dyeIndex] = dyeColor;
            dyed = true;
            IBlockState state = getBlockWorld().getBlockState(this.pos);
            this.getBlockWorld().notifyBlockUpdate(this.pos, state, state, 3);
            this.markDirty();
        }
        dyeIndex++;
        if (dyeIndex > (bladeMeta.length - 1))
            dyeIndex = 0;
        return dyed;
    }


    public int getBladeColor(int blade) {
        return bladeMeta[blade];
    }

    @Override
    public int getColorFromBlade(int blade) {
        return bladeMeta[blade];
    }

    @Override
    public void calculatePower() {
        byte power = 0;
        if (isValid()) {
            if (thunder)
                power = 3;
            else if (rain)
                power = 2;
            else
                power = 1;

            if (tick > 600) {
                if (isOverworld()) {
                    rain = world.isRaining();
                    thunder = world.isThundering();
                }
                tick = 0;
            }
        }

        if (power != this.power) {
            setPower(power);
        }
    }


    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }
}
