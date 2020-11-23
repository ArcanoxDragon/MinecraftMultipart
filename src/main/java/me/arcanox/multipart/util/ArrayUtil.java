package me.arcanox.multipart.util;

public class ArrayUtil {
	public static <T> boolean includes( T[] array, T item ) {
		for ( T t : array ) {
			if ( t == item ) {
				return true;
			}
		}
		
		return false;
	}
}
