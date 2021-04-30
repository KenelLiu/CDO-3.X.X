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
public class Switch implements java.io.Serializable {

    /**
     * Field var.
     */
    private java.lang.String var;

    /**
     * Field caseNull.
     */
    private com.cdoframework.cdolib.database.xsd.CaseNull caseNull;

    private java.util.List<com.cdoframework.cdolib.database.xsd.Case> _caseList;

    /**
     * Field _default.
     */
    private com.cdoframework.cdolib.database.xsd.Default _default;

    public Switch() {
        super();
        this._caseList = new java.util.ArrayList<com.cdoframework.cdolib.database.xsd.Case>();
    }

    /**
     * 
     * 
     * @param vCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCase(final com.cdoframework.cdolib.database.xsd.Case vCase) throws java.lang.IndexOutOfBoundsException {
        this._caseList.add(vCase);
    }

    /**
     * 
     * 
     * @param index
     * @param vCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCase(final int index,final com.cdoframework.cdolib.database.xsd.Case vCase) throws java.lang.IndexOutOfBoundsException {
        this._caseList.add(index, vCase);
    }

    /**
     * Method enumerateCase.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<? extends com.cdoframework.cdolib.database.xsd.Case> enumerateCase() {
        return java.util.Collections.enumeration(this._caseList);
    }

    /**
     * Method getCase.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.cdoframework.cdolib.database.xsd.Case at the given index
     */
    public com.cdoframework.cdolib.database.xsd.Case getCase(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._caseList.size()) {
            throw new IndexOutOfBoundsException("getCase: Index value '" + index + "' not in range [0.." + (this._caseList.size() - 1) + "]");
        }

        return _caseList.get(index);
    }

    /**
     * Method getCase.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.cdoframework.cdolib.database.xsd.Case[] getCase() {
        com.cdoframework.cdolib.database.xsd.Case[] array = new com.cdoframework.cdolib.database.xsd.Case[0];
        return this._caseList.toArray(array);
    }

    /**
     * Method getCaseCount.
     * 
     * @return the size of this collection
     */
    public int getCaseCount() {
        return this._caseList.size();
    }

    /**
     * Returns the value of field 'caseNull'.
     * 
     * @return the value of field 'CaseNull'.
     */
    public com.cdoframework.cdolib.database.xsd.CaseNull getCaseNull() {
        return this.caseNull;
    }

    /**
     * Returns the value of field 'default'.
     * 
     * @return the value of field 'Default'.
     */
    public com.cdoframework.cdolib.database.xsd.Default getDefault() {
        return this._default;
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
     * Method iterateCase.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<? extends com.cdoframework.cdolib.database.xsd.Case> iterateCase() {
        return this._caseList.iterator();
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
    public void removeAllCase() {
        this._caseList.clear();
    }

    /**
     * Method removeCase.
     * 
     * @param vCase
     * @return true if the object was removed from the collection.
     */
    public boolean removeCase(final com.cdoframework.cdolib.database.xsd.Case vCase) {
        boolean removed = _caseList.remove(vCase);
        return removed;
    }

    /**
     * Method removeCaseAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.cdoframework.cdolib.database.xsd.Case removeCaseAt(final int index) {
        java.lang.Object obj = this._caseList.remove(index);
        return (com.cdoframework.cdolib.database.xsd.Case) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCase(final int index,final com.cdoframework.cdolib.database.xsd.Case vCase) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._caseList.size()) {
            throw new IndexOutOfBoundsException("setCase: Index value '" + index + "' not in range [0.." + (this._caseList.size() - 1) + "]");
        }

        this._caseList.set(index, vCase);
    }

    /**
     * 
     * 
     * @param vCaseArray
     */
    public void setCase(final com.cdoframework.cdolib.database.xsd.Case[] vCaseArray) {
        //-- copy array
        _caseList.clear();

        for (int i = 0; i < vCaseArray.length; i++) {
                this._caseList.add(vCaseArray[i]);
        }
    }

    /**
     * Sets the value of field 'caseNull'.
     * 
     * @param caseNull the value of field 'caseNull'.
     */
    public void setCaseNull(final com.cdoframework.cdolib.database.xsd.CaseNull caseNull) {
        this.caseNull = caseNull;
    }

    /**
     * Sets the value of field 'default'.
     * 
     * @param _default
     * @param default the value of field 'default'.
     */
    public void setDefault(final com.cdoframework.cdolib.database.xsd.Default _default) {
        this._default = _default;
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
     * com.cdoframework.cdolib.database.xsd.Switch
     */
    public static com.cdoframework.cdolib.database.xsd.Switch unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.cdoframework.cdolib.database.xsd.Switch) org.exolab.castor.xml.Unmarshaller.unmarshal(com.cdoframework.cdolib.database.xsd.Switch.class, reader);
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
