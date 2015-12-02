package org.helianto.user.repository;

import java.io.Serializable;
import java.util.List;

import org.helianto.user.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User association adapter repository.
 * 
 * @author mauriciofernandesdecastro
 */
public interface UserAssociationAdapterRepository 
	extends JpaRepository<UserGroup, Serializable> 
{

	/**
	 * List user groups already associated to some user.
	 * 
	 * @param userId
	 */
	@Query("select new "
			+ "org.helianto.user.repository.UserGroupNameAdapter("
			+ "association.parent.id, association.parent.userName, 1"
			+ ") "
			+ "from UserAssociation association "
			+ "where association.child.id = ?1 "
			+ "order by association.parent.userName ")
	List<UserGroupNameAdapter> findParentNamesByChildId(int userId);
	
	/**
	 * List user groups not yet associated to any user.
	 * 
	 * @param entityId
	 * @param userId
	 */
	@Query("select new "
			+ "org.helianto.user.repository.UserGroupNameAdapter"
			+ "(userGroup.id, userGroup.userName, 0) "
			+ "from UserGroup userGroup "
			+ "where userGroup.entity.id = ?1 "
			+ "and userGroup.class = 'G' "
			+ "and userGroup.userType not in ('A', 'G') "
			+ "and userGroup.id not in ("
			+ "  select association.parent.id "
			+ "  from UserAssociation association "
			+ "  where association.child.id = ?2 "
			+ ")")
	List<UserGroupNameAdapter> findParentNamesByChildId(int entityId, int userId);

}
