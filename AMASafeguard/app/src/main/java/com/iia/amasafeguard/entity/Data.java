package com.iia.amasafeguard.entity;

/**
 * Les 15 choix qui correspondent au configurateur d'un véhicule.
 * Utile pour retrouver toutes les informations renseignées par le client sur le site internet lors de la création de son dossier.
 * Created by alexis on 19/01/2016.
 */
public class Data {

    /** Data Id */
    protected long id;

    /** Data Name */
    protected String name;

    /** Data Path */
    protected String path;

    /** Data Created_at */
    protected Integer created_at;

    /** Data Updated_at */
    protected Integer updated_at;

    /**
     * Get Data Id
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Set Data Id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get Data Name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set Data Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Data Created_at
     * @return created_at
     */
    public Integer getCreated_at() {
        return created_at;
    }

    /**
     * Set Data Created_at
     * @param created_at
     */
    public void setCreated_at(Integer created_at) {
        this.created_at = created_at;
    }

    /**
     * Get Data Path
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set Data Path
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Get Data Updated_at
     * @return updated_at
     */
    public Integer getUpdated_at() {
        return updated_at;
    }

    /**
     * Set Data Updated_at
     * @param updated_at
     */
    public void setUpdated_at(Integer updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
