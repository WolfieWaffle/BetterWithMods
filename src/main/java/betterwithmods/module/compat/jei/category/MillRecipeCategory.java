package betterwithmods.module.compat.jei.category;

import betterwithmods.BWMod;
import betterwithmods.module.compat.jei.wrapper.BulkRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class MillRecipeCategory extends BWMRecipeCategory<BulkRecipeWrapper> {
    public static final String UID = "bwm.mill";
    private static final int inputSlots = 2;
    private static final int outputSlots = 0;
    private static final ResourceLocation guiTexture = new ResourceLocation(BWMod.MODID, "textures/gui/jei/mill.png");

    @Nonnull
    private final IDrawableAnimated gear;

    public MillRecipeCategory(IGuiHelper helper) {
        super(helper.createDrawable(guiTexture, 5, 6, 158, 36), "inv.mill.name");
        IDrawableStatic flameDrawable = helper.createDrawable(guiTexture, 176, 0, 14, 14);
        this.gear = helper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getModName() {
        return BWMod.NAME;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        gear.draw(minecraft, 80, 19);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull BulkRecipeWrapper wrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        stacks.init(outputSlots, false, 118, 18);
        stacks.init(outputSlots + 1, false, 118 + 18, 18);
        List<List<ItemStack>> input = ingredients.getInputs(ItemStack.class);
        for (int i = 0; i < 3; i++) {
            int index = inputSlots + i;
            stacks.init(index, true, 2 + i * 18, 18);
            if (input.size() > i && input.get(i) != null) {
                stacks.set(index, input.get(i));
            }
        }
        stacks.set(outputSlots, wrapper.getRecipe().getOutputs());
    }
}
