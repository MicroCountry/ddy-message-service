/**
 *
 */
package com.ddy.message.provider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ddy.message.param.EMailMessage;
import com.ddy.message.param.EmailTarget;
import com.ddy.message.result.ResponseDTO;


/**
 * @author wgm
 */
public interface MailProvider {

    /**
     * 发送邮件
     *
     * @param eMailMessage
     *        邮件消息内容
     * @param emailTarget
     *        邮件接受者
     * @param isAsync
     *        是否异步发送.
     * @param isAttachment
     * 		      是否发送附件
     * @param delay
     * 		      延迟发送时间长度
     * @param unit
     * 		      时间单位
     */
    public void sendEMail(EMailMessage eMailMessage, EmailTarget emailTarget, boolean isAsync,boolean isAttachment,Long delay,TimeUnit unit);

    /**
     * 发送普通文本文件
     *
     * @param receiver
     * @param ccReceiver
     * @param subject
     * @param content
     * @return
     */
    public ResponseDTO<?> sendEmail(List<String> receiver, List<String> ccReceiver,
                                    String subject, String content,Long delay,TimeUnit unit);

    /**
     * 发送普通文本文件，异步
     *
     * @param receiver
     * @param ccReceiver
     * @param subject
     * @param content
     * @return
     */
    public void sendEmailAsync(List<String> receiver, List<String> ccReceiver,
                               String subject, String content,Long delay,TimeUnit unit);

    /**
     * 发送带附件的邮件
     *
     * @param receiver
     * @param ccReceiver
     * @param subject
     * @param content
     * @param affixNames
     * @param files
     * @return
     */
    public ResponseDTO<?> sendEmailWithAffix(List<String> receiver, List<String> ccReceiver,
                                             String subject, String content, List<String> affixNames, List<byte[]> files,Long delay,TimeUnit unit);

    /**
     * 发送带附件的邮件,异步
     *
     * @param receiver
     * @param ccReceiver
     * @param subject
     * @param content
     * @param affixNames
     * @param files
     * @return
     */
    public void sendEmailWithAffixAsync(List<String> receiver, List<String> ccReceiver,
                                        String subject, String content, List<String> affixNames, List<byte[]> files,Long delay,TimeUnit unit);
    
    /**
     * 发送带附件的邮件
     *
     * @param receiver
     * @param ccReceiver
     * @param subject
     * @param content
     * @param affixNames
     * @param files
     * @return
     */
    public boolean sendEmailWhithFile(final List<String> toList, final String subject, final String content, List<String> ccList,List<String> affixNames,List<String> filesPath,Long delay,TimeUnit unit);
    
    /**
     * 发送带附件的邮件 ，异步
     *
     * @param receiver
     * @param ccReceiver
     * @param subject
     * @param content
     * @param affixNames
     * @param files
     * @return
     */
    public void sendEmailWhithFileAsync(final List<String> toList, final String subject, final String content, List<String> ccList,List<String> affixNames,List<String> filesPath,Long delay,TimeUnit unit);
}
