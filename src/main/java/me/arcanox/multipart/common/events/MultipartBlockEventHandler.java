package me.arcanox.multipart.common.events;

import me.arcanox.multipart.common.blocks.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MultipartBlockEventHandler {
	@SubscribeEvent
	public void onBlockBreaking( BlockEvent.BreakEvent event ) {
		if ( !event.getState().isIn( Blocks.MULTIPART_CONTAINER.get() ) )
			return;
		
		
	}
}
