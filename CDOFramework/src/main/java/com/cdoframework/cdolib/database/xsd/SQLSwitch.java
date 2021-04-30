/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.4.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cdoframework.cdolib.database.xsd;

/**
 * 
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class SQLSwitch implements java.io.Serializable {

    /**
     * Field var.
     */
    private java.lang.String var;

    /**
     * Field SQLCaseNull.
     */
    private com.cdoframework.cdolib.database.xsd.SQLCaseNull SQLCaseNull;

    private java.util.List<com.cdoframework.cdolib.database.xsd.SQLCase> SQLCaseList;

    /**
     * Field SQLDefault.
     */
    private com.cdoframework.cdolib.database.xsd.SQLDefault SQLDefault;

    public SQLSwitch() {
        super();
        this.SQLCaseList = new java.util.ArrayList<com.cdoframework.cdolib.database.xsd.SQLCase>();
    }

    /**
     * 
     * 
     * @param vSQLCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSQLCase(final com.cdoframework.cdolib.database.xsd.SQLCase vSQLCase) throws java.lang.IndexOutOfBoundsException {
        this.SQLCaseList.add(vSQLCase);
    }

    /**
     * 
     * 
     * @param index
     * @param vSQLCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSQLCase(final int index,final com.cdoframework.cdolib.database.xsd.SQLCase vSQLCase) throws java.lang.IndexOutOfBoundsException {
        this.SQLCaseList.add(index, vSQLCase);
    }

    /**
     * Method enumerateSQLCase.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.cdoframework.cdolib.database.xsd.SQLCase> enumerateSQLCase() {
        return java.util.Collections.enumeration(this.SQLCaseList);
    }

    /**
     * Method getSQLCase.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.cdoframework.cdolib.database.xsd.SQLCase at the given
     * index
     */
    public com.cdoframework.cdolib.database.xsd.SQLCase getSQLCase(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.SQLCaseList.size()) {
            throw new IndexOutOfBoundsException("getSQLCase: Index value '" + index + "' not in range [0.." + (this.SQLCaseList.size() - 1) + "]");
        }

        return SQLCaseList.get(index);
    }

    /**
     * Method getSQLCase.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.cdoframework.cdolib.database.xsd.SQLCase[] getSQLCase() {
        com.cdoframework.cdolib.database.xsd.SQLCase[] array = new com.cdoframework.cdolib.database.xsd.SQLCase[0];
        return this.SQLCaseList.toArray(array);
    }

    /**
     * Method getSQLCaseCount.
     * 
     * @return the size of this collection
     */
    public int getSQLCaseCount() {
        return this.SQLCaseList.size();
    }

    /**
     * Returns the value of field 'SQLCaseNull'.
     * 
     * @return the value of field 'SQLCaseNull'.
     */
    public com.cdoframework.cdolib.database.xsd.SQLCaseNull getSQLCaseNull() {
        return this.SQLCaseNull;
    }

    /**
     * Returns the value of field 'SQLDefault'.
     * 
     * @return the value of field 'SQLDefault'.
     */
    public com.cdoframework.cdolib.database.xsd.SQLDefault getSQLDefault() {
        return this.SQLDefault;
    }

    /**
     * Returns the value of field 'var'.
     * 
     * @return the value of field 'Var'.
     */
    public java.lang.String getVar() {
        return this.var;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Method iterateSQLCase.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.cdoframework.cdolib.database.xsd.SQLCase> iterateSQLCase() {
        return this.SQLCaseList.iterator();
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllSQLCase() {
        this.SQLCaseList.clear();
    }

    /**
     * Method removeSQLCase.
     * 
     * @param vSQLCase
     * @return true if the object was removed from the collection.
     */
    public boolean removeSQLCase(final com.cdoframework.cdolib.database.xsd.SQLCase vSQLCase) {
        boolean removed = SQLCaseList.remove(vSQLCase);
        return removed;
    }

    /**
     * Method removeSQLCaseAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.cdoframework.cdolib.database.xsd.SQLCase removeSQLCaseAt(final int index) {
        java.lang.Object obj = this.SQLCaseList.remove(index);
        return (com.cdoframework.cdolib.database.xsd.SQLCase) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vSQLCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSQLCase(final int index,final com.cdoframework.cdolib.database.xsd.SQLCase vSQLCase) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this.SQLCaseList.size()) {
            throw new IndexOutOfBoundsException("setSQLCase: Index value '" + index + "' not in range [0.." + (this.SQLCaseList.size() - 1) + "]");
        }

        this.SQLCaseList.set(index, vSQLCase);
    }

    /**
     * 
     * 
     * @param vSQLCaseArray
     */
    public void setSQLCase(final com.cdoframework.cdolib.database.xsd.SQLCase[] vSQLCaseArray) {
        //-- copy array
        SQLCaseList.clear();

        for (int i = 0; i < vSQLCaseArray.length; i++) {
                this.SQLCaseList.add(vSQLCaseArray[i]);
        }
    }

    /**
     * Sets the value of field 'SQLCaseNull'.
     * 
     * @param SQLCaseNull the value of field 'SQLCaseNull'.
     */
    public void setSQLCaseNull(final com.cdoframework.cdolib.database.xsd.SQLCaseNull SQLCaseNull) {
        this.SQLCaseNull = SQLCaseNull;
    }

    /**
     * Sets the value of field 'SQLDefault'.
     * 
     * @param SQLDefault the value of field 'SQLDefault'.
     */
    public void setSQLDefault(final com.cdoframework.cdolib.database.xsd.SQLDefault SQLDefault) {
        this.SQLDefault = SQLDefault;
    }

    /**
     * Sets the value of field 'var'.
     * 
     * @param var the value of field 'var'.
     */
    public void setVar(final java.lang.String var) {
        this.var = var;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * com.cdoframework.cdolib.database.xsd.SQLSwitch
     */
    public static com.cdoframework.cdolib.database.xsd.SQLSwitch unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.cdoframework.cdolib.database.xsd.SQLSwitch) org.exolab.castor.xml.Unmarshaller.unmarshal(com.cdoframework.cdolib.database.xsd.SQLSwitch.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
