package tr.org.liderahenk.lider.service.requests;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import tr.org.liderahenk.lider.core.api.persistence.entities.IMailContent;
import tr.org.liderahenk.lider.core.api.persistence.entities.IPlugin;


@JsonIgnoreProperties(ignoreUnknown = true)
public class MailContentReqImpl implements IMailContent{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 53740692634262658L;
	private Long id;
	private String mailContent;
	
	private PluginRequestImpl plugin;

	private Date createDate;

	private Date modifyDate;
	
	private boolean deleted = false;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public PluginRequestImpl getPlugin() {
		return plugin;
	}
	public void setPlugin(PluginRequestImpl plugin) {
		this.plugin = plugin;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
