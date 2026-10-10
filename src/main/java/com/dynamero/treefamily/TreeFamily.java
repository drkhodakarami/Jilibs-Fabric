package com.dynamero.treefamily;

import com.dynamero.shared.utils.BaseHelper;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTagId;
import net.minecraft.world.level.block.grower.TreeGrower;

public record TreeFamily(String modid, String name, TreeGrower treeGrower,
                         WoodsetBlocks blocks, WoodsetSignBlocks signs, WoodsetBoats boats,
                         BlockFamily blockFamily, BlockItemTagId tags)
{
    public Identifier id()
    {
        return BaseHelper.id(modid, name);
    }
}