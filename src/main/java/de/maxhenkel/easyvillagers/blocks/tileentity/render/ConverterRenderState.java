package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;

public class ConverterRenderState extends VillagerRenderStateBase {

    public boolean renderZombieVillager;
    public ZombieVillagerRenderState zombieVillagerRenderState = new ZombieVillagerRenderState();
    public ZombieRenderState zombieRenderState = new ZombieRenderState();

}
