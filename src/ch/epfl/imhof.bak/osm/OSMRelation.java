package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Represente une relation OSM
 * 
 * @author Hugo Hueber (246095)
 * @author Maxime Pisa (247650)
 *
 */
public final class OSMRelation extends OSMEntity {
    private final List<Member> members;

    /**
     * 
     * @param id
     *            Identifiant unique
     * @param members
     *            Membres de la classe
     * @param attributes
     *            Attributs donnes
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<>(members));
    }

    /**
     * 
     * @return La liste des membres de la relation
     */
    public List<Member> members() {
        return this.members;
    }

    /**
     * Represente un membre d'une relation OSM
     * 
     * @author Hugo Hueber (246095)
     * @author Maxime Pisa (247650)
     *
     */
    public static final class Member {
        private final Type type;
        private final String role;
        private final OSMEntity member;

        /**
         * 
         * @param type
         *            Le type du membre
         * @param role
         *            Le role du membre
         * @param member
         *            La valeur du membre
         */
        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        /**
         * 
         * @return Le type du membre
         */
        public Type type() {
            return this.type;
        }

        /**
         * 
         * @return Le role du membre
         */
        public String role() {
            return this.role;
        }

        /**
         * 
         * @return Le membre lui-meme
         */
        public OSMEntity member() {
            return this.member;
        }

        /**
         * Enumeration des differents types
         * 
         * @author Hugo Hueber (246095)
         * @author Maxime Pisa (247650)
         *
         */
        public static enum Type {
            NODE, WAY, RELATION;
        }
    }

    /**
     * Batisseur de la classe OSMRelation
     * 
     * @author Hugo Hueber (246095)
     * @author Maxime Pisa (247650)
     *
     */
    public static final class Builder extends OSMEntity.Builder {
        private List<Member> members;

        /**
         * 
         * @param id
         *            Identifiant unique
         */
        public Builder(long id) {
            super(id);
            this.members = new ArrayList<Member>();
        }

        /**
         * Ajoute un nouveau membre
         * 
         * @param type
         *            Type du membre
         * @param role
         *            Role du membre
         * @param newMember
         *            Valeur du membre
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            this.members.add(new Member(type, role, newMember));
        }

        /**
         * (non-Javadoc)
         * 
         * @see ch.epfl.imhof.osm.OSMEntity.Builder#build()
         */
        public OSMRelation build() throws IllegalStateException {
            if (this.isIncomplete()) {
                throw new IllegalStateException("OSMRelation incomplete");
            } else {
                return new OSMRelation(this.getId(), members, this.getBuilder()
                        .build());
            }
        }
    }
}
