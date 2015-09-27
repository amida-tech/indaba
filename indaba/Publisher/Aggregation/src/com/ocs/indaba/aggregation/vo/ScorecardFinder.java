/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.aggregation.vo;

import com.ocs.indaba.aggregation.common.Constants;
import com.ocs.indaba.util.Tree;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jiangjeff
 */
public class ScorecardFinder {
    //
    // Category -> Sub Category -> Question Set -> Question
    //

    /**
     * Find the specified question in a category tree
     *
     * @param catTree
     * @param questionId
     * @return
     */
    public static QuestionNode findQuestion(List<Tree<ScorecardBaseNode>> catTree, int questionId) {
        if (catTree == null) {
            return null;
        }

        for (Tree<ScorecardBaseNode> tree : catTree) {
            QuestionNode node = findQuestion(tree, questionId);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    public static QuestionNode findQuestion(Tree<ScorecardBaseNode> tree, int questionId) {
        ScorecardBaseNode node = tree.getNode();

        if (node == null) {
            return null;
        } else if (tree.isLeaf() && node.getNodeType() == Constants.NODE_TYPE_QUESTION) {
            return (((QuestionNode) node).getQuestionId() == questionId) ? (QuestionNode) node : null;

        }

        List<Tree<ScorecardBaseNode>> children = tree.getChildren();

        if (children != null && !children.isEmpty()) {
            for (Tree<ScorecardBaseNode> subTree : children) {
                QuestionNode subNode = findQuestion(subTree, questionId);
                if (subNode != null) {
                    return subNode;
                }
            }
        }

        return null;
    }

    public static CategoryNode findSubCategory(List<Tree<ScorecardBaseNode>> catTree, int subcatId) {
        if (catTree == null) {
            return null;
        }
        for (Tree<ScorecardBaseNode> tree : catTree) {
            List<Tree<ScorecardBaseNode>> subcatList = tree.getChildren();
            if (!tree.isLeaf() && subcatList != null && !subcatList.isEmpty()) {
                for (Tree<ScorecardBaseNode> subcatTree : subcatList) {
                    ScorecardBaseNode node = subcatTree.getNode();
                    if (!tree.isLeaf() && node != null && node.getNodeType() == Constants.NODE_TYPE_CATEGORY && node.getId() == subcatId) {
                        return (CategoryNode) node;
                    }
                }
            }
        }

        return null;
    }
    /*
     * public static CategoryNode findSubCategory1(List<Tree<ScorecardBaseNode>>
     * catTree, int subcatId) { if (catTree == null) { return null; } for
     * (Tree<ScorecardBaseNode> tree : catTree) { CategoryNode node =
     * findSubCategory(tree, subcatId); if (node != null) { return node; } }
     *
     * return null; }
     *
     * public static CategoryNode findSubCategory(Tree<ScorecardBaseNode> tree,
     * int subcatId) { ScorecardBaseNode node = tree.getNode();
     *
     * if (node == null) { return null; } else if (tree.isLeaf() ||
     * node.getNodeType() == Constants.NODE_TYPE_QUESTION) { return null; } else
     * if (!tree.isLeaf() && node.getNodeType() == Constants.NODE_TYPE_CATEGORY
     * && node.getId() == subcatId) { return (CategoryNode) node; }
     *
     * List<Tree<ScorecardBaseNode>> children = tree.getChildren();
     *
     * if (children != null && !children.isEmpty()) { for
     * (Tree<ScorecardBaseNode> subTree : children) { CategoryNode subNode =
     * findSubCategory(subTree, subcatId); if (subNode != null) { return
     * subNode; } } }
     *
     * return null; }
     */

    public static void listAllQuestions(List<QuestionNode> list, Tree<ScorecardBaseNode> tree) {
        if (list == null) {
            list = new ArrayList<QuestionNode>();
        }

        ScorecardBaseNode node = tree.getNode();
        if (node.getNodeType() == Constants.NODE_TYPE_CATEGORY) {
            List<Tree<ScorecardBaseNode>> children = tree.getChildren();
            if (children != null && !children.isEmpty()) {
                for (Tree<ScorecardBaseNode> subTree : children) {
                    listAllQuestions(list, subTree);
                }
            }
        } else {
            list.add((QuestionNode) node);
        }
    }

    public static void listAllQuestions(List<QuestionNode> list, List<Tree<ScorecardBaseNode>> catTrees) {
        if (catTrees == null || catTrees.isEmpty()) {
            return;
        }

        if (list == null) {
            list = new ArrayList<QuestionNode>();
        }

        for (Tree<ScorecardBaseNode> tree : catTrees) {
            ScorecardBaseNode node = tree.getNode();
            if (node.getNodeType() == Constants.NODE_TYPE_CATEGORY) {
                List<Tree<ScorecardBaseNode>> children = tree.getChildren();
                if (children != null && !children.isEmpty()) {
                    for (Tree<ScorecardBaseNode> subTree : children) {
                        listAllQuestions(list, subTree);
                    }
                }
            } else {
                list.add((QuestionNode) node);
            }
        }
    }

    public static List<CategoryNode> listCategories(List<Tree<ScorecardBaseNode>> rootCategories) {
        if (rootCategories == null || rootCategories.isEmpty()) {
            return null;
        }
        List<CategoryNode> subcats = new ArrayList<CategoryNode>();
        for (Tree<ScorecardBaseNode> category : rootCategories) {
            if (category.isLeaf() || category.getNode() == null || category.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
                continue;
            }
            List<Tree<ScorecardBaseNode>> subcatNodes = category.getChildren();
            if (subcatNodes == null) {
                continue;
            }
            for (Tree<ScorecardBaseNode> subcat : subcatNodes) { // subcat
                if (subcat.isLeaf() || subcat.getNode() == null || subcat.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
                    continue;
                }
                subcats.add((CategoryNode) subcat.getNode());
            }
        }
        return subcats;
    }

    public static Tree<ScorecardBaseNode> findCategory(List<Tree<ScorecardBaseNode>> catTree, int subcatId) {
        if (catTree == null || catTree.isEmpty()) {
            return null;
        }
        for (Tree<ScorecardBaseNode> tree : catTree) {
            Tree<ScorecardBaseNode> node = findCategory(tree, subcatId);
            if (node != null) {
                return node;
            }
        }
        return null;
    }
    public static int findIndexOfSiblings(List<Tree<ScorecardBaseNode>> catTree, int catId) {
        if (catTree == null || catTree.isEmpty()) {
            return -1;
        }
        int index = 0;
        for (Tree<ScorecardBaseNode> tree : catTree) {
            if(tree.getNode().getId() == catId) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    private static Tree<ScorecardBaseNode> findCategory(Tree<ScorecardBaseNode> tree, int subcatId) {
        if (tree.isLeaf() || tree.getNode() == null) {
            return null;
        }
        if (tree.getNode().getNodeType() == Constants.NODE_TYPE_QUESTION) {
            return null;
        }
        if (tree.getNode().getId() == subcatId) {
            return tree;
        }
        return findCategory(tree.getChildren(), subcatId);
    }

    public static StringBuilder findIndexLabelOfNode(Tree<ScorecardBaseNode> subcat, StringBuilder sBuf) {
        int index = 0;
        int subcatId = subcat.getNode().getId();
        Tree<ScorecardBaseNode> parent = subcat.getParent();
        if (parent == null || parent.getChildren() == null || parent.getChildren().isEmpty()) {
            return sBuf;
        }
        for (Tree<ScorecardBaseNode> node : parent.getChildren()) {
            if (node.getNode().getId() == subcatId) {
                sBuf.insert(0, '.').insert(0, ++index);
                return findIndexLabelOfNode(node, sBuf);
            }
            ++index;
        }
        return sBuf;
    }

    public static int findIndexOfSiblings(Tree<ScorecardBaseNode> nodeTree) {
        int catId = nodeTree.getNode().getId();
        Tree<ScorecardBaseNode> parent = nodeTree.getParent();
        if (parent == null || parent.getChildren() == null || parent.getChildren().isEmpty()) {
            return 0;
        }
        int index = 0; 
        for (Tree<ScorecardBaseNode> node : parent.getChildren()) {
            if (node.getNode().getId() == catId) {
                return index;
            }
            ++index;
        }
        return 0;
    }
}
