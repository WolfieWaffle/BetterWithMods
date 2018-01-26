package betterwithmods.module.gameplay.miniblocks;

import betterwithmods.common.blocks.BWMBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;

public class BlockDynamicMini extends BWMBlock {

    public static final IUnlistedProperty<MiniCacheInfo> MINI_INFO = new UnlistedPropertyGeneric<>("mini", MiniCacheInfo.class);

    public BlockDynamicMini() {
        super(Material.WOOD);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileDynamicMini tile = (TileDynamicMini) worldIn.getTileEntity(pos);
        return tile != null && tile.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (ItemStack material : MiniBlocks.MATERIALS) {
            ItemStack stack = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("texture", material.serializeNBT());
            stack.setTagCompound(tag);
            items.add(stack);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[]{MINI_INFO});
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDynamicMini();
    }


    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileDynamicMini tile = (TileDynamicMini) world.getTileEntity(pos);
        IExtendedBlockState extendedBS = (IExtendedBlockState) super.getExtendedState(state, world, pos);
        if (tile != null) {
            return extendedBS.withProperty(MINI_INFO, MiniCacheInfo.from(tile));
        }
        return extendedBS;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


    private final AxisAlignedBB[] boxes = new AxisAlignedBB[]{
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D),
            new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D),
            new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
    };

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int i = 0;
        TileEntity tile = source.getTileEntity(pos);
        if (tile instanceof TileDynamicMini) {
            TileDynamicMini mini = (TileDynamicMini) tile;
            if (mini.orientation != null && mini.orientation.facing != null)
                i = mini.orientation.top.getOpposite().getIndex();
        }
        return boxes[i];
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return ItemStack.EMPTY;
    }
}
