package me.arcanox.multipart.common.capabilities;

import me.arcanox.multipart.api.common.capabilities.multipart.ICanPlaceMultipart;
import me.arcanox.multipart.api.util.ShapeHelper;
import me.arcanox.multipart.common.blocks.Blocks;
import me.arcanox.multipart.common.tiles.MultipartContainerTileEntity;
import me.arcanox.multipart.util.ArrayUtil;
import me.arcanox.multipart.util.Log;
import me.arcanox.multipart.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class VanillaMultipartPlacer implements ICanPlaceMultipart {
	@Override
	public boolean canPlaceMultipart( IWorldReader world, PlayerEntity player, ItemStack itemStack, Hand hand, BlockPos pos, Direction clickedFace ) {
		BlockPos   clickedIntoPos     = pos.offset( clickedFace );
		BlockState clickedIntoState   = world.getBlockState( clickedIntoPos );
		Block      clickedIntoBlock   = clickedIntoState.getBlock();
		Item       item               = itemStack.getItem();
		boolean    itemSupported      = ArrayUtil.includes( VanillaMultipartHandler.MULTIPART_COMPATIBLE_ITEMS, item );
		boolean    blockSupported     = ArrayUtil.includes( VanillaMultipartHandler.MULTIPART_COMPATIBLE_BLOCKS, clickedIntoBlock );
		boolean    isAlreadyMultipart = clickedIntoBlock == Blocks.MULTIPART_CONTAINER.get();
		
		if ( !( itemSupported && ( blockSupported || isAlreadyMultipart ) ) )
			return false;
		
		// Check shape compatibility
		BlockState placingState = MinecraftUtil.getBlockStateForPlacement( player, itemStack, hand );
		
		if ( placingState == null )
			return false;
		
		VoxelShape placingShape = placingState.getShape( world, clickedIntoPos );
		
		if ( isAlreadyMultipart ) {
			MultipartContainerTileEntity container = Blocks.MULTIPART_CONTAINER.get().getContainerSafe( world, clickedIntoPos );
			
			if ( container == null )
				return false;
			
			VoxelShape[] existingShapes = container.getParts().stream().map( part -> part.getBlockState().getShape( world, clickedIntoPos ) ).toArray( VoxelShape[]::new );
			
			return ShapeHelper.canShapesCoexist( placingShape, existingShapes );
		} else {
			VoxelShape existingShape = clickedIntoState.getShape( world, clickedIntoPos );
			
			return ShapeHelper.canShapesCoexist( placingShape, existingShape );
		}
	}
	
	@Override
	public void placeMultipart( World world, PlayerEntity player, ItemStack itemStack, Hand hand, BlockPos pos, Direction clickedFace ) {
		BlockPos   clickedIntoPos        = pos.offset( clickedFace );
		BlockState clickedIntoState      = world.getBlockState( clickedIntoPos );
		TileEntity clickedIntoTileEntity = world.getTileEntity( clickedIntoPos );
		
		if ( itemStack.getItem() instanceof BlockItem ) {
			BlockState placingState         = MinecraftUtil.getBlockStateForPlacement( player, itemStack, hand );
			boolean    convertedToMultipart = false;
			
			if ( !clickedIntoState.isIn( Blocks.MULTIPART_CONTAINER.get() ) ) {
				// Change tile to multipart
				world.setBlockState( clickedIntoPos, Blocks.MULTIPART_CONTAINER.get().getDefaultState(), Constants.BlockFlags.DEFAULT );
				convertedToMultipart = true;
			}
			
			// Validate that the MCTE was created (if not, something horrible went wrong)
			if ( !( world.getTileEntity( clickedIntoPos ) instanceof MultipartContainerTileEntity ) ) {
				Log.warn( String.format( "Missing MultipartContainerTileEntity at %s!", clickedIntoPos ) );
				return;
			}
			
			MultipartContainerTileEntity container = (MultipartContainerTileEntity) world.getTileEntity( clickedIntoPos );
			
			if ( convertedToMultipart ) {
				// Place existing BlockState and TileEntity (if present) into the container along with the new BlockState
				
				container.insertBlockState( clickedIntoState, clickedIntoTileEntity );
			}
			
			container.insertBlockState( placingState );
			placingState.onBlockAdded( world, pos, net.minecraft.block.Blocks.AIR.getDefaultState(), false );
			MinecraftUtil.playBlockPlacedSound( world, player, clickedIntoPos, placingState );
		}
	}
}
