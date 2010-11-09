package net.libreworks.stellarbase.orm.dao;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;

import net.libreworks.stellarbase.orm.model.Removable;

public class Person implements Removable<Long>
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	private String firstName;
	private Character mi;
	private String lastName;
	private String suffix;
	private Date birthday;
	private String bio;
	private Boolean admin;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Date removedOn;
	private String removedBy;
	private Integer version;

	/**
	 * @return the admin
	 */
	public Boolean getAdmin()
	{
		return admin;
	}

	public String getFullName()
	{
		return new StringBuilder()
			.append(firstName)
			.append(' ')
			.append(mi)
			.append(". ")
			.append(lastName).toString();
	}
	
	/**
	 * @return the bio
	 */
	public String getBio()
	{
		return bio;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday()
	{
		return birthday;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn()
	{
		return createdOn;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @return the mi
	 */
	public Character getMi()
	{
		return mi;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy()
	{
		return modifiedBy;
	}

	/**
	 * @return the modifiedOn
	 */
	public Date getModifiedOn()
	{
		return modifiedOn;
	}

	/**
	 * @return the removedBy
	 */
	public String getRemovedBy()
	{
		return removedBy;
	}

	/**
	 * @return the removedOn
	 */
	public Date getRemovedOn()
	{
		return removedOn;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix()
	{
		return suffix;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion()
	{
		return version;
	}

	public boolean isRemoved()
	{
		return removedOn != null;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(Boolean admin)
	{
		this.admin = admin;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(String bio)
	{
		this.bio = bio;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @param mi
	 *            the mi to set
	 */
	public void setMi(Character mi)
	{
		this.mi = mi;
	}

	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy)
	{
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @param modifiedOn
	 *            the modifiedOn to set
	 */
	public void setModifiedOn(Date modifiedOn)
	{
		this.modifiedOn = modifiedOn;
	}

	/**
	 * @param removedBy
	 *            the removedBy to set
	 */
	public void setRemovedBy(String removedBy)
	{
		this.removedBy = removedBy;
	}

	/**
	 * @param removedOn
	 *            the removedOn to set
	 */
	public void setRemovedOn(Date removedOn)
	{
		this.removedOn = removedOn;
	}

	/**
	 * @param suffix
	 *            the suffix to set
	 */
	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public void setVersion(Serializable version)
	{
		this.version = version instanceof Integer ?
				(Integer)version : Integer.valueOf(ObjectUtils.toString(version));
	}
}
