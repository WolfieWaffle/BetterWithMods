package betterwithmods.module.compat.jei.category;

import betterwithmods.BWMod;
import betterwithmods.module.compat.jei.wrapper.BulkRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class CrucibleRecipeCategory extends BWMRecipeCategory<BulkRecipeWrapper> {
    public static final String UID = "bwm.crucible";
    private static final int inputSlots = 2;
    private static final int outputSlots = 0;

    private static final ResourceLocation guiTexture = new ResourceLocation(BWMod.MODID, "textures/gui/jei/cooking.png");
    @Nonnull
    private final ICraftingGridHelper craftingGrid;
    @Nonnull
    private final IDrawableAnimated flame;

    public CrucibleRecipeCategory(IGuiHelper helper) {
        super(helper.createDrawable(guiTexture, 5, 6, 158, 60), "inv.crucible.name");
        craftingGrid = helper.createCraftingGridHelper(inputSlots, outputSlots);
        IDrawableStatic flameDrawable = helper.createDrawable(guiTexture, 176, 0, 14, 14);
        this.flame = helper.createAnimatedDrawable(flameDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Nonnull
    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        flame.draw(minecraft, 80, 19);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull BulkRecipeWrapper wrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();

        stacks.init(outputSlots, false, 118, 18);
        stacks.init(outputSlots + 1, false, 118 + 18, 18);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = inputSlots + i + (j * 3);
                stacks.init(index, true, 2 + i * 18, j * 18);
            }
        }

        stacks.set(outputSlots, wrapper.getRecipe().getOutputs());
        craftingGrid.setInputs(stacks, ingredients.getInputs(ItemStack.class));
    }

    @Override
    public String getModName() {
        return BWMod.NAME;
    }

}
