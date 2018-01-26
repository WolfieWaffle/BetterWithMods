package betterwithmods.common.blocks;

import betterwithmods.common.blocks.tile.TileFurnace;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFurnace extends net.minecraft.block.BlockFurnace {
    public BlockFurnace(boolean isBurning) {
        super(isBurning);
        if(isBurning) {
            setLightLevel(0.875F);
        }
        setUnlocalizedName("furnace");

        setHardness(3.5F);
        setSoundType(SoundType.STONE);
        setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileFurnace();
    }
}
