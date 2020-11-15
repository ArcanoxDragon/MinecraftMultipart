package me.arcanox.multipart.common.blocks;

import me.arcanox.multipart.common.tiles.MultipartContainerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class MultipartContainerBlock extends Block {
	public MultipartContainerBlock( Properties properties ) {
		super( properties );
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
