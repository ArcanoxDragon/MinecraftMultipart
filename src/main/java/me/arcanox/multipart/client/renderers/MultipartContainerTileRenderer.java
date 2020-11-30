package me.arcanox.multipart.client.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.arcanox.multipart.api.IBlockPart;
import me.arcanox.multipart.client.util.RenderUtil;
import me.arcanox.multipart.common.tiles.MultipartContainerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MultipartContainerTileRenderer extends TileEntityRenderer<MultipartContainerTileEntity> {
	private final BlockRendererDispatcher blockRendererDispatcher;
	
	public MultipartContainerTileRenderer( TileEntityRendererDispatcher rendererDispatcherIn ) {
		super( rendererDispatcherIn );
		
		this.blockRendererDispatcher = Minecraft.getInstance().getBlockRendererDispatcher(); // Constructed in Minecraft(...) from main method, safe to cache here
	}
	
	@Override
	public void render( @Nonnull MultipartContainerTileEntity tileEntity, float partialTicks, @Nonnull MatrixStack matrixStack,
	                    @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay ) {
		World world = tileEntity.getWorld();
		
		if ( world == null || tileEntity.isEmpty() )
			return;
		
		for ( IBlockPart part : tileEntity.getParts() ) {
			BlockState     blockState    = part.getBlockState();
			IBakedModel    partModel     = this.blockRendererDispatcher.getModelForState( blockState );
			IVertexBuilder vertexBuilder = buffer.getBuffer( RenderType.getCutout() );
			
			RenderUtil.renderSimpleModel( partModel, vertexBuilder, matrixStack, blockState, world.getRandom(), combinedLight, combinedOverlay );
		}
	}
}
