package com.betalpha.fosun.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by WangRui on 18/05/2018.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flow implements Serializable {
    private static final long serialVersionUID = 4402749555861223778L;
    private String id;
    private String name;
    private boolean current;
}
