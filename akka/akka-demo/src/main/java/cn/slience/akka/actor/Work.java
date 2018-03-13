/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.akka.actor;

import java.io.Serializable;

/**
 *
 * @author breeze
 */
public final class Work implements Serializable {
  private static final long serialVersionUID = 1L;
  public final String payload;
  public Work(String payload) {
    this.payload = payload;
  }
}
