package com.muse.router.facade.model;

import com.muse.router.facade.enums.NodeType;

import java.util.Map;

import javax.lang.model.element.Element;

/**
 * Created by GuoWee on 2018/4/26.
 */

public class Node {
    private NodeType nodeType;
    private Element rawType;
    private Class<?> destination;
    private String path;            // Path of route
    private String desc;            // Desc of route
    private int priority = -1;
    private Map<String, Integer> paramsType;
    private Map<String, String> paramsDesc;

    public Map<String, Integer> getParamsType() {
        return paramsType;
    }

    public void setParamsType(Map<String, Integer> paramsType) {
        this.paramsType = paramsType;
    }

    public Map<String, String> getParamsDesc() {
        return paramsDesc;
    }

    public void setParamsDesc(Map<String, String> paramsDesc) {
        this.paramsDesc = paramsDesc;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public Element getRawType() {
        return rawType;
    }

    public void setRawType(Element rawType) {
        this.rawType = rawType;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
