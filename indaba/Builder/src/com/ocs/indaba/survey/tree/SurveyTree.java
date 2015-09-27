/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ocs.indaba.survey.tree;

import com.ocs.util.SimpleTree;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author yc06x
 */
public class SurveyTree {

    private SimpleTree<Node> root = null;
    private HashMap<Integer, SimpleTree<Node>> treeMap = null;
    private Node firstNode = null;
    private Node lastNode = null;
    private HashMap<String, Node> nodeMap = null;

    public SurveyTree(List<CategoryNode> cats, List<QuestionNode> questions) throws InvalidTreeStructureException {

        treeMap = new HashMap<Integer, SimpleTree<Node>>();
        nodeMap = new HashMap<String, Node>();

        // set up the root tree
        root = new SimpleTree<Node>(null);
        treeMap.put(0, root);

        if (cats != null && !cats.isEmpty()) {
            for (Node node : cats) {
                SimpleTree<Node> t = new SimpleTree<Node>(node);
                treeMap.put(node.getId(), t);

                // also add to the hash table
                nodeMap.put(nodeKey(node.getType(), node.getId()), node);
            }
        }

        if (questions != null && !questions.isEmpty()) {
            for (Node node : questions) {
                SimpleTree<Node> parent = treeMap.get(node.getParentId());

                if (parent == null) {
                    // expected parent doesn't exist
                    throw new InvalidTreeStructureException();
                }

                parent.addChild(node);

                // also add to the hash table
                nodeMap.put(nodeKey(node.getType(), node.getId()), node);
            }
        }

        if (cats != null && !cats.isEmpty()) {
            for (Node cat : cats) {
                int parentId = cat.getParentId();
                SimpleTree<Node> child = treeMap.get(cat.getId());
                SimpleTree<Node> parent = treeMap.get(parentId);

                if (parent == null) {
                    // expected parent doesn't exist
                    throw new InvalidTreeStructureException();
                }

                parent.addChild(child);
            }
        }

        // sort the categories and question nodes
        root.sort(new NodeComparator());
    }

    static private class NodeComparator implements Comparator<Node> {

        public int compare(Node x, Node y) {
            return x.getWeight() - y.getWeight();
        }
    }

    private boolean isTraditional(int level, SimpleTree<Node> tree) {
        if (tree == null) {
            return false;
        }

        List<SimpleTree<Node>> children = tree.getChildren();

        if (children == null || children.isEmpty()) {
            return false;
        }

        // check each child
        for (SimpleTree<Node> child : children) {
            Node node = child.getValue();

            if (level > 0) {
                // not the last level (question set) yet
                // it cannot have question nodes
                if (node.getType() == Node.NODE_TYPE_QUESTION) {
                    return false;
                }

                if (!isTraditional(level - 1, child)) {
                    return false;
                }
            } else {
                // question set - it must contain only question nodes.
                if (node.getType() == Node.NODE_TYPE_CATEGORY) {
                    return false;
                }
            }
        }

        return true;
    }

    // check whether it is TSC
    public boolean isTraditional() {
        return isTraditional(3, root);
    }

    public SimpleTree<Node> getRoot() {
        return root;
    }

    public void setRoot(SimpleTree<Node> root) {
        this.root = root;
    }

    // Find all subelements for the specified category
    public void findSubElements(int catId, List<Integer> catIds, List<Integer> qstIds) {
        if (root == null) {
            return;
        }

        SimpleTree<Node> cat = treeMap.get(catId);

        if (cat == null) {
            return;
        }

        findSubElements(cat, catIds, qstIds);
    }

    private void findSubElements(SimpleTree<Node> catTree, List<Integer> cats, List<Integer> questions) {
        cats.add(catTree.getValue().getId());

        List<SimpleTree<Node>> children = catTree.getChildren();

        if (children != null && !children.isEmpty()) {
            for (SimpleTree<Node> child : children) {
                if (child.getValue().getType() == Node.NODE_TYPE_QUESTION) {
                    questions.add(child.getValue().getId());
                } else {
                    findSubElements(child, cats, questions);
                }
            }
        }
    }

    public Node findFirstQuestion(int catId) {
        if (root == null) {
            return null;
        }

        SimpleTree<Node> catTree = treeMap.get(catId);

        return findFirstQuestion(catTree);
    }

