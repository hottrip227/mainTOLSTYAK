package dev.archmage.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

/** Original Minecraft-scale geometry; editable source: docs/visual-assets/outfit-geometry.json. */
public final class ArchmageOutfitGeometry {
    private ArchmageOutfitGeometry() {}
    public static ModelPart create() {
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();
        var head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        var body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        var left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        var right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        var cape = root.addOrReplaceChild("cape", CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("collar_back", CubeListBuilder.create().texOffs(0, 0).addBox(-6.1f, -5.2f, 2.6f, 12.2f, 6f, 2f), PartPose.ZERO);
        body.addOrReplaceChild("collar_back_inlay", CubeListBuilder.create().texOffs(128, 0).addBox(-5.6f, -4.7f, 2.45f, 11.2f, 5f, 0.25f), PartPose.ZERO);
        body.addOrReplaceChild("collar_crown", CubeListBuilder.create().texOffs(192, 0).addBox(-6.2f, -5.5f, 2.4f, 12.4f, 0.5f, 2.4f), PartPose.ZERO);
        body.addOrReplaceChild("collar_side_-1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.2f, -4.3f, -0.5f, 1.7f, 4.7f, 3.2f), PartPose.ZERO);
        body.addOrReplaceChild("collar_edge_-1", CubeListBuilder.create().texOffs(192, 0).addBox(-6.2f, -4.6f, -0.6f, 1.7f, 0.5f, 3.4f), PartPose.ZERO);
        right_arm.addOrReplaceChild("pauldron_-1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5f, -3.7f, -3.3f, 5.5f, 2.2f, 6.6f), PartPose.ZERO);
        right_arm.addOrReplaceChild("pauldron_gold_-1", CubeListBuilder.create().texOffs(192, 0).addBox(-4.5f, -4f, -3.4f, 5.5f, 0.55f, 6.8f), PartPose.ZERO);
        right_arm.addOrReplaceChild("pauldron_inlay_-1", CubeListBuilder.create().texOffs(64, 0).addBox(-4f, -4.25f, -2.8f, 4.5f, 0.45f, 5.6f), PartPose.ZERO);
        right_arm.addOrReplaceChild("shoulder_spire_-1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.4f, -7.1f, -1.5f, 1.3f, 5.2f, 3f), PartPose.rotation(0f, 0f, -0.18f));
        right_arm.addOrReplaceChild("shoulder_spire_gold_-1", CubeListBuilder.create().texOffs(0, 64).addBox(-5.4f, -7.4f, -1.65f, 1.3f, 0.6f, 3.3f), PartPose.rotation(0f, 0f, -0.18f));
        right_arm.addOrReplaceChild("cuff_-1", CubeListBuilder.create().texOffs(192, 0).addBox(-3.3f, 7.1f, -2.3f, 4.6f, 1.25f, 4.6f), PartPose.ZERO);
        right_arm.addOrReplaceChild("cuff_inlay_-1", CubeListBuilder.create().texOffs(64, 0).addBox(-3.1f, 7.35f, -2.45f, 4.2f, 0.7f, 0.3f), PartPose.ZERO);
        body.addOrReplaceChild("collar_side_1", CubeListBuilder.create().texOffs(0, 0).addBox(4.5f, -4.3f, -0.5f, 1.7f, 4.7f, 3.2f), PartPose.ZERO);
        body.addOrReplaceChild("collar_edge_1", CubeListBuilder.create().texOffs(192, 0).addBox(4.5f, -4.6f, -0.6f, 1.7f, 0.5f, 3.4f), PartPose.ZERO);
        left_arm.addOrReplaceChild("pauldron_1", CubeListBuilder.create().texOffs(0, 0).addBox(-1f, -3.7f, -3.3f, 5.5f, 2.2f, 6.6f), PartPose.ZERO);
        left_arm.addOrReplaceChild("pauldron_gold_1", CubeListBuilder.create().texOffs(192, 0).addBox(-1f, -4f, -3.4f, 5.5f, 0.55f, 6.8f), PartPose.ZERO);
        left_arm.addOrReplaceChild("pauldron_inlay_1", CubeListBuilder.create().texOffs(64, 0).addBox(-0.5f, -4.25f, -2.8f, 4.5f, 0.45f, 5.6f), PartPose.ZERO);
        left_arm.addOrReplaceChild("shoulder_spire_1", CubeListBuilder.create().texOffs(0, 0).addBox(3.9f, -7.1f, -1.5f, 1.3f, 5.2f, 3f), PartPose.rotation(0f, 0f, 0.18f));
        left_arm.addOrReplaceChild("shoulder_spire_gold_1", CubeListBuilder.create().texOffs(0, 64).addBox(3.9f, -7.4f, -1.65f, 1.3f, 0.6f, 3.3f), PartPose.rotation(0f, 0f, 0.18f));
        left_arm.addOrReplaceChild("cuff_1", CubeListBuilder.create().texOffs(192, 0).addBox(-1.3f, 7.1f, -2.3f, 4.6f, 1.25f, 4.6f), PartPose.ZERO);
        left_arm.addOrReplaceChild("cuff_inlay_1", CubeListBuilder.create().texOffs(64, 0).addBox(-1.1f, 7.35f, -2.45f, 4.2f, 0.7f, 0.3f), PartPose.ZERO);
        head.addOrReplaceChild("hair_crown", CubeListBuilder.create().texOffs(64, 64).addBox(-4.25f, -8.5f, -3.9f, 8.5f, 1.15f, 8.4f), PartPose.ZERO);
        head.addOrReplaceChild("hair_back", CubeListBuilder.create().texOffs(192, 64).addBox(-4.2f, -7.7f, 3.35f, 8.4f, 13.5f, 1.25f), PartPose.ZERO);
        head.addOrReplaceChild("back_strand_0", CubeListBuilder.create().texOffs(128, 64).addBox(-3.6f, -7.7f, 4.35f, 1.2f, 13.3f, 0.35f), PartPose.ZERO);
        head.addOrReplaceChild("back_strand_1", CubeListBuilder.create().texOffs(64, 64).addBox(-1.8f, -7.7f, 4.35f, 1.2f, 12.3f, 0.35f), PartPose.ZERO);
        head.addOrReplaceChild("back_strand_2", CubeListBuilder.create().texOffs(128, 64).addBox(0f, -7.7f, 4.35f, 1.2f, 13.3f, 0.35f), PartPose.ZERO);
        head.addOrReplaceChild("back_strand_3", CubeListBuilder.create().texOffs(64, 64).addBox(1.8f, -7.7f, 4.35f, 1.2f, 12.3f, 0.35f), PartPose.ZERO);
        head.addOrReplaceChild("back_strand_4", CubeListBuilder.create().texOffs(128, 64).addBox(3.6f, -7.7f, 4.35f, 1.2f, 13.3f, 0.35f), PartPose.ZERO);
        head.addOrReplaceChild("hair_side_-1", CubeListBuilder.create().texOffs(64, 64).addBox(-4.55f, -7.3f, -2f, 0.95f, 9.5f, 5.6f), PartPose.ZERO);
        head.addOrReplaceChild("front_strand_-1", CubeListBuilder.create().texOffs(64, 64).addBox(-3.25f, -7.3f, -4.55f, 1.2f, 12.1f, 0.9f), PartPose.ZERO);
        head.addOrReplaceChild("front_strand_light_-1", CubeListBuilder.create().texOffs(128, 64).addBox(-3.1f, -7.3f, -4.7f, 0.45f, 11.6f, 0.2f), PartPose.ZERO);
        head.addOrReplaceChild("hair_side_1", CubeListBuilder.create().texOffs(64, 64).addBox(3.6f, -7.3f, -2f, 0.95f, 9.5f, 5.6f), PartPose.ZERO);
        head.addOrReplaceChild("front_strand_1", CubeListBuilder.create().texOffs(64, 64).addBox(2.05f, -7.3f, -4.55f, 1.2f, 12.1f, 0.9f), PartPose.ZERO);
        head.addOrReplaceChild("front_strand_light_1", CubeListBuilder.create().texOffs(128, 64).addBox(2.2f, -7.3f, -4.7f, 0.45f, 11.6f, 0.2f), PartPose.ZERO);
        cape.addOrReplaceChild("cape_back", CubeListBuilder.create().texOffs(64, 0).addBox(-6.1f, 1f, 3.15f, 12.2f, 21f, 0.65f), PartPose.ZERO);
        cape.addOrReplaceChild("cape_lining", CubeListBuilder.create().texOffs(128, 0).addBox(-5.5f, 1.5f, 3f, 11f, 19.8f, 0.2f), PartPose.ZERO);
        cape.addOrReplaceChild("cape_edge_-6.15", CubeListBuilder.create().texOffs(192, 0).addBox(-6.15f, 1f, 2.95f, 0.55f, 21f, 1f), PartPose.ZERO);
        cape.addOrReplaceChild("cape_edge_5.6", CubeListBuilder.create().texOffs(192, 0).addBox(5.6f, 1f, 2.95f, 0.55f, 21f, 1f), PartPose.ZERO);
        cape.addOrReplaceChild("cape_hem", CubeListBuilder.create().texOffs(192, 0).addBox(-6.1f, 21.5f, 2.95f, 12.2f, 0.55f, 1f), PartPose.ZERO);
        cape.addOrReplaceChild("memory_seal_0", CubeListBuilder.create().texOffs(192, 0).addBox(-0.5f, 8f, 3.83f, 1f, 1f, 0.22f), PartPose.ZERO);
        cape.addOrReplaceChild("memory_seal_1", CubeListBuilder.create().texOffs(192, 0).addBox(-1f, 9f, 3.83f, 2f, 1f, 0.22f), PartPose.ZERO);
        cape.addOrReplaceChild("memory_seal_2", CubeListBuilder.create().texOffs(192, 0).addBox(-1.5f, 10f, 3.83f, 3f, 1f, 0.22f), PartPose.ZERO);
        cape.addOrReplaceChild("memory_seal_3", CubeListBuilder.create().texOffs(192, 0).addBox(-1f, 11f, 3.83f, 2f, 1f, 0.22f), PartPose.ZERO);
        cape.addOrReplaceChild("memory_seal_4", CubeListBuilder.create().texOffs(192, 0).addBox(-0.5f, 12f, 3.83f, 1f, 1f, 0.22f), PartPose.ZERO);
        cape.addOrReplaceChild("seal_gem", CubeListBuilder.create().texOffs(128, 0).addBox(-0.55f, 9.8f, 4.02f, 1.1f, 1.4f, 0.18f), PartPose.ZERO);
        body.addOrReplaceChild("tabard", CubeListBuilder.create().texOffs(64, 0).addBox(-1.9f, 9.7f, -2.75f, 3.8f, 11.5f, 0.5f), PartPose.ZERO);
        body.addOrReplaceChild("tabard_trim", CubeListBuilder.create().texOffs(192, 0).addBox(-2.05f, 20.7f, -2.85f, 4.1f, 0.55f, 0.7f), PartPose.ZERO);
        body.addOrReplaceChild("chest_jewel_frame", CubeListBuilder.create().texOffs(192, 0).addBox(-1.2f, 1.5f, -2.45f, 2.4f, 2.4f, 0.5f), PartPose.ZERO);
        body.addOrReplaceChild("chest_jewel", CubeListBuilder.create().texOffs(128, 0).addBox(-0.65f, 2.05f, -2.75f, 1.3f, 1.3f, 0.5f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 256, 256).bakeRoot();
    }
}
