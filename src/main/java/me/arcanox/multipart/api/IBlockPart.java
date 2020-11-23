package me.arcanox.multipart.api;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public interface IBlockPart {
	BlockState getBlockState();
	TileEntity getTileEntity();
	CompoundNBT write( CompoundNBT nbt );
	void read( CompoundNBT nbt );
}