    static private Node findFirstQuestion(SimpleTree<Node> tree) {
        if (tree == null || tree.getValue() == null) {
            return null;
        }

        if (tree.getValue().getType() == Node.NODE_TYPE_QUESTION) {
            return tree.getValue();
        }

        List<SimpleTree<Node>> children = tree.getChildren();

        if (children == null || children.isEmpty()) {
            return null;
        }

        for (SimpleTree<Node> child : children) {
            Node result = findFirstQuestion(child);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private void buildNodeList() {
        if (root == null) {
            return;
        }
        if (firstNode == null) {
            addToList(root, 0);
        }
    }

    static private String nodeKey(short type, int id) {
        String prefix = (type == Node.NODE_TYPE_CATEGORY) ? "C" : "Q";
        return prefix + id;
    }

    private void addToList(SimpleTree<Node> tree, int level) {
        if (tree == null) {
            return;
        }

        Node node = tree.getValue();
        if (node != null) {
            node.setLevel(level);

            // add it to the end of the list
            node.setNext(null);
            node.setPrev(lastNode);

            int depth = 1;
            if (lastNode != null) {
                lastNode.setNext(node);
                depth = lastNode.getDepth() + 1;
            }
            node.setDepth(depth);

            if (firstNode == null) {
                firstNode = node;
            }
            lastNode = node;
        }

        // add all children
        List<SimpleTree<Node>> children = tree.getChildren();
        if (children != null && !children.isEmpty()) {
            for (SimpleTree<Node> child : children) { 
                Node childNode = child.getValue();
                childNode.setParent(node);
                addToList(child, level+1);
            }
        }
    }

    public Node findNextQuestion(int qstId) {
        buildNodeList();

        if (nodeMap == null) {
            return null;
        }

        Node node = nodeMap.get(nodeKey(Node.NODE_TYPE_QUESTION, qstId));

        if (node == null) {
            return null;
        }

        return findNextQuestion(node);
    }

    private Node findNextQuestion(Node node) {
        // find the next question after this node
        Node next = node;
        while ((next = next.getNext()) != null) {
            if (next.getType() == Node.NODE_TYPE_QUESTION) {
                return next;
            }
        }
        return null;
    }

    public Node findPrevQuestion(int qstId) {
        buildNodeList();

        if (nodeMap == null) {
            return null;
        }

        Node node = nodeMap.get(nodeKey(Node.NODE_TYPE_QUESTION, qstId));

        if (node == null) {
            return null;
        }

        return findPrevQuestion(node);
    }

    private Node findPrevQuestion(Node node) {
        // find the prev question before this node
        Node prev = node;
        while ((prev = prev.getPrev()) != null) {
            if (prev.getType() == Node.NODE_TYPE_QUESTION) {
                return prev;
            }
        }
        return null;
    }


    /*
     * Given the current question, try to find the first question of a next category that has questions.
     * Note that the immediate next category may not have questions. This method keeps going until a next category
     * is found that has questions.
     */
    public Node findFirstQuestionOfNextCategory(int qstId) {
        buildNodeList();

        if (nodeMap == null) {
            return null;
        }

        Node node = nodeMap.get(nodeKey(Node.NODE_TYPE_QUESTION, qstId));

        if (node == null) {
            return null;
        }

        // find the last question in this node series
        Node next = node;
        while ((next = next.getNext()) != null) {
            if (next.getType() == Node.NODE_TYPE_CATEGORY) {
                return findNextQuestion(next);
            }
        }
        return null;
    }


    /*
     * Given the current question, try to find the first question of a prevous category that has questions.
     * Note that the immediate prev category may not have questions. This method keeps going until a prev category
     * is found that has questions.
     */
    public Node findFirstQuestionOfPrevCategory(int qstId) {
        buildNodeList();

        if (nodeMap == null) {
            return null;
        }

        Node node = nodeMap.get(nodeKey(Node.NODE_TYPE_QUESTION, qstId));

        if (node == null) {
            return null;
        }

        // find the first question in this node series
        Node prev = node;
        while ((prev = prev.getPrev()) != null) {
            if (prev.getType() == Node.NODE_TYPE_CATEGORY) {
                break;
            }
        }

        if (prev == null) {
            return null;   // no previous category
        }
        // try to find the last question before this category
        node = findPrevQuestion(prev);

        if (node == null) {
            return null;
        }

        // then find the first question in this node series
        while ((prev = node.getPrev()) != null) {
            if (prev.getType() == Node.NODE_TYPE_CATEGORY) {
                break;
            } else {
                node = prev;
            }
        }

        return node;
    }

    /*
     * List nodes in breadth-first order
     */
    public List<Node> listNodes() {
        buildNodeList();

        if (firstNode == null) {
            return null;
        }

        Node node = firstNode;
        List<Node> results = new ArrayList<Node>();

        while (node != null) {
            results.add(node);
            node = node.getNext();
        }

        return results;
    }


    public List<Node> listQuestions() {
        buildNodeList();

        if (firstNode == null) {
            return null;
        }

        Node node = firstNode;
        List<Node> results = new ArrayList<Node>();

        while (node != null) {
            if (node.getType() == Node.NODE_TYPE_QUESTION) results.add(node);
            node = node.getNext();
        }

        return results;
    }


    public List<Node> getChildren(int catId) {
        if (treeMap == null) {
            return null;
        }

        SimpleTree<Node> tree = treeMap.get(catId);

        List<SimpleTree<Node>> subtrees = tree.getChildren();

        if (subtrees == null || subtrees.isEmpty()) {
            return null;
        }

        List<Node> nodes = new ArrayList<Node>();

        for (SimpleTree<Node> t : subtrees) {
            nodes.add(t.getValue());
        }

        return nodes;
    }

    public Node getNode(short type, int id) {
        return nodeMap.get(nodeKey(type, id));
    }
}
