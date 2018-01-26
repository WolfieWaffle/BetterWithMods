package betterwithmods.client.container.other;

import betterwithmods.common.blocks.tile.FilteredStackHandler;
import betterwithmods.common.blocks.tile.SimpleStackHandler;
import betterwithmods.common.blocks.tile.TileEntityInfernalEnchanter;
import betterwithmods.common.items.ItemArcaneScroll;
import betterwithmods.module.hardcore.creatures.HCEnchanting;
import betterwithmods.util.InvUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by primetoxinz on 9/11/16.
 */
public class ContainerInfernalEnchanter extends Container {
    public int[] enchantLevels;
    public int xpSeed;
    public int bookcaseCount;
    private TileEntityInfernalEnchanter tile;
    private SimpleStackHandler handler;

    public ContainerInfernalEnchanter(EntityPlayer player, TileEntityInfernalEnchanter tile) {
        this.tile = tile;
        this.enchantLevels = new int[5];
        this.bookcaseCount = tile.getBookcaseCount();
        handler = new FilteredStackHandler(2, tile, stack -> stack.getItem() instanceof ItemArcaneScroll, stack -> true) {
            @Override
            public void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                onContextChanged(this);
            }
        };
        this.xpSeed = player.getXPSeed();
        IItemHandler playerInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new SlotItemHandler(handler, 0, 17, 37));
        addSlotToContainer(new SlotItemHandler(handler, 1, 17, 75));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 129 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotItemHandler(playerInv, i, 8 + i * 18, 187));
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        switch (id) {
            default:
                if (id < this.enchantLevels.length) {
                    enchantLevels[id] = data;
                }
                break;
            case 3:
                xpSeed = data;
                break;
            case 4:
                bookcaseCount = data;
                break;
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            this.broadcastData(listener);
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        broadcastData(listener);
    }

    public void broadcastData(IContainerListener listener) {
        for (int i = 0; i < this.enchantLevels.length; i++) {
            listener.sendWindowProperty(this, i, this.enchantLevels[i]);
        }
        listener.sendWindowProperty(this, 3, this.xpSeed & -16);
        listener.sendWindowProperty(this, 4, this.tile.getBookcaseCount());
    }

    public boolean areValidItems(ItemStack scroll, ItemStack item) {
        if (!scroll.isEmpty() && !item.isEmpty()) {
            Enchantment enchantment = ItemArcaneScroll.getEnchantment(scroll);
            if (enchantment == null)
                return false;
            Set<Enchantment> enchantments = EnchantmentHelper.getEnchantments(item).keySet();
            if (enchantments.contains(enchantment))
                return false;
            for (Enchantment e : enchantments) {
                if (!e.isCompatibleWith(enchantment))
                    return false;
            }
            if (HCEnchanting.InfernalEnchantmentType.fromEnchantment(enchantment).canEnchantItem(item.getItem())) {
                return true;
            }
        }
        return false;
    }

    public void onContextChanged(IItemHandler handler) {
        ItemStack scroll = handler.getStackInSlot(0);
        ItemStack item = handler.getStackInSlot(1);

        Enchantment enchantment = null;
        Arrays.fill(enchantLevels, 0);
        int enchantCount = 1;
        if (areValidItems(scroll, item)) {
            enchantment = ItemArcaneScroll.getEnchantment(scroll);
            enchantCount = EnchantmentHelper.getEnchantments(item).size();
        }
        for (int i = 1; i <= enchantLevels.length; i++) {
            if (enchantment == null || i > enchantment.getMaxLevel()) {
                enchantLevels[i - 1] = -1;
            } else {
                double max = Math.min(enchantment.getMaxLevel(), enchantLevels.length);
                double j = i / max;
                enchantLevels[i - 1] = (int) Math.ceil(30.0 * j) + (30 * enchantCount);
            }
        }
        detectAndSendChanges();
    }

    public static final int INV_LAST = 1;

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index > INV_LAST) {
                if (itemstack1.getItem() instanceof ItemArcaneScroll) {
                    if (!mergeItemStack(itemstack1, 0, 1, true))
                        return ItemStack.EMPTY;
                } else {
                    if (!mergeItemStack(itemstack1, 1, 2, true))
                        return ItemStack.EMPTY;
                }
            } else {
                if (!mergeItemStack(itemstack1, 2, 37, true))
                    return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }
        handler.onContentsChanged(index);
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);

            if (!stack.isEmpty() && !playerIn.getEntityWorld().isRemote)
                InvUtils.ejectStack(playerIn.getEntityWorld(), playerIn.posX, playerIn.posY, playerIn.posZ, stack);
        }
    }

    public boolean hasLevels(EntityPlayer player, int level) {
        return player.capabilities.isCreativeMode || (player.experienceLevel >= level && tile.getBookcaseCount() >= level);
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int level) {
        if (this.enchantLevels[level] >= level && hasLevels(player, level)) {
            if (!player.world.isRemote) {
                ItemStack item = this.handler.getStackInSlot(1);
                ItemStack scroll = this.handler.getStackInSlot(0);
                Enchantment enchantment = ItemArcaneScroll.getEnchantment(scroll);
                if (enchantment != null) {
                    scroll.shrink(1);
                    item.addEnchantment(enchantment, level + 1);
                    tile.getWorld().playSound(null, tile.getPos(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.BLOCKS, 1.0F, tile.getWorld().rand.nextFloat() * 0.1F + 0.9F);
                }
            }
            return true;
        }
        return false;
    }
}
