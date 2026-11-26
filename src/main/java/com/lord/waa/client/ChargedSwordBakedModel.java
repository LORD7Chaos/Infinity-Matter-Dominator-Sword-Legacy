package com.lord.waa.client;

import javax.vecmath.Matrix4f;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.client.model.BakedModelWrapper;
import org.apache.commons.lang3.tuple.Pair;

public final class ChargedSwordBakedModel extends BakedModelWrapper<IBakedModel> {
    private final ChargedSwordRenderer renderer;

    public ChargedSwordBakedModel(IBakedModel originalModel, ChargedSwordRenderer renderer) {
        super(originalModel);
        this.renderer = renderer;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        Pair<? extends IBakedModel, Matrix4f> perspective = originalModel.handlePerspective(type);
        if (type != ItemCameraTransforms.TransformType.GUI) {
            return perspective;
        }
        renderer.setPerspective(type, perspective.getLeft());
        return Pair.of(this, perspective.getRight());
    }
}
