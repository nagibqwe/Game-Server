package com.game.worldanswer.structs;

/**
 * 每题每个玩家的答题信息
 */
public class PlayerSelectInfo {

    private long id;//玩家Id

    private int quesiotnID = 0;//题目ID

    private int  anwerCount = 0;  //本题答题的次数，每轮只能答一次

    private int  answerReuslt = 0; //选在的哪个答案

    public void setId(long id){this.id = id;}

    public long getId(){return id;}

    public void setAnwerCount(int anwerCount){this.anwerCount = anwerCount;}

    public int getAnwerCount(){return anwerCount;}

    public void setAnswerReuslt(int answerReuslt){this.answerReuslt = answerReuslt;}

    public int getAnswerReuslt(){return answerReuslt;}

    public void  setQuesiotnID(int quesiotnID){this.quesiotnID = quesiotnID;}

    public int getQuesiotnID(){return quesiotnID;}
}
