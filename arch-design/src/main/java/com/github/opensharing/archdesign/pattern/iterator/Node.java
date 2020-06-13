package com.github.opensharing.archdesign.pattern.iterator;

/**
 * @author jwen
 */
public class Node {
    private Object data;
    private Node nextNode;

    public Node(Object data, Node nextNode) {
        super();
        this.data = data;
        this.nextNode = nextNode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }


}
