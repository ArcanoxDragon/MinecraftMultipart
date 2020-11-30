package me.arcanox.multipart.common.blocks;

import me.arcanox.multipart.api.IBlockPart;
import me.arcanox.multipart.common.tiles.MultipartContainerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;

public class MultipartContainerBlock extends Block {
	public MultipartContainerBlock( Properties properties ) {
		super( properties );
	}
	
	@Nonnull
	@Override
	public BlockRenderType getRenderType( @Nonnull BlockState state ) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	public MultipartContainerTileEntity getContainerSafe( IBlockReader world, @Nonnull BlockPos pos ) {
		if ( world == null || !( world.getTileEntity( pos ) instanceof MultipartContainerTileEntity ) )
			return null;
		
		return (MultipartContainerTileEntity) world.getTileEntity( pos );
	}
	
	private VoxelShape getCombinedShape( @Nonnull BlockState state, IBlockReader worldIn, @Nonnull BlockPos pos, Function<IBlockPart, VoxelShape> shapeProvider, VoxelShape defaultShape ) {
		MultipartContainerTileEntity container = this.getContainerSafe( worldIn, pos );
		
		if ( container == null || container.isEmpty() )
			return defaultShape;
		
		VoxelShape currentShape = VoxelShapes.empty();
		
		for ( IBlockPart part : container.getParts() ) {
			VoxelShape partShape = shapeProvider.apply( part );
			
			if ( !partShape.isEmpty() )
				currentShape = VoxelShapes.combine( currentShape, partShape, IBooleanFunction.OR );
		}
		
		return currentShape;
	}
	
	public IBlockPart getPartBeingLookedAt( IBlockReader world, BlockPos pos, PlayerEntity player ) {
		MultipartContainerTileEntity container = this.getContainerSafe( world, pos );
		
		if ( container == null || container.isEmpty() )
			return null;
		
		// Prepare to raytrace each part shape
		double   blockReachDistance = player.getAttribute( ForgeMod.REACH_DISTANCE.get() ).getValue();
		Vector3d rayTraceStart      = player.getEyePosition( 0f );
		Vector3d rayTraceTowards    = player.getLook( 0f );
		Vector3d rayTraceEnd        = rayTraceStart.add( rayTraceTowards.x * blockReachDistance, rayTraceTowards.y * blockReachDistance, rayTraceTowards.z * blockReachDistance );
		
		for ( IBlockPart part : container.getParts() ) {
			VoxelShape          partShape          = part.getBlockState().getShape( world, pos );
			BlockRayTraceResult partRayTraceResult = partShape.rayTrace( rayTraceStart, rayTraceEnd, pos );
			
			if ( partRayTraceResult != null ) {
				return part;
			}
		}
		
		return null;
	}
	
	private VoxelShape getCombinedShape( @Nonnull BlockState state, IBlockReader worldIn, @Nonnull BlockPos pos, Function<IBlockPart, VoxelShape> shapeProvider ) {
		return this.getCombinedShape( state, worldIn, pos, shapeProvider, VoxelShapes.fullCube() );
	}
	
	@Nonnull
	@Override
	public VoxelShape getShape( @Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context ) {
		return getCombinedShape( state, worldIn, pos, part -> part.getBlockState().getShape( worldIn, pos, context ) );
	}
	
	@Nonnull
	@Override
	public VoxelShape getRayTraceShape( @Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context ) {
		return getCombinedShape( state, reader, pos, part -> part.getBlockState().getRayTraceShape( reader, pos ) );
	}
	
	@Nonnull
	@Override
	public VoxelShape getCollisionShape( @Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context ) {
		return getCombinedShape( state, worldIn, pos, part -> part.getBlockState().getCollisionShape( worldIn, pos, context ), VoxelShapes.empty() );
	}
	
	@Override
	public int getLightValue( BlockState state, IBlockReader world, BlockPos pos ) {
		MultipartContainerTileEntity container = this.getContainerSafe( world, pos );
		
		if ( container == null || container.isEmpty() )
			return world == null ? 15 : world.getMaxLightLevel();
		
		int sum = 0;
		int max = 0;
		
		for ( IBlockPart part : container.getParts() ) {
			int lightValue = part.getBlockState().getLightValue( world, pos );
			
			sum = Math.min( world.getMaxLightLevel(), sum + lightValue );
			max = Math.max( max, lightValue );
		}
		
		// Return the average between the sum and the max
		return (int) ( ( sum + max ) / 2f );
	}
	
	@Override
	public int getOpacity( @Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos ) {
		MultipartContainerTileEntity container = this.getContainerSafe( worldIn, pos );
		
		if ( container == null || container.isEmpty() )
			return 0;
		
		int max = 0;
		
		for ( IBlockPart part : container.getParts() ) {
			int lightValue = part.getBlockState().getLightValue( worldIn, pos );
			
			max = Math.max( max, lightValue );
		}
		
		return max;
	}
	
	@Override
	public boolean removedByPlayer( BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid ) {
		MultipartContainerTileEntity container   = this.getContainerSafe( world, pos );
		IBlockPart                   removedPart = this.getPartBeingLookedAt( world, pos, player );
		
		if ( container == null || container.isEmpty() || removedPart == null )
			return super.removedByPlayer( state, world, pos, player, willHarvest, fluid );
		
		BlockState partState = removedPart.getBlockState();
		
		partState.getBlock().onBlockHarvested( world, pos, partState, player );
		container.removePart( removedPart );
		
		boolean removed = false;
		
		if ( container.getOccupiedSlots().size() == 1 ) {
			int        lastRemainingSlot = container.getOccupiedSlots().get( 0 );
			IBlockPart lastRemainingPart = container.getPart( lastRemainingSlot );
			
			container.removePart( lastRemainingPart );
			
			world.setBlockState( pos, lastRemainingPart.getBlockState(), Constants.BlockFlags.DEFAULT );
			world.setTileEntity( pos, lastRemainingPart.getTileEntity() );
			
			removed = true;
		} else if ( container.isEmpty() ) {
			world.setBlockState( pos, fluid.getBlockState() );
			removed = true;
		}
		
		return removed;
	}
	
	@Override
	@OnlyIn( Dist.CLIENT )
	public void animateTick( @Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand ) {
		MultipartContainerTileEntity container = this.getContainerSafe( world, pos );
		
		if ( container == null || container.isEmpty() )
			return;
		
		container.forEachPart( part -> {
			BlockState partState = part.getBlockState();
			
			partState.getBlock().animateTick( partState, world, pos, rand );
		} );
	}
	
	@Override
	public boolean hasTileEntity( BlockState state ) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
		return new MultipartContainerTileEntity();
	}
}
