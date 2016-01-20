package com.iia.amasafeguard.entity;

import java.util.UUID;

/**
 * Created by antoi on 19/01/2016.
 */
public class User {

    /**
     * User id
     */
    protected long id;

    /**
     * User name
     */
    protected String login;

    /**
     * User mot de passe
     */
    protected String mdp;

    /**
     * User uuid
     */
    protected String uuid;

    /**
     * User connected or not
     */
    protected Integer is_connected;

    protected Integer created_at;

    /**
     * get User id
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * set User id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * get User login
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * set User login
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * get User mot de passe
     * @return
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * set User mot de passe
     * @param mdp
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    /**
     * get User uuid
     * @return
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * set User uuid
     * @param uuid
     */
    public void setUuid(String uuid) {
        // Generation UUID UNIQUE for User
        this.uuid =  UUID.randomUUID().toString();
    }

    /**
     * get User is_connected
     * @return true or false
     */
    public Integer getIs_connected() {
        return is_connected;
    }

    /**
     * set User is_connected
     * @param is_connected
     */
    public void setIs_connected(Integer is_connected) {
        this.is_connected = is_connected;
    }

    public Integer getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Integer created_at) {
        this.created_at = created_at;
    }

    /**
     * Method ToString
     * @return a printable representation of this object.
     */
    @Override
    public String toString() {
        return this.getLogin();
    }
}
