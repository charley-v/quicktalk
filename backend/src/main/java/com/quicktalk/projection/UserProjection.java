package com.quicktalk.projection;

import io.swagger.annotations.ApiModelProperty;

public interface UserProjection {

	@ApiModelProperty(value = "Unique identifier of the user", required=true, example = "1")
	Integer getUserId();
	
	@ApiModelProperty(value = "Indicates name of the user", required=true, example="johnDoe")
    String getUsername();
	
	@ApiModelProperty(value = "Indicates email ID of the user", required=true, example="john@abc.com")
    String getEmail();

}
