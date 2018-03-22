package com.nickteck.restaurantapp.chat.rabbitmq_stomp;

import java.util.Map;

/**
 * (c)2005 Sean Russell
 */
public interface Authenticatable extends MessageReceiver {
  public void error( Map headers, String b );
  public Object token();
}

