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

import static java.lang.Math.toRadians;

import static ch.epfl.imhof.osm.OSMRelation.Member;

/**
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 * 
 *         Reader de OSMMap
 */

public final class OSMMapReader {

    /**
     * Lis un fichier xml et retourne une OSMMap
     */
    private OSMMapReader() {
    }

    /**
     * Lis un fichier XML et retourne une OSMMap
     * 
     * @param fileName
     *            L'emplacement du fichier
     * @param unGZip
     *            Vrai=Fichier GZIP
     * @return L'OSMMap correspondante au fichier passe en parametre
     * @throws IOException
     *             Si le fichier n'arrive pas a etre lu
     * @throws SAXException
     *             Si le fichier est corrompu
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip)
            throws IOException, SAXException {

        try (InputStream stream = unGZip ? new GZIPInputStream(
                new FileInputStream(fileName)) : new FileInputStream(fileName)) {

            OSMMap.Builder mapBuilder = new OSMMap.Builder();

            XMLReader reader = XMLReaderFactory.createXMLReader();

            reader.setContentHandler(new DefaultHandler() {
                // Declaration
                OSMNode.Builder nodeBuilder;
                OSMWay.Builder wayBuilder;
                OSMRelation.Builder relationBuilder;

                String state = "";

                /**
                 * Analyse la balise d'ouverture
                 */
                @Override
                public void startElement(String uri, String lName,
                        String qName, Attributes atts) throws SAXException {
                    switch (qName) {
                    case "node":
                        state = "node";

                        nodeBuilder = new OSMNode.Builder(Long.parseLong(atts
                                .getValue("id")), new PointGeo(toRadians(Double
                                .parseDouble(atts.getValue("lon"))),
                                toRadians(Double.parseDouble(atts
                                        .getValue("lat")))));

                        break;
                    case "way":
                        state = "way";

                        wayBuilder = new OSMWay.Builder(Long.parseLong(atts
                                .getValue("id")));

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

                        relationBuilder = new OSMRelation.Builder(Long
                                .parseLong(atts.getValue("id")));

                        break;
                    case "member":
                        switch (atts.getValue("type")) {
                        case "node":
                            if (mapBuilder.nodeForId(Long.parseLong(atts
                                    .getValue("ref"))) != null) {
                                relationBuilder.addMember(Member.Type.NODE,
                                        atts.getValue("role"), mapBuilder
                                                .nodeForId(Long.parseLong(atts
                                                        .getValue("ref"))));
                            } else {
                                relationBuilder.setIncomplete();
                            }

                            break;
                        case "way":
                            if (mapBuilder.wayForId(Long.parseLong(atts
                                    .getValue("ref"))) != null) {
                                relationBuilder.addMember(Member.Type.WAY, atts
                                        .getValue("role"), mapBuilder
                                        .wayForId(Long.parseLong(atts
                                                .getValue("ref"))));
                            } else {
                                relationBuilder.setIncomplete();
                            }

                            break;
                        case "relation":
                            if (mapBuilder.relationForId(Long.parseLong(atts
                                    .getValue("ref"))) != null) {
                                relationBuilder.addMember(
                                        Member.Type.RELATION,
                                        atts.getValue("role"),
                                        mapBuilder.relationForId(Long
                                                .parseLong(atts.getValue("ref"))));
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

                /**
                 * Analyse la balise de fermeture
                 */
                @Override
                public void endElement(String uri, String lName, String qName) {
                    switch (qName) {
                    case "node":
                        state = "";

                        mapBuilder.addNode(nodeBuilder.build());

                        break;
                    case "way":
                        state = "";

                        if (!wayBuilder.isIncomplete()) {
                            mapBuilder.addWay(wayBuilder.build());
                        }

                        break;
                    case "relation":
                        state = "";

                        if (!relationBuilder.isIncomplete()) {
                            mapBuilder.addRelation(relationBuilder.build());
                        }

                        break;
                    default:
                        break;
                    }
                }
            });

            reader.parse(new InputSource(stream));

            return mapBuilder.build();
        }
    }

}
