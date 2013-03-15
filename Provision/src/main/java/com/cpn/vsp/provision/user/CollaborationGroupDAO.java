package com.cpn.vsp.provision.user;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

@Service
public class CollaborationGroupDAO {

	@PersistenceContext
	EntityManager entityManager;
	
	public CollaborationGroup createGroupForUser(User user) {
		CollaborationGroup collabGroup = new CollaborationGroup(user);
		entityManager.persist(collabGroup);
		return collabGroup;
	}

	public List<CollaborationGroup> getGroupsForUser(User user) {
		return entityManager.createQuery("from CollaborationGroup where :user member of collaberators", CollaborationGroup.class).setParameter("user", user).getResultList();
	}

	public CollaborationGroup addUserToGroup(User user, CollaborationGroup group) {
		//group.getCollaborators().add(user);
		return entityManager.merge(group);
	}

	public CollaborationGroup removeUserFromGroup(User user, CollaborationGroup group) {
		//group.getCollaborators().remove(user);
		return entityManager.merge(group);
	}

}
