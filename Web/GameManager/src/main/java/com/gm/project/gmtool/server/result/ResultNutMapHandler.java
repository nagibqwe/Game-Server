package com.gm.project.gmtool.server.result;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ResultNutMapHandler implements ResultHandler<AjaxResult>  {

	private static Logger log = LoggerFactory.getLogger(ResultNutMapHandler.class);
	@Override
	public AjaxResult hand(TServer server, String result) {
		if (StringUtils.isBlank(result.trim())) {
			result = "Сервер"+server.getServerId()+"连接Ошибка";
			return AjaxResult.info(result).put("ok",false);
		}
		try {
			HashMap<String, Object> map = JsonUtils.parseObject(result, new TypeReference<HashMap<String, Object>>(){});
			if (map != null && map.size() > 0) {
				return AjaxResult.success(map).put("ok",true);
			}
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return AjaxResult.info(result).put("ok",false);
	}

	@Override
	public AjaxResult hand(TServer server, Exception e) {
		log.error("ID сервера:"+server.getId()+"gm 执行Ошибка {"+e.toString()+"}");
		return AjaxResult.error(server.getServerIP() + ":" + server.getServerPort() + " connect time out!");
	}
}
