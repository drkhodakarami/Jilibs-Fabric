/*
 * Copyright (c) 2025 Alireza Khodakarami
 *
 * Licensed under the MIT, (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://opensource.org/license/mit
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dynamero.woodset;

import java.util.function.Function;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;

import com.dynamero.register.ItemRegisterar;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.utils.BaseHelper;

/**
 * Container and registry holder for sign and hanging sign items within a {@link WoodSet}.
 */
@SuppressWarnings("unused")
@Developer("TurtyWurty")
@ModifiedBy("The Mentor")
@CreatedAt("2025-04-15")
@Repository("https://github.com/DaRealTurtyWurty/Industria")
@Discord("https://discord.turtywurty.dev/")
@Youtube("https://www.youtube.com/@TurtyWurty")
public class WoodsetSignItems
{
    /**
     * Resource key for the standing sign item.
     */
    public final ResourceKey<Item> signItemId;

    /**
     * Resource key for the hanging sign item.
     */
    public final ResourceKey<Item> hangingSignItemId;

    /**
     * Registered sign item instance.
     */
    public final SignItem signItem;

    /**
     * Registered hanging sign item instance.
     */
    public final HangingSignItem hangingSignItem;

    /**
     * Constructs and registers sign and hanging sign items for a wood set.
     *
     * @param modid           the mod identifier
     * @param name            the base wood name
     * @param itemRegister    the item registerer helper
     * @param sign            the standing sign block
     * @param wallSign        the wall sign block
     * @param hangingSign     the ceiling hanging sign block
     * @param wallHangingSign the wall hanging sign block
     * @param signItem        factory creating sign items
     * @param hangingSignItem factory creating hanging sign items
     */
    public WoodsetSignItems(String modid, String name,
                            ItemRegisterar itemRegister,
                            StandingSignBlock sign,
                            WallSignBlock wallSign,
                            CeilingHangingSignBlock hangingSign,
                            WallHangingSignBlock wallHangingSign,
                            Function<Item.Properties, SignItem> signItem,
                            Function<Item.Properties, HangingSignItem> hangingSignItem)
    {
        signItemId = BaseHelper.ResourceKeys.create(modid, name + "_sign", Registries.ITEM);
        hangingSignItemId = BaseHelper.ResourceKeys.create(modid, name + "_hanging_sign", Registries.ITEM);

        this.signItem = itemRegister.register(name + "_sign",
                                            signItem == null
                                            ? settings -> new SignItem(sign, wallSign, settings.stacksTo(16))
                                            : signItem).item();

        this.hangingSignItem = itemRegister.register(name + "_hanging_sign",
                                                    hangingSignItem == null
                                                    ? settings -> new HangingSignItem(hangingSign, wallHangingSign, settings.stacksTo(16))
                                                    : hangingSignItem).item();
    }
}