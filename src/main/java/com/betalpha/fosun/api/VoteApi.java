package com.betalpha.fosun.api;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class VoteApi implements Serializable {

    private static final long serialVersionUID = 3221546737125933900L;
    private Set<String> taskIds;
    private String userId;
    private String type;

}
