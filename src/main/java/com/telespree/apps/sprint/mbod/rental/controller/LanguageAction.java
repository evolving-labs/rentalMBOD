/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller;

import java.util.Locale;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.cal10n.LocLogger;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.Action;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * LanguageAction
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/11/01 00:37:30 $
 * @version $Revision: #2 $
 * 
 */
public class LanguageAction extends MbodActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(LanguageAction.class);

    private String language;

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.Action#execute()
     */
    @Override
    public String execute() throws Exception {
        if (StringUtils.hasText(language)) {
            String[] codes = Locale.getISOLanguages();
            if (ArrayUtils.contains(codes, language)) {
                getMbodSession().setLanguage(language);
            }
        }
        return Action.SUCCESS;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     *            the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

}
