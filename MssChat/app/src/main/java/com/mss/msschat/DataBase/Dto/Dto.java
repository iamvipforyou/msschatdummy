package com.mss.msschat.DataBase.Dto;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Column {
	String name();
	String type();
}

public abstract class Dto implements Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

/*	@Column(name = "id", type = "INTEGER PRIMARY KEY AUTOINCREMENT")
	protected String			id					= null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}*/

	/**
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * 
	 */

	public void prepareDbFields(List<String> names, List<Object> values) throws Exception {
		try {
			for (Field f : Dto.class.getDeclaredFields()) {
				Column column = f.getAnnotation(Column.class);
				if (column != null) {
					names.add(column.name());
					Object value = f.get(this);
					if (value != null) {
						if (value instanceof String) {
							// Make sure to escape the '
							String text = ((String) value).replace("'", "''");
							values.add("'" + text + "'");
						} else {
							values.add(value);
						}
					} else {
						values.add("NULL");
					}
				}
			}
		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		for (Field f : this.getClass().getDeclaredFields()) {
			Column column = f.getAnnotation(Column.class);
			if (column != null) {
				names.add(column.name());
				String fieldName = f.getName();
				
				
				String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				Method getter = null;
				
				
				try {
					getter = this.getClass().getMethod(methodName);
				} catch (NoSuchMethodException e) {
					
					methodName = "is" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
					getter = this.getClass().getMethod(methodName);
				}
				Object value = getter.invoke(this);
				if (value != null) {
					if (value instanceof String) {
						// Make sure to escape the '
						String text = ((String) value).replace("'", "''");
						values.add("'" + text + "'");
					} else {
						values.add(value);
					}
				} else {
					values.add("NULL");
				}
			}
		}
	}

	void getFields() {
	}
	// abstract  public String getIdName();
	// abstract  public String getIdValue();
}
