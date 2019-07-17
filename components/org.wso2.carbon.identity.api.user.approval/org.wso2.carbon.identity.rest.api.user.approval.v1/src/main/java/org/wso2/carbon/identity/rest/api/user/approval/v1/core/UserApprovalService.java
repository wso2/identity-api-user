package org.wso2.carbon.identity.rest.api.user.approval.v1.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.axis2.databinding.types.URI;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.humantask.client.api.IllegalAccessFault;
import org.wso2.carbon.humantask.client.api.IllegalArgumentFault;
import org.wso2.carbon.humantask.client.api.IllegalOperationFault;
import org.wso2.carbon.humantask.client.api.IllegalStateFault;
import org.wso2.carbon.humantask.client.api.types.TSimpleQueryCategory;
import org.wso2.carbon.humantask.client.api.types.TSimpleQueryInput;
import org.wso2.carbon.humantask.client.api.types.TStatus;
import org.wso2.carbon.humantask.client.api.types.TTaskSimpleQueryResultSet;
import org.wso2.carbon.humantask.core.TaskOperationService;
import org.wso2.carbon.humantask.core.api.client.TaskOperationsImpl;
import org.wso2.carbon.humantask.core.dao.TaskStatus;
import org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant;
import org.wso2.carbon.identity.api.user.common.error.APIError;
import org.wso2.carbon.identity.api.user.common.error.ErrorResponse;
import org.wso2.carbon.identity.rest.api.user.approval.v1.core.functions.TTaskSimpleQueryResultRowToExternal;
import org.wso2.carbon.identity.rest.api.user.approval.v1.core.functions.TaskModelToExternal;
import org.wso2.carbon.identity.rest.api.user.approval.v1.core.model.TaskModel;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.StateDTO;
import org.wso2.carbon.identity.rest.api.user.approval.v1.dto.TaskSummeryDTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.SERVER_ERROR_CHANGING_APPROVALS_STATE;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.SERVER_ERROR_RETRIEVING_APPROVALS_FOR_USER;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.SERVER_ERROR_RETRIEVING_APPROVAL_OF_USER;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.USER_ERROR_INVALID_INPUT;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.USER_ERROR_INVALID_OPERATION;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.USER_ERROR_INVALID_STATE_CHANGE;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.USER_ERROR_INVALID_TASK_ID;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.USER_ERROR_NON_EXISTING_TASK_ID;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.USER_ERROR_NOT_ACCEPTABLE_INPUT_FOR_NEXT_STATE;
import static org.wso2.carbon.identity.api.user.approval.common.ApprovalConstant.ErrorMessage.USER_ERROR_UNAUTHORIZED_USER;
import static org.wso2.carbon.identity.rest.api.user.approval.v1.dto.StateDTO.ActionEnum.APPROVE;
import static org.wso2.carbon.identity.rest.api.user.approval.v1.dto.StateDTO.ActionEnum.CLAIM;
import static org.wso2.carbon.identity.rest.api.user.approval.v1.dto.StateDTO.ActionEnum.REJECT;
import static org.wso2.carbon.identity.rest.api.user.approval.v1.dto.StateDTO.ActionEnum.RELEASE;

/**
 * Call internal osgi services to perform user's approval task related operations
 */
public class UserApprovalService {

    private static final String APPROVAL_STATUS = "approvalStatus";
    private static final String PENDING = "PENDING";
    private static Log log = LogFactory.getLog(UserApprovalService.class);
    private static final String APPROVAL_DATA_STRING = "<sch:ApprovalCBData xmlns:sch=\"http://ht.bpel.mgt.workflow" +
            ".identity.carbon.wso2.org/wsdl/schema\"><approvalStatus>%s</approvalStatus></sch:ApprovalCBData>";

