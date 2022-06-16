package com.shiro.utils.tree1;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeUtil {

    /**
     * 获取层级列表
     *
     * @param voList
     * @return
     */
    public List<BaseTreeNode> build(List<BaseTreeNode> voList) {
        List<BaseTreeNode> returnList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        for (BaseTreeNode t : voList) {
            tempList.add(t.obtainCode());
        }

        for (Iterator<BaseTreeNode> iterator = voList.iterator(); iterator.hasNext(); ) {
            BaseTreeNode vo = iterator.next();
            if (!tempList.contains(vo.obtainParentCode())) {
                returnList.add(findChildren(voList, vo));
            }
        }
        if (returnList.isEmpty()) {
            returnList = voList;
        }
        return returnList;
    }

    private static BaseTreeNode findChildren(List<BaseTreeNode> voList, BaseTreeNode vo) {
        Iterator<BaseTreeNode> it = voList.iterator();
        while (it.hasNext()) {
            BaseTreeNode node = it.next();
            String parentCode = node.obtainParentCode();
            if (StringUtils.isNotEmpty(parentCode) && vo.obtainCode().equalsIgnoreCase(parentCode)) {
                if (hasChild(voList, node)) {
                    node = findChildren(voList, node);
                }
                vo.add(node);
            }
        }
        return vo;
    }

    private static boolean hasChild(List<BaseTreeNode> voList, BaseTreeNode node) {
        return voList.stream().anyMatch(vo -> StringUtils.isNotEmpty(vo.obtainParentCode()) && node.obtainCode().equalsIgnoreCase(vo.obtainParentCode()));
    }
}