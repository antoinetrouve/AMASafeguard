package com.iia.amasafeguard.entity;

/**
 * Les 15 choix qui correspondent au configurateur d'un véhicule.
 * Utile pour retrouver toutes les informations renseignées par le client sur le site internet lors de la création de son dossier.
 * Created by alexis on 19/01/2016.
 */
public class Extension {

    /** Extension id */
    protected long id;

    /** Extension name */
    protected String name;

    /**
     * Get Extension Id
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Set Extension id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get Extension name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set Extension name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
