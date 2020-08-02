package de.maxhenkel.easyvillagers.integration.waila;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.util.ResourceLocation;

@WailaPlugin
public class PluginEasyVillagers implements IWailaPlugin {

    static final ResourceLocation OBJECT_NAME_TAG = new ResourceLocation("waila", "object_name");
    static final ResourceLocation CONFIG_SHOW_REGISTRY = new ResourceLocation("waila", "show_registry");
    static final ResourceLocation REGISTRY_NAME_TAG = new ResourceLocation("waila", "registry_name");

    @Override
    public void register(IRegistrar registrar) {

    }

}
