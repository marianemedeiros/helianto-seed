package org.helianto.install.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.easymock.EasyMock;
import org.helianto.core.domain.City;
import org.helianto.core.domain.Entity;
import org.helianto.core.domain.Signup;
import org.helianto.core.repository.CityRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class DefaultEntityInstallStrategyTests {

	@Test
	public void domain() {
		Signup form =	new Signup();
		form.setDomain("myDomain.com");
		form.setCityId(1);
		City city = new City();
		CityRepository cityRepository = EasyMock.createMock(CityRepository.class);
		ReflectionTestUtils.setField(strategy, "cityRepository", cityRepository);
		EasyMock.expect(cityRepository.findOne(form.getCityId())).andReturn(city);
		EasyMock.replay(cityRepository);
		List<Entity> entityList = strategy.generateEntityPrototypes(form);
		assertEquals("myDomain.com", entityList.get(0).getAlias());
		assertSame(city, entityList.get(0).getCity());
	}
	
	private DefaultInstallStrategy strategy;
	
	@Before
	public void setUp() {
		strategy = new DefaultInstallStrategy();
	}
	
}
