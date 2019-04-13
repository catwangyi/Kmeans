import com.sun.deploy.cache.CacheEntry;
import com.sun.org.apache.xml.internal.security.Init;

import java.util.Scanner;

/**
 * @author wang
 * @create 2019-04-02 16:14
 * @desc
 **/
public class main {
    public static void main(String[] args) {
        SoccerTeam[] soccerTeams;
        soccerTeams = new SoccerTeam[15];
        //1.归一化数据
        initSoccerTeam(soccerTeams);
        //2.设置初始簇心
        SoccerTeam[] centerTeams;
        centerTeams = new SoccerTeam[3];

        SoccerTeam[] tempTeams;//tempTeams用来检查簇心是否改变
        tempTeams = new SoccerTeam[3];
        for (int i = 0; i < centerTeams.length; i++) {
            centerTeams[i] = new SoccerTeam();
            tempTeams[i] = new SoccerTeam();
        }
        //设置三个初始簇心点
        centerTeams[0].score_2006 = soccerTeams[1].score_2006;
        centerTeams[0].score_2010 = soccerTeams[1].score_2010;
        centerTeams[0].score_2007 = soccerTeams[1].score_2007;
        centerTeams[0].distance = 0;
        centerTeams[0].teamLevel = "A";

        centerTeams[1].score_2006 = soccerTeams[12].score_2006;
        centerTeams[1].score_2010 = soccerTeams[12].score_2010;
        centerTeams[1].score_2007 = soccerTeams[12].score_2007;
        centerTeams[1].distance = 0;
        centerTeams[1].teamLevel = "B";

        centerTeams[2].score_2006 = soccerTeams[9].score_2006;
        centerTeams[2].score_2010 = soccerTeams[9].score_2010;
        centerTeams[2].score_2007 = soccerTeams[9].score_2007;
        centerTeams[2].distance = 0;
        centerTeams[2].teamLevel = "C";
        double realDistance = 1000;
        double mindistance = 1000;
        int p = 1;
        for (int k = 0; k < 100; k++) {//循环100次
            //tempTeams计算簇心点是否改变
            tempTeams[0] = centerTeams[0];
            tempTeams[1] = centerTeams[1];
            tempTeams[2] = centerTeams[2];
            //计算每个点到簇心的距离;
            for (int i = 0; i < soccerTeams.length; i++) {
                //求最小的距离
                mindistance = 1000;
                for (int j = 0; j < centerTeams.length; j++) {
                    //每个球队到各个簇心的距离
                    realDistance = Math.sqrt(Math.pow(soccerTeams[i].score_2006 - centerTeams[j].score_2006, 2) + Math.pow(soccerTeams[i].score_2007 - centerTeams[j].score_2007, 2) + Math.pow(soccerTeams[i].score_2010 - centerTeams[j].score_2010, 2));
                    if (mindistance >= realDistance) {
                        //每个球队到各个簇心的最短距离
                        mindistance = Math.sqrt(Math.pow(soccerTeams[i].score_2006 - centerTeams[j].score_2006, 2) + Math.pow(soccerTeams[i].score_2007 - centerTeams[j].score_2007, 2) + Math.pow(soccerTeams[i].score_2010 - centerTeams[j].score_2010, 2));
                        //给每个球队归类
                        soccerTeams[i].teamLevel = centerTeams[j].teamLevel;
                    }
                }
                soccerTeams[i].distance = mindistance;
                // System.out.println("队伍名称："+soccerTeams[i].teanName+"等级："+soccerTeams[i].teamLevel+"到簇心点的距离:"+soccerTeams[i].distance);
            }
            //重置簇心点
            for (int i = 0; i < centerTeams.length; i++) {
                centerTeams[i].score_2006 = 0;
                centerTeams[i].score_2010 = 0;
                centerTeams[i].score_2007 = 0;
            }
            //计算每个类别的球队有多少支
            int aClassNum = 0;
            int bClassNum = 0;
            int cClassNum = 0;
            for (int i = 0; i < soccerTeams.length; i++) {
                //计算新的簇心点
                switch (soccerTeams[i].teamLevel) {
                    case "A":
                        aClassNum++;
                        centerTeams[0].score_2006 += soccerTeams[i].score_2006;
                        centerTeams[0].score_2010 += soccerTeams[i].score_2010;
                        centerTeams[0].score_2007 += soccerTeams[i].score_2007;
                        break;
                    case "B":
                        centerTeams[1].score_2006 += soccerTeams[i].score_2006;
                        centerTeams[1].score_2010 += soccerTeams[i].score_2010;
                        centerTeams[1].score_2007 += soccerTeams[i].score_2007;
                        bClassNum++;
                        break;
                    case "C":
                        centerTeams[2].score_2006 += soccerTeams[i].score_2006;
                        centerTeams[2].score_2010 += soccerTeams[i].score_2010;
                        centerTeams[2].score_2007 += soccerTeams[i].score_2007;
                        cClassNum++;
                        break;
                }
            }
            //更新簇心的数据
            for (int i = 0; i < centerTeams.length; i++) {
                if (centerTeams[i].teamLevel.equals("A")) {
                    centerTeams[i].score_2006 /= aClassNum;
                    centerTeams[i].score_2010 /= aClassNum;
                    centerTeams[i].score_2007 /= aClassNum;
                } else if (centerTeams[i].teamLevel.equals("B")) {
                    centerTeams[i].score_2006 /= bClassNum;
                    centerTeams[i].score_2010 /= bClassNum;
                    centerTeams[i].score_2007 /= bClassNum;
                } else if (centerTeams[i].teamLevel.equals("C")) {
                    centerTeams[i].score_2006 /= cClassNum;
                    centerTeams[i].score_2010 /= cClassNum;
                    centerTeams[i].score_2007 /= cClassNum;
                }
            }
            //检查簇心是否改变
            for (int i = 0; i < centerTeams.length; i++) {
                if (tempTeams[i].score_2006 != centerTeams[i].score_2006 || tempTeams[i].score_2010 != centerTeams[i].score_2010 || tempTeams[i].score_2007 != centerTeams[i].score_2007) {
                    //如果有一项不符合，就退出循环继续程序
                    break;
                } else if (tempTeams[i].score_2006 == centerTeams[i].score_2006 || tempTeams[i].score_2010 == centerTeams[i].score_2010 || tempTeams[i].score_2007 == centerTeams[i].score_2007) {
                    if (i + 1 == centerTeams.length) {
                        System.out.println("k:"+(k+1));
                        k = 100;
                        //退出循环
                    }
                }
            }
        }
        //输出
        for (int i = 0; i < 3; i++) {
            for (int j=0;j<soccerTeams.length;j++){
                if (soccerTeams[j].teamLevel.equals("A")){
                    if (i==0){
                        System.out.println("队伍名称:"+soccerTeams[j].teanName+"\t等级："+soccerTeams[j].teamLevel);
                    }
                }else if (soccerTeams[j].teamLevel.equals("B")){
                    if (i==1){
                        System.out.println("队伍名称:"+soccerTeams[j].teanName+"\t等级："+soccerTeams[j].teamLevel);
                    }
                }else if (soccerTeams[j].teamLevel.equals("C")){
                    if (i==2){
                        System.out.println("队伍名称:"+soccerTeams[j].teanName+"\t等级："+soccerTeams[j].teamLevel);
                    }
                }
            }
        }
    }


