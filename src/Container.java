/**
 * Container.java 
 * 
 * Used as an indirect reference to a T; reassignments to val will be consistent among
 * all references to the Container.
 * 
 * Author: Wesley Gydé
 */

public class Container<T>{
	public T val;
	
	public Container(T t){
		val = t;
	}

}
