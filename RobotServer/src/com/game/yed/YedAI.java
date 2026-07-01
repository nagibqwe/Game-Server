package com.game.yed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.manager.YedMgr;
import com.game.utils.Stopwatch;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class YedAI {

    private static final Logger log = YedMgr.getInstance().getAiLogger();

    // 和mgr里面的不一样的话重新load
    int reloadIndex = 0;

    public long lastUpdateTime;
    public Yed yed = null;
    public boolean skip = false;
    private Object owner = null;

    // huhu 通用的命令执行对象,在handlefunc的时候排在YedAi,owner之后
    @JsonIgnore
    private ArrayList commonTarget = new ArrayList(10);

    public void addCommonTarget(Object o){
        if(commonTarget.contains(o))
            return;
        commonTarget.add(o);
    }
    /**
     * 自定义的参数列表
     */
    @JsonIgnore
    private Object[] ownerparams = null;
    private boolean updateable = true;
    private int rand = 0;
    private float stateTime = 0.0f;
    private int oneFrameStates = 0;
    private long time = 0;
    private long thisdt = 0;
    private ArrayList<Token> paramStack = new ArrayList<Token>();
    private String lastfunc;
    private final Object[][] args_tmp = {
            new Object[0],
            new Object[1],
            new Object[2],
            new Object[3],
            new Object[4],
            new Object[5],
            new Object[6],
            new Object[7],
            new Object[8],
            new Object[9],
            new Object[10]
    };
    private final Class<?>[][] args_class_tmp = {
            new Class<?>[0],
            new Class<?>[1],
            new Class<?>[2],
            new Class<?>[3],
            new Class<?>[4],
            new Class<?>[5],
            new Class<?>[6],
            new Class<?>[7],
            new Class<?>[8],
            new Class<?>[9],
            new Class<?>[10]
    };
    private HashMap<String, MKV> funcnames = new HashMap<String, MKV>();

    // Use this for initialization
    public YedAI(Object _owner) {
        owner = _owner;
    }

    public void Destory(){
        owner = null;
        yed = null;
    }

    public static Object Cast(Class<?> tart, Object oldv) {
        try {
            Object ret = tart.cast(oldv);
            return ret;
        } catch (Exception e) {
            return CastNumber(tart, oldv);
        }
    }

    private static Object CastNumber(Class<?> tart, Object oldv) {
        Number v = null;
        try {
            v = Number.class.cast(oldv);
        } catch (Exception e) {
            return tart.cast(oldv);
        }
        if (tart == float.class || tart.isAssignableFrom(float.class)) {
            return v.floatValue();
        } else if (tart == int.class || tart.isAssignableFrom(int.class)) {
            return v.intValue();
        } else if (tart == short.class || tart.isAssignableFrom(short.class)) {
            return v.shortValue();
        } else if (tart == byte.class || tart.isAssignableFrom(byte.class)) {
            return v.byteValue();
        } else if (tart == double.class || tart.isAssignableFrom(double.class)) {
            return v.doubleValue();
        }
        return tart.cast(oldv);
    }

    /**
     * 设置跑这个AI的对象
     *
     * @param o
     */
    public void SetOwner(Object o) {
        owner = o;
    }

    public Object GetOwner() {
        return owner;
    }

    public boolean IsExit(){
        return !updateable;
    }

    /**
     * 获取自定义的参数列表,可能为空
     *
     * @return
     */
    public Object[] getOwnerparams() {
        return ownerparams;
    }

    /**
     * 设置一个参数缓存
     *
     * @param ownerparams
     */
    public void setOwnerparams(Object... ownerparams) {
        this.ownerparams = ownerparams;
    }

    // Update is called once per frame
    public void Update(long dt) {
        time += dt;
        thisdt = dt;
        if (!skip && updateable) {
            oneFrameStates = 0;
            updateable = RunYed(yed);
        }
        // 每帧设置,每帧清除,防止循环引用 虽然不太可能
        ownerparams = null;

        // 看看是不是要重新加载了
        if(YedMgr.getInstance().reloadIndex != reloadIndex){
            reloadIndex = YedMgr.getInstance().reloadIndex;
            Load(yed.name, true);
        }
    }

    public void Load(String name, boolean unload/* = false*/) {
        if (unload) {
            yed = new Yed();
            yed.Load(name);
        } else {
            if (yed == null) {
                yed = new Yed();
            }
            yed.Load(name);
        }
    }

    // 2017-9-13 huhu 返回值从void转为boolean
    // 是为了修正当 child ai 调用exit时不应当影响到父ai的执行,父ai可以用IsChildExit()来判断child是否已经exit
    // 而父ai如果没有达成去下一个状态时,将会停留在child,否则child退出,走向父ai的下一个状态.
    // true:该状态机并未退出,继续跑 false:状态机已经exit了.
    private boolean RunYed(Yed yd) {
        if (yd != null) {
            if (oneFrameStates++ > 32) {
                return true;
            }
            if (yd.nextState != null) {
                yd.CleanBeforeChange();
                ExecInstructions(yd.nextState);
                stateTime = time;
                rand = RandomHelper.Range(0, 10000);
                yd.curState = yd.nextState;
                yd.nextState = null;
            }
            if ((yd.curState != null) && (yd.curState.isExit)) {
                //updateable = false;
                return false;
            }
            yd.nextState = TestTransitions(yd.curState);
            if (yd.nextState != null) {
                return RunYed(yd);
            } else {
                // child 的退出不影响到上一层,直到本层exit了才return false;
                RunYed(yd.getChild());
                return true;
            }
        }
        return true;
    }

    private YedState TestTransition(YedTransition yt) {
        Token _ok = ExecInstruction(yt.condition);
        if (!_ok.IsEmpty()) {
            return yt.target;
        }
        return null;
    }

    private YedState TestTransitions(YedState ys) {
        if (ys == null) return null;
        //OurDebug.Log("TestTransitions: "+ys.name);
        if (ys.isDecisionState) {
            Token _ok = ExecInstructions(ys);
            for (int i = 0; i < ys.numTransitions; ++i) {
                YedTransition _trans = ys.transitions[i];
                Token _tok = ExecInstruction(_trans.condition);
                YedState _next = null;
                if (_tok.ValueParam().equals(_ok.ValueParam())) {
                    _next = _trans.target;
                    if (_next.isJunctionState || _next.isDecisionState) {
                        _next = TestTransitions(_next);
                    }
                }
                if (_next != null) {
                    return _next;
                }
            }
        } else {
            for (int i = 0; i < ys.numTransitions; ++i) {
                YedState _next = TestTransition(ys.transitions[i]);
                if (_next != null) {
                    if (_next.isJunctionState || _next.isDecisionState) {
                        _next = TestTransitions(_next);
                        if (_next != null) {
                            return _next;
                        }
                    } else {
                        return _next;
                    }
                }
            }
        }
        return null;
    }

    private Token ExecInstructions(YedState ys) {
        Token _ok = Token.zero;
        for (int i = 0; i < ys.numInstructions; ++i) {
            _ok = ExecInstruction(ys.instructions[i]);
        }
        return _ok;
    }

    private Token ExecInstruction(YedInstruction yi) {
        return Exec(yi.cmds, yi.instructionLength);
    }

    private Token Exec(ArrayList<Object> cmds, int len) {
        int idx = 0;
        while (idx < cmds.size()) {
            int instr = (int) (cmds.get(idx));//ptr.NReadInt();
            idx++;
            if (instr >= EInstr.OP_FIRST_OP && instr <= EInstr.OP_LAST_OP) {
                HandleOp(instr);
            } else if (instr >= EInstr.CMP_FIRST_CMP && instr <= EInstr.CMP_LAST_CMP) {
                HandleCmp(instr);
            } else if (instr == EInstr.FUNC) {
                //int idx = ptr.NReadInt();
                int argc = (int) (cmds.get(idx));//ptr.NReadInt();
                idx++;
                //int funcNameLen = ptr.ReadInt();
                //byte[] funcNameBytes = ptr.Read(funcNameLen);
                //String funcName = BinaryBuffer.Bytes2String(funcNameBytes);
                String funcName = (String) (cmds.get(idx)); //ptr.NReadString();
                idx++;
                HandleFunc(funcName, argc);
                lastfunc = funcName;
            } else if (instr == EInstr.CONSTANT) {
                float number = (float) (cmds.get(idx)); //ptr.NReadFloat();
                idx++;
                PushNumber(number);
            } else if (instr == EInstr.PARAM) {
                //int strLen = ptr.ReadInt();
                //byte[] strBytes = ptr.Read(strLen);
                //String str = BinaryBuffer.Bytes2String(strBytes);
                String str = (String) (cmds.get(idx)); //ptr.NReadString();
                idx++;
                PushParam(str);
            } else if (instr == EInstr.STRING) {
                //int strLen = ptr.ReadInt();
                //byte[] strBytes = ptr.Read(strLen);
                //String str = BinaryBuffer.Bytes2String(strBytes);
                String str = (String) (cmds.get(idx)); //ptr.NReadString();
                idx++;
                PushString(str);
            }
        }
        Token _ok = Token.zero;
        if (paramStack.size() > 0) {
            _ok = paramStack.get(paramStack.size() - 1);
        }
        paramStack.clear();
        return _ok;
    }

    private void HandleOp(int op) {
        double op1 = PopNumber();

        double result = 0;

        //first, unary operators
        if (op == EInstr.OP_UADD) {
            result = +op1;
        } else if (op == EInstr.OP_USUB) {
            result = -op1;
        } else if (op == EInstr.OP_NOT) {
            boolean opv = (op1 != 0);
            result = (!opv ? 1 : 0);
        } else //then, binary operators
        {
            double op2 = PopNumber();

            switch (op) {
                case EInstr.OP_ADD: {
                    result = op1 + op2;
                }
                break;
                case EInstr.OP_SUB: {
                    result = op1 - op2;
                }
                break;
                case EInstr.OP_MUL: {
                    result = op1 * op2;
                }
                break;
                case EInstr.OP_DIV: {
                    result = op1 / op2;
                }
                break;
                case EInstr.OP_MOD: {
                    result = (float) ((int) op1 % (int) op2);
                }
                break;
                case EInstr.OP_AND: {
                    boolean op1v = (op1 != 0);
                    boolean op2v = (op2 != 0);
                    boolean opv = op1v && op2v;
                    result = (opv ? 1 : 0);
                }
                //result = (float)((int)op1 && (int)op2);
                break;
                case EInstr.OP_OR: {
                    boolean op1v = (op1 != 0);
                    boolean op2v = (op2 != 0);
                    boolean opv = op1v || op2v;
                    result = (opv ? 1 : 0);
                }
                //result = (float)((int)op1 || (int)op2);
                break;
                case EInstr.OP_BITAND: {
                    result = (float) ((int) op1 & (int) op2);
                }
                break;
                case EInstr.OP_BITOR: {
                    result = (float) ((int) op1 | (int) op2);
                }
                break;
                case EInstr.OP_BITXOR: {
                    result = (float) ((int) op1 ^ (int) op2);
                }
                break;
                default: {
                    System.out.println("unknown operator: " + op);
                }
                break;
            }
        }

        PushNumber(result);
    }

    ;

    private void HandleCmp(int cmp) {
        Token tok1 = PopToken();
        Token tok2 = PopToken();

        boolean result = false;
        if(tok1 == null || tok2 == null){
            log.error("HandleCmp 参数不对 lastfunc:" + lastfunc);
        }
        if (tok1.IsNumber() && tok2.IsNumber()) {
            double op1 = tok1.ValueNumber();
            double op2 = tok2.ValueNumber();

            switch (cmp) {
                case EInstr.CMP_EQ: {
                    result = op1 == op2;
                }
                break;
                case EInstr.CMP_NEQ: {
                    result = op1 != op2;
                }
                break;
                case EInstr.CMP_LT: {
                    result = op1 < op2;
                }
                break;
                case EInstr.CMP_LTE: {
                    result = op1 <= op2;
                }
                break;
                case EInstr.CMP_GT: {
                    result = op1 > op2;
                }
                break;
                case EInstr.CMP_GTE: {
                    result = op1 >= op2;
                }
                break;
                default: {
                    System.out.println("unknown comparator: " + cmp);
                }
                break;
            }
        } else if (tok1.IsString() && tok2.IsString()) {
            int strcmpresult = tok1.ValueString().compareTo(tok2.ValueString());
            switch (cmp) {
                case EInstr.CMP_EQ: {
                    result = strcmpresult == 0;
                }
                break;
                case EInstr.CMP_NEQ: {
                    result = strcmpresult != 0;
                }
                break;
                case EInstr.CMP_LT: {
                    result = strcmpresult < 0;
                }
                break;
                case EInstr.CMP_LTE: {
                    result = strcmpresult <= 0;
                }
                break;
                case EInstr.CMP_GT: {
                    result = strcmpresult > 0;
                }
                break;
                case EInstr.CMP_GTE: {
                    result = strcmpresult >= 0;
                }
                break;
                default: {
                    System.out.println("unknown comparator: " + cmp);
                }
                break;
            }
        } else {
            System.out.println("invalid types for HandleCmp " + tok1.type + " " + tok2.type);
        }
        if (result) {
            PushNumber(1);
        } else {
            PushNumber(0);
        }
    }

    private void HandleFunc(String funcName, int allargc) {
        Stopwatch watch = new Stopwatch();
        watch.start("AI HandleFunc");
        Object[] args = args_tmp[allargc];
        int argc = 0;
        while (argc < allargc) {
            Token val = PopToken();
            if (val.type == Token.TokenType.Param) {
                val = PopToken();
            }
            int c = argc++;
            args[c] = val.ValueParam();
        }

        if (!funcnames.containsKey(funcName)) {
            MKV kv = searchMethod(funcName);
            funcnames.put(funcName, kv);
            watch.measure("find method " + funcName, 0.1);
        }

        MKV mkv = funcnames.get(funcName);
        if (mkv != null) {
            mkv.m.setAccessible(true);
            Class<?>[] paramsInfo = mkv.m.getParameterTypes();
            if (paramsInfo.length == args.length) {
                for (int i = 0; i < paramsInfo.length; ++i) {
                    // args[i] = Convert.ChangeType(args[i], paramsInfo[i].ParameterType);
                    args[i] = Cast(paramsInfo[i], args[i]);
                }
                try {
                    Object ret = mkv.m.invoke(mkv.o, args);
                    watch.measure("invoke method " + funcName, 0.1);
                    if (ret != null) {
                        HandleReturn(ret);
                        watch.measure("HandleReturn " + funcName, 0.1);
                    }
                } catch (Exception e) {
                    YedMgr.getInstance().getAiLogger().info(e.getMessage() + " func:" + funcName, e);
                }
            } else if (paramsInfo.length == args.length + 1 && paramsInfo[0].equals(YedAI.class)) {
                Object[] nargs = new Object[args.length + 1];
                nargs[0] = this;
                for (int i = 0; i < args.length; ++i) {
                    // nargs[i+1] = Convert.ChangeType(args[i], paramsInfo[i+1].ParameterType);
                    nargs[i + 1] = Cast(paramsInfo[i + 1], args[i]);
                }
                try {
                    Object ret = mkv.m.invoke(mkv.o, nargs);
                    watch.measure("invoke method " + funcName, 0.1);
                    if (ret != null) {
                        HandleReturn(ret);
                        watch.measure("HandleReturn " + funcName, 0.1);
                    }
                } catch (Exception e) {
                    log.error(String.format("调用函数失败 %s nargs len:%d paramsInfo len:%d", funcName, nargs.length, paramsInfo.length), e);
                }
            } else {
                log.error(String.format("调用函数%s失败 paramsInfo.length:%d args.length:%d", funcName, paramsInfo.length, args.length));
                return;
            }
        } else {
            log.info("can't find funcName:" + funcName);
        }
        StringBuilder ret = watch.stop(true, 0.2);
        if (!ret.toString().isEmpty()) {
            log.error(ret.toString());
        }
    }

    private MKV searchMethodInTarget(Object target, String funcName){
        Class<?> c = target.getClass();

        Method[] ms = c.getDeclaredMethods();
        Method m = null;
        for (Method tm : ms) {
            if (tm.getName().equals(funcName)) {
                m = tm;
                break;
            }
        }
        if (m != null) {
            MKV kv = new MKV();
            kv.m = m;
            kv.o = target;
            return kv;
        }
        return null;
    }

    private MKV searchMethod(String funcName) {
        String funn = "Sa_" + funcName;
        MKV kv = searchMethodInTarget(this, funn);
        if(kv != null)
            return kv;
        kv = searchMethodInTarget(owner, funn);
        if(kv != null)
            return kv;
        for(Object target : commonTarget){
            kv = searchMethodInTarget(target, funn);
            if(kv != null)
                return kv;
        }
        return kv;
    }

    public void HandleReturn(Object o) {
        if (o == null) {
            return;
        }
        try {
            if (o.getClass() == Integer.class) {
                int v = (Integer) o;
                PushNumber(v);
            } else if (o.getClass() == Short.class) {
                short v = (Short) o;
                PushNumber(v);
            } else if (o.getClass() == Character.class) {
                char v = (Character) o;
                PushNumber(v);
            } else if (o.getClass() == Float.class) {
                float v = (Float) o;
                PushNumber(v);
            } else if (o.getClass() == Long.class) {
                long v = (Long) o;
                PushNumber(v);
            } else if (o.getClass() == Boolean.class) {
                boolean v = (Boolean) o;
                PushNumber(v ? 1 : 0);
            } else {
                double v = (Double) o;
                PushNumber(v);
            }
            return;
        } catch (Exception e) {
        }

        try {
            String v = (String) o;
            PushString(v);
            return;
        } catch (Exception e) {
        }
        Exception e = new Exception("HandleReturn error o:" + o);
        e.printStackTrace();

        YedMgr.getInstance().getAiLogger().info(e.getMessage(), e);
    }

    public Token PopToken() {
        Token _ok = null;
        if (paramStack.size() > 0) {
            _ok = paramStack.get(paramStack.size() - 1);
            paramStack.remove(paramStack.size() - 1);
        }
        return _ok;
    }

    public double PopNumber() {
        Token _ok = PopToken();
        double _number = 0;
        if (_ok != null) {
            _number = _ok.ValueNumber();
        }
        return _number;
    }

    public void PushToken(Token _ok) {
        paramStack.add(_ok);
    }

    public void PushParam(String str) {
        Token _ok = new Token();
        _ok.type = Token.TokenType.Param;
        _ok.ValueParam(str);
        paramStack.add(_ok);
    }

    public void PushString(String str) {
        Token _ok = new Token(str);
        paramStack.add(_ok);
    }

    public void PushNumber(double val) {
        Token _ok = new Token(val);
        paramStack.add(_ok);
    }

    public float StateTime() {
        return time - stateTime;
    }

    void Sa_Rand(double range) {
        PushNumber(rand % range);
    }

    void Sa_Wait(double time) {
        if (StateTime() >= time) {
            PushNumber(1);
        } else {
            PushNumber(0);
        }
    }

    void Sa_WaitOneFrame() {
        if (StateTime() >= 0.01) {
            PushNumber(1);
        } else {
            PushNumber(0);
        }
    }

    void Sa_RunYed(String yed) {
        Load(yed, false);
    }

    void Sa_IsChildExit() {
        if(yed.curState == null || yed.getChild() == null){
            PushNumber(1);
            return;
        }
        if(yed.getChild().curState == null){
            PushNumber(0);
            return;
        }
        int ret = yed.getChild().curState.isExit ? 1 : 0;
        if(ret == 1){
            log.debug(String.format("ai debug owner:%s Sa_IsChildExit ret:%d", owner.toString(), ret));
        }
        PushNumber(ret);
    }

    void Sa_print(String info){
        System.out.println(info);
    }

    void Sa_Print(String info){
        Sa_print(info);
    }

    public long getThisdt() {
        return thisdt;
    }

    public class EInstr {
        public static final int OP_FIRST_OP = 0;
        public static final int OP_ADD = OP_FIRST_OP;
        public static final int OP_SUB = OP_ADD + 1;
        public static final int OP_MUL = OP_SUB + 1;
        public static final int OP_DIV = OP_MUL + 1;
        public static final int OP_MOD = OP_DIV + 1;
        public static final int OP_NOT = OP_MOD + 1;
        public static final int OP_AND = OP_NOT + 1;
        public static final int OP_OR = OP_AND + 1;
        public static final int OP_BITAND = OP_OR + 1;
        public static final int OP_BITOR = OP_BITAND + 1;
        public static final int OP_BITXOR = OP_BITOR + 1;
        public static final int OP_UADD = OP_BITXOR + 1;
        public static final int OP_USUB = OP_UADD + 1;
        public static final int OP_LAST_OP = OP_USUB;

        public static final int CMP_FIRST_CMP = 20;
        public static final int CMP_EQ = CMP_FIRST_CMP;
        public static final int CMP_NEQ = CMP_EQ + 1;
        public static final int CMP_LT = CMP_NEQ + 1;
        public static final int CMP_GT = CMP_LT + 1;
        public static final int CMP_LTE = CMP_GT + 1;
        public static final int CMP_GTE = CMP_LTE + 1;
        public static final int CMP_LAST_CMP = CMP_GTE;

        public static final int FUNC = 30;

        public static final int CONSTANT = 40;

        public static final int STRING = 50;

        public static final int PARAM = 60;
    }

    class MKV {
        public Object o = null;
        public Method m = null;
    }

}
