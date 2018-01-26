package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockUnfiredPottery;
import betterwithmods.common.registry.TurntableRotationManager;
import betterwithmods.common.registry.blockmeta.managers.TurntableManager;
import betterwithmods.module.Feature;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class TurntableRecipes extends Feature {
    public TurntableRecipes() {
        canDisable = false;
    }

    public static void addTurntableRecipe(Block inputBlock, int iMeta, Block outputBlock, int oMeta, ItemStack... scraps) {
        TurntableManager.INSTANCE.addTurntableRecipe(inputBlock, iMeta, outputBlock, oMeta, scraps);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addTurntableRecipe(Blocks.CLAY, 0, BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.CRUCIBLE.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.CRUCIBLE.getMeta(), BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.PLANTER.getMeta(), ItemStack.EMPTY);
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.PLANTER.getMeta(), BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.VASE.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.VASE.getMeta(), BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.URN.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.URN.getMeta(), null, 0, new ItemStack(Items.CLAY_BALL));

        TurntableRotationManager.addAttachment(b -> b instanceof BlockTorch);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockLever);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockLadder);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockButton);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockWallSign);
        TurntableRotationManager.addAttachment(b -> b instanceof BlockTripWireHook);

        TurntableRotationManager.addRotationHandler(block -> block instanceof BlockTorch, (world, pos) -> {
            IBlockState state = world.getBlockState(pos);
            return state.getValue(BlockTorch.FACING).getAxis().isVertical();
        });
        TurntableRotationManager.addRotationHandler(Blocks.LEVER, (world, pos) -> {
            IBlockState state = world.getBlockState(pos);
            return state.getValue(BlockLever.FACING).getFacing().getAxis().isVertical();
        });
        TurntableRotationManager.addRotationBlacklist(block -> block instanceof BlockPistonExtension);
        TurntableRotationManager.addRotationHandler(BWMBlocks.UNFIRED_POTTERY, new TurntableRotationManager.IRotation() {
            @Override
            public boolean isValid(World world, BlockPos pos) {
                return true;
            }

            @Override
            public boolean canTransmitHorizontally(World world, BlockPos pos) {
                return false;
            }

            @Override
            public boolean canTransmitVertically(World world, BlockPos pos) {
                return false;
            }
        });
        TurntableRotationManager.addRotationHandler(Blocks.CLAY, new TurntableRotationManager.IRotation() {
            @Override
            public boolean isValid(World world, BlockPos pos) {
                return true;
            }

            @Override
            public boolean canTransmitHorizontally(World world, BlockPos pos) {
                return false;
            }

            @Override
            public boolean canTransmitVertically(World world, BlockPos pos) {
                return false;
            }
        });
        TurntableRotationManager.addRotationHandler(block -> block instanceof BlockPistonBase, (world, pos) -> !world.getBlockState(pos).getValue(BlockPistonBase.EXTENDED));
    }
}
