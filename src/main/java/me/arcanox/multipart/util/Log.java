package me.arcanox.multipart.util;

import me.arcanox.multipart.MultipartMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Log {
	private static final Marker MARKER = MarkerManager.getMarker( MultipartMod.MOD_ID.toUpperCase() );
	private static       Logger LOGGER;
	
	static void init() {
		LOGGER = LogManager.getLogger();
	}
	
	static String format( Object message ) {
		return String.format( "[%s] %s", MultipartMod.MOD_NAME, message );
	}
	
	private static Logger getLoggerSafe() {
		return LOGGER == null ? LogManager.getLogger() : LOGGER;
	}
	
	public static void debug( Object message ) { getLoggerSafe().debug( MARKER, message );}
	
	public static void info( Object message )  { getLoggerSafe().info( MARKER, message );}
	
	public static void warn( Object message )  { getLoggerSafe().warn( MARKER, message );}
	
	public static void error( Object message ) { getLoggerSafe().error( MARKER, message );}
	
	public static void fatal( Object message ) { getLoggerSafe().fatal( MARKER, message );}
}
