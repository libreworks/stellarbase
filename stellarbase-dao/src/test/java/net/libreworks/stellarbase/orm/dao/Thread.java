package net.libreworks.stellarbase.orm.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.ObjectUtils;

import net.libreworks.stellarbase.orm.model.Modifiable;

public class Thread implements Modifiable<Long>
{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private boolean sticky;
	private boolean announcement;
	private boolean closed;
	private Date lastPostOn;
	private Integer postsSum;
	private Date createdOn;
	private String createdBy;
	private Date modifiedOn;
	private String modifiedBy;
	private Integer version;

	private Set<Post> posts = new TreeSet<Post>();

	/**
	 * @return the posts
	 */
	public Set<Post> getPosts()
	{
		return posts;
	}

	/**
	 * @param posts
	 *            the posts to set
	 */
	public void setPosts(Set<Post> posts)
	{
		this.posts = posts;
	}

	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
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
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
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
	 * @return the sticky
	 */
	public boolean isSticky()
	{
		return sticky;
	}

	/**
	 * @param sticky
	 *            the sticky to set
	 */
	public void setSticky(boolean sticky)
	{
		this.sticky = sticky;
	}

	/**
	 * @return the announcement
	 */
	public boolean isAnnouncement()
	{
		return announcement;
	}

	/**
	 * @param announcement
	 *            the announcement to set
	 */
	public void setAnnouncement(boolean announcement)
	{
		this.announcement = announcement;
	}

	/**
	 * @return the closed
	 */
	public boolean isClosed()
	{
		return closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	public void setClosed(boolean closed)
	{
		this.closed = closed;
	}

	/**
	 * @return the lastPostOn
	 */
	public Date getLastPostOn()
	{
		return lastPostOn;
	}

	/**
	 * @param lastPostOn
	 *            the lastPostOn to set
	 */
	public void setLastPostOn(Date lastPostOn)
	{
		this.lastPostOn = lastPostOn;
	}

	/**
	 * @return the postsSum
	 */
	public Integer getPostsSum()
	{
		return postsSum == null ? 0 : postsSum;
	}

	/**
	 * @param postsSum
	 *            the postsSum to set
	 */
	public void setPostsSum(Integer postsSum)
	{
		this.postsSum = postsSum;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn()
	{
		return createdOn;
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
	 * @return the createdBy
	 */
	public String getCreatedBy()
	{
		return createdBy;
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
	 * @return the modifiedOn
	 */
	public Date getModifiedOn()
	{
		return modifiedOn;
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
	 * @return the modifiedBy
	 */
	public String getModifiedBy()
	{
		return modifiedBy;
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
	 * @return the version
	 */
	public Integer getVersion()
	{
		return version;
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
		this.version = version instanceof Integer ? (Integer) version : Integer
				.valueOf(ObjectUtils.toString(version));
	}
}
