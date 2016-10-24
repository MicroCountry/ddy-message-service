package com.ddy.message.param;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/5.
 */
public class EMailMessage implements Serializable {
    private static final long serialVersionUID = 4748551432169668482L;
    private String subject;
    private String content;

    public EMailMessage(String content) {
        this(null, content);
    }

    public EMailMessage(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public EMailMessage setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public EMailMessage setContent(String content) {
        this.content = content;
        return this;
    }
}
