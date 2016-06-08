package tr.org.liderahenk.lider.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.org.liderahenk.lider.core.api.configuration.IConfigurationService;
import tr.org.liderahenk.lider.core.api.messaging.enums.Protocol;

/**
 * This class provides configurations throughout the system. Configurations are
 * read from <code>${KARAF_HOME}/etc/tr.org.liderahenk.cfg</code> file.<br/>
 * 
 * The configuration service is also updated automatically when a configuration
 * property is changed via 'config:edit' or manually.
 * 
 * @author <a href="mailto:bm.volkansahin@gmail.com">volkansahin</a>
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class ConfigurationServiceImpl implements IConfigurationService {

	private static Logger logger = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

	// LDAP configuration
	private String ldapServer;
	private String ldapPort;
	private String ldapUsername;
	private String ldapPassword;
	private String ldapRootDn;
	private Boolean ldapUseSsl;
	private String ldapSearchAttributes;

	// XMPP configuration
	private String xmppHost; // host name/server name
	private Integer xmppPort;
	private String xmppUsername;
	private String xmppPassword;
	private String xmppResource;
	private String xmppServiceName; // service name / XMPP domain
	private int xmppMaxRetryConnectionCount;
	private int xmppPacketReplayTimeout;
	private Integer xmppPingTimeout;
	private Boolean xmppUseSsl;
	private String xmppFilePath;

	// Agent configuration
	private String agentLdapBaseDn;
	private String agentLdapIdAttribute;
	private String agentLdapJidAttribute;
	private String agentLdapObjectClasses;

	// User configuration
	private String userLdapBaseDn;
	private String userLdapUidAttribute;
	private String userLdapPrivilegeAttribute;
	private String userLdapObjectClasses;
	private Boolean userAuthorizationEnabled;
	private String groupLdapObjectClasses;

	// Task manager configuration
	private Long taskManagerTaskTimeout;
	private Boolean taskManagerMulticastEnabled;
	private Boolean taskManagerLogXmppMessagesEnabled;

	// Mail configuration
	private String mailAddress;
	private String mailPassword;
	private String mailHost;
	private Integer mailSmtpPort;
	private Boolean mailSmtpAuth;
	private Boolean mailSmtpStartTlsEnable;
	private Boolean mailSmtpSslEnable;
	private Integer mailSmtpConnTimeout;
	private Integer mailSmtpTimeout;
	private Integer mailSmtpWriteTimeout;

	// Hot deployment & plugin distribution configuration
	private String hotDeploymentPath;
	private Protocol agentPluginDistroProtocol;
	private String agentPluginDistroHost;
	private String agentPluginDistroUsername;
	private String agentPluginDistroPassword;
	private String agentPluginDistroPath;
	private String agentPluginDistroUrl;

	public void refresh() {
		logger.info("Configuration updated using blueprint: {}", prettyPrintConfig());
	}

	@Override
	public String toString() {
		return "ConfigurationServiceImpl [ldapServer=" + ldapServer + ", ldapPort=" + ldapPort + ", ldapUsername="
				+ ldapUsername + ", ldapPassword=" + ldapPassword + ", ldapRootDn=" + ldapRootDn + ", ldapUseSsl="
				+ ldapUseSsl + ", ldapSearchAttributes=" + ldapSearchAttributes + ", xmppHost=" + xmppHost
				+ ", xmppPort=" + xmppPort + ", xmppUsername=" + xmppUsername + ", xmppPassword=" + xmppPassword
				+ ", xmppResource=" + xmppResource + ", xmppServiceName=" + xmppServiceName
				+ ", xmppMaxRetryConnectionCount=" + xmppMaxRetryConnectionCount + ", xmppPacketReplayTimeout="
				+ xmppPacketReplayTimeout + ", xmppPingTimeout=" + xmppPingTimeout + ", xmppUseSsl=" + xmppUseSsl
				+ ", xmppFilePath=" + xmppFilePath + ", agentLdapBaseDn=" + agentLdapBaseDn + ", agentLdapIdAttribute="
				+ agentLdapIdAttribute + ", agentLdapJidAttribute=" + agentLdapJidAttribute
				+ ", agentLdapObjectClasses=" + agentLdapObjectClasses + ", userLdapBaseDn=" + userLdapBaseDn
				+ ", userLdapUidAttribute=" + userLdapUidAttribute + ", userLdapPrivilegeAttribute="
				+ userLdapPrivilegeAttribute + ", userLdapObjectClasses=" + userLdapObjectClasses
				+ ", userAuthorizationEnabled=" + userAuthorizationEnabled + ", groupLdapObjectClasses="
				+ groupLdapObjectClasses + ", taskManagerTaskTimeout=" + taskManagerTaskTimeout
				+ ", taskManagerMulticastEnabled=" + taskManagerMulticastEnabled
				+ ", taskManagerLogXmppMessagesEnabled=" + taskManagerLogXmppMessagesEnabled + ", mailAddress="
				+ mailAddress + ", mailPassword=" + mailPassword + ", mailHost=" + mailHost + ", mailSmtpPort="
				+ mailSmtpPort + ", mailSmtpAuth=" + mailSmtpAuth + ", mailSmtpStartTlsEnable=" + mailSmtpStartTlsEnable
				+ ", mailSmtpSslEnable=" + mailSmtpSslEnable + ", mailSmtpConnTimeout=" + mailSmtpConnTimeout
				+ ", mailSmtpTimeout=" + mailSmtpTimeout + ", mailSmtpWriteTimeout=" + mailSmtpWriteTimeout
				+ ", hotDeploymentPath=" + hotDeploymentPath + ", agentPluginDistroProtocol="
				+ agentPluginDistroProtocol + ", agentPluginDistroHost=" + agentPluginDistroHost
				+ ", agentPluginDistroUsername=" + agentPluginDistroUsername + ", agentPluginDistroPassword="
				+ agentPluginDistroPassword + ", agentPluginDistroPath=" + agentPluginDistroPath
				+ ", agentPluginDistroUrl=" + agentPluginDistroUrl + "]";
	}

	public String prettyPrintConfig() {
		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
		} catch (Exception e) {
		}
		return toString();
	}

	@Override
	public String getLdapServer() {
		return ldapServer;
	}

	public void setLdapServer(String ldapServer) {
		this.ldapServer = ldapServer;
	}

	@Override
	public String getLdapPort() {
		return ldapPort;
	}

	public void setLdapPort(String ldapPort) {
		this.ldapPort = ldapPort;
	}

	@Override
	public String getLdapUsername() {
		return ldapUsername;
	}

	public void setLdapUsername(String ldapUsername) {
		this.ldapUsername = ldapUsername;
	}

	@Override
	public String getLdapPassword() {
		return ldapPassword;
	}

	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}

	@Override
	public String getLdapRootDn() {
		return ldapRootDn;
	}

	public void setLdapRootDn(String ldapRootDn) {
		this.ldapRootDn = ldapRootDn;
	}

	@Override
	public Boolean getLdapUseSsl() {
		return ldapUseSsl;
	}

	public void setLdapUseSsl(Boolean ldapUseSsl) {
		this.ldapUseSsl = ldapUseSsl;
	}

	@Override
	public String getXmppHost() {
		return xmppHost;
	}

	public void setXmppHost(String xmppHost) {
		this.xmppHost = xmppHost;
	}

	@Override
	public Integer getXmppPort() {
		return xmppPort;
	}

	public void setXmppPort(Integer xmppPort) {
		this.xmppPort = xmppPort;
	}

	@Override
	public String getXmppUsername() {
		return xmppUsername;
	}

	public void setXmppUsername(String xmppUsername) {
		this.xmppUsername = xmppUsername;
	}

	@Override
	public String getXmppPassword() {
		return xmppPassword;
	}

	public void setXmppPassword(String xmppPassword) {
		this.xmppPassword = xmppPassword;
	}

	@Override
	public String getXmppResource() {
		return xmppResource;
	}

	public void setXmppResource(String xmppResource) {
		this.xmppResource = xmppResource;
	}

	@Override
	public String getXmppServiceName() {
		return xmppServiceName;
	}

	public void setXmppServiceName(String xmppServiceName) {
		this.xmppServiceName = xmppServiceName;
	}

	@Override
	public int getXmppMaxRetryConnectionCount() {
		return xmppMaxRetryConnectionCount;
	}

	public void setXmppMaxRetryConnectionCount(int xmppMaxRetryConnectionCount) {
		this.xmppMaxRetryConnectionCount = xmppMaxRetryConnectionCount;
	}

	@Override
	public int getXmppPacketReplayTimeout() {
		return xmppPacketReplayTimeout;
	}

	public void setXmppPacketReplayTimeout(int xmppPacketReplayTimeout) {
		this.xmppPacketReplayTimeout = xmppPacketReplayTimeout;
	}

	@Override
	public Integer getXmppPingTimeout() {
		return xmppPingTimeout;
	}

	public void setXmppPingTimeout(Integer xmppPingTimeout) {
		this.xmppPingTimeout = xmppPingTimeout;
	}

	@Override
	public Boolean getXmppUseSsl() {
		return xmppUseSsl;
	}

	public void setXmppUseSsl(Boolean xmppUseSsl) {
		this.xmppUseSsl = xmppUseSsl;
	}

	@Override
	public String getXmppFilePath() {
		return xmppFilePath;
	}

	public void setXmppFilePath(String xmppFilePath) {
		this.xmppFilePath = xmppFilePath;
	}

	@Override
	public String getAgentLdapBaseDn() {
		return agentLdapBaseDn;
	}

	public void setAgentLdapBaseDn(String agentLdapBaseDn) {
		this.agentLdapBaseDn = agentLdapBaseDn;
	}

	@Override
	public String getAgentLdapIdAttribute() {
		return agentLdapIdAttribute;
	}

	public void setAgentLdapIdAttribute(String agentLdapIdAttribute) {
		this.agentLdapIdAttribute = agentLdapIdAttribute;
	}

	@Override
	public String getAgentLdapJidAttribute() {
		return agentLdapJidAttribute;
	}

	public void setAgentLdapJidAttribute(String agentLdapJidAttribute) {
		this.agentLdapJidAttribute = agentLdapJidAttribute;
	}

	@Override
	public String getAgentLdapObjectClasses() {
		return agentLdapObjectClasses;
	}

	public void setAgentLdapObjectClasses(String agentLdapObjectClasses) {
		this.agentLdapObjectClasses = agentLdapObjectClasses;
	}

	@Override
	public String getUserLdapBaseDn() {
		return userLdapBaseDn;
	}

	public void setUserLdapBaseDn(String userLdapBaseDn) {
		this.userLdapBaseDn = userLdapBaseDn;
	}

	@Override
	public String getUserLdapUidAttribute() {
		return userLdapUidAttribute;
	}

	public void setUserLdapUidAttribute(String userLdapUidAttribute) {
		this.userLdapUidAttribute = userLdapUidAttribute;
	}

	@Override
	public String getUserLdapPrivilegeAttribute() {
		return userLdapPrivilegeAttribute;
	}

	public void setUserLdapPrivilegeAttribute(String userLdapPrivilegeAttribute) {
		this.userLdapPrivilegeAttribute = userLdapPrivilegeAttribute;
	}

	@Override
	public String getUserLdapObjectClasses() {
		return userLdapObjectClasses;
	}

	public void setUserLdapObjectClasses(String userLdapObjectClasses) {
		this.userLdapObjectClasses = userLdapObjectClasses;
	}

	@Override
	public Boolean getUserAuthorizationEnabled() {
		return userAuthorizationEnabled;
	}

	public void setUserAuthorizationEnabled(Boolean userAuthorizationEnabled) {
		this.userAuthorizationEnabled = userAuthorizationEnabled;
	}

	@Override
	public String getGroupLdapObjectClasses() {
		return groupLdapObjectClasses;
	}

	public void setGroupLdapObjectClasses(String groupLdapObjectClasses) {
		this.groupLdapObjectClasses = groupLdapObjectClasses;
	}

	@Override
	public Long getTaskManagerTaskTimeout() {
		return taskManagerTaskTimeout;
	}

	public void setTaskManagerTaskTimeout(Long taskManagerTaskTimeout) {
		this.taskManagerTaskTimeout = taskManagerTaskTimeout;
	}

	@Override
	public Boolean getTaskManagerMulticastEnabled() {
		return taskManagerMulticastEnabled;
	}

	public void setTaskManagerMulticastEnabled(Boolean taskManagerMulticastEnabled) {
		this.taskManagerMulticastEnabled = taskManagerMulticastEnabled;
	}

	@Override
	public Boolean getTaskManagerLogXmppMessagesEnabled() {
		return taskManagerLogXmppMessagesEnabled;
	}

	public void setTaskManagerLogXmppMessagesEnabled(Boolean taskManagerLogXmppMessagesEnabled) {
		this.taskManagerLogXmppMessagesEnabled = taskManagerLogXmppMessagesEnabled;
	}

	@Override
	public String getLdapSearchAttributes() {
		return ldapSearchAttributes;
	}

	public void setLdapSearchAttributes(String ldapSearchAttributes) {
		this.ldapSearchAttributes = ldapSearchAttributes;
	}

	@Override
	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	@Override
	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	@Override
	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	@Override
	public Integer getMailSmtpPort() {
		return mailSmtpPort;
	}

	public void setMailSmtpPort(Integer mailSmtpPort) {
		this.mailSmtpPort = mailSmtpPort;
	}

	@Override
	public Boolean getMailSmtpAuth() {
		return mailSmtpAuth;
	}

	public void setMailSmtpAuth(Boolean mailSmtpAuth) {
		this.mailSmtpAuth = mailSmtpAuth;
	}

	@Override
	public Boolean getMailSmtpStartTlsEnable() {
		return mailSmtpStartTlsEnable;
	}

	public void setMailSmtpStartTlsEnable(Boolean mailSmtpStartTlsEnable) {
		this.mailSmtpStartTlsEnable = mailSmtpStartTlsEnable;
	}

	@Override
	public Boolean getMailSmtpSslEnable() {
		return mailSmtpSslEnable;
	}

	public void setMailSmtpSslEnable(Boolean mailSmtpSslEnable) {
		this.mailSmtpSslEnable = mailSmtpSslEnable;
	}

	@Override
	public Integer getMailSmtpConnTimeout() {
		return mailSmtpConnTimeout;
	}

	public void setMailSmtpConnTimeout(Integer mailSmtpConnTimeout) {
		this.mailSmtpConnTimeout = mailSmtpConnTimeout;
	}

	@Override
	public Integer getMailSmtpTimeout() {
		return mailSmtpTimeout;
	}

	public void setMailSmtpTimeout(Integer mailSmtpTimeout) {
		this.mailSmtpTimeout = mailSmtpTimeout;
	}

	@Override
	public Integer getMailSmtpWriteTimeout() {
		return mailSmtpWriteTimeout;
	}

	public void setMailSmtpWriteTimeout(Integer mailSmtpWriteTimeout) {
		this.mailSmtpWriteTimeout = mailSmtpWriteTimeout;
	}

	@Override
	public Protocol getAgentPluginDistroProtocol() {
		return agentPluginDistroProtocol;
	}

	public void setAgentPluginDistroProtocol(String agentPluginDistroProtocol) {
		this.agentPluginDistroProtocol = agentPluginDistroProtocol != null
				? Protocol.valueOf(agentPluginDistroProtocol.toUpperCase(Locale.ENGLISH)) : null;
	}

	@Override
	public String getAgentPluginDistroHost() {
		return agentPluginDistroHost;
	}

	public void setAgentPluginDistroHost(String agentPluginDistroHost) {
		this.agentPluginDistroHost = agentPluginDistroHost;
	}

	@Override
	public String getAgentPluginDistroUsername() {
		return agentPluginDistroUsername;
	}

	public void setAgentPluginDistroUsername(String agentPluginDistroUsername) {
		this.agentPluginDistroUsername = agentPluginDistroUsername;
	}

	@Override
	public String getAgentPluginDistroPassword() {
		return agentPluginDistroPassword;
	}

	public void setAgentPluginDistroPassword(String agentPluginDistroPassword) {
		this.agentPluginDistroPassword = agentPluginDistroPassword;
	}

	@Override
	public String getAgentPluginDistroPath() {
		return agentPluginDistroPath;
	}

	public void setAgentPluginDistroPath(String agentPluginDistroPath) {
		this.agentPluginDistroPath = agentPluginDistroPath;
	}

	@Override
	public String getAgentPluginDistroUrl() {
		return agentPluginDistroUrl;
	}

	public void setAgentPluginDistroUrl(String agentPluginDistroUrl) {
		this.agentPluginDistroUrl = agentPluginDistroUrl;
	}

	@Override
	public Map<String, Object> getAgentPluginDistoParams() {
		Map<String, Object> params = new HashMap<String, Object>();
		/*
		 * If you change one of the parameter keys, DO NOT forget to change key
		 * in MissingPluginSubscriberImpl.java as well
		 */
		switch (agentPluginDistroProtocol) {
		case HTTP:
			params.put("url", agentPluginDistroUrl);
			break;
		case SSH:
			params.put("host", agentPluginDistroHost);
			params.put("username", agentPluginDistroUsername);
			params.put("password", agentPluginDistroPassword);
			params.put("path", agentPluginDistroPath);
			// TODO 'port' & 'publicKey'
			break;
		default:
			// TODO TORRENT
		}
		return params;
	}

	@Override
	public String getHotDeploymentPath() {
		return hotDeploymentPath;
	}

	public void setHotDeploymentPath(String hotDeploymentPath) {
		this.hotDeploymentPath = hotDeploymentPath;
	}

}
