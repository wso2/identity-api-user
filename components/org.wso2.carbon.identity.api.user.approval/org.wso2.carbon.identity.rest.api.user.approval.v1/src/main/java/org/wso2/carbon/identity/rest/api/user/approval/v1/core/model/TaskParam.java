package org.wso2.carbon.identity.rest.api.user.approval.v1.core.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Task Param model
 */
@XmlRootElement(name = "xsd-complex-type-wrapper")
public class TaskParam {


//    private Map<String ,String > params;
    private String itemName;
    private String itemValue;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}
