/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.pojo;

/**
 *
 * @author cominfo4
 */
public class RelationEdge {

    private long rzsj;
    private long ldsj;
    private String fh;

    private SourceNode sourceNode;
    private TargetNode targetNode;

    public RelationEdge() {
    }

    public RelationEdge(long p_rzsj, long p_ldsj, String p_fh, SourceNode p_sourceNode, TargetNode p_targetNode) {
        this.rzsj = p_rzsj;
        this.ldsj = p_ldsj;
        this.fh = p_fh;
        this.sourceNode = p_sourceNode;
        this.targetNode = p_targetNode;
    }

    /**
     * @return the rzsj
     */
    public long getRzsj() {
        return rzsj;
    }

    /**
     * @param rzsj the rzsj to set
     */
    public void setRzsj(long rzsj) {
        this.rzsj = rzsj;
    }

    /**
     * @return the ldsj
     */
    public long getLdsj() {
        return ldsj;
    }

    /**
     * @param ldsj the ldsj to set
     */
    public void setLdsj(long ldsj) {
        this.ldsj = ldsj;
    }

    /**
     * @return the fh
     */
    public String getFh() {
        return fh;
    }

    /**
     * @param fh the fh to set
     */
    public void setFh(String fh) {
        this.fh = fh;
    }

    /**
     * @return the sourceNode
     */
    public SourceNode getSourceNode() {
        return sourceNode;
    }

    /**
     * @param sourceNode the sourceNode to set
     */
    public void setSourceNode(SourceNode sourceNode) {
        this.sourceNode = sourceNode;
    }

    /**
     * @return the targetNode
     */
    public TargetNode getTargetNode() {
        return targetNode;
    }

    /**
     * @param targetNode the targetNode to set
     */
    public void setTargetNode(TargetNode targetNode) {
        this.targetNode = targetNode;
    }

}