    /**
     * Get ChallengeQuestionManager osgi service
     *
     * @return TaskOperationService
     */
    public TaskOperationService getTaskOperationService() {
        return (TaskOperationService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(TaskOperationService.class, null);
    }

    public List<TaskSummeryDTO> listTasks(Integer limit, Integer offset, List<String> status) {

        try {
            TSimpleQueryInput queryInput = new TSimpleQueryInput();
            if (limit != null && limit > 0) {
                queryInput.setPageSize(limit);
            }
            if (offset != null && offset > 0) {
                queryInput.setPageNumber(offset);
            }

            TStatus[] tStatuses = getRequiredTStatuses(status);

            queryInput.setSimpleQueryCategory(TSimpleQueryCategory.CLAIMABLE);
            queryInput.setStatus(tStatuses);
            TTaskSimpleQueryResultSet taskResults = getTaskOperationService().simpleQuery(queryInput);
            return Arrays.stream(taskResults.getRow()).map(new TTaskSimpleQueryResultRowToExternal())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw handleException(e, SERVER_ERROR_RETRIEVING_APPROVALS_FOR_USER);
        }
    }

    public Object getTaskData(String taskId) {
        TaskOperationsImpl taskOperations = new TaskOperationsImpl();
        URI taskIdURI = getUri(taskId);
        try {
            String xml = (String) taskOperations.getInput(taskIdURI, null);
            XmlMapper xmlMapper = new XmlMapper();
            TaskModel taskModel = xmlMapper.readValue(xml, TaskModel.class);
            taskModel.setId(taskId);
            addApprovalStatus(taskOperations, taskIdURI, xmlMapper, taskModel);
            return new TaskModelToExternal().apply(taskModel);
        } catch (IllegalAccessFault e) {
            if (log.isDebugEnabled()) {
                log.debug(USER_ERROR_UNAUTHORIZED_USER.getMessage(), e);
            }
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_UNAUTHORIZED_USER);
        } catch (IllegalArgumentFault e) {
            if (log.isDebugEnabled()) {
                log.debug(USER_ERROR_NON_EXISTING_TASK_ID.getMessage(), e);
            }
            throw handleError(Response.Status.NOT_FOUND, USER_ERROR_NON_EXISTING_TASK_ID);
        } catch (IllegalStateFault e) {
            if (log.isDebugEnabled()) {
                log.debug(USER_ERROR_INVALID_TASK_ID.getMessage(), e);
            }
            throw handleError(Response.Status.BAD_REQUEST, USER_ERROR_INVALID_TASK_ID);
        } catch (Exception e) {
            throw handleException(e, SERVER_ERROR_RETRIEVING_APPROVAL_OF_USER);
        }
    }

    private URI getUri(String taskId) {
        URI taskIdURI;
        try {
            taskIdURI = new URI(taskId);
        } catch (URI.MalformedURIException e) {
            throw handleError(Response.Status.BAD_REQUEST, USER_ERROR_INVALID_TASK_ID);
        }
        return taskIdURI;
    }


    public void updateStatus(String taskId, StateDTO nextState) {
        TaskOperationsImpl taskOperations = new TaskOperationsImpl();
        URI taskIdURI = getUri(taskId);
        try {
            switch (nextState.getAction()) {
                case CLAIM:
                    taskOperations.claim(taskIdURI);
                    break;
                case RELEASE:
                    taskOperations.release(taskIdURI);
                    break;
                case APPROVE:
                    completeTask(taskOperations, taskIdURI, APPROVE);
                    break;
                case REJECT:
                    completeTask(taskOperations, taskIdURI, REJECT);
                    break;
                default:
                    handleError(Response.Status.NOT_ACCEPTABLE, USER_ERROR_NOT_ACCEPTABLE_INPUT_FOR_NEXT_STATE);
            }
        } catch (IllegalAccessFault e) {
            throw handleError(Response.Status.FORBIDDEN, USER_ERROR_UNAUTHORIZED_USER);
        } catch (IllegalArgumentFault e) {
            if (e.getMessage().contains("Task lookup failed for task id")) {
                throw handleError(Response.Status.NOT_FOUND, USER_ERROR_NON_EXISTING_TASK_ID);
            }
            throw handleException(e, USER_ERROR_INVALID_INPUT);
        } catch (IllegalOperationFault e) {
            throw handleException(e, USER_ERROR_INVALID_OPERATION);
        } catch (IllegalStateFault e) {
            throw handleException(e, USER_ERROR_INVALID_STATE_CHANGE);
        } catch (Exception e) {
            throw handleException(e, SERVER_ERROR_CHANGING_APPROVALS_STATE);
        }
    }

