/**
 * Auto generated, do not edit it
 *
 * 语言提示表
 */
package com.data;

public enum StringCode{

    /**
     * 系统
     */
    System(159232,1),
    /**
     * 任务目标未达成
     */
    Task_TargetNotomplete(53839,1),
    /**
     * 背包装不下奖励品啦！快去清理下吧。
     */
    Task_BagAlreadyFull(440717,1),
    /**
     * 任务提交等级不足
     */
    Task_CommitLevelNoEnough(494516,1),
    /**
     * 目标距离太远
     */
    Task_NPCDistanceTooFar(37815,1),
    /**
     * 强化等级不能超过玩家等级(强化)
     */
    Equip_StrengthLvNotStockPlayerLevel(175992,1),
    /**
     * 强化已达最大等级(强化)
     */
    Equip_StrengthMaxLevel(334133,1),
    /**
     * 货币不足(强化)
     */
    Equip_StrengthMoneyNoEnough(453560,1),
    /**
     * 材料不足(强化)
     */
    Equip_StrengthMaterialNoEnough(105709,1),
    /**
     * 无此装备(穿戴装备)
     */
    Equip_NoThisEquipUp(328163,1),
    /**
     * 装备未激活(穿戴装备)
     */
    Equip_NoActiveUp(487358,1),
    /**
     * 此装备已穿戴(穿戴装备)
     */
    Equip_AreadyUp(370184,1),
    /**
     * 限时时间已到(穿戴装备)
     */
    Equip_LimitTimeOver(241333,1),
    /**
     * 锁定的不许小于附加属性条目数(洗练)
     */
    Equip_LockAttriNumError(221915,1),
    /**
     * 货币不足(洗练)
     */
    Equip_ConciseMoneyNoEnough(59250,1),
    /**
     * 不在开放时间内(获取商店信息)
     */
    Shop_NoOpenTime(157276,1),
    /**
     * 请先加入战盟
     */
    Shop_NoGuid(156519,1),
    /**
     * 物品不存在(购买)
     */
    Shop_PropNoExist(500289,1),
    /**
     * 购买等级不足(购买)
     */
    Shop_BuyLevelNoEnough(106835,1),
    /**
     * 职业不匹配(购买)
     */
    Shop_CareerNoMatch(187989,1),
    /**
     * 背包剩余格子不足，无法购买
     */
    Shop_CellNoEnough(394632,1),
    /**
     * 货币不足(购买)
     */
    Shop_MoneyNoEnough(210062,1),
    /**
     * 已超出限购个数(购买)
     */
    Shop_BeatLimitNum(348248,1),
    /**
     * 该商店已无库存(购买)
     */
    Shop_NoStocks(128014,1),
    /**
     * 普通商店不能刷新(刷新商店)
     */
    Shop_NorShopNotFlush(468202,1),
    /**
     * 货币不足(刷新商店)
     */
    Shop_FlushMoneyNoEnough(257677,1),
    /**
     * 未通过登录验证
     */
    CreateFailed_NoVerify(418744,1),
    /**
     * 角色数量已最大
     */
    CreateFailed_MaxNum(508692,1),
    /**
     * 角色名包含屏蔽字符
     */
    CreateFailed_Forbidden(211602,1),
    /**
     * 职业、性别、阵营传入错误参数
     */
    CreateFailed_WrongParm(411331,1),
    /**
     * 角色名重复
     */
    CreateFailed_DupNum(118627,1),
    /**
     * 角色名长度不对
     */
    CreateFailed_LengthError(102708,1),
    /**
     * 角色名包含特殊字符无法保存，请换角色名
     */
    CreateFailed_InsertDbError(306752,1),
    /**
     * 角色名首不能为空格
     */
    CreateFailed_FirstWhitespace(411290,1),
    /**
     * 角色名尾不能为空格
     */
    CreateFailed_LastWhitespace(192728,1),
    /**
     * 战盟盟主角色不能删除，请移交盟主或退出战盟
     */
    DeletePlayerFailed_IsGuildChairman(429149,1),
    /**
     * 阵营精英角色不能删除
     */
    DeletePlayerFailed_IsCampElite(75332,1),
    /**
     * 删除数据失败
     */
    DeletePlayerFailed_DBFailed(444928,1),
    /**
     * 此部位置已有宝石(镶嵌宝石)
     */
    Gem_PositionExsitGem(302767,1),
    /**
     * 背包中宝石不存在(镶嵌宝石)
     */
    Gem_NoExistGemByBag(484635,1),
    /**
     * 无此宝石孔(镶嵌宝石)
     */
    Gem_NoCorresponHole(410395,1),
    /**
     * 玩家等级不足(镶嵌宝石)
     */
    Gem_PlayerLevelNoEnough(86148,1),
    /**
     * 配置表中无此宝石(镶嵌宝石)
     */
    Gem_TableNoGem(162498,1),
    /**
     * 该物品不是宝石(镶嵌宝石)
     */
    Gem_PropNotGem(340753,1),
    /**
     * 宝石颜色和位置不匹配(镶嵌宝石)
     */
    Gem_GemColorNoMatch(245726,1),
    /**
     * 此部位置无宝石(卸载宝石)
     */
    Gem_PositinNoGem(125983,1),
    /**
     * 背包已满(卸载宝石)
     */
    Gem_BagAlreadyMax(84495,1),
    /**
     * 交易行
     */
    TradeTitle(221928,1),
    /**
     * 交易行下架
     */
    TradeDownTitle(266095,1),
    /**
     * 交易行购买
     */
    TradeBuyTitle(426794,1),
    /**
     * 交易行出售
     */
    TradeSellTitle(111719,1),
    /**
     * 交易行物品下架成功
     */
    TradeDown(286979,1),
    /**
     * 你出售[6b8e00]{0}[-]道具[6b8e00]{1}[-]个成功，扣除交易税[6b8e00]{2}[-]元宝后，您获得[6b8e00]{3}[-]元宝。
     */
    TradeSellSuccess(206549,1),
    /**
     * 你购买[6b8e00]{0}[-]道具[6b8e00]{1}[-]个成功，消费[6b8e00]{2}[-]元宝。
     */
    TradeBuySuccess(97838,1),
    /**
     * 物品不能被交易
     */
    TradeNotAllow(322499,1),
    /**
     * 自己上架物品数量已达上限
     */
    TradeOwnNumMax(472334,1),
    /**
     * 上架失败，需要消耗{0}金币
     */
    TradeUpNeedMoneyNotEnough(4434,1),
    /**
     * 交易购买所需元宝不足
     */
    TradeBuyNotEnoughGold(265238,1),
    /**
     * 上架物品的售价必须在5-5000之间
     */
    TradeUpCostMoneyError(8326,1),
    /**
     * 不存在此种合成道具(合成道具)
     */
    ComposePropError1(99851,1),
    /**
     * 货币不足(合成道具)
     */
    ComposePropError2(257563,1),
    /**
     * 今日已达最大合成次数(合成道具)
     */
    ComposePropError3(50315,1),
    /**
     * 材料不足(合成道具)
     */
    ComposePropError4(449769,1),
    /**
     * 背包剩余格子不足(合成道具)
     */
    ComposePropError5(147414,1),
    /**
     * 已经拥有队伍，不能再创建队伍
     */
    CreateTeamFailed_HaveTeam(274312,1),
    /**
     * 该地图不支持组队
     */
    TeamFailed_MapNotSupport(124422,1),
    /**
     * 队伍成员{0}所在地图不支持组队
     */
    TeamFailed_MemberMapNotSupport(203423,1),
    /**
     * 对方所在地图不支持组队
     */
    TeamFailed_OtherSideMapNotSupport(385530,1),
    /**
     * 没有队伍不能邀请玩家入队
     */
    InvitePlayerFailed_NoTeam(457252,1),
    /**
     * 该地图不支持组队
     */
    InvitePlayerFailed_MapNotSupport(124422,1),
    /**
     * 对方不在线了
     */
    InvitePlayerFailed_NotOnline(460866,1),
    /**
     * 已经拥有队伍不能再次申请进入其他队伍
     */
    ApplyIntoTeamFailed_HaveTeam(76732,1),
    /**
     * 对方队伍人数已达上限
     */
    ApplyIntoTeamFailed_MaxSize(249266,1),
    /**
     * 已经向该队伍申请，请等待对方确认
     */
    ApplyIntoTeamFailed_Applyed(425548,1),
    /**
     * 队伍已解散
     */
    DealTeamInviteFailed(348711,1),
    /**
     * {0}拒绝了您所属队伍的邀请
     */
    RefuseTeamInveite(414768,1),
    /**
     * 队伍人数已达上限
     */
    IntoTeamFailed_MaxNum(133032,1),
    /**
     * 已经在队伍中，不可重复加入队伍
     */
    IntoTeamFailed_Exist(136527,1),
    /**
     * {0}拒绝了您的入队申请
     */
    RefuseTeamApply(213678,1),
    /**
     * 对方不在线了
     */
    DealTeamApply_OntOnline(460866,1),
    /**
     * 创建队伍成功
     */
    CreateTeamSuccess(514477,1),
    /**
     * 邀请{0}玩家入队成功，请等待对方答复
     */
    InviteIntoTeamSuccess(95249,1),
    /**
     * 申请入队成功，请等待队伍成员答复
     */
    ApplyIntoTeamSuccess(395994,1),
    /**
     * 设置为自动同意队伍邀请
     */
    SetAutoAgreeInvite(416420,1),
    /**
     * 取消自动同意队伍邀请
     */
    CancelAutoAgreeInvite(238362,1),
    /**
     * 邀请失败，对方已有队伍！
     */
    OpposideHaveJoinTeam(188047,1),
    /**
     * 加入队伍成功
     */
    JoinTeamSuccess(336903,1),
    /**
     * 查看队伍失败，队伍已解散
     */
    ViewTeamFailed_NotExist(129210,1),
    /**
     * 只有队长才可以队伍世界喊话
     */
    TeamWorldPropagandaFailed_NotLeader(521171,1),
    /**
     * 只能在世界地图进行队伍世界喊话
     */
    TeamWorldPropagandaFailed_NotWorldMap(52947,1),
    /**
     * 队伍世界喊话cd中
     */
    TeamWorldPropagandaFailed_Cding(219302,1),
    /**
     * 跨服不支持此操作
     */
    NotSupportCross(497275,1),
    /**
     * 签到失败
     */
    CanNotSign(474816,1),
    /**
     * 背包不足,签到失败
     */
    CanNotSign_NoBagCell(499223,1),
    /**
     * 您的今日补签次数已达上限
     */
    CanNoSign_HadSign(262160,1),
    /**
     * 消耗10绑元可补签一次
     */
    CanNotSign_tips(322760,16),
    /**
     * 你被{0}踢出了队伍
     */
    TeamKickOuted(56441,1),
    /**
     * 领取物品失败
     */
    CanNotGetLevelReward(130615,1),
    /**
     * 背包不足,领取物品失败
     */
    CanNotGetLevelReward_NoBagCell(38549,1),
    /**
     * 输入密码不是由数字和字母组成
     */
    SetStoreKeyFailed_Wrong(176732,1),
    /**
     * 输入密码长度超过8个字符
     */
    SetStoreKeyFailed_WrongNum(8539,1),
    /**
     * 旧密码输入错误
     */
    SetStoreKeyFailed_OldKeywrong(228064,1),
    /**
     * 设置仓库密码成功
     */
    SetStoreKeySuccess(210054,1),
    /**
     * 清除仓库密码成功
     */
    ClearStoreKeySuccess(268759,1),
    /**
     * 仓库格子不足，不能放入仓库
     */
    BagToStoreFailed_Nocell(475347,1),
    /**
     * 密码错误，请输入正确密码
     */
    StoreToBagFailed_WrongKey(60291,1),
    /**
     * 背包格子不足，不能从仓库取出物品
     */
    StoreToBagFailed_NoCell(5628,1),
    /**
     * 不符合领取等级奖励的条件
     */
    CanNotGetLevelReward_LevelOver(51547,1),
    /**
     * 奖励已经领取
     */
    CanNotGetLevelReward_HadGet(49172,1),
    /**
     * 背包不足，等级奖励领取失败，去清理一下背包吧！
     */
    CanNotGetLevelReward_NoItemBag(350724,1),
    /**
     * 每日必做活跃达100点可补签
     */
    CanNotSign_NoActivePoint(448324,1),
    /**
     * 世界BOSS排名奖
     */
    WorldBossTitle(184665,1),
    /**
     * 侠士在【{0}】世界BOSS中做出突出贡献，排名第{1}名，获得以下奖励，威武！
     */
    WorldBossRankRewardContent(239417,1),
    /**
     * 侠士在【{0}】世界BOSS中，获得以下奖励，威武！
     */
    WorldBossRankRewardJoin(231672,1),
    /**
     * 【{0}】带领众侠士合力击败了【{1}】！
     */
    WorldBossDieBroad(477134,6),
    /**
     * 1分钟后世界BOSS即将开始攻城，请侠士们前往！<t=4>立即前往,54,[17,23]</t>
     */
    WorldBossWillBegin(405152,6),
    /**
     * 【{0}】出现在【{1}】，请侠士们前往！<t=4>立即前往,54,[17,23]</t>
     */
    WorldBossCreate(127308,6),
    /**
     * 世界BOSS结束，侠士威武！
     */
    WorldBossEnd(355475,4),
    /**
     * {0}秒后【{1}】将要攻击【{2}】，请侠士们前往！<t=4>立即前往,54,[17,23]</t>
     */
    WorldBossAfterCreate(132175,2),
    /**
     * 不能羽化,金币不足
     */
    CanNotFeatherPro_CopperNoEnough(62121,1),
    /**
     * 不能羽化,道具不足
     */
    CanNotFeaturePro_ItemNoEnough(93466,1),
    /**
     * 剑侍已出战
     */
    Pet_onfight(160265,1),
    /**
     * 剑侍已休息
     */
    Pet_offfight(419045,1),
    /**
     * 剑侍不存在
     */
    Pet_NoExist(134264,1),
    /**
     * 该剑侍已上阵
     */
    Pet_AlreadyUp(424060,1),
    /**
     * 参数错误
     */
    Pet_ParamError(522395,1),
    /**
     * 该位置上无剑侍
     */
    Pet_PositionNoPet(96229,1),
    /**
     * 该地图不支持剑侍出战
     */
    Pet_MapNotSupport(384842,1),
    /**
     * 该剑侍已出战
     */
    Pet_AlreadyFit(17054,1),
    /**
     * 该剑侍已在备战状态
     */
    Pet_AlreadyReady(357759,1),
    /**
     * 出战剑侍正在战斗中，无法更换出战剑侍
     */
    Pet_InFighting(245829,1),
    /**
     * 剑侍等级不能超过人物等级
     */
    Pet_AlreadyMaxLevel(237893,1),
    /**
     * 是否自动锁定[FF2333]高品属性[-]？\n[FF2333]4个[-]高品属性即可[FF2333]进阶[-]。
     */
    Pet_xiliantishi(475323,1),
    /**
     * 喂养材料错误
     */
    Pet_FeedMaterialError(395768,1),
    /**
     * 喂养材料不足
     */
    Pet_FeedMaterialNoEnough(153546,1),
    /**
     * 突破已达最大值
     */
    Pet_AlreadyBreakMax(282446,1),
    /**
     * 货币不足
     */
    Pet_MoneyNoEnough(277819,1),
    /**
     * 材料不足
     */
    Pet_MaterialNoEnough(489936,1),
    /**
     * 主侍不存在
     */
    Pet_MainPetNoExist(459678,1),
    /**
     * 副侍不存在
     */
    Pet_NoMainPetNoExist(148446,1),
    /**
     * 不能吞噬上阵剑侍
     */
    Pet_NoDevoureUpPet(181156,1),
    /**
     * 主剑侍已满级
     */
    Pet_MainPetMaxLevel(325419,1),
    /**
     * 剑侍技能不存在
     */
    Pet_SkillNoExsit(178110,1),
    /**
     * 遗忘道具不足
     */
    Pet_ForgetPropNoEnough(195967,1),
    /**
     * 此位置已存在技能
     */
    Pet_positionExsitSkill(366594,1),
    /**
     * 此剑侍已存在此技能
     */
    Pet_ExsitSkill(486129,1),
    /**
     * 该技能不是剑侍技能
     */
    Pet_NoPetSkill(100124,1),
    /**
     * 该剑侍技能不能学习
     */
    Pet_PetSkillNoStudy(127704,1),
    /**
     * 您的技能书不足，无法升级！
     */
    Pet_SkillPropNoExsit(152325,1),
    /**
     * 先学习再升级
     */
    Pet_PleaseStudySkill(340732,1),
    /**
     * 此技能已达最大等级
     */
    Pet_SkillMaxLevel(226470,1),
    /**
     * 剑侍已阵亡
     */
    Pet_AlreadyDead(209935,1),
    /**
     * 剑侍名称长度错误
     */
    Pet_NameLenError(194860,1),
    /**
     * 剑侍名称包含敏感词
     */
    Pet_NameHasForbidden(63699,1),
    /**
     * 货币不足,不能更改剑侍名称
     */
    Pet_NameMoneyNoEnough(288214,1),
    /**
     * 剑侍上阵数量已满
     */
    Pet_OutMax(306118,1),
    /**
     * 不存在该角色
     */
    Friend_NoRole(324454,1),
    /**
     * 好友添加成功！
     */
    Friend_HasInFriendList(1419,1),
    /**
     * 黑名单添加成功。
     */
    Friend_HasInBlackList(368415,1),
    /**
     * 拉黑玩家会使你们之间亲密度清零，你确认要拉黑吗？
     */
    Friend_InBlackList(415384,1),
    /**
     * 搜索的名称不能为空
     */
    Friend_SearchNoName(183559,1),
    /**
     * 请解散队伍之后再进入副本
     */
    CopyMapNotSupportTeam(23926,1),
    /**
     * 副本剩余次数不足
     */
    EnterCopyMap_NoCount(173688,1),
    /**
     * 线路人数已满，无法进入地图
     */
    EnterCopyMapFailed_NoLine(515968,1),
    /**
     * {0}成员离线，无法进入地图
     */
    EnterCopyMapFailed_MemberOffLine(323018,1),
    /**
     * {0}成员未跟随，无法进入地图
     */
    EnterCopyMapFailed_MemberNotFollow(406400,1),
    /**
     * {0}成员等级不足，无法进入地图
     */
    EnterCopyMapFailed_MemberBelowLv(450529,1),
    /**
     * {0}成员等级超过上限，无法进入地图
     */
    EnterCopyMapFailed_MemberAboveLv(391610,1),
    /**
     * {0}成员剩余次数不足，无法进入地图
     */
    EnterCopyMapFailed_MemberNoCount(297221,1),
    /**
     * {0}成员没在同一阵营，无法进入地图
     */
    EnterCopyMapFailed_MemberNotSameCamp(515716,1),
    /**
     * {0}成员没在同一战盟，无法进入地图
     */
    EnterCopyMapFailed_MemberNotSameGuild(398122,1),
    /**
     * 您的好友已达上限，清理才能继续添加
     */
    FriendFull_notice(515830,1),
    /**
     * 战翼等级已达到最大级
     */
    ReachedMaxLev(400196,1),
    /**
     * 余额不足,无法提升战翼
     */
    OverOwnMoney(322243,1),
    /**
     * 道具不足,无法提升战翼
     */
    OverOwnItems(169379,1),
    /**
     * 玩家等级不足,不能羽化
     */
    PlayerLeNoReached(208034,1),
    /**
     * 队长还未创建副本，请耐心等待或者退出队伍
     */
    CopyMapCreateFaile_NotLeader(113517,1),
    /**
     * 坐骑不存在
     */
    Mount_NoExist(80714,1),
    /**
     * 坐骑已达最大阶数
     */
    Mount_AlreadyMaxStage(245440,1),
    /**
     * 角色等级不足
     */
    Mount_RoleLevelNoEnough(165487,1),
    /**
     * 已达玩家等级的最大星数
     */
    Mount_LevelMaxStar(486391,1),
    /**
     * 货币不足
     */
    Mount_MoneyNoEnough(277819,1),
    /**
     * 材料不足
     */
    Mount_MaterialNoEnough(489936,1),
    /**
     * 坐骑技能不存在
     */
    Mount_SkillNoExsit(288896,1),
    /**
     * 坐骑数量不足
     */
    Mount_NumNoEnough(287496,1),
    /**
     * 坐骑技能已达最大等级
     */
    Mount_SkillMaxLevel(220773,1),
    /**
     * 坐骑升级技能书不足
     */
    Mount_SkillBookNoEnough(239206,1),
    /**
     * 该地图不支持骑乘坐骑
     */
    Mount_UpFailed(332977,1),
    /**
     * 触发坐骑技能{0}
     */
    Mount_TriggerNotice(504334,2),
    /**
     * 满阶坐骑将拥有流光特效
     */
    Mount_maxtips(193712,1),
    /**
     * 预览满阶特效
     */
    Mount_eyetips(34383,1),
    /**
     * 物品购买失败，还未到购买时间
     */
    MallBuyFailed_WrongTime(400871,1),
    /**
     * 商品购买失败，货币不足
     */
    MallBuyFailed_NoMoney(384984,1),
    /**
     * 商品购买失败，背包剩余格子不足
     */
    MallBuyFailed_NoCell(72443,1),
    /**
     * 商品购买失败，限购次数已达上限
     */
    MallBuyFailed_NoCount(121214,1),
    /**
     * 商品购买成功
     */
    MallBuySuccess(313647,1),
    /**
     * 商场购买赠送物品
     */
    MallBuyGift(251994,1),
    /**
     * 限购物品数量为0，商城界面刷新
     */
    MallBuyNoCount_Notice(288858,1),
    /**
     * 等级不足，激活技能失败
     */
    ActiveSkillFailed_NotEnoughLv(254029,1),
    /**
     * 所需物品不足，激活技能失败
     */
    ActiveSkillFailed_NoItem(162286,1),
    /**
     * 技能提升已达到玩家等级！
     */
    UpSkillFailed_MaxLv(286878,1),
    /**
     * 技能升级失败，所需金币不足
     */
    UpSkillFailed_NoMoney(45532,1),
    /**
     * 技能升级失败，所需物品不足
     */
    UpSkillFailed_NoItem(223861,1),
    /**
     * 技能升级成功，获取金币来升级更多技能
     */
    UpSkillBreak_NoMoney(43003,1),
    /**
     * 技能升级成功，获取技力石来升级更多技能
     */
    UpSkillBreak_NoItem(475627,1),
    /**
     * 技能已提升至满级
     */
    UpSkillBreak_MaxLv(175726,1),
    /**
     * 不能聊天,扣除物品失败
     */
    CanNotChat_DelItemFailed(425881,1),
    /**
     * 等级不够，不能使用该物品
     */
    ItemUseFailed_NotEnoughLv(230813,1),
    /**
     * 该职业不能使用此物品
     */
    ItemUseFailed_WrongCareer(154414,1),
    /**
     * 生命值已达上限，不能使用物品增加血量
     */
    ItemUseFailed_MaxHp(79157,1),
    /**
     * 等级已达上限，不能使用物品增加等级
     */
    ItemUseFailed_MaxLv(464965,1),
    /**
     * 背包剩余格子不足，使用包裹失败
     */
    ItemUseFailed_NoCell(42705,1),
    /**
     * 坐骑已存在，不能使用该物品增加坐骑
     */
    ItemUseFailed_ExistMount(262732,1),
    /**
     * 头像(称号)已存在，不能使用物品增加头像(称号)
     */
    ItemUseFailed_ExisHead(86346,1),
    /**
     * {0}时装已永久激活，不能使用物品再次激活
     */
    ItemUseFailed_ExisDress(171003,1),
    /**
     * 该地图不能使用药品
     */
    ItemUseFailed_MapNotSupport(311061,1),
    /**
     * 已达到每日使用上限
     */
    ItemUseFailed_NoUseCount(284886,1),
    /**
     * 使用失败，目标不在线
     */
    ItemUseFailed_TargetNotExist(263754,1),
    /**
     * 不能对非好友使用物品增加亲密度
     */
    ItemUseFailed_NotFriend(253810,1),
    /**
     * 剑侍栏位满，无法开启剑侍蛋
     */
    ItemUseFailed_AddPetFailed(310279,1),
    /**
     * 获得{0}剑侍
     */
    ItemUseSuccess_AddPet(383085,1),
    /**
     * 获得{0}坐骑
     */
    ItemUseSuccess_AddMount(165011,1),
    /**
     * 永久获得{0}时装
     */
    ItemUseSuccess_AddDressForever(376582,1),
    /**
     * 增加{0}时装{1}天时效
     */
    ItemUseSuccess_AddDressDays(498670,1),
    /**
     * 复活失败，复活冷却中
     */
    ReviveFailed_CDing(100627,1),
    /**
     * 原地复活失败，所需货币不足
     */
    ReviveFailed_NoMoney(36773,1),
    /**
     * 创建战盟失败，所需角色等级不足
     */
    GuildCreateFailed_NotEnoughLv(436834,1),
    /**
     * 战盟名字长度必须在2~7个字以内
     */
    GuildCreateFailed_NameLenght(232917,1),
    /**
     * 战盟名字重复
     */
    GuildCreateFailed_NameDup(335525,1),
    /**
     * 战盟名字包含了敏感字符
     */
    GuildCreateFailed_NameForbidded(494151,1),
    /**
     * 创建战盟失败，创建所需货币不足
     */
    GuildCreateFailed_NoMoney(383126,1),
    /**
     * 系统繁忙，请稍后再创建战盟
     */
    GuildCreateFailed_SystemBusy(457661,1),
    /**
     * 创建战盟失败，需要先加入一个阵营
     */
    GuildCreateFailed_NoCamp(288338,1),
    /**
     * {0}创建了战盟：{1}，九州六国又增添了一股强劲的势力
     */
    GuildCreateSuccess(359637,4),
    /**
     * {0}加入了战盟，战盟实力又得到了增强,大家快欢迎新朋友吧
     */
    GuildJoinSuccess(169330,32),
    /**
     * 邀请失败，对方已离线
     */
    GuildInviteFailed_NotOnline(5427,1),
    /**
     * 邀请失败，对方等级过低不能加入
     */
    GuildInviteFailed_NotEnoughLv(445138,1),
    /**
     * 邀请失败，对方已有战盟
     */
    GuildInviteFailed_HaveJoined(471821,1),
    /**
     * 邀请失败，对方为敌对阵营
     */
    GuildInviteFailed_NotSameCamp(179510,1),
    /**
     * 邀请失败，您已邀请过啦，休息一会儿吧！
     */
    GuildInviteFailed_Dup(108086,1),
    /**
     * 同意玩家加入战盟失败，玩家已加入战盟
     */
    GuildDealApplyMsgFailed_Joined(16966,1),
    /**
     * 同意玩家加入战盟失败，战盟成员人数已达上限
     */
    GuildDealApplyMsgFailed_MaxNum(415360,1),
    /**
     * 同意玩家加入战盟失败，该玩家和战盟不属于同一阵营
     */
    GuildDealApplyMsgFailed_NotSameCamp(413892,1),
    /**
     * 同意加入战盟失败，对方战盟成员人数已达上限
     */
    GuildDealInviteMsgFailed_MaxNum(502005,1),
    /**
     * 同意加入战盟失败，该邀请消息已过期
     */
    GuildDealInviteMsgFailed_TimeOut(435231,1),
    /**
     * 同意加入战盟失败，自己和战盟不是相同阵营
     */
    GuildDealInviteMsgFailed_NotSameCamp(187039,1),
    /**
     * 邀请失败，对方处于等待冷却中！
     */
    GuildInviteFailed_NotCd(208738,1),
    /**
     * 邀请已发送，请等待对方回复！
     */
    GuildBeInvitedMsg(324756,1),
    /**
     * {0}玩家被{1}踢出了战盟
     */
    GuildMemberKickOut(483330,1),
    /**
     * 你被{0}玩家踢出了战盟
     */
    GuildOwnBeKickOuted(22426,3),
    /**
     * 玩家{0}与战盟理念不合，退出了战盟
     */
    GuildMemberQuit(406811,2),
    /**
     * 同意战盟招募信息失败，信息已过期
     */
    GuildRecruitFailed_TimeOut(1169,1),
    /**
     * 同意战盟招募信息失败，自己阵营与战盟阵营不一致
     */
    GuildRecruitFailed_NotSameCamp(176325,1),
    /**
     * 同意战盟招募信息失败，对方战盟成员人数已达上限
     */
    GuildRecruitFailed_MaxNum(232510,1),
    /**
     * 战盟成员【{0}】上线了
     */
    GuildMemberOnline(125034,2),
    /**
     * 战盟成员【{0}】下线了
     */
    GuildMemberOffline(486255,2),
    /**
     * 招募信息发布成功！
     */
    GuildXM_HavedNoticeRecruitStr(76246,1),
    /**
     * 加入战盟失败，冷却中
     */
    GuildJoinCDing(353006,1),
    /**
     * 战盟招募失败，冷却中
     */
    GuildRecruitFailed_Cding(462451,1),
    /**
     * 战盟招募失败，今日无剩余招募次数
     */
    GuildRecruitFailed_NoCount(395082,1),
    /**
     * 战盟招募失败，今日无剩余招募次数
     */
    GuildXM_RecruitMemberBtnStr(395082,1),
    /**
     * 剩余输入字数：{0}        今日剩余次数：{1}
     */
    Guild_NoticeRecruitNumber(497144,1),
    /**
     * 加入战盟失败,你还处于等待冷却中
     */
    GuildJoinFailed(273731,1),
    /**
     * 创建战盟失败,你还处于等待冷却中
     */
    GuildCreateFailed_Cd(288393,1),
    /**
     * {0}第一战盟需要您来创建！
     */
    GuildapplyFirst_notice(389594,1),
    /**
     * 热烈欢迎！&13&13
     */
    Guildnewnumbnotice(263542,32),
    /**
     * 活跃点不足，不能领取奖励
     */
    GetDailyRewardFalied_NotEnoughPoint(430786,1),
    /**
     * 已领取活跃点奖励
     */
    GetDailyRewardFalied_HaveGet(315334,1),
    /**
     * 背包已满，请清理背包后再领取活跃点奖励
     */
    GetDailyRewardFalied_NoCell(355472,1),
    /**
     * 需至少进入该活动一次,方可扫荡
     */
    WipwCopyFailded_ActivityNeverEnter(341813,1),
    /**
     * 精力值不足,不可扫荡
     */
    EnergyNotEounght_NoWipe(436502,1),
    /**
     * 等级不足,不可扫荡
     */
    LvNoEnought_NoWipe(370307,1),
    /**
     * 背包格子不足，不可以扫荡
     */
    BagsNoEnough_NoWipe(323438,1),
    /**
     * 野外Boss奖励
     */
    WildBoss_MailRewardTitle(68479,1),
    /**
     * 您与队友合力击杀了【{0}】，获得以下奖励。今日还可再获得{1}次Boss奖励！！
     */
    WildBoss_MailRewardContent(313370,1),
    /**
     * 1分钟后【{0}】将出现在【{1}】，请侠士前往降服！<t=4>立即前往,{2},[{3},{4}]</t>
     */
    WilBoss_InitRefresh(190918,6),
    /**
     * 【{0}】带领众侠士成功击杀了【{1}】！
     */
    WildBoss_DeadRefresh(409147,6),
    /**
     * 您今日野外Boss奖励次数已达上限!
     */
    WildBoss_MailRewardOverCountContent(81926,1),
    /**
     * 野外Boss参与奖
     */
    WildBossMailRewardConsolationTitle(276824,1),
    /**
     * 您参与了击杀【{0}】，获得以下奖励。今日还可再获得{1}次参与奖！！
     */
    WildBossMailRewardConsolationContent(437983,1),
    /**
     * 该时装不可购买
     */
    Dress_NotCanBuy(2910,1),
    /**
     * 货币不足,不可购买
     */
    Dress_NotEnoughMoneyBuy(429068,1),
    /**
     * 购买后才可以穿戴
     */
    Dress_NotBuy(316335,1),
    /**
     * 该时装已过期
     */
    Dress_Past(316922,1),
    /**
     * 已穿戴同类型的时装
     */
    Dress_SameType(238846,1),
    /**
     * 羽化失败，提升玩家等级以增加战翼成长的上限
     */
    OverOwnLevel_Wing(188179,1),
    /**
     * 只能从野外地图进入竞技场
     */
    ArenaMapEnterFailed_NotInTheWorldMap(130493,1),
    /**
     * 已在竞技场中
     */
    ArenaMapEnterFailed_HaveIn(282674,1),
    /**
     * 其他玩家正在挑战你，请稍后再对其他玩家发起挑战
     */
    ArenaChallengeFailed_Owning(54720,1),
    /**
     * 不能挑战自己，请换个对象挑战
     */
    ArenaChallengeFailed_NotOwn(187352,1),
    /**
     * 挑战的对象正在和其他人战斗中，请选择其他对手
     */
    ArenaChallengeFailed_Opping(205197,1),
    /**
     * 系统繁忙，请稍后再挑战
     */
    ArenaChallengeFailed_SystemError(238043,1),
    /**
     * 未在竞技场地图，不能挑战
     */
    ArenaChallengeFailed_NotInMap(171096,1),
    /**
     * 今日挑战次数已经用完
     */
    ArenaChallengeFailed_NoCount(410349,1),
    /**
     * 竞技场提示
     */
    ArenaChallengeNotice(484407,1),
    /**
     * 你被 [ff0000]{0}[-] 挑战并被打败,发起者排名低于你,排名变为 [ff0000]{1}[-],立马去教育他！
     */
    ArenaChallengeNotice_cont(385852,1),
    /**
     * 背包已满
     */
    ArenaChallengefullMale(238941,1),
    /**
     * 由于您的背包已满，竞技场挑战获得奖励通过邮件发放，请及时领取！
     */
    ArenaChallengefullCont(138778,1),
    /**
     * 参加日常活动失败，所需主角等级不足
     */
    DailyActiveJoinFailed_NoLv(113815,1),
    /**
     * 参加日常活动失败，今日进入次数已用尽
     */
    DailyActiveJoinFailed_NoCount(181025,1),
    /**
     * 参加日常活动失败，活动未到开启时间
     */
    DailyActiveJoinFailed_WrongTime(406873,1),
    /**
     * 参加日常活动失败，所需活跃点不足
     */
    DailyActiveJoinFailed_NoPoint(52313,1),
    /**
     * 参加日常活动失败，没有在世界地图
     */
    DailyActiveJoinFailed_NotInTheWorldMap(511352,1),
    /**
     * 参加日常活动失败，需退出队伍才能加入
     */
    DailyActiveJoinFailed_NotSingle(502529,1),
    /**
     * 参加个人阵营战场活动失败，需加入阵营才能参加
     */
    DailyActiveJoinFailed_NoCamp(96148,1),
    /**
     * 参加个人阵营战场活动失败，需退出队伍才能参加
     */
    DailyActiveJoinFailed_HaveTeam(508659,1),
    /**
     * 活动
     */
    MallSort1(384122,1),
    /**
     * 元宝
     */
    MallSort2(335269,1),
    /**
     * 绑元
     */
    MallSort3(332546,1),
    /**
     * VIP专属
     */
    MallSort4(262220,1),
    /**
     * 充值
     */
    MallSort5(158420,1),
    /**
     * 推荐
     */
    MallSort1_title1(374171,1),
    /**
     * 成长
     */
    MallSort1_title2(253813,1),
    /**
     * 热销
     */
    MallSort1_title3(386196,1),
    /**
     * 优惠
     */
    MallSort1_title4(322579,1),
    /**
     * 推荐
     */
    MallSort2_title1(374171,1),
    /**
     * 成长
     */
    MallSort2_title2(253813,1),
    /**
     * 热销
     */
    MallSort2_title3(386196,1),
    /**
     * 优惠
     */
    MallSort2_title4(322579,1),
    /**
     * 推荐
     */
    MallSort3_title1(374171,1),
    /**
     * 成长
     */
    MallSort3_title2(253813,1),
    /**
     * 热销
     */
    MallSort3_title3(386196,1),
    /**
     * 优惠
     */
    MallSort3_title4(322579,1),
    /**
     * 推荐
     */
    MallSort4_title1(374171,1),
    /**
     * 成长
     */
    MallSort4_title2(253813,1),
    /**
     * 热销
     */
    MallSort4_title3(386196,1),
    /**
     * 优惠
     */
    MallSort4_title4(322579,1),
    /**
     * 镶嵌神兵技能失败，该技能格子上已镶嵌了技能
     */
    SoulEquipSkillFailed_Had(130807,1),
    /**
     * 镶嵌神兵技能失败，技能格子数已达上限
     */
    SoulEquipSkillFailed_MaxCell(276856,1),
    /**
     * 镶嵌神兵技能失败，使用无效的技能书
     */
    SoulEquipSkillFailed_UnUsefull(68012,1),
    /**
     * 镶嵌神兵技能失败，已镶嵌同类型技能
     */
    SoulEquipSkillFailed_HaveSame(123529,1),
    /**
     * 镶嵌神兵技能失败，所需神兵技能书数量不足
     */
    SoulEquipSkillFailed_NoItem(480152,1),
    /**
     * 卸下神兵技能失败，该技能格子上不存在镶嵌技能
     */
    SoulUnEquipSkillFailed_NoSkill(522256,1),
    /**
     * 卸下神兵技能失败，背包剩余格子不足
     */
    SoulUnEquipSkillFailed_NoBagCell(391024,1),
    /**
     * 活动已结束
     */
    CopyMapHadFinished(448027,1),
    /**
     * 进入下一关倒计时：{0}秒
     */
    TomnDemonTower_NextFloorCountDown(16383,1),
    /**
     * 背包已满，请清理后再领取竞技场排名奖励
     */
    ArenaGetTopRewardFailed_NoCell(137831,1),
    /**
     * 背包已满，请清理后再领取竞技场段位奖励
     */
    ArenaGetDuanRewardFailed_NoCell(87922,1),
    /**
     * 使用此物品不能增加小师妹亲密度
     */
    Minorlv_NoIncrIntimacyUseItem(285493,1),
    /**
     * 背包中无此物品
     */
    Minorlv_NoFindItemBag(234250,1),
    /**
     * 亲密值不足
     */
    Minorlv_IntimacyNoEnough(255777,1),
    /**
     * 亲密等级已达最大
     */
    Minorlv_IntimacyLevelAlreadyMax(358195,1),
    /**
     * 地图id:{0}的传送点参数错误
     */
    ChangeMapFailed_WrongParm(423930,1),
    /**
     * 距离传送点太远，当前玩家坐标：{0}
     */
    ChangeMapFailed_Long(459880,1),
    /**
     * 客户端上传地图名字：{0}和地图id：{1}不匹配
     */
    ChangeMapFailed_NoMatching(391269,1),
    /**
     * 已不在队伍中，请重新加入队伍
     */
    AgreeEnterCopyMapFailed_NoTeam(144117,1),
    /**
     * 副本已销毁，请队长重新创建副本进入
     */
    AgreeEnterCopyMapFailed_NoMap(450521,1),
    /**
     * 获得{0}战盟资金
     */
    GuildXM_Money_Get(259348,1),
    /**
     * 获得{0}战盟物资
     */
    GuildXM_Material_Get(516928,1),
    /**
     * 获得{0}战盟军需
     */
    Guild_MilitarySupply_Get(409612,1),
    /**
     * 只能申请同阵营的战盟
     */
    Guildapplycampnotice(282282,1),
    /**
     * {0}成员等级不够，进入活动失败
     */
    EnterDailyActiveMapFailed_NoLv(200056,1),
    /**
     * {0}成员活跃点不够，进入活动失败
     */
    EnterDailyActiveMapFailed_NoPoint(239753,1),
    /**
     * {0}成员剩余次数不足，进入活动失败
     */
    EnterDailyActiveMapFailed_NoCount(89150,1),
    /**
     * {0}成员离线，进入活动失败
     */
    EnterDailyActiveMapFailed_OffLine(114947,1),
    /**
     * {0}成员等级超过地图上限，进入活动失败
     */
    EnterDailyActiveMapFailed_AboveLv(333596,1),
    /**
     * {0}成员未跟随，进入活动失败
     */
    EnterDailyActiveMapFailed_NotFollow(198329,1),
    /**
     * 队长还未开启活动，进入活动失败
     */
    EnterDailyActiveMapFailed_NoCreate(213738,1),
    /**
     * 活动已结束，进入失败
     */
    EnterDailyActiveMapFailed_Finish(217684,1),
    /**
     * 未能成功守护林煮酒，需再接再厉！
     */
    ProtectNpcDie(64991,1),
    /**
     * 守护林煮酒成功
     */
    ProtectNpcSuccess(484064,1),
    /**
     * 该技能无法对BOSS释放！
     */
    ProtectNpcEffectUseFailed(460669,1),
    /**
     * 队伍至少有2个人才能进入副本！
     */
    Tower_enternubnot(143468,3),
    /**
     * [ec8f0b]{0}[-]释放了技能[ec8f0b]{1}[-],给林煮酒回复了大量生命值！
     */
    ProtectNPC_tips1(302720,3),
    /**
     * [ec8f0b]{0}[-]释放了技能[ec8f0b]{1}[-],给全队队友提升了一倍攻击力！
     */
    ProtectNPC_tips2(299175,3),
    /**
     * [ec8f0b]{0}[-]释放了技能[ec8f0b]{1}[-],秒杀了场景中大量的怪物！
     */
    ProtectNPC_tips3(299045,3),
    /**
     * [ec8f0b]{0}[-]释放了技能[ec8f0b]{1}[-],给全队增加免疫控制效果！
     */
    ProtectNPC_tips4(390964,3),
    /**
     * 团队试炼成功奖
     */
    GuildActive_Boss_AllRewardMailTitle(123303,1),
    /**
     * 在大家的齐心努力下，试炼首领在活动时间内被击破，[33bb43]所有战盟成员[-]获得如下奖励！参与活动可获得额外奖励！
     */
    GuildActive_Boss_AllRewardMailContent(86337,1),
    /**
     * 团队试炼参与奖
     */
    GuildActive_Boss_KillRewardMailTitle(149789,1),
    /**
     * 在大家的共同努力下，战盟首领在指定时间内被击破，您积极的[33bb43]参与了活动[-]，获得如下奖励！
     */
    GuildActive_Boss_KillRewardMailContent(430724,1),
    /**
     * 团队试炼失败奖
     */
    GuildActive_Boss_NoKillRewardMailTitle(488052,1),
    /**
     * 在战盟团队试炼中，未在指定时间内击破试炼首领，请再接再厉，同时获取如下奖励！
     */
    GuildActive_Boss_NoKillRewardMailContent(419929,1),
    /**
     * 在大家的齐心努力下，试炼首领在5分钟内被击破，首领等级提升1级，击破后可获得更高的奖励！
     */
    GuildActive_Boss_levelUp(415231,32),
    /**
     * 战盟试炼首领未在指定时间内被击破，首领等级降低1级，可通过快速击破提高其等级。
     */
    GuildActive_Boss_LevelDown(204467,32),
    /**
     * 击败战盟试炼首领，但未提升等级，可通过快速击破提高其等级。
     */
    GuildActive_Boss_Die(58568,32),
    /**
     * 您没有战盟，不能开启战盟团队试炼
     */
    GuildActive_Boss_Error_NoGuild(159020,1),
    /**
     * 战盟团队试炼已开启
     */
    GuildActive_Boss_Error_IsOpen(477879,1),
    /**
     * 战盟团队试炼今天已经开启过了
     */
    GuildActive_Boss_Error_OpenedToday(284825,1),
    /**
     * 开启战盟团队试炼失败，请联系客服
     */
    GuildActive_Boss_Error_CreateMonsterError(248717,1),
    /**
     * 只有盟主或护法才能开启战盟团队试炼
     */
    GuildActive_Boss_Error_NoChairman(204864,1),
    /**
     * 您的等级不足
     */
    GuildActive_DartCar_NoPlayerLv(468806,1),
    /**
     * 由于你的战盟成员没有在活动开启10分钟内及时参与活动，本日活动已提前结束！
     */
    GuildActive_DartCar_outtime(486004,1),
    /**
     * 战盟等级不足,不可开启活动
     */
    GuildActive_DartCar_NoReachLv(447363,1),
    /**
     * 战盟押镖额外奖
     */
    GuildActive_DartCar_KillRewardMailTitle(55208,1),
    /**
     * 战盟在镖车活动中，成功守护镖车，下次再接再厉，获得如下的额外奖励！
     */
    GuildActive_DartCar_KillRewardMailContent(372079,1),
    /**
     * 战盟押镖失败奖
     */
    GuildActive_DartCar_NoKillRewardMailTitle(291005,1),
    /**
     * 战盟在镖车活动中，未能成功守护镖车，获得如下保底奖励！
     */
    GuildActive_DartCar_NoKillRewardMailContent(173523,1),
    /**
     * 战盟押镖基础奖
     */
    GuildActive_DartCar_KillBaseRewardMailTitle(172437,1),
    /**
     * 战盟在镖车活动中，成功守护镖车，获得如下的基础奖励！
     */
    GuildActive_DartCar_KillBaseRewardMailContent(501214,1),
    /**
     * 战盟Boss将在{0}秒后出现，大家快快回驻地击退试炼怪物吧！
     */
    GuildActive_Boss_Active_Count(507114,32),
    /**
     * 战盟镖车将在{0}秒后出发，镖车需要你的保护，镖车血量越高奖励越高哦！
     */
    GuildActive_DartCar_Count(378592,32),
    /**
     * 战盟镖车活动已经开启了！！！快快行动吧！镖车需要你来守护！
     */
    GuildActive_DartCar_Start(445655,32),
    /**
     * 你还没有加入战盟呢,快快去加入一个战盟来体验吧！
     */
    GuildActive_DartCar_NoGuild(474201,1),
    /**
     * 该活动已提前结束！请下次再来！
     */
    GuildActive_DartCar_TimeOver(171627,1),
    /**
     * 战盟桃树已经在帮会驻地中开启，快快前往参与，桃树等级越高，获得奖励越多！
     */
    GuildActive_tree_opennotic(462059,32),
    /**
     * 在各个盟友的共同努力下，本次桃树会达到{0}级，吸引大量偷桃顽猴！
     */
    GuildActive_tree_treefinish(519689,32),
    /**
     * 偷桃顽猴已经在驻地中出现，限时{0}分钟内击败{1}个偷桃顽猴，可刷出高奖励顽猴首领！
     */
    GuildActive_tree_openmonster(455590,32),
    /**
     * 由于你们没能到达刷出头领的条件，本日顽猴头领不会出现！还请大家更加积极参与活动！
     */
    GuildActive_tree_noopenboss(125911,32),
    /**
     * 顽猴头领已经在战盟驻地中出现！
     */
    GuildActive_tree_openboss(295221,32),
    /**
     * 战盟桃树活动已结束，还请明日及时参与！
     */
    GuildActive_tree_killboss(117034,32),
    /**
     * 邮件已满
     */
    GuildActive_treeMailTitle(196478,1),
    /**
     * 您在战盟桃树活动中因背包已满，奖励通过邮件发放！
     */
    GuildActive_treeMailContent(109700,1),
    /**
     * 活动时间已结束，首领未能及时击杀，现已逃走！
     */
    GuildActive_tree_nokill(99591,32),
    /**
     * 在大家的齐心努力下，镖车的血量大于80%，镖车的等级提升1级，守护成功后可获得更高的奖励！
     */
    GuildActive_DartCar_levelUp(240702,32),
    /**
     * 战盟镖车的活动内，未挑战成功，镖车等级减1，还请积极参加战盟镖车活动！可通过守护成功获得更高的奖励！
     */
    GuildActive_DartCar_LevelDown(373351,32),
    /**
     * 战盟镖车的活动内，镖车血量正常，镖车等级未发生变化，镖车血量越高获得的奖励越多！
     */
    GuildActive_DartCar_Die(60737,32),
    /**
     * 战盟镖车的活动内，镖车的血量低于20%，当前等级为最低等级，积极守护可获取更高的奖励！
     */
    GuildActive_DartCar_LevelLowest(92309,32),
    /**
     * 战盟试炼首领已出现在战盟驻地，大家快去合力战斗吧！
     */
    GuildBossStartnow(491940,32),
    /**
     * 战盟镖车已开始前行，大家快去合力守护吧！
     */
    GuildDartStartnow(227409,32),
    /**
     * 元宝不足,不可参加摇树活动
     */
    MoneyTree_NotEnough(302879,1),
    /**
     * 今日摇取次数已达上限,不可继续,明日再来
     */
    MoneyTree_NoEnoughCount(377261,1),
    /**
     * 一键摇树次数超过今日摇取上限啦~
     */
    MoneyTree_OverMaxCount(408076,1),
    /**
     * 战盟增加{0}经验
     */
    GuildAddExp(301705,1),
    /**
     * 战盟联赛分组
     */
    GuildBattle_GroupTitle(55753,1),
    /**
     * 您的战盟排名第[ff0000]{0}[-],被分到了[ff0000]{1}[-]组，请在今晚[ff0000]20：00[-]参与最强战盟之争！
     */
    GuildBattle_GroupAMailContent(111648,1),
    /**
     * 您的战盟排名第[ff0000]{0}[-],被分到了[ff0000]{1}[-]组，请在今晚[ff0000]20：00[-]参加战盟联赛，获取大量奖励！
     */
    GuildBattle_GroupOtherMailContent(454141,1),
    /**
     * 您的战盟排名第[ff0000]{0}[-],没有匹配到合适的对手，活动结束后将发放轮空奖励！
     */
    GuildBattle_GroupNone(199746,1),
    /**
     * 战盟联赛胜利
     */
    GuildBattle_WinMailTitle(140345,1),
    /**
     * 您所在的战盟，在战盟联赛中击败所有对手，获得胜利，所有战盟成员获得如下奖励！
     */
    GuildBattle_WinMailContent(78037,1),
    /**
     * 战盟联赛落败
     */
    GuildBattle_LoseMailTitle(207584,1),
    /**
     * 您所在战盟在战盟联赛中不幸落败！卧薪尝胆，下周再战！所有成员获得如下奖励！
     */
    GuildBattle_LoseMailContent(263379,1),
    /**
     * 战盟联赛弃权
     */
    GuildBattle_GiveUpMailTitle(465325,1),
    /**
     * 战盟联赛期间你所在战盟获得积分低于10分，联赛官员认定你所在战盟弃权，不发放奖励！
     */
    GuildBattle_GiveUpMailContent(269868,1),
    /**
     * 战盟联赛轮空
     */
    GuildBattle_TurnEmptyMailTitle(199255,1),
    /**
     * 您所在的战盟，在本次战盟联赛中未匹配到合适的对手，所有成员获得轮空奖励！
     */
    GuildBattle_TurnEmptyMailContent(262089,1),
    /**
     * 战盟联赛个人奖励
     */
    GuildBattle_ScoreRewardMailTitle(375376,1),
    /**
     * 您在战盟联赛中获得{0}战功，获得对应的战盟称号！
     */
    GuildBattle_ScoreRewardMailContent(480750,1),
    /**
     * [00ff00]【{1}】[-]战盟在[00ff00]{0}[-]带领下击败所有强大战盟，成为[00ff00]最强战盟[-]！
     */
    GuildBattle_GetKing(413311,6),
    /**
     * 最强战盟
     */
    GuildBattle_KingrRewarMailTitle(276730,1),
    /**
     * 你所在战盟战力无双，在战盟联赛中镇压全服获得最强战盟称号！
     */
    GuildBattle_KingrRewarMailContent(119194,1),
    /**
     * 您击败了【{0}】,获得【{1}】点战功
     */
    GuildBattle_KillNotice(256248,1),
    /**
     * 您协助【{0}】击败了【{1}】,获得【{2}】点战功
     */
    GuildBattle_SuportNotice(439840,1),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]{1}[-]人,无人可挡！
     */
    GuildBattle_MonsterKillNotice(355241,1),
    /**
     * 还未到战盟联赛活动时间！
     */
    GuildBattle_NotOpenTips(8000,1),
    /**
     * 对战分组将在周四凌晨开启！
     */
    GuildBattle_NotOpen(259891,1),
    /**
     * 您还没有加入任何战盟
     */
    GuildBattle_HaveNoGuild(450654,1),
    /**
     * 您所在的战盟未能匹配到合适对手，本轮轮空！
     */
    GuildBattle_NoJoinRight(179304,1),
    /**
     * 找不到战盟联赛地图
     */
    GuildBattle_CanNotFindBattleCopy(355686,1),
    /**
     * 战盟联赛只允许{0}名战盟成员进入，人数已满！
     */
    GuildBattle_JoinNumLimit(305559,1),
    /**
     * 战火已燃，战盟争霸！战盟联赛开启！谁才是最强战盟，拭目以待！
     */
    GuildBattle_BattleOpenNotice(295666,6),
    /**
     * 地图错误
     */
    GuildBattle_Fire_MapError(174209,1),
    /**
     * 炮击成功，获得{0}战功
     */
    GuildBattle_Fire_Success(16054,1),
    /**
     * 闪避！闪避！【{0}】战盟炮击了【{1}】区域！
     */
    GuildBattle_Fire_Success_Broadcast(183104,1),
    /**
     * 您并没有选取打击范围！
     */
    GuildBattle_Fire_NoTarget(393482,1),
    /**
     * 开始战斗！！！！
     */
    GuildBattle_StartFight(426613,1),
    /**
     * 【{0}】战盟占领{1}旗帜！
     */
    GuildBattle_FlagOccupiedSeccess(510743,1),
    /**
     * 击中了[ff0000]{0}[-]玩家
     */
    GuildBattle_Fire_CauseDamage(189648,1),
    /**
     * 您未击中任何敌人！
     */
    GuildBattle_Fire_No_Effect(266861,1),
    /**
     * 战场将在{0}秒后关闭！
     */
    GuildBattle_Map_Delete_Notice(130794,1),
    /**
     * 激活[ff0000]{0}[-]技能
     */
    ActivateSoulSkill(194385,1),
    /**
     * 声望已达今日获取上限,还请明日继续！
     */
    CampBattle_PerUpTodayExploitLimit(275676,1),
    /**
     * 每日俸禄,今日已领取,请明日继续！
     */
    CampBattle_PerGotTodaySalary(272746,1),
    /**
     * 您的等级不足,不可领取每日俸禄.加油升等级吧！下次来就可以点我了哦！
     */
    CampBattle_PerNoGetTodaySalary(224503,1),
    /**
     * 俸禄领取成功！
     */
    CampBattle_PerGetTodaySalarySuccess(378633,1),
    /**
     * 俸禄领取失败!
     */
    CampBattle_PerGetTodaySalaryError(373767,1),
    /**
     * 召集失败，召集时间已过期
     */
    AgreeCallTogetherFailed_TimrOut(413876,1),
    /**
     * 召集失败，地图销毁
     */
    AgreeCallTogetherFailed_MapDestory(35012,1),
    /**
     * 召集失败，只支持本地图召集
     */
    AgreeCallTogetherFailed_OnlyMap(68483,1),
    /**
     * 召集失败，不支持活动地图召集野外地图
     */
    AgreeCallTogetherFailed_NotSupport(483522,1),
    /**
     * 进入地图失败，角色等级不足
     */
    EnterMapFailed_LevelBelow(386019,1),
    /**
     * 进入地图失败，角色等级过高
     */
    EnterMapFailed_LevelAbove(372713,1),
    /**
     * 进入地图失败，该地图线路人数已满
     */
    EnterMapFailed_MaxNum(13858,1),
    /**
     * 水晶塔
     */
    ForeverFight_CrystalTower(402223,1),
    /**
     * 南北战场开始了,是否进入战场?
     */
    ForeverFight_StartIsEntry(77145,1),
    /**
     * 胜利
     */
    ForeverFight_Win(368329,1),
    /**
     * 失败
     */
    ForeverFight_Fail(221004,1),
    /**
     * 平局
     */
    ForeverFight_Peace(227365,1),
    /**
     * 听风阁
     */
    ForeverFight_BattleField_1(367463,1),
    /**
     * 吹雪楼
     */
    ForeverFight_BattleField_2(393519,1),
    /**
     * 您选择的场次时间已过,不能报名
     */
    ForeverFight_Apply_CountEnd(306947,1),
    /**
     * 报名人数不足，战场未开启
     */
    ForeverFight_Apply_NoBody(352234,2),
    /**
     * 战场点不足,不能领取该奖励
     */
    ForeverFight_Reward_NoScore(384897,1),
    /**
     * 奖励已领取
     */
    ForeverFight_Reward_HasGet(42165,1),
    /**
     * [ffffff]您击败了[33bb43]{0}[-],获得[33bb43]{1}[-]积分[-]
     */
    ForeverFight_Kill_Tips(271411,1),
    /**
     * [ffffff]您被[ff0000]{0}[-]击败,损失[ff0000]{1}[-]积分[-]
     */
    ForeverFight_Killed_Tips(309703,1),
    /**
     * [ffffff]您协助[33bb43]{0}[-]击败[33bb43]{1}[-],获得[33bb43]{2}[-]积分[-]
     */
    ForeverFight_Assists_Tips(45810,1),
    /**
     * [ffffff][33bb43]{0}[-]已累积击败[33bb43]{1}[-]人,如有神助！[-]
     */
    ForeverFight_Kill_Notice(277725,1),
    /**
     * [ffffff]您摧毁了[33bb43]水晶塔[-],获得[33bb43]{0}[-]积分[-]
     */
    ForeverFight_killcrystal_Tips(248389,1),
    /**
     * [ffffff]己方[33bb43]{0}[-]结算,获得[33bb43]{1}[-]积分[-]
     */
    ForeverFight_addcrystal_Tips(123847,1),
    /**
     * 战场结果:[33bb43]胜利[-],获得战场点:[33bb43]{0}[-]
     */
    ForeverFight_endtips1(482097,1),
    /**
     * 战场结果:[33bb43]失败[-],获得战场点:[33bb43]{0}[-]
     */
    ForeverFight_endtips2(288515,1),
    /**
     * 战场结果:[33bb43]平局[-],获得战场点:[33bb43]{0}[-]
     */
    ForeverFight_endtips3(396525,1),
    /**
     * 未开启
     */
    ForeverFight_unopened(136047,1),
    /**
     * 你已经令其他人闻风丧胆，无人报名应战。
     */
    ForeverFight_onlyonetips(499868,1),
    /**
     * 战火已燃,南北相争！快来报名应战。
     */
    ForeverFight_notice(347138,6),
    /**
     * 金币不足,不可找回,去商城加点金币,再来找回.
     */
    Recover_NoEnoughCopper(127771,1),
    /**
     * 绑元不足,可以找回,去商城买点绑元,再来找回
     */
    Recover_NoEnoughCoupons(493174,1),
    /**
     * 你的VIP等级不足,可以换成金币找回
     */
    Recover_NoReachLv(173601,1),
    /**
     * 你从未进过该活动,不可找回,去做一次活动吧！
     */
    Recover_NeverenterActivity(410008,1),
    /**
     * 该副本未通关,不可找回
     */
    Recover_NoFinishedCopy(386090,1),
    /**
     * 您已被阵营官员禁言，还需要[33bb43]{0}[-]分钟可自动解除！
     */
    CampForbidTalkprom(184031,3),
    /**
     * 您无权禁言该玩家！
     */
    CampForbidTalk(247422,1),
    /**
     * 您要查看的玩家不在线
     */
    LooKOtherPlayerInfo_OtherPlayerOffLine(161869,1),
    /**
     * 您查看的玩家不存在
     */
    LookOtherPlayerInfo_OtherPlayerNoExit(67196,1),
    /**
     * 你已被阵营官员禁言
     */
    CampChairmanForbid(176850,1),
    /**
     * 对方已离线，留言成功
     */
    OffLineMsgSuccess(506614,1),
    /**
     * 只有加入战盟才可开启战盟商店
     */
    GuildShopUnopenNeedJoinGuild(261801,1),
    /**
     * 限时战翼不能分解成碎片
     */
    TimerWingCanNotExplan(36591,1),
    /**
     * {0}拿下第一滴血！监天司方声势大振！
     */
    Singlebatkill1camp1(385245,3),
    /**
     * {0}拿下第一滴血！神都监方声势大振！
     */
    Singlebatkill1camp2(82154,3),
    /**
     * {0}已经主宰比赛！
     */
    Singlebatkill4(107710,3),
    /**
     * {0}已经无人可挡！
     */
    Singlebatkill8(20390,3),
    /**
     * {0}已经如同神一般！快去阻止他吧！
     */
    Singlebatkill15(505604,3),
    /**
     * {0}已经超神破敌！求求你们快去终结他吧！
     */
    Singlebatkill25(49447,3),
    /**
     * {0}已经摧毁神都监守护塔,监天司阵营向胜利迈进！
     */
    Singlebatdestroycap1(355230,3),
    /**
     * {0}已经摧毁监天司守护塔,神都监阵营向胜利迈进！
     */
    Singlebatdestroycap2(312780,3),
    /**
     * {0}已经摧毁神都监大营,监天司阵营取得胜利！
     */
    Singlebatdstcap1(46313,3),
    /**
     * {0}已经摧毁监天司大营,神都监阵营取得胜利！
     */
    Singlebatdstcap2(147755,3),
    /**
     * 排名奖励
     */
    RankReward_Title(20794,1),
    /**
     * 排名奖励
     */
    RankReward_content(20794,1),
    /**
     * 服务器未到开启时间
     */
    ServerNotSetOpenTime(353488,1),
    /**
     * 已读邮件删除完毕
     */
    MailDeleteSuccess(317422,1),
    /**
     * 无可收取的附件
     */
    MailNoAttachment(407034,1),
    /**
     * 战斗模式已切换为:{0}
     */
    PKModeManager_NowPKModeChange(18675,1),
    /**
     * 已进入无PK限制的地图
     */
    PKModeManager_NowMapKillNoScore(113100,1),
    /**
     * 已达总次数上限
     */
    Backend_UpTotalNumLimit(72957,1),
    /**
     * 已达今日次数上限
     */
    Backend_UpDayNumLimit(308377,1),
    /**
     * 获取道具失败
     */
    Backend_AddItemWrong(500098,1),
    /**
     * {0}队员不能进入地图，退出跟随状态
     */
    TeamQuitFollow_MemberNotInMap(463975,2),
    /**
     * 不能进入队长的地图，退出跟随状态
     */
    TeamQuitFollow_OwnNotInMap(355109,2),
    /**
     * 货币不足,需要:{0}金币才能修理
     */
    Equip_DurMoneyNoEnough(213486,1),
    /**
     * 背包剩余格子不足，无法收取邮件附件
     */
    ReceiveMailAttachmentFaile_NoCell(140903,1),
    /**
     * 物品下架失败，已经被购买了
     */
    TradeDownIsFail(9765,1),
    /**
     * 每日最多可发30个红包，已达上限
     */
    RedpacketGiveNumLimTips(89698,1),
    /**
     * 每日最多可抢30个红包，已达上限
     */
    RedpacketGetNumLimTips(92857,1),
    /**
     * 请清理背包，否则带队礼包不能正常发放。
     */
    GiveTeamLeaderRewardFailed(14579,1),
    /**
     * 请清理背包，否则助战礼包不能正常发放。
     */
    GiveTeamMemberRewardFailed(134243,1),
    /**
     * 妖魔：已经逃了一半路程了，哈哈！
     */
    DailyMoneyPosListWarning1(447976,1),
    /**
     * [ff0000]妖魔：马上就达到逃离点了，你已经阻止不了我了！[-]
     */
    DailyMoneyPosListWarning2(479416,1),
    /**
     * 成功发送聚义令！
     */
    CallOthers(495921,1),
    /**
     * 激活码不存在，验证失败
     */
    ActiveCodeUseFailed_NoExist(248837,1),
    /**
     * 激活码已被使用，验证失败
     */
    ActiveCodeUseFailed_Used(8361,1),
    /**
     * 激活码未到可使用时间，验证失败
     */
    ActiveCodeUseFailed_WrongTime_before(217873,1),
    /**
     * 激活码已过期，验证失败
     */
    ActiveCodeUseFailed_WrongTime_late(274818,1),
    /**
     * 该激活码不支持在当前服务器使用，验证失败
     */
    ActiveCodeUseFailed_WrongServer(38869,1),
    /**
     * 该激活码不支持当前账号平台，验证失败
     */
    ActiveCodeUseFailed_WrongPlatform(383678,1),
    /**
     * 已经使用过同类型激活码，不可重复使用
     */
    ActiveCodeUseFailed_SameTypeUsed(220695,1),
    /**
     * 激活码验证成功，请查收邮件
     */
    ActiveCodeUseSuccess(296696,1),
    /**
     * 激活码
     */
    ActiveCodeUseTitle(104882,1),
    /**
     * 激活码验证成功，以下是您通过激活码获取的道具。
     */
    ActiveCodeUseContean(32790,1),
    /**
     * 背包已满，将不再获得任何道具，请及时清理！
     */
    BagFullGetNothing(202159,1),
    /**
     * 客户端与服务器协议号不匹配
     */
    VersionWrong(514824,1),
    /**
     * 每日反馈最多三次，请明日再来反馈，谢谢您的反馈
     */
    PlayerFeedbackCount(308681,1),
    /**
     * 谢谢您的反馈
     */
    PlayerFeedbackSuccess(243985,1),
    /**
     * 您的背包已满,将不再获得掉落物,请及时处理!
     */
    Practice_BagFullGetNothing(311690,1),
    /**
     * 【{0}】将于{1}秒后出现在【{2}】，击败它们可是会掉落奖励哟！<t=4>立即前往,2203,[8,27]</t>
     */
    KillmonsterTips(29370,6),
    /**
     * 活动结束，捣蛋的小妖离开地图了！
     */
    KillmonsterEndTips(283250,4),
    /**
     * 驱逐小妖活动已开启是否前往?
     */
    KillmonsterOpenTips(106127,1),
    /**
     * 捣蛋的小妖都被干掉啦！
     */
    KillmonsterKillallTips(485729,1),
    /**
     * 邮件活动
     */
    MailActivity_Titile(458548,1),
    /**
     * 你收到一份等级奖励
     */
    MailActivity_MoneyContent(374629,1),
    /**
     * 您的等级不足,不能进入此修炼场
     */
    Practice_EnoughLevel(104640,1),
    /**
     * 混沌战场日结算
     */
    MailChaosbattleti_Titile(492538,1),
    /**
     * 贵人多忘事,侠士您忘领取今日混沌战场奖励。现通过邮件补发,请注意查收!
     */
    MailChaosbattleti_Content(211294,1),
    /**
     * 混沌战场周结算
     */
    MailChaosbattleWeek_Titile(28994,1),
    /**
     * 你本周在[青铜]段位中排名{0}位！获得以下奖励，可喜可贺！
     */
    MailChaosbattleWeek_Content1(2254,1),
    /**
     * 你本周在[白银]段位中排名{0}位！获得以下奖励，可喜可贺！
     */
    MailChaosbattleWeek_Content2(90061,1),
    /**
     * 你本周在[黄金]段位中排名{0}位！获得以下奖励，可喜可贺！
     */
    MailChaosbattleWeek_Content3(348097,1),
    /**
     * 你本周在[铂金]段位中排名{0}位！获得以下奖励，可喜可贺！
     */
    MailChaosbattleWeek_Content4(91343,1),
    /**
     * 你本周在[王者]段位中排名{0}位！获得以下奖励，可喜可贺！
     */
    MailChaosbattleWeek_Content5(506097,1),
    /**
     * 你本周在[皇者]段位中排名{0}位！获得以下奖励，可喜可贺！
     */
    MailChaosbattleWeek_Content6(341457,1),
    /**
     * 你本周在[帝王]段位中排名{0}位！获得以下奖励，可喜可贺！
     */
    MailChaosbattleWeek_Content7(232776,1),
    /**
     * 混沌战场升段位
     */
    MailChaosbattleUp_Titile(158583,1),
    /**
     * 祝贺你！本周您的混沌战场段位升级啦！
     */
    MailChaosbattleUp_Content(192844,1),
    /**
     * 混沌战场降段位
     */
    MailChaosbattleDown_Titile(78525,1),
    /**
     * 很遗憾！你没有达到最低周积分要求，本周您的混沌战场段位降级了。
     */
    MailChaosbattleDown_Content(46995,1),
    /**
     * 混沌战场段位不变
     */
    MailChaosbattleUnchang_Titile(277947,1),
    /**
     * 本周您的混沌战场段位保持不变。
     */
    MailChaosbattleUnchan_Content(366918,1),
    /**
     * [00ff00]{0}[-]靠近神秘漩涡获得【{1}】效果
     */
    ChaosFightObjectBuff(158545,3),
    /**
     * [00ff00]{0}[-]靠近神秘漩涡获得{1}积分
     */
    ChaosFightObjectIntegral(219773,3),
    /**
     * 神秘漩涡出现在战场,靠近神秘漩涡会激发特殊效果！
     */
    ChaosbattleMap2050(386897,3),
    /**
     * 注意！[00ff00]{0}[-]唤醒了沉睡的【{1}】！
     */
    ChaosbattleMap2051(186959,3),
    /**
     * 在本次阵营战中所在阵营获得胜利
     */
    Camp_war_target_desc_win(300597,1),
    /**
     * 在本次阵营战中[00b404]破敌{0}[-]人以上
     */
    Camp_war_target_desc_skill(425351,1),
    /**
     * 在本次阵营战中原地复活{0}次以上
     */
    Camp_war_target_desc_resurrection(125333,1),
    /**
     * 在本次阵营战中英勇牺牲{0}次以上
     */
    Camp_war_target_desc_dead(170778,1),
    /**
     * 在本次阵营战中[00b404]助攻{0}[-]次以上
     */
    Camp_war_target_desc_assis(349291,1),
    /**
     * 在本次阵营战中[00b404]积分{0}[-]分以上
     */
    Camp_war_target_desc_mark(305896,1),
    /**
     * 该地图能进入的同阵营玩家已满，请联系阵营领袖来调节！
     */
    Camp_war_limit_numb(409652,1),
    /**
     * 阵营战活动奖励
     */
    Camp_war_activeMailTitle(190091,1),
    /**
     * 您在阵营战中英勇战斗，完成了很多的阵营目标，获得如下的奖励！
     */
    Camp_war_activeMailContent(64789,1),
    /**
     * 守护击杀奖励
     */
    Camp_war_nomalMailTitle(74815,1),
    /**
     * 由于您的积极参与，成功击杀敌方阵营守护，获得如下的奖励！每日[6b8e00]首次击杀[-]可获得大量奖励！
     */
    Camp_war_nomalMailContent(498216,1),
    /**
     * 您的等级不足！
     */
    Camp_war_lvlimit(406862,1),
    /**
     * 活动未开启
     */
    Camp_war_activeiopen(34941,1),
    /**
     * 阵营战将在{0}分钟后开启，活动只在1线地图进行，参与活动将获得大量奖励！
     */
    Camp_war_opennoticefore(484950,6),
    /**
     * 阵营战活动已在双方阵营领地地图中1线开启，参与活动将获得大量奖励！
     */
    Camp_war_opennotice(514060,6),
    /**
     * 领地中出现3个特殊怪！在地图中标识的位置！
     */
    Camp_war_buffnoticefor(12501,3),
    /**
     * 玩家[f02c2c]{0}[-]击破在{1}中的加持怪物{2}，在该地图的同阵营玩家获得该加成！
     */
    Camp_war_buffkill(164434,3),
    /**
     * 玩家[f02c2c]{0}[-]击破在{1}中的恢复灵兽，阻止了{2}回血！
     */
    Camp_war_buffSepkill(437574,3),
    /**
     * 玩家[f02c2c]{0}[-]击破在{1}中的恢复灵兽，给{2}恢复大量的血量！
     */
    Camp_war_buffSepkillhell(450652,3),
    /**
     * {0}中的[f02c2c]{1}[-]已经被击败，怪物将在5分钟后复活！
     */
    Camp_war_buffkillres(53574,3),
    /**
     * 玩家[33bb43]{0}[-]击破[f02c2c]{1}[-]，为[33bb43]{2}[-]阵营带来无上荣耀！
     */
    Camp_war_bosskill(502629,6),
    /**
     * [ffffff]您击败了[33bb43]{0}[-],获得[33bb43]{1}[-]点积分[-]
     */
    Camp_war_killnotice(436717,1),
    /**
     * [ffffff]您协助[33bb43]{0}[-]击败了[33bb43]{1}[-],获得[33bb43]{2}[-]点积分[-]
     */
    Camp_war_assisnotice(91600,1),
    /**
     * [ffffff][33bb43]{0}[-]已累积击败[33bb43]{1}[-]人,无人敢当！[ffffff]
     */
    Camp_war_killmanynotice(523281,1),
    /**
     * 阵营守护正在遭受敌方攻击，当前剩余[00B404]{0}%[-]血量，速速去支援吧！<t=4>前往支援,{1},[{2},{3}]</t>
     */
    Camp_war_bosshpnotice(27218,65),
    /**
     * 在本次阵营战斗中{0}、{1}和{2}积分排名前3为本阵营做出了绝对的贡献！
     */
    Camp_war_rankingnotice(261916,65),
    /**
     * [ffffff]伟大的阵营领袖[33bb43][{0}][-]上线了！[-]
     */
    Camp_leader_onlinenotice(403503,65),
    /**
     * 阵营镖车[33bb43]{0}[-]分钟后将出现在[33bb43]监天司领地[-]，摧毁镖车，赢得奖励！<t=4>立即前往,53,[139.9,59]</t>
     */
    Camp_rob_open_notice1(100947,68),
    /**
     * 阵营镖车[33bb43]{0}[-]分钟后将出现在[33bb43]神都监领地[-]，摧毁镖车，赢得奖励！<t=4>立即前往,52,[56.4,107.3]</t>
     */
    Camp_rob_open_notice2(204564,68),
    /**
     * 镖车：我已到终点，你们劫镖失败！只有下次继续咯！
     */
    Camp_finish_notice(337479,3),
    /**
     * 镖车：要想从我身上拿到奖励，你们就要快点来！
     */
    Camp_move_notice(516386,3),
    /**
     * [33bb43]{0}[-]对阵营镖车造成致命一击，为[33bb43]监天司阵营[-]带来无上荣耀，在活动领地中的监天司玩家将获得大量奖励！
     */
    Camp_dart_killnotice1(511147,70),
    /**
     * [33bb43]{0}[-]对阵营镖车造成致命一击，为[33bb43]神都监阵营[-]带来无上荣耀，在活动领地中的神都监玩家将获得大量奖励！
     */
    Camp_dart_killnotice2(58965,70),
    /**
     * 阵营镖车已在[33bb43]{0}[-]中刷新，对镖车造成[33bb43]最后一击[-]的玩家及在领地中同阵营玩家，均可获得大量奖励！<t=4>立即前往,{1},[{2},{3}]</t>
     */
    Camp_dart_open_notice(424682,64),
    /**
     * {0}在【阵营领地】中击败5人！谁与争锋！
     */
    Camp_dart_kill1(44315,3),
    /**
     * {0}在【阵营领地】中击败10人！天降神威！
     */
    Camp_dart_kill2(401398,3),
    /**
     * {0}在【阵营领地】中击败20人！势不可挡！
     */
    Camp_dart_kill3(9999,3),
    /**
     * {0}在【阵营领地】中击败30人！战神附体！
     */
    Camp_dart_kill4(6245,3),
    /**
     * {0}在【阵营领地】中击败50人！如帝临尘！
     */
    Camp_dart_kill5(265825,3),
    /**
     * {0}在【阵营领地】中击败100人！斩碎虚空！
     */
    Camp_dart_kill6(101516,3),
    /**
     * 阵营镖车参与奖
     */
    Camp_war_MailloseTitle(165894,1),
    /**
     * 您所在的阵营在镖车活动中未能获得镖车归属，由于您的积极参与活动获得如下奖励！
     */
    Camp_war_MailloseContent(6298,1),
    /**
     * 阵营镖车参与奖
     */
    Camp_war_MailnokillTitle(165894,1),
    /**
     * 镖车未在规定的时间内被摧毁，所有参与活动的玩家获得如下奖励！
     */
    Camp_war_MailnokillContent(415453,1),
    /**
     * [1E90FF][{0}][-]在[33bb43]{5}[-]中英勇击败剑魔{1},竟然获得了[33bb43]{3}[-]个[33bb43]<t=2>,{4},{2}</t>[-]！
     */
    ItemDropGetBroadCast(200140,18),
    /**
     * [1E90FF][{0}][-]鸿运天降,竟然从[1E90FF]<t=2>,{1},{2}</t>[-]中获得[33bb43]{3}[-]个[33bb43]<t=2>,{4},{5}</t>[-],可喜可贺！
     */
    ItemOpenPackageGetBroadCast(440215,18),
    /**
     * [1E90FF][{0}][-]按照[1E90FF]{1}[-]的提示,一铲下去竟是[33bb43]{3}[-]个[33bb43]<t=2>,{4},{2}</t>[-],真是好运！
     */
    ItemTreasureGetBroadCast(432627,18),
    /**
     * [1E90FF][{0}][-]在[33bb43]{5}[-]中英勇击败剑魔{1},竟然获得了[33bb43]{3}[-]个[33bb43]<t=2>,{4},{2}</t>[-]！
     */
    ItemDropGetBroadCast1(200140,18),
    /**
     * [1E90FF][{0}][-]鸿运天降,竟然从[1E90FF]<t=2>,{1},{2}</t>[-]中获得[33bb43]{3}[-]个[33bb43]<t=2>,{4},{5}</t>[-],可喜可贺！
     */
    ItemOpenPackageGetBroadCast1(440215,18),
    /**
     * [1E90FF][{0}][-]按照[1E90FF]{1}[-]的提示,一铲下去竟是[33bb43]{3}[-]个[33bb43]<t=2>,{4},{2}</t>[-],真是好运！
     */
    ItemTreasureGetBroadCast1(432627,18),
    /**
     * 受到【剑灵守护】，防御值效果大大增强！
     */
    Map_guard_add(226008,1),
    /**
     * 受到【恶灵守护】，攻击值效果大大增强！
     */
    Map_damage_add(126801,1),
    /**
     * 今日福缘已满！！请继续期待下一次活动吧！！
     */
    Preach_no(378153,1),
    /**
     * 讲道结束！修道之路漫漫,请君下次聆听！
     */
    Preach_over(433270,6),
    /**
     * {0}秒后，讲道道人将在【长陵】讲道,请大家勿要错过！<t=4>立即前往,2,[120,192]</t>
     */
    Preach_notice(416950,6),
    /**
     * 讲道奖励
     */
    Preach_mailtittle(508281,1),
    /**
     * 您在听道过程中似有所悟，请注意查收奖励！
     */
    Preach_mailcontent(346110,1),
    /**
     * 灵
     */
    NewPetType1(291995,1),
    /**
     * 魔
     */
    NewPetType2(12972,1),
    /**
     * 妖
     */
    NewPetType3(184305,1),
    /**
     * 拥有:{0}/{1}
     */
    Soul_Havenum(364329,1),
    /**
     * {0}件效果:
     */
    SoulFrontsuit(438532,1),
    /**
     * 获得神兵：{0}
     */
    Soul_Get(330465,1),
    /**
     * 提升剑侍经验:{0}
     */
    New_PetUpExp(345384,1),
    /**
     * 只有加入阵营后才能进入该地图。
     */
    Entermap_need_camp(446620,3),
    /**
     * 只有加入战盟后才能进入该地图。
     */
    Entermap_need_guild(46482,3),
    /**
     * 只有拥有配偶后才能进入该地图，快去寻找你缘分的另一半吧！2016.11.21张龙预留的。
     */
    Entermap_need_cp(44492,3),
    /**
     * 只有拥有师徒关系后才能进入该地图，快去寻找你的同路人吧！2016.11.21张龙预留的。
     */
    Entermap_need_teach(494281,3),
    /**
     * 该地图必须以单人形式进入，你已自动离队。
     */
    Entermap_need_oneplayer(370424,3),
    /**
     * 队伍中有不同阵营的玩家，你已自动离队。
     */
    Entermap_exit_camp(176223,3),
    /**
     * 队伍中有不同战盟的玩家，你已自动离队。
     */
    Entermap_exit_guild(30501,3),
    /**
     * 只有队伍成员互为夫妻关系才能进入该地图。2016.11.21张龙预留的。
     */
    Entermap_member_cp(227456,3),
    /**
     * 只有队伍成员互为师徒关系才能进入该地图。2016.11.21张龙预留的。
     */
    Entermap_member_tech(56186,3),
    /**
     * 地图【{0}】要求组队双方必须为同一阵营。
     */
    Entermap_lead_samecamp(294284,3),
    /**
     * 地图【{0}】要求组队双方必须为同一战盟。
     */
    Entermap_lead_sameguild(336450,3),
    /**
     * 小师妹
     */
    Girlmailtitle(39815,1),
    /**
     * 少侠,你的背包满了哟,这是方才你点赞获得的奖励哟。
     */
    Girlmaildec_place(33262,1),
    /**
     * 今天又有10个小伙伴给我点赞了，好开心，跟你一起分享这份喜悦吧！
     */
    Girlmaildec_own(230008,3),
    /**
     * 礼物已经放置好,好友点赞有概率拿走！
     */
    Girl_gift_place(82508,1),
    /**
     * 点赞成功
     */
    Girl_gift(318783,1),
    /**
     * 运气真不错，获得一个礼包
     */
    Girl_gift_additional(340459,1),
    /**
     * 已发送[世界]频道咯！
     */
    Girl_convenient(477830,1),
    /**
     * 留言成功
     */
    Girl_message(272677,1),
    /**
     * 不能刷屏哟！明天再来留言吧~
     */
    Girl_messageerror(416866,1),
    /**
     * 小师妹求点赞！点我头像咯！ヽ(•̀ω•́ )ゝ
     */
    Girl_world1(249379,2),
    /**
     * 师妹空间求点赞！
     */
    Girl_world2(136795,2),
    /**
     * 在“镜子”中给小师妹选一个动作吧！
     */
    Girl_action(464007,1),
    /**
     * 对方没有给小师妹设置动作偏好
     */
    Girl_action_other(472588,1),
    /**
     * 已点赞，明天再来吧！
     */
    Girl_thumbup(95788,1),
    /**
     * 每日10次点赞奖励已拿完啦！
     */
    Girl_ceiling(372208,1),
    /**
     * 每日20次点赞次数已用完！
     */
    Girl_thumbup_end(144360,1),
    /**
     * 您所需的货币不足！
     */
    Money_notenough(219880,0),
    /**
     * 恭喜【[00ff00]{0}[-]】获得稀有剑侍【[ffa800]{1}[-]】，战斗中如虎添翼，即将在九州六国一展雄风！
     */
    Pet_broadcast1(413505,18),
    /**
     * 恭喜【[00ff00]{0}[-]】获得极品剑侍【[ff0000]{1}[-]】，战斗中如有仙魔助力，一统天下指日可待！
     */
    Pet_broadcast2(162083,18),
    /**
     * 阵营地图无法进入副本！
     */
    Camp_enterfb_notice(198061,3),
    /**
     * 聊天次数已上限，[ff0000]每日必做[-]活跃90或[ff0000]vip1[-]可解除聊天限制
     */
    WorldChatNumLimit(267102,3),
    /**
     * 聊天次数已上限，[ff0000]每日必做[-]活跃90或[ff0000]vip1[-]可解除聊天限制
     */
    CampChatNumLimit(267102,3),
    /**
     * 击败{0}时队伍成员[ec8f0b]{1}[-]获得场景技能[ec8f0b][技.{2}][-]，必要时能扭转大局！
     */
    ProtectNPCGetEffect(184394,3),
    /**
     * 与【{0}】的亲密度增加{1}点
     */
    Intimacytips(117666,2),
    /**
     * 今日与【{0}】通过副本获得亲密度已满
     */
    IntimacyFbMaxtips(174106,2),
    /**
     * 每日优惠购
     */
    MailforDay_Titile(129336,1),
    /**
     * 因背包空间不足，你购买的物品由邮件发送。请查收
     */
    MailforDay_Content(443018,1),
    /**
     * 去购买月卡吧,就可以找回资源咯!
     */
    Recovery_MonthCard(228177,1),
    /**
     * 购买月卡吧,就可以开启扫荡了,减少时间也可以获得资源咯
     */
    Sweeping_MonthCard(333852,1),
    /**
     * 本周没有阵营精英
     */
    Camp_BanTalk_NoElite(400286,1),
    /**
     * 您不是阵营官员，没有禁言的权限
     */
    Camp_BanTalk_YouNoneThisFun(355018,1),
    /**
     * 不能对阵营官员使用禁言
     */
    Camp_BanTalk_CannotBanElite(174117,1),
    /**
     * 今日的禁言次数已经用完了
     */
    Camp_BanTalk_CountEnd(372485,1),
    /**
     * 您已经对{0}禁言成功
     */
    Camp_BanTalk_Success(479546,1),
    /**
     * 监天司
     */
    Camp1_tianji(299533,1),
    /**
     * 神都监
     */
    Camp2_xuanzong(146194,1),
    /**
     * 申请成功
     */
    Guild_applynotice(426674,1),
    /**
     * 师妹的答谢
     */
    MultipleCopyTitle(374318,1),
    /**
     * 谢谢您来搭救我，这些礼物是送给您的，快收下啦！
     */
    MultipleCopyContent(402404,1),
    /**
     * 重连失败，活动已经结束
     */
    CrossActiveOver(243514,1),
    /**
     * 今日接取数量已达上限，无法接取！
     */
    Task_master_accept_failed_limit(112779,1),
    /**
     * 您已经接取了该类型任务,先完成了前面的任务,再来接取吧！
     */
    Camp_task_recnotic(331297,1),
    /**
     * 阵营激励物资
     */
    Camp_dayrewTitle(360872,1),
    /**
     * 您所在的战盟在阵营中排名前10，奖励已在邮件附件中发送。还请再接再厉！
     */
    Camp_dayrewAMailContent(313261,1),
    /**
     * 阵营官员奖励
     */
    Camp_offrewTitle(247238,1),
    /**
     * 您所在的战盟在阵营领袖角逐上获得最终胜利！您作为战盟的核心成员，获得的奖励已在邮件附件中发送。
     */
    Camp_offrewMailContent(19237,1),
    /**
     * 王盟成员奖励
     */
    Camp_memberrewTitle(384979,1),
    /**
     * 您所在的战盟在阵营领袖角逐上获得最终胜利！您作为战盟的主要成员，获得的奖励已在邮件附件中发送。
     */
    Camp_memberrewMailContent(272634,1),
    /**
     * 鼓舞次数已满，无须再鼓舞啦！
     */
    InspireMax(435109,1),
    /**
     * 领取成功
     */
    Sing_tower_notic(306943,1),
    /**
     * 背包已满，请及时清理。掉落物进入临时背包。
     */
    Bag_full_forserver(273444,3),
    /**
     * 掉落物返还
     */
    Item_return_mailname(255447,1),
    /**
     * 少侠，这是您遗落在副本中的道具。每次副本中因背包满而遗失的怪物掉落，都会在切换地图时返还。
     */
    Item_return_mail(300107,1),
    /**
     * 阵营镖车奖励
     */
    Camp_dart_rewTitle(459656,1),
    /**
     * 您所在的阵营获得最终镖车的归属，参与活动获得如下奖励！还请下次继续！
     */
    Camp_dart_rewMailContent(259956,1),
    /**
     * 背包已满
     */
    Camp_dart_bloodrewTitle(238941,1),
    /**
     * 您在镖车活动中获得奖励因背包已满，通过邮件发放！请及时领取！
     */
    Camp_dart_bloodrewMailContent(426070,1),
    /**
     * 您已接取该任务！
     */
    Schoolnotic(259777,1),
    /**
     * VIP1级开启一键全橙功能！
     */
    Schoolnotic_1(466652,1),
    /**
     * 您的任务进度未满，不可开启宝箱
     */
    Schoolnotic_2(28674,1),
    /**
     * 今日宝箱已领取！
     */
    Schoolnotic_3(144384,1),
    /**
     * 今日任务可接取次数已达最大上限！
     */
    Schoolnotic_4(79203,1),
    /**
     * 背包已满
     */
    Bag_full_forsingfight(238941,1),
    /**
     * 您在青藤战场中获得奖励因背包已满，通过邮件发放！请及时领取！
     */
    Bag_full_forsingfi_Content(144285,1),
    /**
     * 背包已满
     */
    Bag_full_forsingtower(238941,1),
    /**
     * 您在通天塔中获得奖励因背包已满，通过邮件发放！请及时领取！
     */
    Bag_full_forsingtow_Content(23764,1),
    /**
     * 邮件箱已满，系统将自动删除超出容量的旧邮件！请及时清理！
     */
    Mailbox_autodelete(215289,3),
    /**
     * 今日组队侠义值获取：{0}/1000
     */
    Friendship_ing(482878,3),
    /**
     * 今日通过组队获取侠义值已达上限，少侠真是乐于助人的英雄！
     */
    Friendship_max(36054,3),
    /**
     * 队伍成员未满!
     */
    JJC4V4TeamNumberNotEngouth(446723,1),
    /**
     * 竞技场排名不足{0}，无法进入此战场！
     */
    Underground_enter(300040,1),
    /**
     * 同阵营成员人数已达上限，请选择其他地宫！
     */
    Underground_maxnum(108762,1),
    /**
     * 已绑定{0}，活动开启1小时后解绑！
     */
    Underground_binding(435854,1),
    /**
     * [DAA520]“阵营地宫”[-]中出现尊贵无比的至尊宝箱，快去看看！
     */
    Underground_box_begin(92429,6),
    /**
     * {0} {1}在{2}中开启了[DAA520]{3}[-]，你获得共享奖励{4}
     */
    Underground_getitem(78313,2),
    /**
     * {0} {1}在{2}中击败了BOSS[DAA520]{3}[-]，你获得共享奖励{4}
     */
    Underground_getitem_boss(359572,2),
    /**
     * [9933ff]监天司[-]的[1E90FF]{1}[-]在“[DAA520]{2}[-]”击杀[DAA520]BOSS[-]，同地宫同阵营成员共享奖励！
     */
    Underground_kill_boss1(302808,18),
    /**
     * [0066ff]神都监[-]的[1E90FF]{1}[-]在“[DAA520]{2}[-]”击杀[DAA520]BOSS[-]，同地宫同阵营成员共享奖励！
     */
    Underground_kill_boss2(265229,18),
    /**
     * 巅峰4v4胜利奖励
     */
    JJC4v4_winMailTitle(108442,1),
    /**
     * 你和你的队友在巅峰4v4击败对手，获得以下奖励
     */
    JJC4v4_winMailContent(234713,1),
    /**
     * 巅峰4v4战败奖励
     */
    JJC4v4_loseMailTitle(465840,1),
    /**
     * 你和你的队友在巅峰4v4中棋差一招，获得以下奖励
     */
    JJC4v4_loseMailContent(183375,1),
    /**
     * 您已经死亡，是否进入观战状态！
     */
    JJC4v4_MatchModeTips(295441,1),
    /**
     * 你不是队长无法排队!
     */
    JJC4v4_teamowner(324352,1),
    /**
     * {0}成员等级不足，无法参加！
     */
    JJC4v4_level(27696,1),
    /**
     * {0}成员离线，无法参加！
     */
    JJC4v4_Offline(102381,1),
    /**
     * 好友改名
     */
    Name_card_title(386335,1),
    /**
     * 您的好友[6b8e00]{0}[-]已将名字更改为[6b8e00]{1}[-]
     */
    Name_card_content(510130,1),
    /**
     * 元宝不足购买失败
     */
    Fund_tips(475098,1),
    /**
     * 背包不足，无法领取
     */
    Fund_bag(294194,1),
    /**
     * 需要{0}级才能领取
     */
    Fund_level(176393,1),
    /**
     * 转动次数已达上限
     */
    Turnplate_time(118836,1),
    /**
     * 背包已满
     */
    Turnplate_bag(238941,1),
    /**
     * 所需货币不足
     */
    Turnplate_money(330092,1),
    /**
     * 该活动已结束
     */
    Turnplate_end(304817,1),
    /**
     * 背包已满
     */
    Discount_bag(238941,1),
    /**
     * 所需货币不足
     */
    Discount_money(330092,1),
    /**
     * 您使用的物品已过期
     */
    Discount_past(141981,1),
    /**
     * 每日礼包
     */
    DailySale_Bagfull_title(11301,1),
    /**
     * 少侠，这是您购买的每日礼包！因为您的背包满啦，所以我帮您放在了邮件的附件哟！
     */
    DailySale_Bagfull_content(235260,1),
    /**
     * 同阵营人数达到地图上限，不能进入阵营地图
     */
    Map_CampMax(159936,1),
    /**
     * 当一个好盟主
     */
    Guild_creat_title(383720,1),
    /**
     * 你已经建立战盟，带领大家踊跃参加每日活动吧！[6b8e00]每日俸禄[-]和昨天的[6b8e00]全战盟活跃度[-]密切挂钩哦！所以多组织大家参与战盟活动哦！
     */
    Guild_creat_content(349384,1),
    /**
     * 当一个好盟友
     */
    Guild_enter_title(35230,1),
    /**
     * 你已经加入了一个战盟，踊跃参加每日活动吧！[6b8e00]每日俸禄[-]和昨天的[6b8e00]全战盟活跃度[-]密切挂钩哦！所以请催促盟主多组织战盟活动吧！
     */
    Guild_enter_content(345389,1),
    /**
     * 道具过期
     */
    ItemtimeOut_title(250713,1),
    /**
     * 少侠你背包中的{0}已过期作废，我们已将其自动出售！
     */
    ItemtimeOut_content(153335,1),
    /**
     * [228B22]{0}[-]在幸运转轮第{1}轮获得最高档：[FFD700]{2}绑元[-],[228B22]我要参加。
     */
    Turnplate_tips7(440086,18),
    /**
     * 经验 +{0}
     */
    Exp_offline(8805,1),
    /**
     * 战盟已经解散
     */
    Guild_dissolve_title(215212,1),
    /**
     * 您所在的战盟由于连续3天不满5个活跃玩家，已经被系统解散。请加入其它战盟，结识新的朋友吧！
     */
    Guild_dissolve_content(122083,1),
    /**
     * 战盟盟主更换
     */
    Guild_leadchange_title(452913,1),
    /**
     * 您所在的战盟盟主由于超过2天没有保持活跃状态，系统已将盟主之位移交给{0}。请新盟主带领大家继续战斗吧！
     */
    Guild_leadchange_content(393286,1),
    /**
     * 活跃的玩家
     */
    Activenotice_title(298533,1),
    /**
     * 少侠啊，你已经完成了很多事情了。去帮助别人完成各类秘境、守护林煮酒、天凉山，就可以获得侠义值哦！侠义值是一种稀有货币，可在侠义商店兑换各类珍稀道具！
     */
    Activenotice_content(94627,1),
    /**
     * 充值出了一点异常
     */
    Recharge_faild_title(373952,1),
    /**
     * 您刚才有一笔价格为{0}{1}，订单号为:{0}的充值行为因重复购买失败了，对您造成的困扰我们非常抱歉。请您点击【设置】按钮，联系客服以获取您的补偿。
     */
    Recharge_faild_content(324758,1),
    /**
     * 今日可从交易行购买的次数已经达到上限啦，少侠请明日再来吧！
     */
    Trade_maxnum_notice(245563,1),
    /**
     * 充值返还
     */
    RechargeReturnTitle(516356,1),
    /**
     * 亲爱的少侠，游戏现已正式上线，我们将返还您付费删档测试期间充值的Vip经验、150%返还您账号充值的元宝数量，请您在福利界面领取！
     */
    RechargeReturnContent(154715,1),
    /**
     * {0}已对领地{1}发起宣战，今晚20:00开始，战盟领地战奖励丰厚，希望大家积极参与！
     */
    Manor_Panel_war(460645,32),
    /**
     * {0}的{1}使用了1个[33bb43]{2}[-]！
     */
    Manor_Panel_useitem(247470,2),
    /**
     * {0}已被摧毁！
     */
    Manor_Panel_gate(31931,1),
    /**
     * {0}已被摧毁，{1}累积伤害最高获得归属！
     */
    Manor_Panel_effigy(142507,1),
    /**
     * 战盟领地战
     */
    Manor_Panel_title(376981,1),
    /**
     * 领地战结束，你战盟拥有领地：{0}，奖励请查收。
     */
    Manor_Panel_win(334896,1),
    /**
     * 领地战结束，所在战盟未拥有领地，奖励请查收。
     */
    Manor_Panel_lost(433576,1),
    /**
     * 竞价失败
     */
    Manor_Panel_biddingtitle(101593,1),
    /**
     * 竞价被{0}超过，请尽快重新竞价。\n每个领地最多3个战盟有征战资格。
     */
    Manor_Panel_bidding(448428,1),
    /**
     * 只有盟主、副盟主可竞价。
     */
    Manor_Panel_biddingtips(394972,1),
    /**
     * 领地战结束时，占领神像积分最高的战盟获得归属，暂时领先战盟为：{0}
     */
    Manor_Panel_active1(345423,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]10[-]人,小试牛刀！
     */
    Manor_Panel_kill10(107903,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]20[-]人,渐入佳境！
     */
    Manor_Panel_kill20(207397,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]30[-]人,轻而易举！
     */
    Manor_Panel_kill30(365952,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]40[-]人,开始暴走！
     */
    Manor_Panel_kill40(124959,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]50[-]人,谁能挡我！
     */
    Manor_Panel_kill50(296708,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]60[-]人,无人可挡！
     */
    Manor_Panel_kill60(385385,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]70[-]人,万夫莫敌！
     */
    Manor_Panel_kill70(395574,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]80[-]人,犹如鬼神！
     */
    Manor_Panel_kill80(199248,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]90[-]人,超神杀戮！
     */
    Manor_Panel_kill90(183661,4),
    /**
     * [ff0000]{0}[-]已经累积击败了[ff0000]100[-]人,百人斩！
     */
    Manor_Panel_kill100(309982,4),
    /**
     * [ff0000]{0}[-]已经统治比赛！~
     */
    Manor_Panel_kill110(244786,4),
    /**
     * 当前挑战次数不足,整点可回复挑战次数！请稍后
     */
    Godsoul_tips(60041,1),
    /**
     * 限时抢购宝箱
     */
    Bag_full_saleTitle(256654,1),
    /**
     * 因背包空间不足，您购买的物品通过邮件发放！请及时领取！
     */
    Bag_full_saleMail(102531,1),
    /**
     * 祈福物品
     */
    ActivitiesCliffordTitle(210802,1),
    /**
     * 因背包空间不足，您祈福获得物品以通过邮件发放！
     */
    ActivitiesCliffordContent(90275,1),
    /**
     * 哇,[6C88F0]{0}[-]在神树祈福中人品爆发获得<t=2>,{1},{2}</t>x{3}!超幸运!
     */
    ActivitiesClifforBroad(192508,16),
    /**
     * [6C88F0]{0}[-]祈福获得了{1}x{2}!
     */
    ActivitiesClifforShow(359567,1),
    /**
     * 月卡奖励
     */
    MonthCard_tittle(269665,1),
    /**
     * 因背包空间不足，您购买的月奖励卡物品通过邮件发放！请及时领取！
     */
    MonthCard_desc(83706,1),
    /**
     * 高战力的剑侍助阵加成更高
     */
    Pet_Refined_tips(499805,1),
    /**
     * 该阵已有{0}，不能再助阵相同剑侍！
     */
    Pet_Refined_tips1(421806,1),
    /**
     * 狂欢排名奖励
     */
    ActivitiesBashTitle(501140,1),
    /**
     * 您在剑舞狂欢消费排行活动中排名第{0}，获得如下奖励！
     */
    ActivitiesBashContent(255210,1),
    /**
     * 狂欢嗨点奖励
     */
    ActivitiesBashActiveTitle(320544,1),
    /**
     * 您在剑舞狂欢嗨点活动中有奖励未领取，现已通过邮件发放！
     */
    ActivitiesBashActiveContent(225167,1),
    /**
     * [1E90FF][{0}][-]触发天机，福从天降，在[1E90FF][剑舞狂欢][-]中获得[33bb43]<t=2>,{1},{2}</t>[-]。<t=118>0,32002,[u][21A734]我也想要[-]</t>。
     */
    Activitiessystemtips(375858,22),
    /**
     * 只有防守或进攻目标是{0}，才能进入该地图!
     */
    Manor_Panel_goto(522024,1),
    /**
     * 多宝锦盒奖励
     */
    Raflle_title(487626,1),
    /**
     * 因背包空间不足，您抽奖所得奖品通过邮件进行发放！请及时领取！
     */
    Raflle_mail(54420,1),
    /**
     * 多宝锦盒周奖励
     */
    Raflle_week_title(276130,1),
    /**
     * 因背包空间不足，多宝锦盒周奖品通过邮件进行发放！请及时领取！
     */
    Raflle_week_mail(135934,1),
    /**
     * [1E90FF][{0}][-]鸿运天降,在开启多宝锦盒时获得了[33bb43]{1}[-]个[33bb43]<t=2>,{2},{3}</t>[-],可喜可贺！
     */
    Affle_broad(489197,16),
    /**
     * 战盟拍卖即将开始，[FF0000]参与{0}[-]的成员可获得拍卖分红。
     */
    Auction_tips1(281497,2),
    /**
     * {0}，出价更新，预计本次分红：[21A734]{1}元宝[-]和[21A734]{2}绑元[-]。<t=118>0,5800,[u][21A734]查看拍卖行[-]</t>
     */
    Auction_change(31299,2),
    /**
     * 战盟拍卖出价开始。<t=118>0,5800,[u][21A734]查看拍卖行[-]</t>
     */
    Auction_begin(100496,2),
    /**
     * 战盟拍卖出价开始。<t=118>0,5800,[u][21A734]查看拍卖行[-]</t>
     */
    Auction_worldbegin(100496,2),
    /**
     * 战盟拍卖距离结束，剩余10分钟。<t=118>0,5800,[u][21A734]查看拍卖行[-]</t>
     */
    Auction_end(30351,2),
    /**
     * 恭喜参与{0}的活动成员，获得本次活动分红[21A734]{1}元宝[-]和[21A734]{2}绑元[-]。
     */
    Auction_getmoney(104261,2),
    /**
     * 您有竞价被超过，退款已从邮件发送。
     */
    Auction_moneyout(92680,2),
    /**
     * 竞价退款
     */
    Auction_title1(420933,1),
    /**
     * 物品：{0}出价被超过，退款请附件收取。
     */
    Auction_email1(223029,1),
    /**
     * 竞价成功
     */
    Auction_title2(358514,1),
    /**
     * 恭喜拍得：{0}，请附件收取，欢迎再次光临拍卖行。
     */
    Auction_email2(138386,1),
    /**
     * 活动分红
     */
    Auction_title3(411474,1),
    /**
     * 恭喜你在【{0}】拍卖中获得分红：{1}元宝和{2}绑元，请查收附件。
     */
    Auction_email3(390605,1),
    /**
     * 结婚请帖
     */
    Marry_mailtittle1(71746,1),
    /**
     * 我们将于{0}在长陵举行婚礼，诚邀您参与，感激不尽！{1}&{2}
     */
    Marry_mailcontent1(208042,1),
    /**
     * [F5FF44]您的爱人[-][FB7FD9]{0}[-][F5FF44]上线了！[-]
     */
    Marry_notice(367714,1),
    /**
     * 不可删除情侣
     */
    Marry_NotRemoveFriend(56888,1),
    /**
     * 战盟领地战
     */
    Manor_email_title(376981,1),
    /**
     * 今天晚上有战盟领地战活动，奖励非常丰厚，别错过。\n没有领地的战盟，需要在【日常】-【限时】-【战盟领地战】进行资格竞价。没有资格是无法参加的哟！\n拥有领地的战盟默认拥有参与资格。
     */
    Manor_email_content(218980,1),
    /**
     * 门派首席
     */
    Blood_Fight_title(482147,1),
    /**
     * 您在门派首席中存活到了第{0}位，获得{1}积分，得到如下奖励！
     */
    Blood_Fight_content(235268,1),
    /**
     * 门派首席
     */
    Blood_Fight_join_title(482147,1),
    /**
     * 您积极的参与了门派首席活动，获得如下奖励！
     */
    Blood_Fight_join_content(50084,1),
    /**
     * [ffffff]您击败了[33bb43]{0}[-],获得[33bb43]{1}[-]积分[-]
     */
    Blood_Fight_intergal_kill(271411,1),
    /**
     * [ffffff]活动安全区域将在[33bb43]{0}秒[-]后向地图中心缩小！！！[-]
     */
    Blood_Fight_safe_notice(1547,1),
    /**
     * 神秘buff已经在场景中出现，逆天效果尽在其中！
     */
    Blood_Fight_buff_ref(199496,1),
    /**
     * 恭喜[33bb43]{0}[-]成为门派首席！获得唯一称号和主城雕像！可以[33bb43]个性化设计自己雕像[-]！可以[33bb43]个性化设计自己雕像[-]！可以[33bb43]个性化设计自己雕像[-]！[33bb43]获得雕像后转职本轮不可再编辑雕像[-]！
     */
    Blood_Fight_notice(260889,4),
    /**
     * 恭喜[33bb43]{0}[-]问鼎天下成为天下第一！获得唯一奖励和主城雕像！
     */
    Blood_Fight_notice1(150008,4),
    /**
     * 当前最高竞价者是您，无法购买
     */
    Auction_yours(521990,1),
    /**
     * 战盟评选
     */
    Guild_translate_title(154334,1),
    /**
     * 战盟评选结算结果\n您所在战盟排名：第{0}名\n您的战盟职位为：盟主\n请附件查收奖励
     */
    Guild_translate_dec1(212713,1),
    /**
     * 战盟评选结算结果\n您所在战盟排名：第{0}名\n您的战盟职位为：副盟主、元老、执法使、传功使、掌旗使、内务使、战盟宝贝\n请附件查收奖励
     */
    Guild_translate_dec2(344113,1),
    /**
     * 战盟评选结算结果\n您所在战盟排名：第{0}名\n您的战盟职位为：精英或成员\n请附件查收奖励
     */
    Guild_translate_dec3(179489,1),
    /**
     * 天梯赛季结算奖励
     */
    CrossArenaRankMail_Title(73005,1),
    /**
     * 你在S{0}赛季天梯争雄中世界排名第{1},获得以下奖励！
     */
    CrossArenaRankMail_Content(102045,1),
    /**
     * 天梯赛季结算奖励
     */
    CrossArenaMail_Title(73005,1),
    /**
     * 你在S{0}赛季天梯争雄中处于【新手】段位,获得以下奖励！
     */
    CrossArenaMail_Content1(457744,1),
    /**
     * 你在S{0}赛季天梯争雄中处于【青铜{1}】段位,获得以下奖励！
     */
    CrossArenaMail_Content2(356791,1),
    /**
     * 你在S{0}赛季天梯争雄中处于【白银{1}】段位,获得以下奖励！
     */
    CrossArenaMail_Content3(53204,1),
    /**
     * 你在S{0}赛季天梯争雄中处于【黄金{1}】段位,获得以下奖励！
     */
    CrossArenaMail_Content4(347440,1),
    /**
     * 你在S{0}赛季天梯争雄中处于【钻石{1}】段位,获得以下奖励！
     */
    CrossArenaMail_Content5(452860,1),
    /**
     * 你在S{0}赛季天梯争雄中处于【王者{1}】段位,获得以下奖励！
     */
    CrossArenaMail_Content6(242368,1),
    /**
     * 婚礼通知
     */
    Marry_mailtittle2(240835,1),
    /**
     * 亲爱的玩家，你们的婚礼时间到了，请及时组队前往长陵月老处开启婚礼，长时间未开启婚礼视为取消婚礼，祝您新婚愉快！
     */
    Marry_mailcontent2(79429,1),
    /**
     * 喜分礼钱
     */
    Marry_mailtittle3(463957,1),
    /**
     * 恭喜您，在婚礼中分得{0}绑元礼钱。
     */
    Marry_mailcontent3(64140,1),
    /**
     * 道具提示
     */
    Marry_mailtittle4(422223,1),
    /**
     * 少侠，你背包满了哟，这是你方才赠送婚礼道具获得的奖励，请查收。
     */
    Marry_mailcontent4(366888,1),
    /**
     * 婚礼取消通知
     */
    Marry_mailtittle5(210141,1),
    /**
     * 亲爱的玩家，由于半小时内未开启婚礼，婚礼自动已取消，请重新预约！
     */
    Marry_mailcontent5(488622,1),
    /**
     * 婚礼取消通知
     */
    Marry_mailtittle6(210141,1),
    /**
     * 亲爱的玩家，婚礼已被取消。请重新预约！
     */
    Marry_mailcontent6(109499,1),
    /**
     * 强制离婚通知
     */
    Marry_mailtittle7(195012,1),
    /**
     * 亲爱的玩家，{0}已强制解除与您的婚姻关系，您恢复为单身状态。
     */
    Marry_mailcontent7(284450,1),
    /**
     * 婚礼通知
     */
    Marry_mailtittle8(240835,1),
    /**
     * 亲爱的玩家，{0}&{1}的婚宴已经开启，赶紧前往长陵[33bb43]【织女】[-]处点击进入婚宴地图祝贺吧！
     */
    Marry_mailcontent8(268287,1),
    /**
     * 新人婚礼正在婚宴地图火热进行，赶紧去凑凑热闹吧，还有机会获得神秘大礼哦！！！<t=4>立即前往,2,[209,29]</t>
     */
    Marry_news(431001,2),
    /**
     * 喜糖将在{0}秒后刷新，大家撸起袖子做好准备啊！
     */
    Marry_candycd1(336851,1),
    /**
     * 喜糖已在婚宴地图刷新，大家快去粘粘喜气吧！
     */
    Marry_candycd2(347500,4),
    /**
     * 当前雕像不归属于你,你无权修改！
     */
    Action_editor_outtime(321996,1),
    /**
     * 保存成功
     */
    Blood_fight_save_notice(43058,1),
    /**
     * 抓紧时间，{0}将在{1}秒后复活。
     */
    Msrry_Refreshprompt(81449,1),
    /**
     * 对方拒绝了你的求婚！别灰心，恒心也很重要哦~
     */
    Marry_reject1(271690,1),
    /**
     * 对方拒绝了你的婚礼安排，请另择它选吧！
     */
    Marry_reject2(461248,1),
    /**
     * 对方答应了你的求婚，赶紧去预定婚礼日期吧！
     */
    Marry_BackTittle8(226036,1),
    /**
     * 选择日期成功，请准时参加！
     */
    Marry_BackTittle9(11096,1),
    /**
     * 重新预约婚礼通知
     */
    Marry_mailtittle10(472524,1),
    /**
     * 亲爱的玩家，非常抱歉。因为停服导致您婚礼举行失败，现将您婚礼花费全部退还给您，请重新预约婚礼吧
     */
    Marry_mailcontent10(308609,1),
    /**
     * 结婚取消通知
     */
    Marry_mailtittle11(237721,1),
    /**
     * 夫妻本是同林鸟，奈何也有缘尽时。你们已离婚，请另觅良缘吧~~
     */
    Marry_mailcontent11(20314,1),
    /**
     * 时装领取通知
     */
    Marry_mailtittle12(2346,1),
    /**
     * 亲爱的玩家，因为你长期未开启婚宴，系统已判定您自动结婚成功！同时赠送您时装一套，祝你婚姻长长久久~
     */
    Marry_mailcontent12(191853,1),
    /**
     * 悔婚通知
     */
    Marry_mailtittle13(434028,1),
    /**
     * 亲爱的玩家，您的对象取消了与您的订婚关系，您现在恢复单身状态了~
     */
    Marry_mailcontent13(522956,1),
    /**
     * 当前时间段没有婚礼
     */
    Marry_BackTittle14(303309,1),
    /**
     * 悔婚成功
     */
    Marry_BackTittle15(59214,1),
    /**
     * 取消订婚成功
     */
    Marry_BackTittle16(452458,1),
    /**
     * 离婚成功
     */
    Marry_BackTittle17(222578,1),
    /**
     * 良缘天定，{0}和{1}的婚礼已在{2}开启，请大家前往祝贺吧，还有机会获得极品奖励哦~~
     */
    Marry_BackTittle18(500390,4),
    /**
     * {0}秒后才能再次发送邀请消息！
     */
    Marry_BackTittle19(27060,1),
    /**
     * 结婚中，不能删除角色
     */
    Marry_notdeleterole(58731,1),
    /**
     * 对方拒绝了您的传送邀请！
     */
    Marry_ring_refuse(80590,1),
    /**
     * 对方所在地图不支持召请！
     */
    Marry_ring_fail(69408,1),
    /**
     * 您的伴侣已离线！
     */
    Marry_ring_offline(266394,1),
    /**
     * 该地图只支持同一地图内召请！
     */
    Marry_ring_map(156041,1),
    /**
     * 婚戒等级不足，该技能未激活！
     */
    Marry_ring_close(497962,1),
    /**
     * 对应战旗未激活，不可使用该物品！
     */
    Flag_no_activate(13040,1),
    /**
     * 该地图要求组队双方必须有婚姻关系！
     */
    Marry_maplimit(220959,1),
    /**
     * 与队伍玩家不是婚姻关系，你已自动离队！
     */
    Marry_maplimit1(467605,1),
    /**
     * 只能结婚后才能进入该地图哦~
     */
    Marry_maplimit2(316428,1),
    /**
     * 与队伍成员不是婚姻关系，无法进入地图
     */
    Marry_maplimit3(502561,1),
    /**
     * 采集失败，已达今日采集上限！
     */
    Marry_maplimit4(514677,1),
    /**
     * 发放失败，超过地图存在最大上限！
     */
    Marry_objectmaxNum(421804,1),
    /**
     * VIPBOSS
     */
    VIP_Boss(280194,1),
    /**
     * 个人BOSS
     */
    Single_Boss(17835,1),
    /**
     * 挑战次数不足
     */
    ChangeTime_less(158729,1),
    /**
     * 归属剩余{0}次
     */
    Wildboss_hasnum1(382235,1),
    /**
     * 参与剩余{0}次
     */
    Wildboss_hasnum2(311569,1),
    /**
     * 不同野外BOSS收益次数共享
     */
    Wildboss_sharenum(479420,1),
    /**
     * 挑战冷却 {0}
     */
    Wildboss_cooltime(213741,1),
    /**
     * 等级{0}开启
     */
    Single_Boss_openlevel(510939,1),
    /**
     * 不同VIPBOSS挑战次数共享
     */
    Vip_Boss_shareTime(78011,1),
    /**
     * 不同个人BOSS挑战次数共享
     */
    Single_Boss_shareTime(229748,1),
    /**
     * VIP{0}开启
     */
    Vip_Boss_openlevel(501634,1),
    /**
     * 副本掉落
     */
    BOSSroomtitle(20296,1),
    /**
     * 由于背包已满，这是您刚刚战胜副本首领的奖励物品，请附件查收
     */
    BOSSroomdec(256194,1),
    /**
     * 对方拒绝离婚！
     */
    Marry_tishi11(152116,1),
    /**
     * 十大高手结算
     */
    Top_ten_Mail(51338,1),
    /**
     * 由于你在零点排行榜战力排名前10，获得十大高手的特殊荣耀展示！\n聊天频道可见特殊称号展示！
     */
    Top_ten_MailContent(59195,1),
    /**
     * 您的等级小于{0}级！
     */
    Marry_openlimit1(451766,1),
    /**
     * 对方等级小于{0}级！
     */
    Marry_openlimit2(173641,1),
    /**
     * 您的功能尚未开启！
     */
    Marry_openlimit3(194859,1),
    /**
     * 对方功能尚未开启！
     */
    Marry_openlimit4(441022,1),
    /**
     * 角色{0}级可升阶！
     */
    Mount_stage_notice(402633,1),
    /**
     * 副本掉落
     */
    Hell_bag_ful_title(20296,1),
    /**
     * 由于你背包已满，这是您刚刚战胜怪物的奖励物品，请附件查收
     */
    Hell_bag_ful_content(509111,1),
    /**
     * 内测之星
     */
    Test_reward1(448411,1),
    /**
     * 内测期间，等级达到68级及以上的玩家，可获得永久专属称号“内测之星”
     */
    Test_content1(246661,1),
    /**
     * 残影手速
     */
    Test_reward2(115610,1),
    /**
     * 内测期间，累积领取红包排行前20名的玩家可在不删档测试开启后获得永久专属称号“残影手速”
     */
    Test_content2(283163,1),
    /**
     * 一掷千金
     */
    Test_reward3(269259,1),
    /**
     * 内测期间，消耗1000非绑定元宝及以上的玩家，可在不删档测试开启后获得永久专属称号“一掷千金”
     */
    Test_content3(364727,1),
    /**
     * 3D夕昭
     */
    Test_reward4(512834,1),
    /**
     * 内测期间，获得了5个紫色及以上品质的剑侍的玩家，可在不删档测试开启后获得15天专属头像“3D夕昭”
     */
    Test_content4(352976,1),
    /**
     * 逆天颜值
     */
    Test_reward5(174477,1),
    /**
     * 内测期间，坐骑总战力100000及以上的玩家，可在不删档测试开启后获得15天专属头像“逆天颜值”
     */
    Test_content5(293541,1),
    /**
     * 道具或货币不足，无法扫荡
     */
    Hell_sweep_notice(365717,1),
    /**
     * 战斗后，今日将锁定{0}难度，确定挑战吗？
     */
    Fight_myself_begin(421969,1),
    /**
     * 扫荡后，今日将锁定{0}难度，确定扫荡吗？
     */
    Fight_myself_tips0(70535,1),
    /**
     * 需要全通{0}，且等级{1}开启！
     */
    Fight_myself_tips1(194391,1),
    /**
     * 需要全通{0}，且等级{1}才能扫荡！
     */
    Fight_myself_tips4(16786,1),
    /**
     * 请先选择战斗增益！
     */
    Fight_myself_tips6(138376,1),
    /**
     * 已经开始战斗，无法扫荡！
     */
    Fight_myself_tips7(167600,1),
    /**
     * 战斗场次已用完，明天再来吧！
     */
    Fight_myself_tips8(163856,1),
    /**
     * 恭喜全通本难度心魔挑战！\n\n剩余战斗场次{0}次，可额外开启{1}个宝箱！
     */
    Fight_myself_tips9(24752,1),
    /**
     * [ff0000]{0}级[-]战盟才能编辑公告栏！
     */
    GuildNoticeLimitTips(485973,1),
    /**
     * 角色等级达到[ff0000]{0}级[-]才能编辑公告栏！
     */
    GuildNoticeLevelTips(311872,1),
    /**
     * 商城活动奖励
     */
    MallforThree_Mail(267386,1),
    /**
     * 恭喜！您在商城购买活动中额外获得道具奖励！请注意查收
     */
    MallforThree_MailContent(366223,1),
    /**
     * 恭喜你购买造成暴击，获得{0}倍加成。暴击赠送物品请在邮件查找
     */
    MallforThreeTips(57276,1),
    /**
     * 暴击率{0}% 倍数{1}
     */
    MallforThreeshow(338640,1),
    /**
     * 该服角色已满，请选择其他服务器吧！
     */
    Server_maxlimit(23572,1),
    /**
     * 精英击杀奖励
     */
    Killnewmonster_Mail(71285,1),
    /**
     * 恭喜在精英首领活动中成功击杀精英boss，请注意查收奖励。
     */
    Killnewmonster_MailContent(38612,1),
    /**
     * 精英参与奖励
     */
    Joinnewmonster_Mail(519375,1),
    /**
     * 感谢您在精英首领活动中协助击败精英boss，请注意查收奖励。
     */
    Joinnewmonster_MailContent(192473,1),
    /**
     * 【{0}】成功击杀了入侵的精英首领【{1}】，为保卫家园做出了卓越贡献！
     */
    Newmonster_DeadRefresh(346934,6),
    /**
     * 背包已满
     */
    Lovers_dream_Mail(238941,1),
    /**
     * 因为少侠的背包已满，我们将您情人节许愿的礼物通过邮件发放，请尽快收取哦。
     */
    Lovers_dream_MailContent(105943,1),
    /**
     * 花语传情积分奖励
     */
    Lovers_rank_Mail(427741,1),
    /**
     * 少侠在花语传情活动中倾心值达到{0}，送上奖励一份，请尽快领取。
     */
    Lovers_rank_MailContent_score1(157442,1),
    /**
     * 少侠在花语传情活动中魅力值达到{0}，送上奖励一份，请尽快领取。
     */
    Lovers_rank_MailContent_score2(32061,1),
    /**
     * 花语传情结算奖励
     */
    Lovers_rank_Mail2(495559,1),
    /**
     * 少侠在花语传情活动中倾心榜排名第{0}，以下是获得的奖励，请尽快领取。
     */
    Lovers_rank_MailContent1(400265,1),
    /**
     * 少侠在花语传情活动中魅力榜排名第{0}，以下是获得的奖励，请尽快领取。
     */
    Lovers_rank_MailContent2(21994,1),
    /**
     * 倾心值
     */
    Lovers_rank1(123807,1),
    /**
     * 魅力值
     */
    Lovers_rank2(378769,1),
    /**
     * 告白信
     */
    Lovers_letter_Mail(396549,1),
    /**
     * 有位不愿意透露姓名的玩家对你说：\n{0}
     */
    Lovers_letter_MailContent1(206446,1),
    /**
     * 【{1}】深情款款的对你说：\n{0}
     */
    Lovers_letter_MailContent2(516927,1),
    /**
     * 背包已满
     */
    Lovers_letter_fulltitel(238941,1),
    /**
     * 因为少侠的背包已满，您使用告白笺获得的道具通过邮件发放给您，请尽快领取哦。
     */
    Levers_letter_fullcontent(224131,1),
    /**
     * 全服采集数量不足
     */
    Lovers_collection(229622,1),
    /**
     * 今日奖励数量已达上限，只记入全服采集次数！
     */
    Lovers_collection_full(163671,1),
    /**
     * 变身道具使用成功！
     */
    Item_shapeshift_success(8608,1),
    /**
     * 移动或战斗中无法取消变身状态！
     */
    Item_shapeshift_delete(102888,1),
    /**
     * 请先解除当前变身状态！
     */
    Item_shapeshift_conflict(133403,1),
    /**
     * 战盟秒伤赛排名奖励！
     */
    Guild_exper_rank_Mail(216235,1),
    /**
     * 您所在的战盟在战盟秒伤赛的活动中，今日综合伤害排名第[33bb43]{0}[-]名，获得如下奖励！本日全服输出前3为[33bb43]{1}[-]、[33bb43]{2}[-]、[33bb43]{3}[-]！
     */
    Guild_exper_rank_MailContent(321430,1),
    /**
     * 背包已满
     */
    Guild_exper_bagfull_Mail(238941,1),
    /**
     * 由于您的背包已满，你在战盟试炼中的奖励通过邮件发放，获得奖励如下
     */
    Guild_exper_bagfull_MailContent(220287,1),
    /**
     * 使用该道具不可有婚姻关系！
     */
    Useitem_changesex(352843,1),
    /**
     * 阵营争夺奖励
     */
    CampBossMailTittle(365777,1),
    /**
     * 您在阵营争夺中为本阵营争的无数功勋，以下奖励请查收
     */
    CampBossMailContent(155921,1),
    /**
     * 侍月妖人已被赶走，大家齐心协力保护了家园！
     */
    SpringFe_boss_tips3(284172,4),
    /**
     * [33bb43]{0}[-]反应迅速，抢到侍月妖人第一击，奖励伤害[33bb43]{1}点[-]！
     */
    SpringFe_boss_tips4(312058,6),
    /**
     * [33bb43]{0}[-]完成最后一击，额外奖励[33bb43]666绑元[-]！
     */
    SpringFe_boss_tips5(129021,6),
    /**
     * 驱赶侍月妖人奖励
     */
    SpringFe_boss_title(386900,1),
    /**
     * 多谢少侠参与驱赶侍月妖人活动。\n您的伤害排名：第{0}名。\n奖励请附件查收！
     */
    SpringFe_boss_mail(21089,1),
    /**
     * 幸运儿
     */
    SpringFe_boss_titlekill(86819,1),
    /**
     * 恭喜您成为幸运儿！最后补刀击杀侍月妖人。\n奖励请附件查收！
     */
    SpringFe_boss_mailkill(50808,1),
    /**
     * 大红包
     */
    SpringFe_redpacket_title(174111,1),
    /**
     * 贵人多忘事吧，这是你忘记领取的大红包。\n下次别忘了！只能帮你到这里了！\n节日快乐！
     */
    SpringFe_redpacket_mail(52721,1),
    /**
     * 战天下重置
     */
    FightAllFirstKing_title(246688,1),
    /**
     * 战天下重置，恭喜您成为王者，铁血铸就威名！
     */
    FightAllFirstKing_mail(243366,1),
    /**
     * 战天下胜利
     */
    FightAllKingDefenseWin_title(380728,1),
    /**
     * 斩天下豪雄，冠盖满京华！恭贺君成为王者！
     */
    FightAllKingDefenseWin_mail(386122,1),
    /**
     * 战天下失败
     */
    FightAllKingDefenseLose_title(233402,1),
    /**
     * 纵天赋异禀，然宵小甚多！惜败惜败
     */
    FightAllKingDefenseLose_mail(471437,1),
    /**
     * 战天下胜利
     */
    FightAllKingAttackWin_title(380728,1),
    /**
     * 乱战之中唯君天命所归！可喜可贺
     */
    FightAllKingAttackWin_mail(482798,1),
    /**
     * 战天下失败
     */
    FightAllKingAttackLose_title(233402,1),
    /**
     * 胜败乃兵家常事,重整旗鼓改日再战！
     */
    FightAllKingAttackLose_mail(453364,1),
    /**
     * 一王战天下
     */
    FightAllNoKing_title(269679,1),
    /**
     * 因现任王者放弃，您自动成为战天下的新王者！
     */
    FightAllNoKing_mail(321968,1),
    /**
     * 战天下胜利
     */
    FightAllKingAttackWin_title2(380728,1),
    /**
     * 虽未斩将夺旗亦有从龙之功！
     */
    FightAllKingAttackWin_mail2(182079,1),
    /**
     * 战天下伤害奖
     */
    FightAllKingAttackWinRank_title(422977,1),
    /**
     * 您在打破结界和击杀元神中做出突出贡献，排名第{0}名，获得以下奖励！
     */
    FightAllKingAttackWinRank__mail(392471,1),
    /**
     * 斩天下豪雄，冠盖满京华！恭贺【{0}】成为战天下的王者！
     */
    FightAllKing(450057,6),
    /**
     * 参与人数已满，请稍后再试
     */
    FightAllMax(188637,1),
    /**
     * 天机令
     */
    Item_treasure_title(83611,1),
    /**
     * 咦这似乎是你使用神机令后遗落的宝贝！
     */
    Item_treasure_mail(263019,1),
    /**
     * 【{0}】在{1}_{2}线，变成了怪物，大家赶紧去铲除它！<t=4>立即前往,{3},[{4},{5}]</t>
     */
    Item_treasure_four(380297,2),
    /**
     * 【{0}】胆小如鼠离开了{1}_{2}线，并在地图中留下了宝物，大家快去寻找寻找!<t=4>立即前往,{3},[{4},{5}]</t>
     */
    Item_treasure_fourleave(343374,2),
    /**
     * 【{0}】使用什么奇怪的法术在{1}_{2}线，召唤出了3只大妖怪！赶紧去找找。<t=4>立即前往,{3},[{4},{5}]</t>
     */
    Item_treasure_three(451032,2),
    /**
     * 【{0}】在{1}_{2}线挖塌了妖怪的房子，【狐王】和她的手下都无家可归啦！赶紧去找找。<t=4>立即前往,{3},[{4},{5}]</t>
     */
    Item_treasure_three1(261735,2),
    /**
     * 【{0}】在{1}_1线找到了一个不得了的藏宝洞，有意探索的朋友赶紧联系他！<t=4>立即前往,{2},[{3},{4}]</t>
     */
    Item_treasure_five(146069,2),
    /**
     * 天机令
     */
    Item_treasure_faildTitle(83611,1),
    /**
     * 天机不可测，既然放弃探索了，这份奖励收好权当辛苦费吧！
     */
    Item_treasure_faildMail(154994,1),
    /**
     * 呀,您还有个藏宝洞没有探索，不能使用天机令！
     */
    Item_treasure_faildFive(14201,1),
    /**
     * 您正在探索天机呢，新的天机令一会再用吧！
     */
    Item_treasure_faildone(322796,1),
    /**
     * 长时间未探索，藏宝洞消失掉了！
     */
    Item_treasure_fiveOvertime(70383,1),
    /**
     * 野外地图才能使用天机令
     */
    ItemIsDeformMap(404816,1),
    /**
     * [33bb43]【{0}】[-]击杀了{1},为[33bb43]神都监[-]阵营抢夺了大量物资，胜利天秤开始向[33bb43]神都监[-]倾斜！
     */
    CrossSeverKillMonsterTips1(25282,6),
    /**
     * [33bb43]【{0}】[-]击杀了{1},为[33bb43]监天司[-]阵营抢夺了大量物资，胜利天秤开始向[33bb43]监天司[-]倾斜！
     */
    CrossSeverKillMonsterTips2(166710,6),
    /**
     * 击杀首领
     */
    Camp_Race_KillBoss(159212,1),
    /**
     * [33bb43]{0}[-]找到[33bb43]{1}[-]号获得[33bb43]10绑元[-]！\n新目标为[33bb43]{2}[-]号！
     */
    Camp_Race_NextNPC(440648,1),
    /**
     * [33bb43]{0}[-]找到[33bb43]{1}[-]号获得[33bb43]10绑元[-]！\n[33bb43]即将进入二阶段[-]！
     */
    Camp_Race_NextNPCend1(509191,1),
    /**
     * 本阵营已完成活动，不能再次进入！
     */
    Camp_Race_errortips(112704,1),
    /**
     * 活动即将开始！
     */
    Camp_Race_tips4(398948,1),
    /**
     * [33bb43]{0}[-]完成最后一刀！[33bb43]{1}[-]获得幸运奖励！
     */
    Camp_Race_endtips5(423436,4),
    /**
     * 阵营竞速
     */
    Camp_findnpctitle(467541,1),
    /**
     * 原来你就是天选之人啊！\n每关首领被击败都有奖励结算。\n这是您的奖励，请查收附件。
     */
    Camp_findnpcdec1(510423,1),
    /**
     * 成功通关本关。\n每关首领被击败都有奖励结算。\n这是您的奖励，请查收附件。
     */
    Camp_findnpcdec2(235289,1),
    /**
     * 恭喜，本次阵营竞速获得胜利。\n这是您的阵营胜利奖励，请查收附件。
     */
    Camp_findnpcdec3(288408,1),
    /**
     * 再接再厉，本次阵营竞速失败。\n这是您的阵营失败奖励，请查收附件。
     */
    Camp_findnpcdec4(294373,1),
    /**
     * 不可思议，本次阵营竞速居然是平局。\n这是您的阵营平局奖励，请查收附件。
     */
    Camp_findnpcdec5(244260,1),
    /**
     * 恭喜抢到最后一刀！\n这是您的奖励，请查收附件。
     */
    Camp_findnpcdec6(33875,1),
    /**
     * 恭喜【监天司】阵营获得最后的胜利！活动奖励即将发放！
     */
    Camp_findnpcOver1(198736,5),
    /**
     * 恭喜【神都监】阵营获得最后的胜利！活动奖励即将发放！
     */
    Camp_findnpcOver2(181380,5),
    /**
     * 【监天司】和【神都监】阵营打成平手，势均力敌！活动奖励即将发放！
     */
    Camp_findnpcOver3(442376,5),
    /**
     * 未激活或限时坐骑无法装备
     */
    Mount_equip_tips1(481698,1),
    /**
     * 已装备装甲等级≥替换装甲，不需要替换
     */
    Mount_equip_tips3(111457,1),
    /**
     * 背包已满，无法卸下！
     */
    Mount_equip_bagtips(5946,1),
    /**
     * 连续冲值补偿
     */
    Active_charge_Mail(373517,1),
    /**
     * 现连续充值活动关闭，根据你的充值情况[33bb43]补偿[-]当前连续充值的[33bb43]最高奖励[-]给你，还请查收！
     */
    Active_charge_MailContent(333958,1),
    /**
     * 我在交易行中上架了[ff0000]{0}[-]个[33bb43]<t=2>,{1},{2}</t>[-],只卖[ff0000]{3}[-]元宝，求秒！<t=118>{4},700,[u][21A734]【我要购买】[-]</t>
     */
    AhBroadCast(160436,2),
    /**
     * 交易行吆喝不能复制
     */
    AhBroadError(409551,1),
    /**
     * 手慢了，物品没了！去看看其他物品吧！
     */
    Ah_AdtipsNot(47864,1),
    /**
     * 吆喝成功！
     */
    Ah_AdSuccess(250755,1),
    /**
     * 闪耀榜奖励
     */
    FlowerRankShineMailTitle(332710,1),
    /**
     * 您在闪耀榜上排名第{0}，请查收以下奖励
     */
    FlowerRankShineMailContent(311444,1),
    /**
     * 守护榜奖励
     */
    FlowerRankGuardMailTitle(254793,1),
    /**
     * 您在守护榜上排名第{0}，请查收以下奖励
     */
    FlowerRankGuardMailContent(4365,1),
    /**
     * 财神已降临,{0}分钟后会离开，不要错过了！
     */
    Mammon_come_tips(86831,4),
    /**
     * 财神即将在{0}分钟后降临，准备去找他吧！
     */
    Mammon_come_tips2(132129,4),
    /**
     * 排名奖励
     */
    Running_pig_tittle1(20794,1),
    /**
     * 你在超超快跑简单模式中排名第{0}，请查收以下奖励
     */
    Running_pig_Content1(87210,1),
    /**
     * 排名奖励
     */
    Running_pig_tittle2(20794,1),
    /**
     * 你在超超快跑地狱模式中排名第{0}，请查收以下奖励
     */
    Running_pig_Content2(86410,1),
    /**
     * 参与奖励
     */
    Running_pig_tittle3(82624,1),
    /**
     * 虽然未能通关，但您的坚持值得这份奖励，请查收！
     */
    Running_pig_Content3(364529,1),
    /**
     * 通关奖励
     */
    Running_pig_tittle4(359266,1),
    /**
     * 恭喜通关超超快跑，请查收以下奖励
     */
    Running_pig_Content4(335806,1),
    /**
     * 改玩家已到每日被禁言最大次数，本日不能对改玩家进行禁言！
     */
    Camp_Gagmaxnum_notiec(256744,1),
    /**
     * 结义
     */
    Sworn_mailtitle(202533,1),
    /**
     * 少侠背包满了，快清理清理吧，这是结义奖励。
     */
    Sworn_maildec1(291913,1),
    /**
     * 由于你多日失踪，被请离了结义组织。
     */
    Sworn_maildec2(105171,1),
    /**
     * {0}恩断义绝，脱离了结义。
     */
    Sworn_tips1(237179,1),
    /**
     * {0}失踪多日，被{1}请离了。
     */
    Sworn_tips2(174913,1),
    /**
     * 队伍中有多个结义关系，不能发起结义。
     */
    Sworn_tips3(362152,1),
    /**
     * 当前队伍中有结义伙伴不在，无法发起结义。
     */
    Sworn_tips4(211402,1),
    /**
     * 您的结义队伍人数已满。
     */
    Sworn_tips5(488793,1),
    /**
     * 您已离开结义队伍！
     */
    Sworn_tips6(465929,1),
    /**
     * 有人还在犹豫，未按下指印！
     */
    Sworn_tips9(381567,1),
    /**
     * 波波的奖励
     */
    ZombieMailTitle(210195,1),
    /**
     * 您在活动中获得{0}星评价，获得以下奖励，注意查收
     */
    ZombieMailContent(515465,1),
    /**
     * 技能使用失败，技能在CD中
     */
    ZombieSkillCd(209506,1),
    /**
     * 技能使用失败，技能剩余数量不足
     */
    ZombieSkillNum(413986,1),
    /**
     * 未发现尸王尸将的踪迹
     */
    ZombieSkillSearch(107190,1),
    /**
     * 被动技能无须使用
     */
    ZombieSkillPassive(409701,1),
    /**
     * 守护值增加{0}
     */
    Flower_Guard(249218,1),
    /**
     * 亲密值增加{0}
     */
    Flower_Friendship(125237,1),
    /**
     * 闪耀值增加{0}
     */
    Flower_Shine(93089,1),
    /**
     * 另一难度玩法地图尚未关闭，请完成后再试！
     */
    Running_pig_Content6(94753,1),
    /**
     * 当前结义队伍浇水次数不够，无法领取奖励
     */
    Sworn_nummin(340514,1),
    /**
     * 老玩家返利
     */
    Vietnam_fanli_tittle(52161,1),
    /**
     * 达成返利要求，请查收奖励。
     */
    Vietnam_fanli_content(136779,1),
    /**
     * [33bb43]{0}[-]购买了特惠首充！赢在起跑线！
     */
    First_rechargebroadCast(171708,4),
    /**
     * [33bb43]{0}[-]购买了每日首充！点滴优势汇成海！
     */
    Day_rechargebroadCast(232276,4),
    /**
     * 恭喜【[00ff00]{0}[-]】获得珍稀坐骑【[ffa800]{1}[-]】，战斗中如虎添翼，即将在九州六国一展雄风！
     */
    Mount_broadcast(96264,18),
    /**
     * 恭喜【[00ff00]{0}[-]】获得珍稀战翼【[ffa800]{1}[-]】，战斗中傲剑江湖，一统天下指日可待！
     */
    Wing_broadcast(457739,18),
    /**
     * 幸运骰子
     */
    Festival_dice_name(415694,1),
    /**
     * 活动排名第{0}名。\n请领取您的排行奖励！期待您的下次参与！
     */
    Festival_dice_dec(88785,1),
    /**
     * 一本万利
     */
    Festival_invest_name(55606,1),
    /**
     * 亲爱的少侠，这是您的活动奖励，请附件查收。\n期待下一次您的参与！
     */
    Festival_invest_dec(260253,1),
    /**
     * 劲爆！劲爆！劲爆！玩家[1E90FF][{0}][-]超级幸运，竟然从观星抽奖中获得极品材料[f02c2c]<t=2>,{1},{2}</t>[-]！
     */
    Star_watching_text(465418,6),
    /**
     * 合成成功
     */
    Star_watching_change5(121645,1),
    /**
     * 分解成功
     */
    Star_watching_change6(523057,1),
    ;

    /**
     * 语言包id
     */
    public final int code;
    /**
     * 提示类型：1：tips；2：系统聊天4：跑马灯8：喇叭消息16：messagebox
     */
    public final int type;
    /**
     * 字符
     */
    public final String str;

    /**
     *
     * @param name 语言包id
     * @param code 提示类型：1：tips；2：系统聊天4：跑马灯8：喇叭消息16：messagebox
     */
    private StringCode(int code, int type) {
        this.code = code;
        this.type = type;
        this.str = "@" + code;
    }

    @Override
    public String toString(){
        return str;
    }
}
