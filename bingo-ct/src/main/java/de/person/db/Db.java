package de.person.db;

import java.lang.reflect.InvocationTargetException;

public interface Db<T> {
	final static String DB_FILENAME = "DB";
	
	/**
	 * Save all data.
	 * 
	 * @param object T
	 * @return boolean <code>true</code> iff successful.
	 */
	public boolean backup(final T object);
	
	/**
	 * Restore all Data.
	 * 
	 * @param classOfT Class<T>
	 * @return T Data. Iff no backup was found, new Instance of T.
	 */
	public T restore(final Class<T> classOfT);

	/**
	 * Since Java 8.
	 * 
	 * @param <T>
	 * @param classOfT
	 * @return
	 */
	default T createOne(Class<T> classOfT) {
		try {
			T created = classOfT.getDeclaredConstructor().newInstance();
			if (created instanceof HasSetup) {
				((HasSetup) created).setup();
			}
			return created;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}


}