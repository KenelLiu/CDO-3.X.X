/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.4.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.cdoframework.cdolib.database.xsd.types;

/**
 * Enumeration SQLTransPropagationType.
 * 
 * @version $Revision$ $Date$
 */
public enum SQLTransPropagationType {


      //------------------/
     //- Enum Constants -/
    //------------------/

    /**
     * Constant REQUIRED
     */
	REQUIRED("REQUIRED"),
    /**
     * Constant REQUIRED
     */
	SUPPORTS("SUPPORTS"),	
    /**
     * Constant REQUIRED
     */
	MANDATORY("MANDATORY"),		
    /**
     * Constant REQUIRES_NEW
     */
	REQUIRES_NEW("REQUIRES_NEW"),
    /**
     * Constant NOT_SUPPORTED
     */
	NOT_SUPPORTED("NOT_SUPPORTED"),
    /**
     * Constant NOT_SUPPORTED
     */
	NEVER("NEVER"),		
    /**
     * Constant NESTED
     */
    NESTED("NESTED");
    /**
     * Field value.
     */
    private final java.lang.String value;

    /**
     * Field enumConstants.
     */
    private static final java.util.Map<java.lang.String, SQLTransPropagationType> enumConstants = new java.util.HashMap<java.lang.String, SQLTransPropagationType>();


    static {
        for (SQLTransPropagationType c: SQLTransPropagationType.values()) {
            SQLTransPropagationType.enumConstants.put(c.value, c);
        }

    }

    private SQLTransPropagationType(final java.lang.String value) {
        this.value = value;
    }

    /**
     * Method fromValue.
     * 
     * @param value
     * @return the constant for this value
     */
    public static com.cdoframework.cdolib.database.xsd.types.SQLTransPropagationType fromValue(final java.lang.String value) {
        SQLTransPropagationType c = SQLTransPropagationType.enumConstants.get(value);
        if (c != null) {
            return c;
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * 
     * 
     * @param value
     */
    public void setValue(final java.lang.String value) {
    }

    /**
     * Method toString.
     * 
     * @return the value of this constant
     */
    public java.lang.String toString() {
        return this.value;
    }

    /**
     * Method value.
     * 
     * @return the value of this constant
     */
    public java.lang.String value() {
        return this.value;
    }

}
