package com.shiro;

import com.alibaba.fastjson.JSON;
import com.shiro.utils.AesCbcUtil;
import com.shiro.utils.SecretConstants;
import com.shiro.utils.parm.impl.BaseCommonParam;
import com.shiro.utils.tree.TreeNode;
import com.shiro.utils.tree.TreeUtils;
import com.shiro.utils.tree1.BaseTreeNode;
import com.shiro.utils.tree1.TreeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.utils
 * @Description: TODO
 * @date Date : 2021年07月02日 下午2:48
 */
public class Test {
    public static void main(String[] args) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        A a1 = new A();
        a1.setId(1l);
        a1.setParentId(0l);
        a1.setName("一级");

        A a2 = new A();
        a2.setId(2l);
        a2.setParentId(1l);
        a2.setName("2级");

        A a3 = new A();
        a3.setId(3l);
        a3.setParentId(2l);
        a3.setName("3级");

        A a4 = new A();
        a4.setId(1l);
        a4.setParentId(0l);
        a4.setName("一级");

        treeNodeList.add(a1);
        treeNodeList.add(a2);
        treeNodeList.add(a3);
        treeNodeList.add(a4);


        List<TreeNode> treeNodes = TreeUtils.convertToTree(treeNodeList);
        System.out.println(JSON.toJSONString(treeNodes));

        List<BaseTreeNode> baseTreeNodes = new ArrayList<>();
        B b = new B("1", "一级", "0");
        B b1 = new B("2", "二级", "7");
        B b2 = new B("3", "三级", "2");
        B b3 = new B("4", "四级", "1");
        baseTreeNodes.add(b);
        baseTreeNodes.add(b1);
        baseTreeNodes.add(b2);
        baseTreeNodes.add(b3);
        List<BaseTreeNode> build = new TreeUtil().build(baseTreeNodes);
        System.out.println(JSON.toJSONString(build));
        BaseCommonParam commonParam = new BaseCommonParam();
        commonParam.setAppId("9999");
        commonParam.setVersion("999");

        String encode = AesCbcUtil.encode(JSON.toJSONString(commonParam), SecretConstants.AES_KEY_REQ, SecretConstants.AES_IV_REQ);
        System.out.println(encode);
    }
}

class A implements TreeNode {
    List<TreeNode> children;
    private Long id;
    private Long parentId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    public A setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}

@Data
class B extends BaseTreeNode {
    private String code;
    private String parentCode;
    private String name;
    private List<B> children = new ArrayList<>();

    public B() {

    }

    public B(String code, String name, String parentCode) {
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
    }

    @Override
    public String obtainCode() {
        return code;
    }

    @Override
    public String obtainParentCode() {
        return parentCode;
    }

    @Override
    public void add(BaseTreeNode node) {
        children.add((B) node);
    }
}
