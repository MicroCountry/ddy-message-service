package com.ddy.message.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.ddy.message.param.EMailMessage;
import com.ddy.message.param.EmailTarget;
import com.ddy.message.util.FileByteTransfer;
import com.ddy.message.util.MessageUtil;

@Service
public class EmailService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${mail.ddy.own.username}")
	private String username;
	@Value("${mail.ddy.own.password}")
	private String password;
	@Value("${mail.ddy.own.server}")
	private String server;
	@Value("${mail.port}")
	private String port;
	@Value("${mail.auth}")
	private String auth;
	
	@Resource
	private TaskExecutor mailTaskExecutor;

	public void sendEmail(final EMailMessage eMailMessage, final EmailTarget emailTarget, boolean isAsync,final boolean isAttachment) {
		if (!isAsync) {
			if(!isAttachment){
				sendEmail(emailTarget.getReceiver(), eMailMessage.getSubject(), eMailMessage.getContent(), emailTarget.getCcReceiver());
			}else{
				sendEmailWhithFile(emailTarget.getReceiver(), eMailMessage.getSubject(), eMailMessage.getContent(), emailTarget.getCcReceiver(), emailTarget.getAffixNames(), emailTarget.getFilesPath());
			}
		} else {
			try {
				mailTaskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						if(!isAttachment){
							sendEmail(emailTarget.getReceiver(), eMailMessage.getSubject(), eMailMessage.getContent(), emailTarget.getCcReceiver());
						}else{
							sendEmailWhithFile(emailTarget.getReceiver(), eMailMessage.getSubject(), eMailMessage.getContent(), emailTarget.getCcReceiver(), emailTarget.getAffixNames(), emailTarget.getFilesPath());
						}
					}
				});
			} catch (Throwable e) {
				logger.warn("", e);
			}
		}
	}

	public boolean sendEmail(final List<String> toList, final String subject, final String content, List<String> ccList) {
		Session session = MessageUtil.getSession(username, password, server, port,auth);
		try {
			MimeMessage mimeMsg = MessageUtil.generateMessage(username, toList, ccList, subject, content, session);
			Transport.send(mimeMsg);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error("Email send exception", e);
			return false;
		}
	}
	
	public boolean sendEmailWhithFile(final List<String> toList, final String subject, final String content, List<String> ccList,List<String> affixNames,List<String> filesPath){
		List<byte[]> files = new ArrayList<byte[]>();
		for(String path:filesPath){
			byte[] fb=FileByteTransfer.getBytes(path);
			files.add(fb);
		}
		return this.sendEmailWithAffix(toList, subject, content, ccList, affixNames, files);
	}

	public boolean sendEmailWithAffix(final List<String> toList, final String subject, final String content, List<String> ccList,List<String> affixNames,List<byte[]> files) {
		Session session = MessageUtil.getSession(username, password, server, port,auth);
		try {
			MessageUtil.sendEmailWithAffix(username, toList, ccList, subject, content, affixNames,files, session);
			return true;
		} catch (Exception e) {
			logger.error("Email send exception", e);
			return false;
		}
	}
}
