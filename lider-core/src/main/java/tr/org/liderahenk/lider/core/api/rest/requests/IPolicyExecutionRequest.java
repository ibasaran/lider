package tr.org.liderahenk.lider.core.api.rest.requests;

import java.util.List;

import tr.org.liderahenk.lider.core.api.rest.enums.RestDNType;

public interface IPolicyExecutionRequest extends IRequest {
	
	Long getId();
	
	List<String> getDnList();
	
	RestDNType getDnType();
}