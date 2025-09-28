package de.maxhenkel.easyvillagers.blocks.tileentity.render;

import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;

public class IronFarmRenderState extends VillagerRenderStateBase {

    public ZombieRenderState zombieRenderState = new ZombieRenderState();
    public boolean renderIronGolem = false;
    public IronGolemRenderState ironGolemRenderState = new IronGolemRenderState();

}
