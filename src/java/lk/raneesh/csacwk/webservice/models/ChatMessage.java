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
@XmlType(name="ChatMessage")
public class ChatMessage {
    
    @XmlElement(name="threadId")
    private int threadId;   
    
    @XmlElement(name="messageId")
    private int messageId;
    
    @XmlElement(name="messageBody")
    private String messageBody;
    
    @XmlElement(name="messageAuthor")
    private String messageAuthor;
    
    @XmlElement(name="messageDateTime")
    private String messageDateTime;

    public ChatMessage(int threadId, int messageId, String messageBody, String messageAuthor, String messageDateTime) {
        this.threadId = threadId;        
        this.messageId = messageId;
        this.messageBody = messageBody;
        this.messageAuthor = messageAuthor;
        this.messageDateTime = messageDateTime;
    }       

    /**
     * @return the messageBody
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @param messageBody the messageBody to set
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * @return the messageAuthor
     */
    public String getMessageAuthor() {
        return messageAuthor;
    }

    /**
     * @param messageAuthor the messageAuthor to set
     */
    public void setMessageAuthor(String messageAuthor) {
        this.messageAuthor = messageAuthor;
    }

    /**
     * @return the messageDateTime
     */
    public String getMessageDateTime() {
        return messageDateTime;
    }

    /**
     * @param messageDateTime the messageDateTime to set
     */
    public void setMessageDateTime(String messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    /**
     * @return the messageId
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
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
