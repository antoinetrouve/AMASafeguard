package com.iia.amasafeguard.entity;

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
        this.uuid = uuid;
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
     * Returns a string containing a concise, human-readable description of this
     * object. Subclasses are encouraged to override this method and provide an
     * implementation that takes into account the object's type and data. The
     * default implementation is equivalent to the following expression:
     * <pre>
     *   getClass().getName() + '@' + Integer.toHexString(hashCode())</pre>
     * <p>See <a href="{@docRoot}reference/java/lang/Object.html#writing_toString">Writing a useful
     * {@code toString} method</a>
     * if you intend implementing your own {@code toString} method.
     *
     * @return a printable representation of this object.
     */
    @Override
    public String toString() {
        return this.getLogin();
    }
}
