/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.raneesh.csacwk.webservice.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Raneesh Gomez
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ChatThread")
public class ChatThread {
    
    @XmlElement(name="threadId")
    private int threadId;
    
    @XmlElement(name="threadTitle")
    private String threadTitle;
    
    @XmlElement(name="threadCreator")
    private String threadCreator;
    
    @XmlElement(name="threadDateTime")
    private String threadDateTime;

    public ChatThread(int threadId, String threadTitle, String threadCreator, String threadDateTime) {
        this.threadId = threadId;
        this.threadTitle = threadTitle;
        this.threadCreator = threadCreator;
        this.threadDateTime = threadDateTime;
    }   
    
    

    /**
     * @return the threadTitle
     */
    public String getThreadTitle() {
        return threadTitle;
    }

    /**
     * @param threadTitle the threadTitle to set
     */
    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    /**
     * @return the threadCreator
     */
    public String getThreadCreator() {
        return threadCreator;
    }

    /**
     * @param threadCreator the threadCreator to set
     */
    public void setThreadCreator(String threadCreator) {
        this.threadCreator = threadCreator;
    }

    /**
     * @return the threadDateTime
     */
    public String getThreadDateTime() {
        return threadDateTime;
    }

    /**
     * @param threadDateTime the threadDateTime to set
     */
    public void setThreadDateTime(String threadDateTime) {
        this.threadDateTime = threadDateTime;
    }

    /**
     * @return the threadId
     */
    public int getThreadId() {
        return threadId;
    }

    /**
     * @param threadId the threadId to set
     */
    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
    
    
    
}
