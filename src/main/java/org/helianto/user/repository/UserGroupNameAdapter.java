package org.helianto.user.repository;

import java.io.Serializable;

/**
 * Simple user group name adapter.
 * 
 * @author mauriciofernandesdecastro
 */
public class UserGroupNameAdapter 
	implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String userName;
	
	private boolean checked = false;
	
	/**
	 * Empty constructor.
	 */
	public UserGroupNameAdapter() {
		super();
	}

	/**
	 * Full constructor.
	 * 
	 * @param id
	 * @param userName
	 * @param checked
	 */
	public UserGroupNameAdapter(int id, String userName, boolean checked) {
		this();
		this.id = id;
		this.userName = userName;
		this.checked = checked;
	}

	/**
	 * Full constructor.
	 * 
	 * @param id
	 * @param userName
	 * @param numChecked
	 */
	public UserGroupNameAdapter(int id, String userName, int numChecked) {
		this(id, userName, (numChecked==1));
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
