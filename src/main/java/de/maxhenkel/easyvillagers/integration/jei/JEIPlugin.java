package de.maxhenkel.easyvillagers.integration.jei;

import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.blocks.ModBlocks;
import de.maxhenkel.easyvillagers.integration.jei.breeder.BreederCategory;
import de.maxhenkel.easyvillagers.integration.jei.converter.ConverterCategory;
import de.maxhenkel.easyvillagers.integration.jei.incubator.IncubatorCategory;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static final ResourceLocation CATEGORY_BREEDING = new ResourceLocation(Main.MODID, "breeding");
    public static final ResourceLocation CATEGORY_CONVERTING = new ResourceLocation(Main.MODID, "converting");
    public static final ResourceLocation CATEGORY_INCUBATING = new ResourceLocation(Main.MODID, "incubating");

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Main.MODID, "easy_villagers");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BREEDER), CATEGORY_BREEDING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CONVERTER), CATEGORY_CONVERTING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INCUBATOR), CATEGORY_INCUBATING);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new BreederCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ConverterCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new IncubatorCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Collection<ItemStack> foods = Villager.FOOD_POINTS.entrySet().stream().map(itemIntegerEntry -> new ItemStack(itemIntegerEntry.getKey(), (int) Math.ceil(24D / (double) itemIntegerEntry.getValue()))).collect(Collectors.toList());
        registration.addRecipes(foods, CATEGORY_BREEDING);

        Collection<ItemStack> potions = new ArrayList<>();
        potions.add(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WEAKNESS));
        potions.add(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_WEAKNESS));
        potions.add(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.WEAKNESS));
        potions.add(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_WEAKNESS));
        potions.add(PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Potions.WEAKNESS));
        potions.add(PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Potions.LONG_WEAKNESS));
        registration.addRecipes(potions, CATEGORY_CONVERTING);

        registration.addRecipes(Collections.singletonList(VillagerItem.getBabyVillager()), CATEGORY_INCUBATING);
    }

}
