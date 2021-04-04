package com.rafeememon.gcr2r2r;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rafeememon.util.Documents;
import com.rafeememon.util.Nodes;

public class Main {

    private static final String SOURCE_PATH = "D:\\albums\\2020-10-12 Grand Canyon R2R2R\\activity_5669778770.tcx";
    private static final String TARGET_PATH =
            "D:\\albums\\2020-10-12 Grand Canyon R2R2R\\activity_5669778770_fixed.tcx";

    public static void main(String[] args) throws Exception {
        Document document = Documents.readDocument(SOURCE_PATH);

        // Update ID
        Node id = Nodes.getChildNode(document, "TrainingCenterDatabase", "Activities", "Activity", "Id");
        id.setTextContent(id.getTextContent() + "-fixed");

        // Fix track
        Node track = Nodes.getChildNode(document, "TrainingCenterDatabase", "Activities", "Activity", "Lap", "Track");
        Tracks.fixPositions(track);
        BigDecimal distance = Tracks.fixDistances(track);
        Nodes.getChildNode(document, "TrainingCenterDatabase", "Activities", "Activity", "Lap", "DistanceMeters")
                .setTextContent(distance.toPlainString());

        Documents.writeDocument(TARGET_PATH, document);
        Documents.removeXmlWhitespace(TARGET_PATH);
    }

}
