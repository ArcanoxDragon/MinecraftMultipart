package me.arcanox.multipart.client.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.arcanox.multipart.api.IBlockPart;
import me.arcanox.multipart.client.util.RenderUtil;
import me.arcanox.multipart.common.blocks.Blocks;
import me.arcanox.multipart.common.tiles.MultipartContainerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MultipartBlockSelectionRenderer {
	private final Minecraft mc;
	
	public MultipartBlockSelectionRenderer() {
		this.mc = Minecraft.getInstance();
	}
	
	@SubscribeEvent
	public void onHighlightBlock( DrawHighlightEvent.HighlightBlock event ) {
		final World               world          = this.mc.world;
		final PlayerEntity        player         = this.mc.player;
		final BlockRayTraceResult rayTraceResult = event.getTarget();
		final BlockPos            pos            = rayTraceResult.getPos();
		final BlockState          blockState     = world.getBlockState( pos );
		
		if ( !blockState.isIn( Blocks.MULTIPART_CONTAINER.get() ) )
			return;
		
		MultipartContainerTileEntity container = Blocks.MULTIPART_CONTAINER.get().getContainerSafe( world, pos );
		
		if ( container == null || container.isEmpty() )
			return;
		
		// Cancel the event since we will draw the outline ourselves
		event.setCanceled( true );
		
		IBlockPart lookedAtPart = Blocks.MULTIPART_CONTAINER.get().getPartBeingLookedAt( world, pos, player );
		VoxelShape outlineShape = lookedAtPart == null ? VoxelShapes.empty() : lookedAtPart.getBlockState().getShape( world, pos );
		
		if ( !outlineShape.isEmpty() ) {
			// Draw the outline
			MatrixStack    matrixStack = event.getMatrix();
			IVertexBuilder buffer      = event.getBuffers().getBuffer( RenderType.LINES );
			Vector3d       view        = event.getInfo().getProjectedView();
			
			RenderUtil.drawShape( matrixStack, buffer, outlineShape, pos.getX() - view.getX(), pos.getY() - view.getY(), pos.getZ() - view.getZ(), 0f, 0f, 0f, 0.4f );
		}
	}
}
