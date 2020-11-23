package me.arcanox.multipart.api;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

public class BlockPart implements IBlockPart {
	private BlockState blockState;
	private TileEntity tileEntity;
	
	public BlockPart( @Nonnull BlockState blockState, TileEntity tileEntity ) {
		this.blockState = blockState;
		this.tileEntity = tileEntity;
	}
	
	public BlockPart( @Nonnull BlockState blockState ) {
		this( blockState, null );
	}
	
	public BlockPart( @Nonnull CompoundNBT nbt ) {
		this.read( nbt );
	}
	
	@Override
	@Nonnull
	public BlockState getBlockState() {
		return this.blockState;
	}
	
	public void setBlockState( @Nonnull BlockState blockState ) {
		this.blockState = blockState;
	}
	
	@Override
	public TileEntity getTileEntity() {
		return this.tileEntity;
	}
	
	public void setTileEntity( TileEntity tileEntity ) {
		this.tileEntity = tileEntity;
	}
	
	@Override
	public CompoundNBT write( CompoundNBT nbt ) {
		nbt.put( "BlockState", NBTUtil.writeBlockState( this.getBlockState() ) );
		
		if ( this.getTileEntity() != null ) {
			nbt.put( "TileEntity", this.getTileEntity().write( new CompoundNBT() ) );
		}
		
		return nbt;
	}
	
	@Override
	public void read( CompoundNBT nbt ) {
		this.setBlockState( NBTUtil.readBlockState( nbt.getCompound( "BlockState" ) ) );
		
		if ( this.getTileEntity() != null ) {
			this.getTileEntity().read( this.getBlockState(), nbt.getCompound( "TileEntity" ) );
		}
	}
}
