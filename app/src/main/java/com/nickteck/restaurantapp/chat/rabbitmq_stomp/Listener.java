package com.nickteck.restaurantapp.chat.rabbitmq_stomp;

import java.util.Map;

/**
 * (c)2005 Sean Russell
 */
public interface Listener {
  public void message( Map headers, String body );
}
