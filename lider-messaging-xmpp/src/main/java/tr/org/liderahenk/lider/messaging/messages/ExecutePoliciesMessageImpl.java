package tr.org.liderahenk.lider.messaging.messages;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import tr.org.liderahenk.lider.core.api.messaging.enums.LiderMessageType;
import tr.org.liderahenk.lider.core.api.messaging.messages.IExecutePoliciesMessage;
import tr.org.liderahenk.lider.core.api.persistence.entities.IProfile;

/**
 * Default implementation for {@link IExecutePoliciesMessage}. This message is
 * sent <b>from Lider to agent</b> in order to execute specified policies. As a
 * response {@link PolicyStatusMessageImpl} will be returned.
 *
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 * @see tr.org.liderahenk.lider.messaging.messages.PolicyStatusMessageImpl
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = { "recipient" })
public class ExecutePoliciesMessageImpl implements IExecutePoliciesMessage {

	private static final long serialVersionUID = 8283628510292186821L;

	private LiderMessageType type = LiderMessageType.EXECUTE_POLICY;

	private String recipient;

	private List<IProfile> userPolicyProfiles;

	private String userPolicyVersion;

	private Long userCommandExecutionId;

	private List<IProfile> agentPolicyProfiles;

	private String agentPolicyVersion;

	private Long agentCommandExecutionId;

	private Date timestamp;

	public ExecutePoliciesMessageImpl(String recipient, List<IProfile> userPolicyProfiles, String userPolicyVersion,
			Long userCommandExecutionId, List<IProfile> agentPolicyProfiles, String agentPolicyVersion,
			Long agentCommandExecutionId, Date timestamp) {
		super();
		this.recipient = recipient;
		this.userPolicyProfiles = userPolicyProfiles;
		this.userPolicyVersion = userPolicyVersion;
		this.userCommandExecutionId = userCommandExecutionId;
		this.agentPolicyProfiles = agentPolicyProfiles;
		this.agentPolicyVersion = agentPolicyVersion;
		this.agentCommandExecutionId = agentCommandExecutionId;
		this.timestamp = timestamp;
	}

	@Override
	public LiderMessageType getType() {
		return type;
	}

	public void setType(LiderMessageType type) {
		this.type = type;
	}

	@Override
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@Override
	public List<IProfile> getUserPolicyProfiles() {
		return userPolicyProfiles;
	}

	public void setUserPolicyProfiles(List<IProfile> userPolicyProfiles) {
		this.userPolicyProfiles = userPolicyProfiles;
	}

	@Override
	public String getUserPolicyVersion() {
		return userPolicyVersion;
	}

	public void setUserPolicyVersion(String userPolicyVersion) {
		this.userPolicyVersion = userPolicyVersion;
	}

	@Override
	public Long getUserCommandExecutionId() {
		return userCommandExecutionId;
	}

	public void setUserCommandExecutionId(Long userCommandExecutionId) {
		this.userCommandExecutionId = userCommandExecutionId;
	}

	@Override
	public List<IProfile> getAgentPolicyProfiles() {
		return agentPolicyProfiles;
	}

	public void setAgentPolicyProfiles(List<IProfile> agentPolicyProfiles) {
		this.agentPolicyProfiles = agentPolicyProfiles;
	}

	@Override
	public Long getAgentCommandExecutionId() {
		return agentCommandExecutionId;
	}

	public void setAgentCommandExecutionId(Long agentCommandExecutionId) {
		this.agentCommandExecutionId = agentCommandExecutionId;
	}

	@Override
	public String getAgentPolicyVersion() {
		return agentPolicyVersion;
	}

	public void setAgentPolicyVersion(String agentPolicyVersion) {
		this.agentPolicyVersion = agentPolicyVersion;
	}

	@Override
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
