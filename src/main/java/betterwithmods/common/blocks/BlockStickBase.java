package betterwithmods.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockStickBase extends BWMBlock {
    public static final PropertyEnum<Connection> CONNECTION = PropertyEnum.create("connection", Connection.class);
    public static final PropertyBool GROUND = PropertyBool.create("ground");

    public BlockStickBase(Material material) {
        super(material);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTION, GROUND);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean ground = (meta & 1) == 1;
        int connection = meta >> 1;
        return getDefaultState().withProperty(GROUND, ground).withProperty(CONNECTION, Connection.VALUES[connection]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int connection = state.getValue(CONNECTION).ordinal() << 1;
        int ground = state.getValue(GROUND) ? 1 : 0;
        return ground | connection;
    }

    public abstract IBlockState getConnections(IBlockState state, IBlockAccess world, BlockPos pos);

    public abstract double getHeight(IBlockState state);

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState newState = state;
        if (worldIn.getBlockState(pos.down()).isBlockNormalCube()) {
            newState = newState.withProperty(GROUND, true);
        }
        IBlockState above = worldIn.getBlockState(pos.up());
        Block block = above.getBlock();
        ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(above));


        return getConnections(newState, worldIn, pos);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing face) {
        if (face == EnumFacing.UP)
            return BlockFaceShape.SOLID;
        return BlockFaceShape.CENTER_SMALL;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        double y = getHeight(state);
        return new AxisAlignedBB(7d / 16d, 0, 7d / 16d, 9d / 16d, y, 9d / 16d);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }


    public enum Connection implements IStringSerializable {
        DISCONNECTED,
        CONNECTED,
        CANDLE,
        SKULL;

        public static Connection[] VALUES = values();

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }
}
