package com.rafeememon.tallac;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.rafeememon.util.Documents;
import com.rafeememon.util.Nodes;

public class Main {

    private static final String SOURCE_PATH = "D:\\albums\\2021-04-01 Tallac Cathedral Bowl\\activity_6532207772.tcx";
    private static final String TARGET_PATH =
            "D:\\albums\\2021-04-01 Tallac Cathedral Bowl\\activity_6532207772_fixed.tcx";

    private static final String CORRECTION_START_POINT = "2021-04-02T09:21:46.000Z";
    private static final int CORRECTION_MILLIS = -65534000;

    public static void main(String[] args) throws Exception {
        Document document = Documents.readDocument(SOURCE_PATH);

        // Update ID
        Node id = Nodes.getChildNode(document, "TrainingCenterDatabase", "Activities", "Activity", "Id");
        id.setTextContent(id.getTextContent() + "-fixed");

        // Fix points
        Node track = Nodes.getChildNode(document, "TrainingCenterDatabase", "Activities", "Activity", "Lap", "Track");
        List<Node> trackpoints = Nodes.getChildNodes(track, "Trackpoint");
        Date correctionStartDate = fromIsoString(CORRECTION_START_POINT);
        int count = 0;
        for (Node trackpoint : trackpoints) {
            Node timeNode = Nodes.getChildNode(trackpoint, "Time");
            Date date = fromIsoString(timeNode.getTextContent());
            if (date.compareTo(correctionStartDate) >= 0) {
                Date corrected = addMillis(date, CORRECTION_MILLIS);
                timeNode.setTextContent(toIsoString(corrected));
                count++;
            }
        }

        System.out.println("Corrected " + count + " points.");

        Documents.writeDocument(TARGET_PATH, document);
        Documents.removeXmlWhitespace(TARGET_PATH);
        System.out.println("Wrote to: " + TARGET_PATH);
    }

    private static Date fromIsoString(String string) {
        return Date.from(Instant.parse(string));
    }

    private static String toIsoString(Date date) {
        String noMillis = date.toInstant().toString();
        return noMillis.replace("Z", ".000Z");
    }

    private static Date addMillis(Date date, int millis) {
        return new DateTime(date).plusMillis(millis).toDate();
    }

}
