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

package com.dynamero.machines;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import com.dynamero.fluid.block.AbstractFluidContainerBlock;
import com.dynamero.shared.annotations.*;
import com.dynamero.shared.properties.BlockProperties;

/**
 * Abstract base machine block pre-configured with horizontal facing, GUI,
 * inventory, comparator output, ticking, and standard machine block state properties
 * (lit, locked, powered, enabled, unstable).
 */
@SuppressWarnings("unused")
@Developer("TheMentor")
@CreatedAt("2026-10-08")
@ModifiedAt("2026-10-08")
@ModifiedBy("TheMentor")
@Website("https://dynamero.com")
@Repository("https://github.com/drkhodakarami/")
@Discord("https://discord.gg/pmM4emCbuH")
@Youtube("https://www.youtube.com/@TheMentorCodeLab")
@Modrinth("https://modrinth.com/user/jiraiyah")
public class AbstractBaseMachineBlock extends AbstractFluidContainerBlock implements MachineBlockHandler
{
    /**
     * Constructs an AbstractBaseMachineBlock and sets up standard machine state properties.
     *
     * @param properties    the vanilla block properties
     * @param blockProperty the custom block properties builder
     */
    public AbstractBaseMachineBlock(Properties properties, BlockProperties<?> blockProperty)
    {
        super(properties, blockProperty
                .addHorizontalFacing()
                .addGui()
                .facingOpposite()
                .addComparatorOutput()
                .addInventory()
                .addLitProperty()
                .addLockedProperty()
                .addPoweredProperty()
                .addEnabledProperty()
                .addUnstableProperty()
                .tick());

        registerDefaultState(defaultBlockState()
                                .setValue(BlockStateProperties.LIT, false)
                                .setValue(BlockStateProperties.LOCKED, false)
                                .setValue(BlockStateProperties.POWERED, false)
                                .setValue(BlockStateProperties.UNSTABLE, false)
                                .setValue(BlockStateProperties.ENABLED, true));
    }
}