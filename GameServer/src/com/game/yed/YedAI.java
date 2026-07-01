package com.game.yed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.yed.scripts.YedMethodScript;
import game.core.util.stopWatch.Stopwatch;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class YedAI{

    private static final Logger log = YedMgr.getInstance().getAiLogger();

    // 和mgr里面的不一样的话重新load
    private int reloadIndex = 0;
    private int cleanFuncsIndex = 0;
    private long lastUpdateTime;
    private Yed yed = null;
    private boolean skip = false;
    private YedMethodScript defaultScript = null;
    private YedMethodScript owner = null;
    @JsonIgnore
    private YedStack stack = new YedStack();

    // huhu 通用的命令执行对象,在handlefunc的时候排在YedAi,owner之后
    @JsonIgnore
    private transient ArrayList<YedMethodScript> commonTarget = new ArrayList<>(10);
    /**
     * 自定义的参数列表
     */
    @JsonIgnore
    private transient Object[] ownerparams = null;
    /**
     * 函数返回值
     */
    @JsonIgnore
    private transient Object funcRet = null;
    private boolean updateable = true;
    private float stateTime = 0.0f;
    private final HashMap<String, Float> stateTimeG = new HashMap<>();
    private int oneFrameStates = 0;
    private long time = 0;
    private long thisdt = 0;


    // Use this for initialization
    public YedAI(YedMethodScript _owner) {
        defaultScript = YedMgr.getInstance().deal();
        owner = _owner;
    }

    public void Destory() {
        owner = null;
        yed = null;
    }

    public boolean IsExit() {
        return !updateable;
    }

    // Update is called once per frame
    public void Update() {
        //计算上一帧和当前帧的时间差
        long now = System.currentTimeMillis();
        if (lastUpdateTime <= 0) {
            lastUpdateTime = now;
        }
        long dt = (now - lastUpdateTime);
        time += dt;
        thisdt = dt;
        lastUpdateTime = now;

        //开始运行Yed
        if (!skip && updateable) {
            oneFrameStates = 0;
            updateable = RunYed(yed);
        }
        // 每帧设置,每帧清除,防止循环引用 虽然不太可能
        ownerparams = null;

        // 看看是不是要重新加载了
        if (YedMgr.getInstance().reloadIndex != reloadIndex) {
            reloadIndex = YedMgr.getInstance().reloadIndex;
            Load(yed.name, true);
        }
        if (YedMgr.getInstance().cleanFuncsIndex != cleanFuncsIndex) {
            cleanFuncsIndex = YedMgr.getInstance().cleanFuncsIndex;
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
            //一帧最多执行32个状态
            if (oneFrameStates++ > 32) {
                return true;
            }

            //执行下一个状态
            if (yd.nextState != null) {
                yd.CleanBeforeChange();
                ExecInstructions(yd.nextState);

                //执行完毕后,把执行的状态设置为当前状态
                yd.curState = yd.nextState;
                yd.nextState = null;
                stateTime = time;

            }

            //判断当前状态是否是退出
            if ((yd.curState != null) && (yd.curState.isExit)) {
                //updateable = false;
                return false;
            }

            //根据当前状态获取下一个状态
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
        if (ys == null) {
            return null;
        }

        if (ys.isDecisionState) {
            //如果当前节点是需要进行判定处理的,那么就计算当前节点的值,然后再计算所有连线的值,最后经过对比获取下一个节点,如果这个节点是链接和判定的,则再进行测试,一直递归下去
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
            //如果节点不需要进行判定,那么直接对链接进行计算和处理,然后某个链路执行为true,那么就获取下一个节点.
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

    private Token ExecInstruction(YedInstruction yi){
        ArrayList<YedCommand> cmds = yi.cmdOrders;
        int idx = 0;
        while (idx < cmds.size()) {
            YedCommand cmd = cmds.get(idx);
            int opCode = cmd.opCode;
            ++idx;
            switch (opCode){
                case YedOpCode.FUNC:{
                    int argc = (int) (cmd.Params[0]);
                    String funcName = (String) (cmd.Params[1]);
                    HandleFunc(funcName, argc);
                    stack.setLastFuncName(funcName);
                    break;
                }
                case YedOpCode.CONSTANT:{
                    float number = (float) (cmd.Params[0]);
                    stack.PushNumber(number);
                    break;
                }
                case YedOpCode.STRING:{
                    String str = (String) (cmd.Params[0]);
                    stack.PushString(str);
                    break;
                }
                case YedOpCode.PARAM:{
                    String str = (String) (cmd.Params[0]);
                    stack.PushParam(str);
                    break;
                }
                default: {
                    if (opCode >= YedOpCode.OP_FIRST_OP && opCode <= YedOpCode.OP_LAST_OP) {
                        //运算操作
                        HandleOp(opCode);
                    } else if (opCode >= YedOpCode.CMP_FIRST_CMP && opCode <= YedOpCode.CMP_LAST_CMP) {
                        //比较操作
                        HandleCmp(opCode);
                    }
                    else{
                        log.error("发现无效YedCommand的操作符:opCode="+opCode);
                    }
                    break;
                }
            }
        }
        Token _ok = stack.PopToken();
        stack.Clear();
        return _ok == null?Token.zero:_ok;
    }

    //运算操作
    private void HandleOp(int op) {
        double op1 = stack.PopNumber();

        double result = 0;

        //first, unary operators
        if (op == YedOpCode.OP_UADD) {
            result = +op1;
        } else if (op == YedOpCode.OP_USUB) {
            result = -op1;
        } else if (op == YedOpCode.OP_NOT) {
            boolean opv = (op1 != 0);
            result = (!opv ? 1 : 0);
        } else //then, binary operators
        {
            double op2 = stack.PopNumber();

            switch (op) {
                case YedOpCode.OP_ADD: {
                    result = op1 + op2;
                }
                break;
                case YedOpCode.OP_SUB: {
                    result = op1 - op2;
                }
                break;
                case YedOpCode.OP_MUL: {
                    result = op1 * op2;
                }
                break;
                case YedOpCode.OP_DIV: {
                    result = op1 / op2;
                }
                break;
                case YedOpCode.OP_MOD: {
                    result = (float) ((int) op1 % (int) op2);
                }
                break;
                case YedOpCode.OP_AND: {
                    boolean op1v = (op1 != 0);
                    boolean op2v = (op2 != 0);
                    boolean opv = op1v && op2v;
                    result = (opv ? 1 : 0);
                }
                //result = (float)((int)op1 && (int)op2);
                break;
                case YedOpCode.OP_OR: {
                    boolean op1v = (op1 != 0);
                    boolean op2v = (op2 != 0);
                    boolean opv = op1v || op2v;
                    result = (opv ? 1 : 0);
                }
                //result = (float)((int)op1 || (int)op2);
                break;
                case YedOpCode.OP_BITAND: {
                    result = (float) ((int) op1 & (int) op2);
                }
                break;
                case YedOpCode.OP_BITOR: {
                    result = (float) ((int) op1 | (int) op2);
                }
                break;
                case YedOpCode.OP_BITXOR: {
                    result = (float) ((int) op1 ^ (int) op2);
                }
                break;
                default: {
                    System.out.println("unknown operator: " + op);
                }
                break;
            }
        }

        stack.PushNumber(result);
    }

    //比较操作
    private void HandleCmp(int cmp) {
        Token tok1 = stack.PopToken();
        Token tok2 = stack.PopToken();

        boolean result = false;
        if (tok1 == null || tok2 == null) {
            //错误就抛异常
            log.error("HandleCmp 参数不对 lastfunc:" + stack.getLastFuncName(),new NullPointerException());
        } else {

            if (tok1.IsNumber() && tok2.IsNumber()) {
                double op1 = tok1.ValueNumber();
                double op2 = tok2.ValueNumber();
                //log.error("HandleCmp:"+op1+";;;"+cmp+";;;;"+op2);
                switch (cmp) {
                    case YedOpCode.CMP_EQ: {
                        result = op1 == op2;
                    }
                    break;
                    case YedOpCode.CMP_NEQ: {
                        result = op1 != op2;
                    }
                    break;
                    case YedOpCode.CMP_LT: {
                        result = op1 < op2;
                    }
                    break;
                    case YedOpCode.CMP_LTE: {
                        result = op1 <= op2;
                    }
                    break;
                    case YedOpCode.CMP_GT: {
                        result = op1 > op2;
                    }
                    break;
                    case YedOpCode.CMP_GTE: {
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
                    case YedOpCode.CMP_EQ: {
                        result = strcmpresult == 0;
                    }
                    break;
                    case YedOpCode.CMP_NEQ: {
                        result = strcmpresult != 0;
                    }
                    break;
                    case YedOpCode.CMP_LT: {
                        result = strcmpresult < 0;
                    }
                    break;
                    case YedOpCode.CMP_LTE: {
                        result = strcmpresult <= 0;
                    }
                    break;
                    case YedOpCode.CMP_GT: {
                        result = strcmpresult > 0;
                    }
                    break;
                    case YedOpCode.CMP_GTE: {
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
        }
        stack.PushBoolean(result);
    }

    private void HandleFunc(String funcName, int allargc) {
        Stopwatch watch = new Stopwatch();
        //log.error("HandleFunc:"+funcName);
        watch.start("AI HandleFunc");
        this.funcRet = null;
        Object[] args = stack.AllocArray(allargc);
        int argc = 0;
        while (argc < allargc) {
            Token val = stack.PopToken();
            if (val.type == Token.TokenType.Param) {
                val = stack.PopToken();
            }
            int c = argc++;
            args[c] = val.ValueParam();
        }

        //1.先在默认的脚本中查找方法,进行执行
        boolean execSuccess = defaultScript.execAiMethod(this,funcName,args);
        if(execSuccess){
            callFuntion(watch,funcName);
            return;
        }
        //2.在Owner的脚本上查找方法,执行.
        if(owner != null){
            execSuccess = owner.execAiMethod(this,funcName,args);
            if(execSuccess){
                callFuntion(watch,funcName);
                return;
            }
        }
        //3.如果Owner脚本上没有,就从一些通用目标上执行
        if(commonTarget.size()>0){
            for (YedMethodScript target : commonTarget) {
                execSuccess = target.execAiMethod(this,funcName,args);
                if(execSuccess){
                    callFuntion(watch,funcName);
                    return;
                }
            }
        }
    }

    private void callFuntion(Stopwatch watch,String funcName){
        watch.measure("invoke method " + funcName, 0.1);
        if (this.funcRet != null) {
            stack.PushObject(this.funcRet);
            this.funcRet = null;
            watch.measure("HandleReturn " + funcName, 0.1);
        }
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public float StateTime() {
        return time - stateTime;
    }

    public float StateTime(String key) {
        float state = time;
        if (!stateTimeG.containsKey(key)) {
            stateTimeG.put(key, state);
        } else {
            state = stateTimeG.get(key);
        }
        return time - state;
    }

    public HashMap<String,Float> getStateTimeG(){
        return stateTimeG;
    }

    public long getThisdt() {
        return thisdt;
    }

    public void setFuncRet(Object funcRet) {
        this.funcRet = funcRet;
    }

    public Yed getYed(){
        return yed;
    }
    /**
     * 设置跑这个AI的对象
     *
     * @param o
     */
    public void SetOwner(YedMethodScript o) {
        owner = o;
    }

    public YedMethodScript getOwner() {
        return owner;
    }

    public void addCommonTarget(YedMethodScript o) {
        commonTarget.add(o);
    }

    public void cleanCommonTarget() {
        commonTarget.clear();
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
}
