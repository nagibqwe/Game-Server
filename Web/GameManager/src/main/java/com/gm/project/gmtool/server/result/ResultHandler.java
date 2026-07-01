package com.gm.project.gmtool.server.result;

import com.gm.project.gmtool.server.send.GMSendException;
import com.gm.project.gmtool.server.domain.TServer;

public interface ResultHandler<T> {

	 T hand(TServer server, String result) throws GMSendException;

	 T hand(TServer server, Exception e);
}
