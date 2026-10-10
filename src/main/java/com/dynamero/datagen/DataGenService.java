package com.dynamero.datagen;

import com.dynamero.datagen.base.ConventionTag;
import net.minecraft.tags.TagKey;

public final class DataGenService implements DataGenerationService {
    @Override
    public <T> TagKey<T> resolveConventionTag(ConventionTag<T> tag) {
        return TagKey.create(tag.registry(), tag.fabricId());
    }
}