    private void completeTask(TaskOperationsImpl taskOperations, URI taskIdURI, StateDTO.ActionEnum action) throws
            Exception {
        taskOperations.start(taskIdURI);
        taskOperations.complete(taskIdURI, String.format(APPROVAL_DATA_STRING, action.name()));
    }

    private void addApprovalStatus(TaskOperationsImpl taskOperations, URI taskIdURI, XmlMapper xmlMapper,
                                   TaskModel taskModel) throws Exception {
        String approvalStatus = PENDING;
        String approvalData = (String) taskOperations.getOutput(taskIdURI, null);
        if (StringUtils.isNotEmpty(approvalData)) {
            JsonNode node = xmlMapper.readTree(approvalData);
            approvalStatus = node.get(APPROVAL_STATUS) != null ? node.get(APPROVAL_STATUS).textValue() :
                    PENDING;
            taskModel.setApprovalStatus(approvalStatus);
        }
        taskModel.setApprovalStatus(approvalStatus);

    }

    private TStatus[] getRequiredTStatuses(List<String> status) {
        List<String> allStatuses = Arrays.asList(TaskStatus.RESERVED.toString(), TaskStatus.READY.toString(),
                TaskStatus.COMPLETED.toString());
        TStatus[] tStatuses = getTStatus(allStatuses);

        if (CollectionUtils.isNotEmpty(status)) {
            List<String> requestedStatus = status.stream().filter((s) -> allStatuses.contains(s)).collect
                    (Collectors.toList());
            if (CollectionUtils.isNotEmpty(requestedStatus)) {
                tStatuses = getTStatus(requestedStatus);
            }
        }
        return tStatuses;
    }

    private TStatus[] getTStatus(List<String> statuses) {
        return statuses.stream().map(s -> getTStatus(s)).toArray(TStatus[]::new);
    }

    private TStatus getTStatus(String status) {
        TStatus tStatus = new TStatus();
        tStatus.setTStatus(status);
        return tStatus;
    }


    /**
     * Handle Exceptions
     *
     * @param e
     * @param errorEnum
     * @return
     */
    private APIError handleException(Exception e, ApprovalConstant.ErrorMessage errorEnum) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(log, e, errorEnum.getDescription());

        if (e instanceof IllegalAccessFault) {
            return handleError(Response.Status.FORBIDDEN, USER_ERROR_UNAUTHORIZED_USER);
        } else if (e instanceof IllegalArgumentFault || e instanceof IllegalStateFault || e instanceof
                IllegalOperationFault) {
            errorResponse.setDescription(e.getMessage());
            return new APIError(Response.Status.BAD_REQUEST, errorResponse);
        } else {
            return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
        }
    }

    /**
     * Handle User errors
     *
     * @param status
     * @param error
     * @return
     */
    private APIError handleError(Response.Status status, ApprovalConstant.ErrorMessage error) {
        return new APIError(status, getErrorBuilder(error).build());

    }

    /**
     * Get ErrorResponse Builder for Error enum
     *
     * @param errorEnum
     * @return
     */
    private ErrorResponse.Builder getErrorBuilder(ApprovalConstant.ErrorMessage errorEnum) {

        return new ErrorResponse.Builder().withCode(errorEnum.getCode()).withMessage(errorEnum.getMessage())
                .withDescription(errorEnum.getDescription());
    }


}
