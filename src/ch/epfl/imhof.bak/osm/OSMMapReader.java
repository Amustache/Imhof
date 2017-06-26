/*
 *	Author:      Pisa Maxime
 *	Date:        9 mars 2015
 */

package ch.epfl.imhof.osm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

/**
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 *
 */

public final class OSMMapReader {

    /**
     * Lis un fichier xml et retourne une OSMMap
     */
    private OSMMapReader() {
    }

    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {

        OSMMap.Builder mapBuilder = new OSMMap.Builder();

        InputStream i = new FileInputStream(fileName);
        XMLReader r = XMLReaderFactory.createXMLReader();
        GZIPInputStream g = null;

        if (unGZip) {
            g = new GZIPInputStream(i);
        }

        r.setContentHandler(new DefaultHandler() {

            // déclarations perso
            OSMNode.Builder nodeBuilder;
            OSMWay.Builder wayBuilder;
            OSMRelation.Builder relationBuilder;

            String state = "";

            @Override
            public void startElement(String uri, String lName, String qName,
                    Attributes atts) throws SAXException {

                double lon;
                double lat;

                long idNode;
                long idWay;
                long idRelation;

                switch (qName) {
                case "node":

                    state = "node";

                    lon = Double.parseDouble(atts.getValue("lon"));
                    lat = Double.parseDouble(atts.getValue("lat"));
                    idNode = Long.parseLong(atts.getValue("id"));

                    PointGeo pointGeo = new PointGeo(Math.toRadians(lon), Math
                            .toRadians(lat));
                    nodeBuilder = new OSMNode.Builder(idNode, pointGeo);
                    break;

                case "way":

                    state = "way";

                    idWay = Long.parseLong(atts.getValue("id"));

                    wayBuilder = new OSMWay.Builder(idWay);
                    break;

                case "nd":
                    if (mapBuilder.nodeForId(Long.parseLong(atts
                            .getValue("ref"))) == null) {
                        wayBuilder.setIncomplete();
                    } else {
                        wayBuilder.addNode(mapBuilder.nodeForId(Long
                                .parseLong(atts.getValue("ref"))));
                    }
                    break;

                case "tag":

                    switch (state) {
                    case "node":

                        nodeBuilder.setAttribute(atts.getValue("k"),
                                atts.getValue("v"));

                        break;
                    case "way":
                        wayBuilder.setAttribute(atts.getValue("k"),
                                atts.getValue("v"));

                        break;
                    case "relation":

                        relationBuilder.setAttribute(atts.getValue("k"),
                                atts.getValue("v"));

                        break;

                    default:
                        break;
                    }

                    break;

                case "relation":

                    state = "relation";
                    idRelation = Long.parseLong(atts.getValue("id"));
                    relationBuilder = new OSMRelation.Builder(idRelation);

                    break;

                case "member":
                    switch (atts.getValue("type")) {
                    case "node":
                        if (mapBuilder.nodeForId(Long.parseLong(atts
                                .getValue("ref"))) != null) {
                            relationBuilder
                                    .addMember(
                                            ch.epfl.imhof.osm.OSMRelation.Member.Type.NODE,
                                            atts.getValue("role"),
                                            mapBuilder.nodeForId(Long
                                                    .parseLong(atts
                                                            .getValue("ref"))));
                        } else {
                            relationBuilder.setIncomplete();
                        }

                        break;

                    case "way":
                        if (mapBuilder.wayForId(Long.parseLong(atts
                                .getValue("ref"))) != null) {
                            relationBuilder
                                    .addMember(
                                            ch.epfl.imhof.osm.OSMRelation.Member.Type.WAY,
                                            atts.getValue("role"),
                                            mapBuilder.wayForId(Long
                                                    .parseLong(atts
                                                            .getValue("ref"))));
                        } else {
                            relationBuilder.setIncomplete();
                        }

                        break;

                    case "relation":
                        if (mapBuilder.relationForId(Long.parseLong(atts
                                .getValue("ref"))) != null) {
                            relationBuilder
                                    .addMember(
                                            ch.epfl.imhof.osm.OSMRelation.Member.Type.RELATION,
                                            atts.getValue("role"),
                                            mapBuilder.relationForId(Long
                                                    .parseLong(atts
                                                            .getValue("ref"))));
                        } else {
                            relationBuilder.setIncomplete();
                        }

                        break;

                    default:
                        relationBuilder.setIncomplete();
                        break;
                    }

                default:
                    break;
                }
            }

            // ******************************************************************************************************************************************************

            @Override
            public void endElement(String uri, String lName, String qName) {

                switch (qName) {
                case "node":
                    mapBuilder.addNode(nodeBuilder.build());
                    state = "";
                    break;

                case "way":
                    if (!wayBuilder.isIncomplete()) {
                        mapBuilder.addWay(wayBuilder.build());
                    }
                    state = "";

                    break;
                case "relation":
                    if (!relationBuilder.isIncomplete()) {
                        mapBuilder.addRelation(relationBuilder.build());
                    }
                    state = "";

                    break;

                default:
                    break;
                }
            }
        });

        if (unGZip) {
            r.parse(new InputSource(g));
        } else {
            r.parse(new InputSource(i));
        }
        return mapBuilder.build();
    }

}
