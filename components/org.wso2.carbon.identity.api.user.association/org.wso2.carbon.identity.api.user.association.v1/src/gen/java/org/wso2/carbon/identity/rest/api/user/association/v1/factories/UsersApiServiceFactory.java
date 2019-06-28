package org.wso2.carbon.identity.rest.api.user.association.v1.factories;

import org.wso2.carbon.identity.rest.api.user.association.v1.UsersApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.impl.UsersApiServiceImpl;

public class UsersApiServiceFactory {

   private final static UsersApiService service = new UsersApiServiceImpl();

   public static UsersApiService getUsersApi()
   {
      return service;
   }
}
