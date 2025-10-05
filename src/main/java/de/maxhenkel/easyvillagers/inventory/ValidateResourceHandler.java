package de.maxhenkel.easyvillagers.inventory;

import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.function.Predicate;

public class ValidateResourceHandler extends ListAccessItemStacksResourceHandler {

    protected Predicate<ItemResource> inputValidator;

    public ValidateResourceHandler(int size, Predicate<ItemResource> inputValidator) {
        super(size);
        this.inputValidator = inputValidator;
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return inputValidator.test(resource) && super.isValid(index, resource);
    }
}
