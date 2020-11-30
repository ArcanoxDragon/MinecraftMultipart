package me.arcanox.multipart.common.capabilities;

import me.arcanox.multipart.api.common.capabilities.multipart.ICanPlaceMultipart;
import me.arcanox.multipart.util.ArrayUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * This class is responsible for handling AttachCapabilitiesEvents for Vanilla content and attaching the appropriate
 * multipart capabilities to said content, as well as delegating item right-click events to the vanilla placer
 */
public class VanillaMultipartHandler {
	public static final Item[] MULTIPART_COMPATIBLE_ITEMS = {
		Items.TORCH,
		Items.REDSTONE_TORCH,
		Items.SOUL_TORCH
	};
	
	public static final Block[] MULTIPART_COMPATIBLE_BLOCKS = {
		Blocks.TORCH,
		Blocks.WALL_TORCH,
		Blocks.REDSTONE_TORCH,
		Blocks.REDSTONE_WALL_TORCH,
		Blocks.SOUL_TORCH,
		Blocks.SOUL_WALL_TORCH
	};
	
	@SubscribeEvent
	public void onItemStackConstructing( AttachCapabilitiesEvent<ItemStack> event ) {
		ItemStack itemStack = event.getObject();
		
		if ( !ArrayUtil.includes( MULTIPART_COMPATIBLE_ITEMS, itemStack.getItem() ) )
			return;
		
		if ( !itemStack.getCapability( Capabilities.MULTIPART_PLACER ).isPresent() ) {
			event.addCapability( VanillaMultipartProvider.PROVIDER_KEY, new VanillaMultipartProvider() );
		}
	}
	
	@SubscribeEvent
	public void onPlayerRightClickBlock( PlayerInteractEvent.RightClickBlock event ) {
		World        world     = event.getWorld();
		PlayerEntity player    = event.getPlayer();
		ItemStack    itemStack = event.getItemStack();
		Hand         hand      = event.getHand();
		
		if ( itemStack == null || itemStack.isEmpty() )
			return;
		
		LazyOptional<ICanPlaceMultipart> placerCapability = itemStack.getCapability( Capabilities.MULTIPART_PLACER );
		
		placerCapability.ifPresent( placer -> {
			if ( !( placer instanceof VanillaMultipartPlacer ) )
				return;
			
			// Figure out the block *into* which we're placing
			Direction clickedFace = event.getFace();
			BlockPos  clickedPos  = event.getPos();
			
			if ( placer.canPlaceMultipart( world, player, itemStack, hand, clickedPos, clickedFace ) ) {
				placer.placeMultipart( world, player, itemStack, hand, clickedPos, clickedFace );
				event.setCancellationResult( ActionResultType.SUCCESS );
				event.setCanceled( true );
			}
		} );
	}
}
