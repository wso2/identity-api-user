package org.wso2.carbon.identity.rest.api.user.association.v1.factories;

import org.wso2.carbon.identity.rest.api.user.association.v1.UserIdApiService;
import org.wso2.carbon.identity.rest.api.user.association.v1.impl.UserIdApiServiceImpl;

public class UserIdApiServiceFactory {

   private final static UserIdApiService service = new UserIdApiServiceImpl();

   public static UserIdApiService getUserIdApi()
   {
      return service;
   }
}
