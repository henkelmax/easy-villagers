package de.maxhenkel.easyvillagers.integration.jei.breeder;

import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.integration.jei.InputOutputCategory;
import de.maxhenkel.easyvillagers.integration.jei.JEIPlugin;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class BreederCategory extends InputOutputCategory {

    public BreederCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(ModBlocks.BREEDER.get());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemStack recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).add(VanillaTypes.ITEM_STACK, recipe);
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 1);
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 1);
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 1);

        builder.addSlot(RecipeIngredientRole.OUTPUT, 1, 32).add(VanillaTypes.ITEM_STACK, VillagerItem.createBabyVillager());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 19, 32);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 37, 32);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 32);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.easy_villagers.breeding");
    }

    @Override
    public IRecipeType<ItemStack> getRecipeType() {
        return JEIPlugin.CATEGORY_BREEDING;
    }

}