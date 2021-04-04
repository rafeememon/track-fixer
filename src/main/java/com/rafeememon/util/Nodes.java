package com.rafeememon.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Nodes {

    public static Node getChildNode(Node node, String... types) {
        Node result = node;
        for (int k = 0; k < types.length; k++) {
            String type = types[k];
            List<Node> childNodes = getChildNodes(result, type);
            if (childNodes.size() != 1) {
                throw new RuntimeException("Expected exactly one child of type: " + type);
            }
            result = childNodes.get(0);
        }
        return result;
    }

    public static Optional<Node> getOptionalChildNode(Node node, String type) {
        List<Node> childNodes = getChildNodes(node, type);
        if (childNodes.size() == 0) {
            return Optional.empty();
        } else if (childNodes.size() == 1) {
            return Optional.of(childNodes.get(0));
        } else {
            throw new RuntimeException("Expected zero or one child of type: " + type);
        }
    }

    public static List<Node> getChildNodes(Node node, String type) {
        List<Node> result = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int k = 0; k < childNodes.getLength(); k++) {
            Node childNode = childNodes.item(k);
            if (childNode.getNodeName().equals(type)) {
                result.add(childNode);
            }
        }
        return result;
    }

    public static void removeNode(Node node) {
        node.getParentNode().removeChild(node);
    }

    public static String getTextNode(Node node, String... types) {
        return Nodes.getChildNode(node, types).getTextContent();
    }

    public static BigDecimal getBigDecimalNode(Node node, String... types) {
        return new BigDecimal(Nodes.getChildNode(node, types).getTextContent());
    }

    public static void setTextNode(Node node, String type, String value) {
        getChildNode(node, type).setTextContent(value);
    }

    private Nodes() {
        // utility class
    }

}
