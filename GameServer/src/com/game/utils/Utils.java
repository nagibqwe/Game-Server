package com.game.utils;

import com.data.CfgManager;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Offstring_Bean;
import com.data.bean.Cfg_Shielding_symbol_Bean;
import com.data.bean.Cfg_UrlMarquee_Bean;
import com.data.struct.*;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.db.bean.ForbidWordBean;
import com.game.equip.struct.EquipDefine;
import com.game.friend.struct.Friend;
import com.game.friend.struct.RelationInfo;
import com.game.jjc.structs.JJCReport;
import com.game.manager.Manager;
import com.game.map.structs.DynamicBlock;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.player.structs.PlayerWorldInfo;
import com.game.recharge.structs.Recharge;
import com.game.structs.Fighter;
import com.game.structs.ServerStr;
import game.core.json.TypeReference;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {

    private final static Logger log = LogManager.getLogger(Utils.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        Utils manager;

        Singleton() {
            this.manager = new Utils();
        }

        Utils getProcessor() {
            return manager;
        }
    }

    public Utils() {
    }

    //Utils
    public static Utils getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public static boolean isMessagePrint() {
        String val = System.getProperty("MsgPrint");
        return val != null && val.equals("true");
    }

    //获取两点距离
    public static float getDistance(Position pos1, Position pos2) {
        if (pos1 == null || pos2 == null) {
            log.error("请求计算距离的倾情出错了！", new Exception());
            return 99999f;
        }
        return (float) Math.sqrt((pos1.getX() - pos2.getX()) * (pos1.getX() - pos2.getX()) + (pos1.getY() - pos2.getY()) * (pos1.getY() - pos2.getY()));
    }

    //获取两个对象之间的距离
    public static float getDistance(Fighter r, Fighter b) {
        if (r.gainMapModelId() != b.gainMapModelId() || r.gainMapId() != b.gainMapId() || r.gainLine() != b.gainLine()) {
            return 99999f;
        }
        return getDistance(r.gainCurPos(), b.gainCurPos());
    }

    //获取地图格子类型
    public static int getPosType(MapObject map, Position pos) {

        if (null == map) {
//            LOGGER.error("getBlocks error no map null");
            return MapDefine.CELL_TYPE_BLOCK;
        }
        if (pos == null) {
            return MapDefine.CELL_TYPE_BLOCK;
        }
        if (pos.getY() >= map.getRowCellCount()) {
            //LOGGER.info("getBlocks error pos overflow");
            return MapDefine.CELL_TYPE_BLOCK;
        }

        if (pos.getX() >= map.getColCellCount()) {
            return MapDefine.CELL_TYPE_BLOCK;
        }

        int ix = (int) pos.getX();
        int iy = (int) pos.getY();

        if (ix < 0 || iy < 0) {
            return MapDefine.CELL_TYPE_BLOCK;
        }
//        int index = map.getColCellCount() * iy + ix;
//
//        if (index >= map.getBlocks().length || index < 0) {
//            return MapDefine.CELL_TYPE_BLOCK;
//        }
//        
//        return map.getBlocks()[index];

        return map.getBlocks()[iy][ix];
    }

    public static boolean isCanFly(MapObject map, Position from, Position to) {
        if (!isCanFly(map, to)) {
            return false;
        }

        float dis = getDistance(from, to);
        Position dir = getDir(from, to);
        while (dis > 0) {
            dis -= 0.4f;
            if (dis < 0) {
                break;
            }
            Position pos = getPosByDir(from, dir, dis);
            if (!isCanFly(map, pos)) {
                return false;
            }
        }
        return true;
    }

    //是否可飞跃
    public static boolean isCanFly(MapObject map, Position pos) {
        //地图格子类型  大于2的类型可以移动， 大于1的可以跳跃
        int type = getPosType(map, pos);
        if (type == MapDefine.CELL_TYPE_NONE) {
            return false;
        }
        if (type == MapDefine.CELL_TYPE_BLOCK) {
            return false;
        }
        if (type == MapDefine.CELL_TYPE_USER_BLOCK) {
            return false;
        }

        return !isDynamicBlock(map, pos);
    }

    public static boolean isCanMove(MapObject map, Position from, Position to) {

        if (!isCanMove(map, to)) {
            return false;
        }

        float dis = getDistance(from, to);
        Position dir = getDir(from, to);
        while (dis > 0) {
            dis -= 0.4f;
            if (dis < 0) {
                break;
            }
            Position pos = getPosByDir(from, dir, dis);
            if (!isCanMove(map, pos)) {
                return false;
            }
        }
        return true;
    }

    //是否阻挡
    public static boolean isCanMove(MapObject map, Position pos) {
        //地图格子类型  大于2的类型可以移动， 大于1的可以跳跃
        int type = getPosType(map, pos);
        if (type == MapDefine.CELL_TYPE_NONE) {
            return false;
        }
        if (type == MapDefine.CELL_TYPE_BLOCK) {
            return false;
        }
        if (type == MapDefine.CELL_TYPE_JUMP) {
            return false;
        }
        if (type == MapDefine.CELL_TYPE_USER_BLOCK) {
            return false;
        }

        return !isDynamicBlock(map, pos);
    }

    public static boolean isCanJump(MapObject map, Position from, Position to) {

        if (!isCanJump(map, to)) {
            return false;
        }

        float dis = getDistance(from, to);
        Position dir = getDir(from, to);
        while (dis > 0) {
            dis -= 0.9f;
            Position pos = getPosByDir(from, dir, dis);
            if (!isCanJump(map, pos)) {
                return false;
            }
        }
        return true;
    }

    //是否可跳跃
    public static boolean isCanJump(MapObject map, Position pos) {
        int type = getPosType(map, pos);
        if (type == MapDefine.CELL_TYPE_NONE) {
            return false;
        }
        if (type == MapDefine.CELL_TYPE_BLOCK) {
            return false;
        }
        return type != MapDefine.CELL_TYPE_USER_BLOCK;
    }

    public static boolean isCanFight(Fighter fighter) {
        if (fighter == null) {
            return false;
        }

        MapObject map = Manager.mapManager.getMap(fighter.gainMapId());
        if (null == map) {
            return false;
        }

        return isCanFight(map, fighter.gainCurPos());
    }

    public static boolean isCanFight(MapObject map, Position pos) {
        int type = getPosType(map, pos);
        return !(type == MapDefine.CELL_TYPE_SAFE || !isCanMove(map, pos));
    }

    public static float DirXDir(Position a, Position b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    /**
     * 返回一个当前坐标的沿着指定方向，最大不过超过maxRange距离的坐标点
     * @param map 当前地图
     * @param curPos 当前坐标
     * @param dirPos 方向坐标
     * @param maxRange 最大距离
     * @return 返回节点
     */
    public static Position getCanFightPosByDir(MapObject map, Position curPos, Position dirPos , float maxRange){
        if( Float.compare(maxRange, 0f) <= 0){
            return curPos;
        }

        while(Float.compare(maxRange, 0.0f) > 0){
            Position pos = getPosByDir(curPos, dirPos, maxRange);
            if( isCanFight(map, pos)){
                return pos;
            }
            maxRange -= 0.4f;
        }

        return curPos;
    }

    /**
     * 返回一个当前坐标的沿着指定方向，最大不过超过maxRange距离的坐标点
     * @param map 当前地图
     * @param curPos 当前坐标
     * @param dirPos 方向坐标
     * @param maxRange 最大距离
     * @return 返回节点
     */
    public static Position getMovePosByDir(MapObject map, Position curPos, Position dirPos , float maxRange){
        if( Float.compare(maxRange, 0f) <= 0){
            return curPos;
        }

        while(Float.compare(maxRange, 0.0f) > 0){
            Position pos = getPosByDir(curPos, dirPos, maxRange);
            if( isCanMove(map, pos)){
                return pos;
            }
            maxRange -= 0.4f;
        }

        return curPos;
    }
    //沿着向量方向curPos -> targetPos， 取距离curPos 点 range位置处的一个坐标点
    public static Position getDirPos(Position curPos, Position targetPos, float range) {
        if (range == 0f) {
            return curPos;
        }
        float distance = getDistance(curPos, targetPos);
        if (distance == 0f) {
            return curPos;
        }
        float r = range / distance;
        float x = r * targetPos.getX() - r * curPos.getX() + curPos.getX();
        float y = r * targetPos.getY() - r * curPos.getY() + curPos.getY();
        x = x > 0 ? x : 0;
        y = y > 0 ? y : 0;
        return new Position(x, y);
    }

    //沿着方向向量dir 距离range处的坐标
    public static Position getPosByDir(Position curPos, Position dir, float range) {
        if (range == 0f) {
            return curPos;
        }
        float f = (float) Math.sqrt(dir.getX() * dir.getX() + dir.getY() * dir.getY());
        f = range / f;
        // t.x - c.x, t.y -c.y = r.dir
        float x = f * dir.getX() + curPos.getX();
        float y = f * dir.getY() + curPos.getY();
        return new Position(x, y);

    }

    //获取一个单位向量
    public static Position getDir(Position begin, Position end) {
        float distance = getDistance(begin, end);
        if (distance == 0f) {
            return new Position(0, -1);
        }
        float x = (end.getX() - begin.getX()) / distance;
        float y = (end.getY() - begin.getY()) / distance;
        return new Position(x, y);
    }

    //获取一个单位向量
    public static Position getDir(float angle) {
        float x = (float) Math.cos(angle);
        float y = (float) Math.sin(angle);
        return new Position(x, y);
    }

    //是否动态阻挡点
    public static boolean isDynamicBlock(MapObject map, Position pos) {
        if (map.getDoors().isEmpty()) {
            return false;
        }
        List<DynamicBlock> list = new ArrayList<>(map.getDoors().values());
        for (DynamicBlock dynamic : list) {
            if (dynamic.isBlock(pos)) {
                return true;
            }
        }
        return false;
    }

    //检查这个字串是否存在， 不区分大小写
    public static boolean isListContain(Collection<String> list, String str) {
        for (String ss : list) {
            if (ss.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是屏蔽字符不包含空格
     *
     * @param str
     * @return
     */
    public static boolean isForbiddenStrNoSpace(String str) {
//        if (str.contains("\t") || str.contains("\n") || str.contains("'") || str.contains("-") || str.contains("=")) {
//            return true;
//        }
        if (str.contains("\t") ||  str.contains("'") || str.contains("-") || str.contains("=")) {
            return true;
        }
        String tmp = str.toLowerCase();
        for (Cfg_Offstring_Bean bean : CfgManager.getCfg_Offstring_Container().getValuees()) {
            if (StringUtils.isBlank(bean.getForbiddenStr())) {
                continue;
            }
            if (tmp.contains(bean.getForbiddenStr().toLowerCase())) {
                return true;
            }
        }
        for (ForbidWordBean bean : Manager.chatManager.getForbidWords().values()) {
            if (tmp.contains(bean.getWord())) {
                return true;
            }
        }
        //检查是否含有emoji表情符
        if (StringUtils.isNotBlank(str)) {
            String ss = str.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
            if (ss.length() != str.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是屏蔽字符
     *
     * @param str
     * @return
     */
    public static boolean isForbiddenStr(String str) {
        if (str.contains("\t") || str.contains("\n") || str.contains("'") || str.contains(" ") || str.contains("-") || str.contains("=")) {
            return true;
        }
        String tmp = str.toLowerCase();
        for (Cfg_Offstring_Bean bean : CfgManager.getCfg_Offstring_Container().getValuees()) {
            if (StringUtils.isBlank(bean.getForbiddenStr())) {
                continue;
            }
            if (tmp.contains(bean.getForbiddenStr().toLowerCase())) {
                return true;
            }
        }
        for (ForbidWordBean bean : Manager.chatManager.getForbidWords().values()) {
            if (tmp.contains(bean.getWord())) {
                return true;
            }
        }
        //检查是否含有emoji表情符
        if (StringUtils.isNotBlank(str)) {
            String ss = str.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
            if (ss.length() != str.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将emoji表情替换成*
     *
     * @param source
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if (StringUtils.isNotBlank(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "×");
        } else {
            return source;
        }
    }

    public static String replaceAll(String str, String key, String replacement) {
        if (str != null && key != null && replacement != null && !str.equals("") && !key.equals("")) {
            StringBuilder strbuf = new StringBuilder();
            int begin = 0;
            int slen = str.length();
            int npos = 0;
            int klen = key.length();
            for (; begin < slen && (npos = str.indexOf(key, begin)) >= begin; begin = npos + klen) {
                strbuf.append(str.substring(begin, npos)).append(replacement);
            }

            if (begin == 0) {
                return str;
            }
            if (begin < slen) {
                strbuf.append(str.substring(begin));
            }
            return strbuf.toString();
        } else {
            return str;
        }
    }

    /**
     * 把屏蔽字替换为星
     *
     * @param source
     * @return
     */
    public static String filterWords(String source) {
        String ss = filterEmoji(source);
        for (Cfg_Shielding_symbol_Bean bean : CfgManager.getCfg_Shielding_symbol_Container().getValuees()) {
            if (bean.getType().isEmpty()) {
                continue;
            }
            if (ss.contains(bean.getType())) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bean.getType().toCharArray().length; i++) {
                    sb.append("*");
                }
                ss = replaceAll(ss, bean.getType(), sb.toString());
            }
        }

        for (Cfg_Offstring_Bean bean : CfgManager.getCfg_Offstring_Container().getValuees()) {
            if (StringUtils.isBlank(bean.getForbiddenStr())) {
                continue;
            }
            if (ss.toLowerCase().contains(bean.getForbiddenStr().toLowerCase())) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bean.getForbiddenStr().toCharArray().length; i++) {
                    sb.append("*");
                }
                ss = replaceAll(ss, bean.getForbiddenStr(), sb.toString());
            }
        }
        return ss;
    }

    /**
     * 判断字符是否包含屏蔽的标点符号
     *
     * @param str
     * @return
     */
    public static boolean isContainsShielding_symbol(String str) {
        if (str.contains("\t") || str.contains("\n") || str.contains("'") || str.contains(" ") || str.contains("-") || str.contains("=")) {
            return true;
        }
        for (Cfg_Shielding_symbol_Bean bean : CfgManager.getCfg_Shielding_symbol_Container().getValuees()) {
            if (bean.getType().isEmpty()) {
                continue;
            }
            if (str.contains(bean.getType())) {
                return true;
            }
        }
        //检查是否含有emoji表情符
        if (StringUtils.isNotBlank(str)) {
            String ss = str.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
            if (ss.length() != str.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将目标文本写入文件.
     *
     * @param text 文本内容
     * @param file 要写入的文件
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(String text, File file)
            throws FileNotFoundException, IOException {
        BufferedReader in = new BufferedReader(new StringReader(text));
        PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(file)));
        String line;
        try {
            while (null != (line = in.readLine())) {
                out.println(line);
            }
            out.flush();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    //将List内容转化成String
    public static String toMakeStringByList(List<List<Integer>> list) {
        StringBuilder sb = new StringBuilder();
        for (List<Integer> e : list) {
            for (int i = 0; i < e.size(); i++) {
                sb.append(e.get(i)).append("_");
            }
            sb.deleteCharAt(sb.length() - 1).append(";");
        }
        return sb.toString();
    }

    public static String makeString(ReadIntegerArrayEs list) {
        StringBuilder sb = new StringBuilder();
        for (ReadArray<Integer> e : list.getValuees()) {
            for (int i = 0; i < e.size(); i++) {
                sb.append(e.get(i)).append("_");
            }
            sb.deleteCharAt(sb.length() - 1).append(";");
        }
        return sb.toString();
    }

    public static void main(String[] arg) {
        try {
            String data = "[{\"succ\":true,\"target\":\"首席试炼官\",\"score\":1002,\"rank\":997,\"time\":1599766804,\"tiaozhao\":true},{\"succ\":true,\"target\":\"首席试炼官\",\"lastRank\":997,\"rank\":818,\"time\":1599285876,\"tiaozhao\":true},{\"succ\":true,\"target\":\"首席试炼官\",\"lastRank\":818,\"rank\":638,\"time\":1599285893,\"tiaozhao\":true}]";
            List<JJCReport> jjcReports2 = JsonUtils.parseObject(data, new TypeReference<ArrayList<JJCReport>>(){});

            String text = "This is a smiley \uD83C\uDFA6 face\uD860\uDD5D \uD860\uDE07 \uD860\uDEE2 \uD863\uDCCA \uD863\uDCCD \uD863\uDCD2 \uD867\uDD98 ";
            String mess = "{\"order_no\":\"202008111002108605\",\"goods_name\":\"6元充值\",\"game_money\":\"0\",\"role_id\":\"5630448627497511357\",\"goods_code\":\"15\",\"total_fee\":\"6\",\"sign\":\"05b42c719cf9946acc45fb316878e433\",\"goods_id\":\"15\",\"ext_param\":\"1\",\"goods_type\":\"1\",\"goods_ext\":\"1\",\"sign_type\":\"md5\"}";
            HashMap<String,List<String>> test = new HashMap<>();
            test.put("hello", new ArrayList<>());
            test.get("hello").add("world");
            String hstr = JsonUtils.toJSONString(test);
            HashMap<String,List<String>> test2 = JsonUtils.parseObject(hstr, new TypeReference<HashMap<String,List<String>>>(){});
            Recharge recharge = JsonUtils.toJavaObject(mess, Recharge.class);
            String str = "毛泽东";
            System.out.println(text);
            System.out.println(text.length());
//            System.out.println(text.replaceAll("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]", "×"));
            System.out.println("dfddddd" + filterWords(str));
        } catch (Exception ex) {
            log.error("ex", ex);
        }
    }

    /**
     * 构建多个物品公告字符串（可点开tips）
     *
     * @param itemList 物品列表
     * @return itemsStr
     */
    public static String makeItemsStr(List<Item> itemList) {
        if (itemList == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Item item : itemList) {
            if (sb.length() > 0) {
                if (ServerConfig.getLangType().equalsIgnoreCase("ros")) {
                    sb.append("<t=0>,,, </t>");
                } else {
                    sb.append("<t=0>,,、</t>");
                }
            }
            sb.append(Manager.backpackManager.manager().getChatInfo(item));
        }
        return sb.toString();
    }

    /**
     * 构建可链接字符串格式 可链接类型的跑马灯文字以参数的形式加在MessageString末尾，举个栗子：
     * 恭喜[FF0000]【{0}】[-]获得{1}，杀怪经验+50%，练级效率666！ {2} {2}即该方法生成的可链接字符串
     * 其中该条跑马灯的languageId需要添加到配置表UrlMarquee中的唯一id编号，表中其它内容由策划配置
     * 为保持格式统一美观，玩家角色名统一使用[FF0000]【{0}】[-]格式 内容与最后一个参数之间加一个空格
     *
     * @param languageId 语言包ID值
     * @return urlStr
     */
    public static String makeUrlStr(int languageId) {
        if (languageId <= 0) {
            return "";
        }
        Cfg_UrlMarquee_Bean bean = CfgManager.getCfg_UrlMarquee_Container().getValueByKey(languageId);
        if (bean == null) {
            return "";
        }
        //与前端约定:
        //最层的2&_是在服务端解析的,用很明确的值来告诉客户端,整段话中包含要进行翻译的内容.
        //内部的URLText的[&]..[/&]是告诉让客户端根据这个标记来明确要解析这一段内容.
        String text = bean.getUrl_text();
        return ServerStr.getChatTableName("<t=11>4," + bean.getFunctionId() + ",[&]" + text + "[/&]</t>");
    }

    /**
     * 获取装备颜色
     *
     * @param equip 装备
     * @return 颜色
     */
    public static int getEquipColor(Equip equip) {
        if (equip == null) {
            return 0;
        }
        Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (bean == null) {
            return 0;
        }
        return bean.getQuality();
    }

    /**
     * 是否橙色以上及以上品质装备
     *
     * @param equip 物品
     * @return bool
     */
    public static boolean isUpOrangeEquip(Equip equip) {
        return equip != null && getEquipColor(equip) >= EquipDefine.EquipQuality_Orange;
    }

    /**
     * 根据源始传的字符串，检查是否有分隔号标记，如有则返回标记后面的内容
     *
     * @param srcValue 源串
     * @param destValue 目标结果串
     * @return 是否有特殊标记
     */
    public static boolean getMarkAfterString(String srcValue, StringBuilder destValue) {
        
        if (srcValue.length() > 2) {
            char mark1 = srcValue.charAt(1);
            char mark2 = srcValue.charAt(2);
            if (mark1 == '&' && mark2 == '_') {
                destValue.append(srcValue.substring(3));
                return true;
            }
        }
        destValue.append(srcValue);
        return false;
    }
    
     /**
     * 根据源始传的字符串，检查是否有分隔号标记，如有则返回标记后面的内容
     *
     * @param srcValue 源串
     * @return 是否有特殊标记
     */
    public static String getMarkAfterStr(String srcValue) {
        if (srcValue.length() > 2) {
            char mark1 = srcValue.charAt(1);
            char mark2 = srcValue.charAt(2);
            if (mark1 == '&' && mark2 == '_') {
                return srcValue.substring(3);
            }
        }
        return srcValue;
    }

    public static ReadIntegerArrayEs toReadIntegerArrayEsByList(List<List<Integer>> list) {
        ReadArray<Integer>[] valuees = new ReadIntegerArray[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Integer[] v = new Integer[list.get(i).size()];
            for (int j = 0; j < list.get(i).size(); j++) {
                v[j] = list.get(i).get(j);
            }
            valuees[i] = new ReadIntegerArray(v);
        }
        return new ReadIntegerArrayEs(valuees);
    }

    public static ReadLongArrayEs toReadLongArrayEsByList(List<List<Long>> list) {
        ReadArray<Long>[] valuees = new ReadLongArray[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Long[] v = new Long[list.get(i).size()];
            for (int j = 0; j < list.get(i).size(); j++) {
                v[j] = list.get(i).get(j);
            }
            valuees[i] = new ReadLongArray(v);
        }
        return new ReadLongArrayEs(valuees);
    }

    public static ReadIntegerArrayEs toReadIntegerArrayEsByArray(List<ReadArray<Integer>> list) {
        ReadArray<Integer>[] valuees = new ReadIntegerArray[list.size()];
        for (int i = 0; i < list.size(); i++) {
            valuees[i] = new ReadIntegerArray(list.get(i).getValue());
        }
        return new ReadIntegerArrayEs(valuees);
    }

    public static ReadLongArrayEs toReadLongArrayEsByArray(List<ReadArray<Long>> list) {
        ReadArray<Long>[] valuees = new ReadLongArray[list.size()];
        for (int i = 0; i < list.size(); i++) {
            valuees[i] = new ReadLongArray(list.get(i).getValue());
        }
        return new ReadLongArrayEs(valuees);
    }
	
	//指定小数保留n位有效数字，低位舍余
    public static double roundToSignificantFigures(double num, int n) {
        if(num == 0) {
            return 0;
        }

        final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
        final int power = n - (int) d;

        final double magnitude = Math.pow(10, power);
        final double shifted = Math.floor(num*magnitude);
        return shifted/magnitude;
    }

    /**
     * 在一个半径圆中获取一个随机坐标，3次机会，如果失败，则返回源点
     * @param mapObject
     * @param source
     * @param radius
     * @return
     */
    public static Position getRandomPos(MapObject mapObject, Position source, float radius) {
        return getRandomPos(mapObject, source, radius, 3);
    }

    /**
     * 在一个半径圆中获取一个随机坐标，remain次机会，如果失败，则返回源点
     * @param mapObject
     * @param source
     * @param radius
     * @param remain 最多随机几次
     * @return
     */
    public static Position getRandomPos(MapObject mapObject, Position source, float radius, int remain) {
        if (remain <= 0)
            return source;
        float x = RandomUtils.randomFloatValue(-radius, radius);
        float y = RandomUtils.randomFloatValue(-radius, radius);
        Position pos = new Position(x, y);
        pos.add(source);
        if (isCanMove(mapObject, pos))
            return pos;
        return getRandomPos(mapObject, source, radius, --remain);
    }

    public static <T> List<T> find(Collection<T> c, Function<T,Boolean> filter) {
        List<T> list = new ArrayList<>();
        for (T t: c) {
            if (filter.apply(t)) {
                list.add(t);
            }
        }
        return list;
    }
    public static <T> List<T> split(Collection<T> c, Function<T,Boolean> filter) {
        List<T> list = new ArrayList<>();
        Iterator<T> iterator = c.iterator();
        while (iterator.hasNext()){
            T next = iterator.next();
            if (filter.apply(next)) {
                list.add(next);
                iterator.remove();
            }
        }
        return list;
    }
    public static <T> T findOne(Collection<T> c, Function<T,Boolean> filter) {
        for (T t: c) {
            if (filter.apply(t)) {
                return t;
            }
        }
        return null;
    }
    public static <T> T findOne(T[] c, Function<T,Boolean> filter) {
        for (T t: c) {
            if (filter.apply(t)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 从常用的数据结构Map<String,Object>数据结构中读取数据,如果没有读取默认
     * @param mapData
     * @param strKey
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getOrDefaultFromMap(Map<String,Object> mapData,String strKey,T defaultValue){
        if (mapData.containsKey(strKey)) {
            return (T) mapData.get(strKey);
        }
        return defaultValue;
    }

    /**
     * 写不存在的Key到Map中,如果存在就不做处理,用于初始化map信息
     * @param mapData
     * @param strKey
     * @param defaultValue
     * @param <T>
     */
    public static<T> void putNoExistInMap(Map<String,Object> mapData,String strKey,T defaultValue){
        if(!mapData.containsKey(strKey)){
            mapData.put(strKey,defaultValue);
        }
    }

}
