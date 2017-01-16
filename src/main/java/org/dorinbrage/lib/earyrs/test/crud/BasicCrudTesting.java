package org.dorinbrage.lib.earyrs.test.crud;

import org.dorinbrage.lib.earyrs.test.common.CommonSettings;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public abstract class BasicCrudTesting<T, E> extends CommonSettings<T, E> {
  
  
  @Before
  
  @Test
  @InSequence(1)
  public void createEntity() {
    Assert.assertNotNull(create);
    
    @SuppressWarnings("unchecked")
    Object created = (E) client.post(create);
    
    Assert.assertNotNull(created);
  }

  @Test
  @InSequence(2)
  public void updateEntity() {    
    //Class<E> updatedEntity = (Class<O>) client.post(getMapper().getUpdateData());
    //Assert.assertNotNull(updatedEntity);
  }

  @Test
  @InSequence(3)
  public void getAllEntities() {
    Assert.assertTrue(true);
  }

  @Test
  @InSequence(4)
  public void deleteEntity() {
    Assert.assertTrue(true);
  }
}
