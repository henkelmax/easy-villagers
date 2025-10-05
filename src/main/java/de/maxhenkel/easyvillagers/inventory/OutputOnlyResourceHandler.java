package de.maxhenkel.easyvillagers.inventory;

import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.Supplier;

public class OutputOnlyResourceHandler extends DelegatingResourceHandler<ItemResource> {

    public OutputOnlyResourceHandler(ResourceHandler<ItemResource> delegate) {
        super(delegate);
    }

    public OutputOnlyResourceHandler(Supplier<ResourceHandler<ItemResource>> delegate) {
        super(delegate);
    }

    @Override
    public int insert(ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        return 0;
    }

}
