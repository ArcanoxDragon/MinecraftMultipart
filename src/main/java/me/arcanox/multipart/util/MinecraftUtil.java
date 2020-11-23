package me.arcanox.multipart.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class MinecraftUtil {
	public static void playBlockPlacedSound( IWorld world, PlayerEntity player, BlockPos blockPos, BlockState blockState ) {
		SoundType blockSound = blockState.getSoundType( world, blockPos, player );
		
		world.playSound( player, blockPos, blockSound.getPlaceSound(), SoundCategory.BLOCKS, ( blockSound.getVolume() + 1f ) / 2f, blockSound.getPitch() * 0.8f );
	}
}
