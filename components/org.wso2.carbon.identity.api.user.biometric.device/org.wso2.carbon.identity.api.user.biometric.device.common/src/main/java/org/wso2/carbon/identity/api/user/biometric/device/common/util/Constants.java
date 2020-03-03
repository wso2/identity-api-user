package org.wso2.carbon.identity.api.user.biometric.device.common.util;

/**
 *.
 */
public class Constants {

      public enum ErrorMessages {
        ERROR_CODE_INVALID_SIGNATURE(
                "BDH-15001",
                "The credibility of the request could not be verified" ,
                "An error occurred when the digital signatre is not verified"),

        ERROR_CODE_DEVICE_HANDLER_SQL_EXCEPTION(
                "BDH-15002",
                "The operation was not executed due to a database error",
                "A SQL exception occurred while trying to perform database operation "),
        ERROR_CODE__JSON_PROCESSING_EXCEPTION(
                "BDH-15003",
                "Error occured while trying to convert an object to json format",
                "" + "A System error occurred while trying to convert an object to a json"),
        ERROR_CODE_INTERNAL_SERVER_ERROR(
                "BDH-15004",
                "Failed to process request due to an Internal server error",
                "A System error occurred internally while trying to process a request"),
        ERROR_CODE_USER_STORE_ERROR(
                "BDH-15005",
                "Failed to get User store manager of the user",
                "A System error occurred internally while trying to get a UserStoreManager"),
        ERROR_CODE_FAILED_SIGNATURE_VALIDATION(
                "BDH-15006",
                "Failed to get User store manager of the user",
                "A System error occurred internally while trying to get a UserStoreManager"),
        ERROR_CODE_GET_DEVICE_SERVER_ERROR(
                "BDH-15007",
                "A system error occurred while trying to retrieve a device"),
        ERROR_CODE_LIST_DEVICE_SERVER_ERROR(
                "BDH-15008",
                "A system error occurred while trying to retrieve registered devices of a user"),
        ERROR_COOE_UNREGISTER_DEVICE_SERVER_ERROR(
                "BDH-15009",
                "A System error occurred while trying to remove a specific device"),
        ERROR_CODE_EDIT_DEVICE_NAME_SERVER_ERROR(
                "BDH-15010",
                "The device could not be modified",
                "A System error occurred while trying to modify the device name"),
        ERROR_CODE_REGISTER_DEVICE_SERVER_ERROR(
                "BDH-15011",
                "A System error occurred while trying to register a new device"),
        ERROR_CODE_REGISTER_DEVICE_CLIENT_ERROR(
                "BDH-10001",
                "A System error occurred while trying to register a new device"),
        ERROR_CODE_GET_DEVICE_CLIENT_ERROR(
                "BDH-10002",
                "A system error occurred while trying to retrieve a device"),
        ERROR_CODE_LIST_DEVICE_CLIENT_ERROR(
                "BDH-10003",
                "A system error occurred while trying to retrieve registered devices of a user"),
        ERROR_COOE_UNREGISTER_DEVICE_CLIENT_ERROR(
                "BDH-10004",
                "A System error occurred while trying to remove a specific device"),
        ERROR_CODE_EDIT_DEVICE_NAME_CLIENT_ERROR(
                "BDH-10005",
                "The device could not be modified",
                "A System error occurred while trying to modify the device name");

        private final String code;
        private  String message;
        private final String description;

          ErrorMessages(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        ErrorMessages(String code, String description) {
          this.code = code;
          this.description = description;
        }

        public void setMessage(String message) {
          this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return getCode() + " | " + getMessage() + " | " + getDescription();
        }

    }

}

