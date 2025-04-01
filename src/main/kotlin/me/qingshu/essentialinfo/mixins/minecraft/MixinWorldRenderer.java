package me.qingshu.essentialinfo.mixins.minecraft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
            at = @At("HEAD")
    )
    private void beforeRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        RenderAfterWorldEvent.get(matrix4f, matrix4f2, camera, bufferBuilders);
    }

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/client/util/math/MatrixStack"
            )
    )
    private MatrixStack setMatrixStack(MatrixStack matrixStack) {
        RenderAfterWorldEvent.setMatrix(matrixStack);
        return matrixStack;
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/Camera;)V"
            )
    )
    private void onChunkDebugRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci){
        EventHandler.emit(RenderAfterWorldEvent.INSTANCE);
    }
}
