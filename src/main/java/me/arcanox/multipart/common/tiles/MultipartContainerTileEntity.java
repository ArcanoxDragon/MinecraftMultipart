package me.arcanox.multipart.common.tiles;

import me.arcanox.multipart.api.BlockPart;
import me.arcanox.multipart.api.IBlockPart;
import me.arcanox.multipart.util.Log;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class MultipartContainerTileEntity extends TileEntity {
	private final ArrayList<Integer>           occupiedSlots = new ArrayList<>();
	private final HashMap<Integer, IBlockPart> slotParts     = new HashMap<>();
	
	public MultipartContainerTileEntity() {
		super( TileEntities.MULTIPART_CONTAINER.get() );
	}
	
	public List<Integer> getOccupiedSlots() {
		return Collections.unmodifiableList( this.occupiedSlots );
	}
	
	public IBlockPart getPart( int slot ) {
		if ( !this.occupiedSlots.contains( slot ) )
			return null;
		
		return this.slotParts.get( slot );
	}
	
	public BlockState getBlockState( int slot ) {
		IBlockPart blockPart = this.getPart( slot );
		
		return blockPart == null ? null : blockPart.getBlockState();
	}
	
	public TileEntity getTileEntity( int slot ) {
		IBlockPart blockPart = this.getPart( slot );
		
		return blockPart == null ? null : blockPart.getTileEntity();
	}
	
	public void insertBlockState( BlockState blockState ) {
		int        maxSlot  = this.occupiedSlots.stream().max( Integer::compareTo ).orElse( -1 );
		int        nextSlot = maxSlot + 1; // TODO: Configurable max-slots-per-block?
		IBlockPart part;
		
		if ( blockState.hasTileEntity() ) {
			TileEntity tileEntity = blockState.createTileEntity( this.world ); // TODO: Might need world wrapper that protects access to this container's BlockState?
			
			tileEntity.onLoad();
			part = new BlockPart( blockState, tileEntity );
		} else {
			part = new BlockPart( blockState );
		}
		
		this.occupiedSlots.add( nextSlot );
		this.slotParts.put( nextSlot, part );
	}
	
	public void removeBlockState( int slot ) {
		if ( !this.occupiedSlots.contains( slot ) )
			return;
		
		this.slotParts.remove( slot );
		this.occupiedSlots.remove( slot );
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability( @Nonnull Capability<T> cap, @Nullable Direction side ) {
		// TODO: Maybe provide a way of prioritizing this in case multiple slotted TEs provide the same capability?
		for ( IBlockPart blockPart : this.slotParts.values() ) {
			TileEntity tileEntity = blockPart.getTileEntity();
			
			if ( tileEntity == null )
				continue;
			
			LazyOptional<T> tileEntityCapability = tileEntity.getCapability( cap, side );
			
			if ( tileEntityCapability.isPresent() )
				return tileEntityCapability.cast();
		}
		
		return super.getCapability( cap, side );
	}
	
	@Nonnull
	@Override
	public CompoundNBT write( @Nonnull CompoundNBT compound ) {
		super.write( compound );
		
		ListNBT occupiedSlotNBTs = new ListNBT();
		ListNBT blockPartNBTs    = new ListNBT();
		
		for ( int slot : this.occupiedSlots ) {
			IBlockPart  blockPart    = this.slotParts.get( slot );
			CompoundNBT partCompound = new CompoundNBT();
			
			partCompound.putInt( "Slot", slot );
			occupiedSlotNBTs.add( IntNBT.valueOf( slot ) );
			blockPartNBTs.add( blockPart.write( partCompound ) );
		}
		
		compound.put( "OccupiedSlots", occupiedSlotNBTs );
		compound.put( "BlockParts", blockPartNBTs );
		
		return compound;
	}
	
	@Override
	public void read( @Nonnull BlockState state, @Nonnull CompoundNBT nbt ) {
		super.read( state, nbt );
		
		ListNBT occupiedSlotNBTs = nbt.getList( "OccupiedSlots", Constants.NBT.TAG_INT );
		ListNBT blockPartNBTs    = nbt.getList( "BlockParts", Constants.NBT.TAG_COMPOUND );
		
		this.occupiedSlots.clear();
		this.slotParts.clear();
		
		for ( INBT slotNBT : occupiedSlotNBTs ) {
			if ( !( slotNBT instanceof IntNBT ) )
				continue;
			
			this.occupiedSlots.add( ( (IntNBT) slotNBT ).getInt() );
		}
		
		for ( INBT blockPartNBT : blockPartNBTs ) {
			if ( !( blockPartNBT instanceof CompoundNBT ) )
				continue;
			
			CompoundNBT partCompound = (CompoundNBT) blockPartNBT;
			IBlockPart  blockPart    = new BlockPart( partCompound );
			int         slot         = partCompound.getInt( "Slot" );
			
			if ( !this.occupiedSlots.contains( slot ) ) {
				Log.warn( String.format( "Warning: Multipart Container at %s just tried to deserialize a part for non-populated slot %d", this.pos, slot ) );
				continue;
			}
			
			this.slotParts.put( slot, blockPart );
		}
		
		// Iterate through a copy so we can safely remove slots inside the loop
		for ( int slot : new ArrayList<>( this.occupiedSlots ) ) {
			if ( !this.slotParts.containsKey( slot ) ) {
				Log.warn( String.format( "Warning: Multipart Container at %s should have had slot %d populated but no part was found", this.pos, slot ) );
				this.occupiedSlots.remove( slot );
			}
		}
	}
}
