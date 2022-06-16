package com.shiro.utils.tree1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNode<T> extends BaseTreeNode {
 
    private static final long serialVersionUID = -1586013635569434864L;
    /**
     * 节点code
     */
    private T code;
 
    /**
     * 节点名
     */
    private String name;
 
    /**
     * 父节点code
     */
    @JsonIgnore
    private T parentCode;
 
    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeNode> children = new ArrayList<>();
 
    public TreeNode(){}
 
    public TreeNode(T code,String name,T parentCode){
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
    }
 
    @Override
    public String obtainCode() {
       return String.valueOf(this.code);
    }
 
    @Override
    public String obtainParentCode() {
        return String.valueOf(this.parentCode);
    }
 
    @Override
    public void add(BaseTreeNode node) {
        this.children.add((TreeNode) node);
    }
}
