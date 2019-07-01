package org.wso2.carbon.identity.api.user.authorized.apps.v1.factories;

import org.wso2.carbon.identity.api.user.authorized.apps.v1.MeApiService;

public class MeApiServiceFactory {

   private final static MeApiService service = new MeApiServiceImpl();

   public static MeApiService getMeApi()
   {
      return service;
   }
}
