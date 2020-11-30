package me.arcanox.multipart.api.common.capabilities.multipart;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public interface ICanPlaceMultipart {
	boolean canPlaceMultipart( IWorldReader world, PlayerEntity player, ItemStack itemStack, Hand hand, BlockPos pos, Direction clickedFace );
	void placeMultipart( World world, PlayerEntity player, ItemStack itemStack, Hand hand, BlockPos pos, Direction clickedFace );
}
