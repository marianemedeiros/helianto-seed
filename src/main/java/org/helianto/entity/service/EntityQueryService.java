package org.helianto.entity.service;

import javax.inject.Inject;

import org.helianto.core.internal.SimpleCounter;
import org.helianto.core.repository.EntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Entity query service.
 * 
 * @author Eldevan Nery Junior
 *
 */
@Service
public class EntityQueryService {

	private static final Logger logger = LoggerFactory.getLogger(EntityQueryService.class);

	@Inject
	private EntityRepository entityRepository;

	/**
	 * Count entities with alias.
	 *
	 * @param contextId
	 * @param entityAlias
     */
	public SimpleCounter countEntity(int contextId, String entityAlias) {
		return new SimpleCounter("COUNT", entityRepository.countByOperator_IdAndAliasIgnoreCase(contextId, entityAlias));
	}
}
