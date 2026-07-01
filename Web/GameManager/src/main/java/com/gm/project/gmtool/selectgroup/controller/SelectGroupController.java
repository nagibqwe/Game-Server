package com.gm.project.gmtool.selectgroup.controller;

import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.selectgroup.service.ISelectGroupService;
import com.gm.project.gmtool.server.domain.TServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务器组Controller
 */
@Controller
@RequestMapping("/gmtool/selectgroup")
public class SelectGroupController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(SelectGroupController.class);

	@Resource
	private ISelectGroupService selectGroupService;

	/**
	 * 查询平台分组标识
	 * @return
	 */
	@PostMapping("/selectServerGroup")
	@ResponseBody
	public TableDataInfo selectServerGroup() {
		List<String> list = selectGroupService.selectServerGroup();
		return getDataTable(list);
	}

	/**
	 * 查询游戏服务器列表 包含正式服和测试服
	 * @param groupName 平台分组标识
	 * @param ignoreMerge 忽略合服 0不忽略合服， 1忽略合服
	 * @return
	 */
	@PostMapping("/gameServer")
	@ResponseBody
	public TableDataInfo gameServer(String groupName, int ignoreMerge) {
		List<TServer> list = selectGroupService.selectServerList(groupName, ignoreMerge, "0,1");
		return getDataTable(list);
	}

	/**
	 * 查询游戏服务器列表 包含正式服、测试服和战斗服
	 * @param groupName 平台分组标识
	 * @return
	 */
	@PostMapping("/gameAndFightServer")
	@ResponseBody
	public TableDataInfo gameAndFightServer(String groupName, int ignoreMerge) {
		List<TServer> list = selectGroupService.selectServerList(groupName, ignoreMerge, "0,1,4");
		return getDataTable(list);
	}

	/**
	 * 查询游戏服日志库列表 包含正式服和测试服
	 */
	@PostMapping("/selectServerList")
	@ResponseBody
	public TableDataInfo selectServerList(String groupName, int ignoreMerge,String serverTypeList) {
		List<TServer> list = selectGroupService.selectServerList(groupName,ignoreMerge,serverTypeList);
		return getDataTable(list);
	}

	/**
	 * 查询游戏服务器列表
	 * @param groupName 平台分组标识
	 * @return
	 */
	@PostMapping("/gameServerByServerType")
	@ResponseBody
	public TableDataInfo gameServerByServerType(String groupName, int serverType, int ignoreMerge) {
		List<TServer> list = selectGroupService.selectServerList(groupName,ignoreMerge,String.valueOf(serverType));
		return getDataTable(list);
	}

	/**
	 * 查询登录服列表
	 */
	@PostMapping("/loginServer")
	@ResponseBody
	public TableDataInfo loginServer(String groupName) {
		List<TServer> list = selectGroupService.selectServerList(groupName, 0, "2");
		return getDataTable(list);
	}

	/**
	 * 查询公共服列表
	 */
	@PostMapping("/publicServer")
	@ResponseBody
	public TableDataInfo publicServer(String groupName) {
		List<TServer> list = selectGroupService.selectServerList(groupName, 0, "3");
		return getDataTable(list);
	}

	/**
	 * 查询战斗服列表
	 */
	@PostMapping("/fightServer")
	@ResponseBody
	public TableDataInfo fightServer(String groupName, int ignoreMerge) {
		List<TServer> list = selectGroupService.selectServerList(groupName, ignoreMerge, "4");
		return getDataTable(list);
	}

}
