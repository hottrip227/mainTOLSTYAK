package dev.archmage.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;

/** Animated attachments follow vanilla head/body/arm poses rather than floating world entities. */
public final class ArchmageOutfitLayer extends RenderLayer<AvatarRenderState,PlayerModel> {
    private final ModelPart geometry=ArchmageOutfitGeometry.create();
    public ArchmageOutfitLayer(RenderLayerParent<AvatarRenderState,PlayerModel> parent) { super(parent); }
    @Override public void submit(PoseStack pose,SubmitNodeCollector collector,int light,AvatarRenderState state,float yaw,float pitch) {
        if(!ArchmageAppearance.isEnabledFor(state.id)||state.isInvisible||state.isSpectator)return;
        var player=Minecraft.getInstance().player;
        if(player.getItemBySlot(EquipmentSlot.HEAD).isEmpty())draw("head",getParentModel().head,pose,collector,light);
        // Keep vanilla armour readable; optional costume pieces hide under chest armour.
        if(!player.getItemBySlot(EquipmentSlot.CHEST).isEmpty())return;
        draw("body",getParentModel().body,pose,collector,light);
        draw("left_arm",getParentModel().leftArm,pose,collector,light);
        draw("right_arm",getParentModel().rightArm,pose,collector,light);
        var cape=geometry.getChild("cape");
        cape.xRot=0.16f+(float)Math.sin(state.ageInTicks*0.06f)*0.025f+Math.min(state.walkAnimationSpeed,1f)*0.14f;
        draw("cape",getParentModel().body,pose,collector,light);
    }
    private void draw(String part,ModelPart parent,PoseStack pose,SubmitNodeCollector collector,int light) {
        pose.pushPose();parent.translateAndRotate(pose);
        collector.order(0).submitModelPart(geometry.getChild(part),pose,RenderTypes.entityCutout(ArchmageAppearance.OUTFIT_TEXTURE),light,OverlayTexture.NO_OVERLAY,null);
        pose.popPose();
    }
}
