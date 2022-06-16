package com.shiro.utils.tree;

import java.util.List;

public interface TreeNode {
    Long getId();

    Long getParentId();

    List<TreeNode> getChildren();

    void setChildren(List<TreeNode> children);
}