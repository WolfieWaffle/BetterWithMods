package betterwithmods.client.container.bulk;

import betterwithmods.common.blocks.mechanical.tile.TileEntityMill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMill extends Container {
    private final TileEntityMill mill;
    private int lastMillCounter;
    public boolean blocked;

    public ContainerMill(EntityPlayer player, TileEntityMill mill) {
        this.mill = mill;

        for (int j = 0; j < 3; j++) {
            addSlotToContainer(new SlotItemHandler(mill.inventory, j, 62 + j * 18, 43));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), j + i * 9 + 9, 8 + j * 18, 76 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotItemHandler(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), i, 8 + i * 18, 134));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return mill.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (index < 3) {
                if (!mergeItemStack(stack1, 3, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(stack1, 0, 3, false))
                return ItemStack.EMPTY;
            if (stack1.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }
        return stack;
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, this.mill.grindCounter);
        listener.sendWindowProperty(this, 1, this.mill.blocked ? 0 : 1);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener craft : this.listeners) {
            if (this.lastMillCounter != this.mill.grindCounter) {
                craft.sendWindowProperty(this, 0, this.mill.grindCounter);
            }
            craft.sendWindowProperty(this, 1, this.mill.blocked ? 1 : 0);
        }
        this.lastMillCounter = this.mill.grindCounter;
        this.blocked = this.mill.blocked;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        switch (index) {
            case 0:
                this.mill.grindCounter = value;
                break;
            case 1:
                blocked = value == 1;
                break;
        }
    }
}
