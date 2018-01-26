package betterwithmods.network;

import betterwithmods.BWMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHarnessSync extends NetworkMessage {
    public ItemStack harness = ItemStack.EMPTY;
    public int entityID;

    public MessageHarnessSync() {
        this(0,ItemStack.EMPTY);
    }

    public MessageHarnessSync(int entityID, ItemStack harness) {
        this.entityID = entityID;
        this.harness = harness;
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        BWMod.proxy.syncHarness(entityID, harness);
        return super.handleMessage(context);
    }
}
