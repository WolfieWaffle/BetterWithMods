package betterwithmods.module.tweaks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockUnfiredPottery;
import betterwithmods.common.blocks.tile.TileCamo;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.blockmeta.managers.KilnManager;
import betterwithmods.common.registry.blockmeta.recipe.KilnRecipe;
import betterwithmods.module.Feature;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlaceClay extends Feature {


    @Override
    public String getFeatureDescription() {
        return "Clay and Nether Sludge are placeable blocks.";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        KilnManager.INSTANCE.addRecipe(new KilnRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.BRICK.getMeta(), Lists.newArrayList(new ItemStack(Items.BRICK))) {
            @Override
            public ItemStack getStack() {
                return new ItemStack(Items.CLAY_BALL);
            }
        });
        KilnManager.INSTANCE.addRecipe(new KilnRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.NETHER_BRICK.getMeta(), Lists.newArrayList(new ItemStack(Items.NETHERBRICK))) {
            @Override
            public ItemStack getStack() {
                return ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE);
            }
        });

    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlaceClay(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled())
            return;


        if (event.getItemStack().isItemEqual(new ItemStack(Items.CLAY_BALL))) {
            if (canPlaceAt(event.getEntityPlayer(), event.getWorld(), event.getPos())) {
                PlayerContainer container = new PlayerContainer(event.getEntityPlayer(), BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.BRICK));
                ItemBlock item = (ItemBlock) Item.getItemFromBlock(BWMBlocks.UNFIRED_POTTERY);
                EnumActionResult result = item.onItemUse(container, event.getWorld(), event.getPos(), event.getHand(), event.getFace(), (float) event.getHitVec().x, (float) event.getHitVec().y, (float) event.getHitVec().z);
                if (result == EnumActionResult.SUCCESS) {
                    if (!event.getEntityPlayer().capabilities.isCreativeMode)
                        event.getItemStack().shrink(1);
                    event.getEntityPlayer().swingArm(event.getHand());
                }
            }
        }

        if (event.getItemStack().isItemEqual(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE))) {
            if (canPlaceAt(event.getEntityPlayer(), event.getWorld(), event.getPos())) {
                PlayerContainer container = new PlayerContainer(event.getEntityPlayer(), BlockUnfiredPottery.getStack(BlockUnfiredPottery.EnumType.NETHER_BRICK));
                ItemBlock item = (ItemBlock) Item.getItemFromBlock(BWMBlocks.UNFIRED_POTTERY);
                EnumActionResult result = item.onItemUse(container, event.getWorld(), event.getPos(), event.getHand(), event.getFace(), (float) event.getHitVec().x, (float) event.getHitVec().y, (float) event.getHitVec().z);
                if (result == EnumActionResult.SUCCESS) {
                    if (!event.getEntityPlayer().capabilities.isCreativeMode)
                        event.getItemStack().shrink(1);
                    event.getEntityPlayer().swingArm(event.getHand());
                }
            }
        }
        
    }

    private boolean canPlaceAt(EntityPlayer player, World world, BlockPos pos) {
        if (player.isSneaking())
            return true;
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && !(tile instanceof TileCamo))
            return false;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return !(block instanceof BlockWorkbench || block instanceof BlockDoor || block instanceof BlockTrapDoor || block instanceof BlockFenceGate);
    }


    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @SuppressWarnings("EntityConstructor")
    private class PlayerContainer extends EntityPlayer {
        private EntityPlayer player;
        private ItemStack held;

        public PlayerContainer(World worldIn, GameProfile gameProfileIn) {
            super(worldIn, gameProfileIn);
        }

        public PlayerContainer(EntityPlayer player, ItemStack held) {
            super(player.getEntityWorld(), player.getGameProfile());
            this.player = player;
            this.held = held;
        }

        @Override
        public boolean isSpectator() {
            return player.isSpectator();
        }

        @Override
        public boolean isCreative() {
            return player.isCreative();
        }

        @Override
        public ItemStack getHeldItem(EnumHand hand) {
            return held;
        }

        @Override
        public boolean canPlayerEdit(BlockPos pos, EnumFacing facing, ItemStack stack) {
            return player.canPlayerEdit(pos, facing, stack);
        }
    }
}
