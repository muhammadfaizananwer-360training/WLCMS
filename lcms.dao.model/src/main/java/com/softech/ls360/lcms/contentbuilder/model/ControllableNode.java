package com.softech.ls360.lcms.contentbuilder.model;

import java.util.Map;

/**
 *
 * @author abdul.wahid
 */
public interface ControllableNode {
    public boolean isError();
    public void setError(boolean isError);
    
    public String getAction();
    public void setAction(String action);
    
    public ControllableNode getParent();
    public void setParent(ControllableNode parent);

    public String getNodeKey();
    public Map<String,? extends ControllableNode> getChildren();
}
