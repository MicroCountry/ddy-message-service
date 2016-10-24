package com.ddy.message.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.util.MailSSLSocketFactory;
import com.ddy.message.exception.BusinessException;


public class MessageUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtil.class);
	public static Session getSession(final String userName, final String password, String server, String port,String auth) {
		Properties mailpro = new Properties();
		mailpro.setProperty("mail.debug", "true");
		mailpro.setProperty("mail.host", server);
		mailpro.put("mail.smtp.port", port);
		mailpro.put("mail.smtp.connectiontimeout", 25000);
		mailpro.put("mail.smtp.timeout", 25000);
		mailpro.setProperty("mail.smtp.auth",auth);
		mailpro.setProperty("mail.transport.protocol", "smtp");
		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sf.setTrustAllHosts(true);
		mailpro.put("mail.smtp.ssl.enable", "true");
		mailpro.put("mail.smtp.ssl.socketFactory", sf);
		//mailpro.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//mailpro.put("mail.smtp.socketFactory.port", port);
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};

		Session session = Session.getDefaultInstance(mailpro, authenticator);
        //session.setDebug(true);
		return session;
	}

	public static MimeMessage generateMessage(String sender, List<String> receiver, List<String> ccReceiver, String subject,
			String content, Session session) {
		MimeMessage mimeMsg = new MimeMessage(session);
		try {
			String from = "【点点盈】";
			Address fromAdd = new InternetAddress(sender, MimeUtility.encodeText(from, "gb2312", "B"));// 发件人的邮件地址
			mimeMsg.setFrom(fromAdd);
			//mimeMsg.setRecipients(Message.RecipientType.TO, new InternetAddress(receiver));
			if (CollectionUtils.isNotEmpty(receiver)) {
				if (receiver.size() > 0) {
					List<Address> l = new ArrayList<Address>();
					Address[] addreses = new Address[l.size()];
					for (String r: receiver) {
						l.add(new InternetAddress(r));
					}
					mimeMsg.setRecipients(Message.RecipientType.TO, l.toArray(addreses));
				}
			}
			String subjectFinal = subject == null ? "" : subject;
			mimeMsg.setSubject(MimeUtility.encodeText(subjectFinal, "gb2312", "B"));
			if (CollectionUtils.isNotEmpty(ccReceiver)) {
				if (ccReceiver.size() > 0) {
					List<Address> l = new ArrayList<Address>();
					Address[] addreses = new Address[l.size()];
					for (String cc : ccReceiver) {
						l.add(new InternetAddress(cc));
					}
					mimeMsg.setRecipients(Message.RecipientType.CC, l.toArray(addreses));
				}
			}
			mimeMsg.setContent(content, "text/html; charset=gbk");
		} catch (Exception e) {
			LOGGER.error("generateMessage error :", e);
			throw new BusinessException("发送邮件时生成MimeMessage失败", e);
		}
		return mimeMsg;
	}

	public static void sendEmailWithAffix(String sender, List<String> receiver, List<String> ccReceiver, String subject,
			String content, List<String> affixNames,List<byte[]> files, Session session) throws MessagingException, UnsupportedEncodingException {
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(sender));
		//msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
		if (CollectionUtils.isNotEmpty(receiver)) {
			if (receiver.size() > 0) {
				List<Address> l = new ArrayList<Address>();
				Address[] addreses = new Address[l.size()];
				for (String r: receiver) {
					l.add(new InternetAddress(r));
				}
				msg.setRecipients(Message.RecipientType.TO, l.toArray(addreses));
			}
		}
		msg.setSubject(MimeUtility.encodeText(subject, "gb2312", "B"));
		if (ccReceiver != null) {
			if (ccReceiver.size() > 0) {
				List<Address> ccReceivers = new ArrayList<Address>();
				Address[] addreses = new Address[ccReceivers.size()];
				for (String cc : ccReceiver) {
					ccReceivers.add(new InternetAddress(cc));
				}
				msg.setRecipients(Message.RecipientType.CC, ccReceivers.toArray(addreses));
			}
		}
		msg.setSentDate(new Date());
		// 添加附件必须设置邮件类型
		MimeMultipart msgMultipart = new MimeMultipart("mixed");
		msg.setContent(msgMultipart);

		MimeBodyPart contentPart = new MimeBodyPart();
		contentPart.setContent(content, "text/html; charset=gbk");
		msgMultipart.addBodyPart(contentPart);

		if (CollectionUtils.isNotEmpty(files)) {
			int index = 0;
			for (byte[] file : files) {
				MimeBodyPart eachAttachPart = new MimeBodyPart();
				msgMultipart.addBodyPart(eachAttachPart);

				// 设置附件的名称
				eachAttachPart.setFileName(affixNames.get(index++));

				// 设置附件的句柄给这个附件对象
				eachAttachPart.setDataHandler(new DataHandler(new ByteArrayDataSource(file,"text/plain")));
			}
		}

		Transport.send(msg);
	}
}
