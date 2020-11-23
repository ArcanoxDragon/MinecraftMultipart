package me.arcanox.multipart.common.capabilities;

import me.arcanox.multipart.api.common.capabilities.multipart.ICanPlaceMultipart;
import me.arcanox.multipart.api.common.capabilities.multipart.IMultipart;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
	@CapabilityInject( IMultipart.class )
	public static Capability<IMultipart> MULTIPART;
	
	@CapabilityInject( ICanPlaceMultipart.class )
	public static Capability<ICanPlaceMultipart> MULTIPART_PLACER;
	
	public static void register() {
		CapabilityManager.INSTANCE.register( IMultipart.class, new DummyStorage<>(), Capabilities::noDefaultImplementationFactory );
		CapabilityManager.INSTANCE.register( ICanPlaceMultipart.class, new DummyStorage<>(), Capabilities::noDefaultImplementationFactory );
	}
	
	private static <T> T noDefaultImplementationFactory() {
		throw new UnsupportedOperationException( "No default implementation is available for this capability type" );
	}
}
