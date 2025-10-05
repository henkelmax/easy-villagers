package de.maxhenkel.easyvillagers.inventory;

import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.Supplier;

public class InputOnlyResourceHandler extends DelegatingResourceHandler<ItemResource> {

    public InputOnlyResourceHandler(ResourceHandler<ItemResource> delegate) {
        super(delegate);
    }

    public InputOnlyResourceHandler(Supplier<ResourceHandler<ItemResource>> delegate) {
        super(delegate);
    }

    @Override
    public int extract(ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

}