    public static void initSoccerTeam(SoccerTeam[] soccerTeams){
        for (int i=0;i<soccerTeams.length;i++){
            soccerTeams[i]=new SoccerTeam();
            switch (i){
                case 0:
                    soccerTeams[i].teanName="中国";
                    soccerTeams[i].score_2006=50;
                    soccerTeams[i].score_2010=50;
                    soccerTeams[i].score_2007=9;
                    break;
                case 1:
                    soccerTeams[i].teanName="日本";
                    soccerTeams[i].score_2006=28;
                    soccerTeams[i].score_2010=9;
                    soccerTeams[i].score_2007=4;
                    break;
                case 2:
                    soccerTeams[i].teanName="韩国";
                    soccerTeams[i].score_2006=17;
                    soccerTeams[i].score_2010=15;
                    soccerTeams[i].score_2007=3;
                    break;
                case 3:
                    soccerTeams[i].teanName="伊朗";
                    soccerTeams[i].score_2006=25;
                    soccerTeams[i].score_2010=40;
                    soccerTeams[i].score_2007=5;
                    break;
                case 4:
                    soccerTeams[i].teanName="沙特";
                    soccerTeams[i].score_2006=28;
                    soccerTeams[i].score_2010=40;
                    soccerTeams[i].score_2007=2;
                    break;
                case 5:
                    soccerTeams[i].teanName="伊拉克";
                    soccerTeams[i].score_2006=50;
                    soccerTeams[i].score_2010=50;
                    soccerTeams[i].score_2007=1;
                    break;
                case 6:
                    soccerTeams[i].teanName="卡塔尔";
                    soccerTeams[i].score_2006=50;
                    soccerTeams[i].score_2010=40;
                    soccerTeams[i].score_2007=9;
                    break;
                case 7:
                    soccerTeams[i].teanName="阿联酋";
                    soccerTeams[i].score_2006=50;
                    soccerTeams[i].score_2010=40;
                    soccerTeams[i].score_2007=9;
                    break;
                case 8:
                    soccerTeams[i].teanName="乌兹别克斯坦";
                    soccerTeams[i].score_2006=40;
                    soccerTeams[i].score_2010=40;
                    soccerTeams[i].score_2007=5;
                    break;
                case 9:
                    soccerTeams[i].teanName="泰国";
                    soccerTeams[i].score_2006=50;
                    soccerTeams[i].score_2010=50;
                    soccerTeams[i].score_2007=9;
                    break;
                case 10:
                    soccerTeams[i].teanName="越南";
                    soccerTeams[i].score_2006=50;
                    soccerTeams[i].score_2010=50;
                    soccerTeams[i].score_2007=5;
                    break;
                case 11:
                    soccerTeams[i].teanName="阿曼";
                    soccerTeams[i].score_2006=50;
                    soccerTeams[i].score_2010=50;
                    soccerTeams[i].score_2007=9;
                    break;
                case 12:
                    soccerTeams[i].teanName="巴林";
                    soccerTeams[i].score_2006=40;
                    soccerTeams[i].score_2010=40;
                    soccerTeams[i].score_2007=9;
                    break;
                case 13:
                    soccerTeams[i].teanName="朝鲜";
                    soccerTeams[i].score_2006=40;
                    soccerTeams[i].score_2010=32;
                    soccerTeams[i].score_2007=17;
                    break;
                case 14:
                    soccerTeams[i].teanName="印尼";
                    soccerTeams[i].score_2006=50;
                    soccerTeams[i].score_2010=50;
                    soccerTeams[i].score_2007=9;
                    break;
            }
        }
        SoccerTeam minScoreIn2006=soccerTeams[0];
        SoccerTeam minScoreIn2007=soccerTeams[0];
        SoccerTeam minScoreIn2010=soccerTeams[0];
        SoccerTeam maxScoreIn2006=soccerTeams[0];
        SoccerTeam maxScoreIn2007=soccerTeams[0];
        SoccerTeam maxScoreIn2010=soccerTeams[0];
        for (int i=0;i<soccerTeams.length;i++) {
            if (soccerTeams[i].score_2006 <= minScoreIn2006.score_2006) {
                minScoreIn2006 = soccerTeams[i];
            }
            if (soccerTeams[i].score_2007 <= minScoreIn2007.score_2007) {
                minScoreIn2007 = soccerTeams[i];
            }
            if (soccerTeams[i].score_2010 <= minScoreIn2010.score_2010) {
                minScoreIn2010 = soccerTeams[i];
            }
            if (soccerTeams[i].score_2006 >= maxScoreIn2006.score_2006) {
                maxScoreIn2006 = soccerTeams[i];
            }
            if (soccerTeams[i].score_2007 >= maxScoreIn2007.score_2007) {
                maxScoreIn2007 = soccerTeams[i];
            }
            if (soccerTeams[i].score_2010 >= maxScoreIn2010.score_2010) {
                maxScoreIn2010 = soccerTeams[i];
            }
        }
        for (int i=0;i<soccerTeams.length;i++){
            // System.out.println(soccerTeams[i].teanName+"2006:"+soccerTeams[i].score_2006+" 2010:"+soccerTeams[i].score_2010+" 2007:"+soccerTeams[i].score_2007);
            soccerTeams[i].score_2006=(soccerTeams[i].score_2006-minScoreIn2006.score_2006)/(maxScoreIn2006.score_2006-minScoreIn2006.score_2006);
            soccerTeams[i].score_2010=(soccerTeams[i].score_2010-minScoreIn2010.score_2010)/(maxScoreIn2010.score_2010-minScoreIn2010.score_2010);
            soccerTeams[i].score_2007=(soccerTeams[i].score_2007-minScoreIn2007.score_2007)/(maxScoreIn2007.score_2007-minScoreIn2007.score_2007);
            //System.out.println(soccerTeams[i].teanName+"2006:"+soccerTeams[i].score_2006+" 2010:"+soccerTeams[i].score_2010+" 2007:"+soccerTeams[i].score_2007);
        }

    }
}

