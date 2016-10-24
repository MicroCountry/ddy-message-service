package com.ddy.message.param;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/5.
 */
public class EmailTarget implements Serializable {

    private static final long serialVersionUID = 8975207786550238649L;

    private List<String> receiver;//接收人
    private List<String> ccReceiver;//抄送人
    private List<String> affixNames;//附件名
    private List<String> filesPath;//附件路径

    public List<String> getAffixNames() {
		return affixNames;
	}

	public void setAffixNames(List<String> affixNames) {
		this.affixNames = affixNames;
	}

	public List<String> getFilesPath() {
		return filesPath;
	}

	public void setFilesPath(List<String> filesPath) {
		this.filesPath = filesPath;
	}

	public EmailTarget(List<String> receiver) {
        this.receiver = receiver;
    }

    public List<String> getReceiver() {
        return receiver;
    }

    public EmailTarget setReceiver(List<String> receiver) {
        this.receiver = receiver;
        return this;
    }

    public List<String> getCcReceiver() {
        return ccReceiver;
    }

    public EmailTarget setCcReceiver(List<String> ccReceiver) {
        this.ccReceiver = ccReceiver;
        return this;
    }
}
