package me.arcanox.multipart.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MinecraftUtil {
	private static final Method BlockItem$getStateForPlacement = ObfuscationReflectionHelper.findMethod( BlockItem.class, "func_195945_b", BlockItemUseContext.class );
	
	static {
		BlockItem$getStateForPlacement.setAccessible( true );
	}
	
	public static void playBlockPlacedSound( IWorld world, PlayerEntity player, BlockPos blockPos, BlockState blockState ) {
		SoundType blockSound = blockState.getSoundType( world, blockPos, player );
		
		world.playSound( player, blockPos, blockSound.getPlaceSound(), SoundCategory.BLOCKS, ( blockSound.getVolume() + 1f ) / 2f, blockSound.getPitch() * 0.8f );
	}
	
	/**
	 * Named version of ISelectionContext.func_216378_a which ignores the unused boolean parameter
	 */
	public static boolean isOnTopOf( ISelectionContext context, VoxelShape shape, BlockPos pos ) {
		return context.func_216378_a( shape, pos, false );
	}
	
	@Nullable
	public static BlockState getBlockStateForPlacement( @Nonnull PlayerEntity player, ItemStack itemStack, Hand hand ) {
		if ( !( itemStack.getItem() instanceof BlockItem ) )
			return null;
		
		BlockItem      blockItem      = (BlockItem) itemStack.getItem();
		double         reachDistance  = player.getAttribute( ForgeMod.REACH_DISTANCE.get() ).getValue();
		RayTraceResult rayTraceResult = player.pick( reachDistance, 0f, false );
		
		if ( !( rayTraceResult instanceof BlockRayTraceResult ) )
			return null;
		
		BlockItemUseContext useContext = new BlockItemUseContext( player, hand, itemStack, (BlockRayTraceResult) rayTraceResult );
		BlockState          stateForPlacement;
		
		try {
			Object stateForPlacementObj = BlockItem$getStateForPlacement.invoke( blockItem, useContext );
			
			stateForPlacement = stateForPlacementObj == null ? null : (BlockState) stateForPlacementObj;
		} catch ( Exception e ) {
			throw new RuntimeException( e );
		}
		
		return stateForPlacement;
	}
}
