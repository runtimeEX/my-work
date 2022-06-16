package com.shiro.utils.tree1;

import java.io.Serializable;

public abstract class BaseTreeNode implements Serializable {
 
    /**
     * 获取当前code
     * @return
     */
    public abstract String obtainCode();
 
    /**
     * 获取父code
     * @return
     */
    public abstract String obtainParentCode();
 
    /**
     * 添加到子集
     * @param node
     */
    public abstract void add(BaseTreeNode node);
}