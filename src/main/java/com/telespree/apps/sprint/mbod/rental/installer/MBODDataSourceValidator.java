/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.installer;

import com.izforge.izpack.installer.AutomatedInstallData;
import com.izforge.izpack.installer.DataValidator;
import com.telespree.common.installer.validator.DatabaseValidator;

/**
 * MBODDataSourceValidator
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2012/03/21 16:12:57 $
 * @version $Revision: #1 $
 * 
 */
public class MBODDataSourceValidator implements DataValidator {

    private DatabaseValidator databaseValidator = new DatabaseValidator(
            DatabaseValidator.DB_TYPE_ORACLE, "mbod.jdbc.host",
            "mbod.jdbc.port", "mbod.jdbc.sid", "mbod.jdbc.user",
            "mbod.jdbc.password");

    /*
     * (non-Javadoc)
     * 
     * @see com.izforge.izpack.installer.DataValidator#getDefaultAnswer()
     */
    public boolean getDefaultAnswer() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.izforge.izpack.installer.DataValidator#getErrorMessageId()
     */
    public String getErrorMessageId() {
        return databaseValidator.getErrorMessageId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.izforge.izpack.installer.DataValidator#getWarningMessageId()
     */
    public String getWarningMessageId() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.izforge.izpack.installer.DataValidator#validateData(com.izforge.izpack
     * .installer.AutomatedInstallData)
     */
    public Status validateData(AutomatedInstallData arg0) {
        return databaseValidator.validateData(arg0);
    }

}
