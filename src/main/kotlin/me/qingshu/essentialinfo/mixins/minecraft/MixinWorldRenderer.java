package me.qingshu.essentialinfo.mixins.minecraft;

import me.qingshu.essentialinfo.events.EventHandler;
import me.qingshu.essentialinfo.events.entity.entity.EntityRenderEvent;
import me.qingshu.essentialinfo.events.render.RenderAfterWorldEvent;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow @Final
    private BufferBuilderStorage bufferBuilders;

    @Inject(
            method = "renderEntity",
            at = @At("RETURN")
    )
    private void onRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        EventHandler.emit(EntityRenderEvent.get(matrices, entity));
    }

    @Inject(
            method = "render",
            at = @At("RETURN")
    )
    private void onRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        EventHandler.emit(RenderAfterWorldEvent.get(matrix4f, camera, bufferBuilders.getEntityVertexConsumers()));
    }
}
