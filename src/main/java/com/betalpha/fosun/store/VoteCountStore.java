package com.betalpha.fosun.store;

import com.betalpha.fosun.model.Ballot;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;
public class VoteCountStore implements Serializable {

    private static final long serialVersionUID = 1174040031190202406L;

    private VoteCountStore() {

    }

    public static final Map<String, Ballot> voteResult = Maps.newConcurrentMap();
}
