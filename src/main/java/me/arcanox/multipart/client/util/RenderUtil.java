package me.arcanox.multipart.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.List;
import java.util.Random;

public class RenderUtil {
	public static void renderSimpleModel( IBakedModel model, IVertexBuilder vertexBuilder, MatrixStack matrixStack,
	                                      BlockState blockState, Random random, int combinedLight, int combinedOverlay ) {
		List<BakedQuad> quads = model.getQuads( blockState, null, random, EmptyModelData.INSTANCE );
		
		for ( BakedQuad quad : quads ) {
			vertexBuilder.addVertexData( matrixStack.getLast(), quad, 1f, 1f, 1f, combinedLight, combinedOverlay, false );
		}
	}
	
	/**
	 * Functionally equivalent to the private WorldRenderer.drawShape method
	 */
	public static void drawShape( MatrixStack matrixStack, IVertexBuilder buffer, VoxelShape shape, double x, double y, double z, float red, float green, float blue, float alpha ) {
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		
		shape.forEachEdge( ( x1, y1, z1, x2, y2, z2 ) -> {
			buffer.pos( matrix, (float) ( x + x1 ), (float) ( y + y1 ), (float) ( z + z1 ) ).color( red, green, blue, alpha ).endVertex();
			buffer.pos( matrix, (float) ( x + x2 ), (float) ( y + y2 ), (float) ( z + z2 ) ).color( red, green, blue, alpha ).endVertex();
		} );
	}
}
