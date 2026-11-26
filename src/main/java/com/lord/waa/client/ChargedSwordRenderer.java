package com.lord.waa.client;

import java.util.Random;
import java.nio.FloatBuffer;
import javax.vecmath.Matrix4f;
import org.lwjgl.BufferUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public final class ChargedSwordRenderer extends TileEntityItemStackRenderer {
    private static final float STAR_RADIUS = 1.2F;
    private static final float INGOT_STAR_SCALE = 1.0F / 1.5F;
    private static final float STAR_SPEED = -24.0F;
    private static final float RAINBOW_SPEED = 3.0F;
    private static final float[][] RAYS = new float[100][6];
    private final boolean ingot;
    private final boolean blockItem;
    private ItemCameraTransforms.TransformType currentType = ItemCameraTransforms.TransformType.GUI;
    private IBakedModel currentModel;
    private FloatBuffer overlayCorrection;

    public ChargedSwordRenderer(boolean ingot) {
        this(ingot, false);
    }

    public ChargedSwordRenderer(boolean ingot, boolean blockItem) {
        this.ingot = ingot;
        this.blockItem = blockItem;
    }

    static {
        Random random = new Random(98765L);
        for (int i = 0; i < RAYS.length; ++i) {
            RAYS[i][0] = random.nextFloat() * 360.0F;
            RAYS[i][1] = 0.6F + random.nextFloat();
            RAYS[i][2] = 0.02F + random.nextFloat() * 0.025F;
            RAYS[i][3] = random.nextFloat();
            RAYS[i][4] = random.nextFloat() * (float) (Math.PI * 2.0);
            RAYS[i][5] = 0.5F + random.nextFloat() * 1.5F;
        }
    }

    public void setOverlayCorrection(Matrix4f correction) {
        if (correction == null) {
            overlayCorrection = null;
            return;
        }
        FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
        matrix.put(new float[] {
            correction.m00, correction.m10, correction.m20, correction.m30,
            correction.m01, correction.m11, correction.m21, correction.m31,
            correction.m02, correction.m12, correction.m22, correction.m32,
            correction.m03, correction.m13, correction.m23, correction.m33
        });
        matrix.flip();
        overlayCorrection = matrix;
    }

    public void setPerspective(ItemCameraTransforms.TransformType type, IBakedModel model) {
        currentType = type;
        currentModel = model;
    }

    @Override
    public void renderByItem(ItemStack stack) {
        if (currentModel == null || stack.isEmpty()) return;
        float time = (System.currentTimeMillis() % 1000000L) / 1000.0F;
        boolean background = currentType == ItemCameraTransforms.TransformType.GUI;

        GL11.glPushMatrix();
        try {
            if (background) {
                GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT
                        | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_CURRENT_BIT);
                try {
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_CULL_FACE);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glPushMatrix();
                    try {
                        if (blockItem && overlayCorrection != null) {
                            overlayCorrection.rewind();
                            GL11.glMultMatrix(overlayCorrection);
                        }
                        drawStar(time);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                        drawRays(time);
                    } finally {
                        GL11.glPopMatrix();
                    }
                } finally {
                    GL11.glPopAttrib();
                }
            }

            GL11.glPushMatrix();
            try {
                if (background && !ingot) {
                    float t = time * 1.25F;
                    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                    GL11.glRotatef((float) Math.sin(t) * 9.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef((float) Math.sin(t) * 3.5F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef((float) Math.cos(t) * 3.5F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                }
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, currentModel);
            } finally {
                GL11.glPopMatrix();
            }
        } finally {
            GL11.glPopMatrix();
        }
    }

    private void drawStar(float time) {
        GL11.glPushMatrix();
        try {
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef(time * STAR_SPEED, 0.0F, 0.0F, 1.0F);
            if (ingot) GL11.glScalef(INGOT_STAR_SCALE, INGOT_STAR_SCALE, 1.0F);
            float[] xs = new float[8];
            float[] ys = new float[8];
            int[] order = {0, 3, 6, 1, 4, 7, 2, 5};
            for (int i = 0; i < 8; ++i) {
                double a = i * Math.PI / 4.0 - Math.PI / 2.0;
                xs[i] = (float) Math.cos(a) * STAR_RADIUS;
                ys[i] = (float) Math.sin(a) * STAR_RADIUS;
            }

            BufferBuilder b = Tessellator.getInstance().getBuffer();
            b.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i < 8; ++i) {
                int a = order[i];
                int c = order[(i + 1) % 8];
                b.pos(0, 0, 0).color(0.06F, 0.05F, 0.12F, 0.35F).endVertex();
                b.pos(xs[a], ys[a], 0).color(0.06F, 0.05F, 0.12F, 0.35F).endVertex();
                b.pos(xs[c], ys[c], 0).color(0.06F, 0.05F, 0.12F, 0.35F).endVertex();
            }
            Tessellator.getInstance().draw();

            int col = java.awt.Color.HSBtoRGB((time * RAINBOW_SPEED) % 1.0F, 0.4F, 0.25F);
            float r = ((col >> 16) & 255) / 255.0F;
            float g = ((col >> 8) & 255) / 255.0F;
            float blue = (col & 255) / 255.0F;
            b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            for (int i = 0; i < 8; ++i) {
                int a = order[i];
                int c = order[(i + 1) % 8];
                float dx = xs[c] - xs[a];
                float dy = ys[c] - ys[a];
                float len = (float) Math.sqrt(dx * dx + dy * dy);
                float nx = -dy / len * 0.015F;
                float ny = dx / len * 0.015F;
                b.pos(xs[a] - nx, ys[a] - ny, 0.001).color(r,g,blue,1.0F).endVertex();
                b.pos(xs[c] - nx, ys[c] - ny, 0.001).color(r,g,blue,1.0F).endVertex();
                b.pos(xs[c] + nx, ys[c] + ny, 0.001).color(r,g,blue,1.0F).endVertex();
                b.pos(xs[a] + nx, ys[a] + ny, 0.001).color(r,g,blue,1.0F).endVertex();
            }
            Tessellator.getInstance().draw();
        } finally {
            GL11.glPopMatrix();
        }
    }

    private void drawRays(float time) {
        GL11.glPushMatrix();
        try {
            GL11.glTranslatef(0.5F, 0.5F, 0.1F);
            GL11.glRotatef(time * 8.0F, 0.0F, 0.0F, 1.0F);
            BufferBuilder b = Tessellator.getInstance().getBuffer();
            b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            for (float[] ray : RAYS) {
                double angle = Math.toRadians(ray[0]);
                float pulse = 0.55F + 0.45F * (float) Math.sin(time * ray[5] + ray[4]);
                float length = ray[1] * pulse;
                float width = ray[2];
                int color = java.awt.Color.HSBtoRGB((ray[3] + time * 0.08F) % 1.0F, 1.0F, 1.0F);
                int r = (color >> 16) & 255, g = (color >> 8) & 255, blue = color & 255;
                int alpha = (int) (220.0F * pulse);
                float cos = (float) Math.cos(angle);
                float sin = (float) Math.sin(angle);
                float nx = -sin * width, ny = cos * width;
                float sx = cos * 0.015F, sy = sin * 0.015F;
                float tx = cos * length, ty = sin * length;
                b.pos(sx - nx, sy - ny, 0).color(r,g,blue,alpha).endVertex();
                b.pos(tx - nx * 0.12F, ty - ny * 0.12F, 0).color(r,g,blue,0).endVertex();
                b.pos(tx + nx * 0.12F, ty + ny * 0.12F, 0).color(r,g,blue,0).endVertex();
                b.pos(sx + nx, sy + ny, 0).color(r,g,blue,alpha).endVertex();
            }
            Tessellator.getInstance().draw();
        } finally {
            GL11.glPopMatrix();
        }
    }
}
