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

package com.dynamero.base.client;

import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import com.dynamero.shared.annotations.*;

/**
 * Base block entity renderer (BER) supporting item stack rendering and entity rendering
 * via modern Minecraft render state submission.
 *
 * <p>Use Case:
 * <pre>
 * {@code
 * public class CustomBERenderer extends AbstractBaseBER<CustomBE, CustomBERS>
 * {
 *      public CustomBERenderer(BlockEntityRendererProvider.Context context)
 *      {
 *          // Pass the method reference to constructor of CustomBERS
 *          super(context, CustomBERS::new);
 *      }
 * }
 * }
 * </pre>
 *
 * @param <T> the block entity type being rendered
 * @param <U> the custom render state type extending {@link BaseBERS}
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
public abstract class AbstractBaseBER<T extends BlockEntity, U extends BaseBERS> implements BlockEntityRenderer<T, U>
{
    /**
     * Resolver for building item render states.
     */
    private final ItemModelResolver itemModelManager;

    /**
     * Dispatcher for extracting and submitting entity render states.
     */
    private final EntityRenderDispatcher entityRenderManager;

    /**
     * Cached entity instance displayed inside or above the block entity.
     */
    protected Entity displayEntity;

    /**
     * Factory creating fresh render state instances.
     */
    private final Supplier<U> renderStateFactory;

    /**
     * Constructs an AbstractBaseBER using the provided renderer context and render state factory.
     *
     * @param context            the renderer provider context
     * @param renderStateFactory supplier producing render state objects
     */
    public AbstractBaseBER(BlockEntityRendererProvider.Context context, Supplier<U> renderStateFactory)
    {
        itemModelManager = context.itemModelResolver();
        entityRenderManager = context.entityRenderer();

        this.renderStateFactory = renderStateFactory;
    }

    @Override
    public @NotNull U createRenderState()
    {
        return renderStateFactory.get();
    }

    @Override
    public void extractRenderState(@NonNull T blockEntity, @NonNull U state, float tickProgress, @NotNull Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay)
    {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        state.blockPos = blockEntity.getBlockPos();
        state.world = blockEntity.getLevel();

        handleItemstack(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        handleEntity(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    }

    @Override
    public void submit(@NonNull U state, @NotNull PoseStack matrices, @NotNull SubmitNodeCollector queue, @NotNull CameraRenderState cameraState)
    {
        if (shouldRenderItem())
            renderItemStack(matrices, queue, state.itemRenderState, cameraState);

        if (shouldRenderEntities() && displayEntity != null)
            renderDisplayEntities(matrices, queue, state.displayEntityRenderState, cameraState);
    }

    private void updateEntityManager(float tickProgress)
    {
        entityRenderManager.extractEntity(displayEntity, tickProgress);
    }

    private void updateItemModelManager(T blockEntity, U state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay)
    {
        ItemStack stack = getItemStack(blockEntity);
        if(stack != null && !stack.isEmpty())
            itemModelManager.updateForTopItem(state.itemRenderState, getItemStack(blockEntity), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    /**
     * Updates the item render state if item rendering is enabled.
     *
     * @param blockEntity      the block entity
     * @param state            the render state
     * @param tickProgress     the tick progress delta
     * @param cameraPos        the camera position
     * @param crumblingOverlay the optional crumbling overlay
     */
    protected void handleItemstack(T blockEntity, U state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay)
    {
        if (shouldRenderItem())
            updateItemModelManager(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    }

    /**
     * Extracts the display entity render state if entity rendering is enabled.
     *
     * @param blockEntity      the block entity
     * @param state            the render state
     * @param tickProgress     the tick progress delta
     * @param cameraPos        the camera position
     * @param crumblingOverlay the optional crumbling overlay
     */
    protected void handleEntity(T blockEntity, U state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay)
    {
        if (shouldRenderEntities())
        {
            if (displayEntity == null)
                displayEntity = getDisplayEntities(state.world);

            if (displayEntity != null)
                updateEntityManager(tickProgress);
        }
    }

    /**
     * Whether this block entity should render an item stack.
     *
     * @return true if an item should be rendered, false otherwise
     */
    protected boolean shouldRenderItem()
    {
        return false;
    }

    /**
     * Whether this block entity should render a display entity.
     *
     * @return true if an entity should be rendered, false otherwise
     */
    protected boolean shouldRenderEntities()
    {
        return false;
    }

    /**
     * Retrieves the ItemStack to render for the given block entity.
     *
     * @param blockEntity the block entity
     * @return the ItemStack to render, or null/empty if none
     */
    protected ItemStack getItemStack(T blockEntity)
    {
        return null;
    }

    /**
     * Creates or retrieves the entity instance to display.
     *
     * @param world the level
     * @return the entity to render, or null if none
     */
    protected Entity getDisplayEntities(Level world)
    {
        return null;
    }

    /**
     * Renders the display entity using the submitted matrix stack and draw queue.
     *
     * @param matrices          the pose stack
     * @param queue             the submit node collector
     * @param state             the entity render state
     * @param cameraRenderState the camera render state
     */
    protected void renderDisplayEntities(PoseStack matrices, SubmitNodeCollector queue, EntityRenderState state, CameraRenderState cameraRenderState)
    {}

    /**
     * Renders the item stack using the submitted matrix stack and draw queue.
     *
     * @param matrices          the pose stack
     * @param queue             the submit node collector
     * @param state             the item stack render state
     * @param cameraRenderState the camera render state
     */
    protected void renderItemStack(PoseStack matrices, SubmitNodeCollector queue, ItemStackRenderState state, CameraRenderState cameraRenderState)
    {}
}