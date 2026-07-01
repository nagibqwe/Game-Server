package com.backend.gm.result;


import com.backend.bean.Server;
import com.backend.gm.send.GMSendException;

public interface ResultHandler<T> {

	 T hand(Server server, String result) throws GMSendException;

	 T hand(Server server, Exception e);
}
