package org.wso2.carbon.identity.rest.api.user.approval.v1.core.factories;

import org.wso2.carbon.identity.workflow.engine.ApprovalEventService;

public class ApprovalEventServiceFactory {
    private static final ApprovalEventService SERVICE;

    static {
        SERVICE = new ApprovalEventService();
    }

    public static ApprovalEventService getApprovalEventService() {

        return SERVICE;
    }
}
