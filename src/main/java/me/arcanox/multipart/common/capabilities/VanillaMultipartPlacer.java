package me.arcanox.multipart.common.capabilities;

import me.arcanox.multipart.api.common.capabilities.multipart.ICanPlaceMultipart;
import me.arcanox.multipart.common.blocks.Blocks;
import me.arcanox.multipart.util.ArrayUtil;
import me.arcanox.multipart.util.MinecraftUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.Constants;

public class VanillaMultipartPlacer implements ICanPlaceMultipart {
	@Override
	public boolean canPlaceMultipart( IWorldReader world, PlayerEntity player, ItemStack itemStack, BlockPos pos, Direction clickedFace ) {
		BlockState state = world.getBlockState( pos );
		Block      block = state.getBlock();
		Item       item  = itemStack.getItem();
		
		return ArrayUtil.includes( VanillaMultipartHandler.MULTIPART_COMPATIBLE_ITEMS, item ) &&
		       ( block == Blocks.MULTIPART_CONTAINER.get() || ArrayUtil.includes( VanillaMultipartHandler.MULTIPART_COMPATIBLE_BLOCKS, block ) );
	}
	
	@Override
	public void placeMultipart( IWorld world, PlayerEntity player, ItemStack itemStack, BlockPos pos, Direction clickedFace ) {
		// TODO:
	}
}
