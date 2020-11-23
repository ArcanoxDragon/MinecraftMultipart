package me.arcanox.multipart.api.common.capabilities.multipart;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public interface ICanPlaceMultipart {
	boolean canPlaceMultipart( IWorldReader world, PlayerEntity player, ItemStack itemStack, BlockPos pos, Direction clickedFace );
	void placeMultipart( IWorld world, PlayerEntity player, ItemStack itemStack, BlockPos pos, Direction clickedFace );
}
