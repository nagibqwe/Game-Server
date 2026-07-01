package com.backend.gm.result;

import com.backend.bean.Server;
import com.backend.gm.send.GMSendException;
import com.backend.utils.JsonUtils;
import com.backend.utils.Toolkit;
import com.backend.utils.TypeReference;
import com.fasterxml.jackson.core.JsonParser;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.log4j.Logger;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;

import java.util.HashMap;
import java.util.Map;

public class ResultNutMapHandler implements ResultHandler<NutMap>  {

	private static final Logger log = Logger.getLogger(ResultNutMapHandler.class);

	@Override
	public NutMap hand(Server server,String result) {
		if (Strings.isBlank(result.trim())) {
			result = "服务器连接失败";
			return Toolkit.outResult(false, result);
		}
		try {
			HashMap<String, Object> map = JsonUtils.parseObject(result, new TypeReference<HashMap<String, Object>>(){});
			if (map != null && map.size() > 0) {
				return new NutMap(map);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return Toolkit.outResult(false, result);
	}

	@Override
	public NutMap hand(Server server, Exception e) {
		log.error("gm 执行失败 {"+e.toString()+"}");
		return Toolkit.outResult(false, server.getServerIP() + ":" + server.getServerPort()	+ " connect time out!");
	}

}
