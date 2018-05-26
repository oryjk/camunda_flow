package com.betalpha.fosun.model;

import com.google.common.collect.Sets;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class Ballot implements Serializable {

    private static final long serialVersionUID = -2549330207692318924L;
    private Set<String> approve = Sets.newConcurrentHashSet();
    private Set<String> reject = Sets.newConcurrentHashSet();
    private Set<String> continues = Sets.newConcurrentHashSet();

}
