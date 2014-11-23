package com.libreworks.stellarbase.orm.dao;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;

import com.libreworks.stellarbase.orm.model.Modifiable;

public class Post implements Modifiable<Long>, Comparable<Post>
{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Thread thread;
	private Person author;
	private String title;
	private String body;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Integer version;
	
	/**
	 * @return the author
	 */
	public Person getAuthor()
	{
		return author;
	}

	/**
	 * @return the body
	 */
	public String getBody()
	{
		return body;
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
	 * @return the id
	 */
	public Long getId()
	{
		return id;
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
	 * @return the thread
	 */
	public Thread getThread()
	{
		return thread;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion()
	{
		return version;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(Person author)
	{
		this.author = author;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body)
	{
		this.body = body;
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
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
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
	 * @param thread
	 *            the thread to set
	 */
	public void setThread(Thread thread)
	{
		this.thread = thread;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
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

	public int compareTo(Post o)
	{
		if ( this == o ) {
			return 0;
		}
		return getCreatedOn().compareTo(o.getCreatedOn());
	}
}
