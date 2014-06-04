/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.cal10n.LocLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.telespree.apps.sprint.mbod.rental.model.SubscriberHistory;
import com.telespree.apps.sprint.mbod.rental.service.NewBillingService;
import com.telespree.barracuda.beans.ResponseError;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * BillingController
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/01/24 23:23:46 $
 * @version $Revision: #3 $
 * 
 */
@Controller
public class BillingController {

    private static final LocLogger log = TelespreeLogger
            .getLogger(BillingController.class);

    private NewBillingService newBillingService;

    /**
     * @return the newBillingService
     */
    public NewBillingService getNewBillingService() {
        return newBillingService;
    }

    /**
     * @param newBillingService
     *            the newBillingService to set
     */
    @Autowired
    @Qualifier("newQualutionBillingService")
    public void setNewBillingService(NewBillingService newBillingService) {
        this.newBillingService = newBillingService;
    }

    /**
     * @param httpSession
     * @param corpId
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/rest/{corpId}/{userId}/history", method = RequestMethod.GET)
    public ModelAndView getSubscriberHistory(HttpSession httpSession,
            @PathVariable("corpId") String corpId,
            @PathVariable("userId") String userId) throws Exception {

        ResponseError error = null;
        List<SubscriberHistory> historyList = getNewBillingService()
                .getSubscriberHistory(corpId, userId, null, null);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
        List<Map<String, String>> mapList = null;
        if (!CollectionUtils.isEmpty(historyList)) {
            mapList = new ArrayList<Map<String, String>>(historyList.size());
            for (SubscriberHistory subscriberHistory : historyList) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", subscriberHistory.getUserId());
                map.put("deviceId", subscriberHistory.getDeviceId());
                map.put("historyNotes", subscriberHistory.getHistoryNotes());
                map.put("historyTime", sdf.format(subscriberHistory
                        .getHistoryTime().getTime()));
                map.put("historyType", subscriberHistory.getHistoryType());
                mapList.add(map);
            }
        } else {
            mapList = new ArrayList<Map<String, String>>();
        }
        return new ModelAndView("rest").addObject("result", mapList)
                .addObject("error", error).addObject("reference", "1");
    }

}
