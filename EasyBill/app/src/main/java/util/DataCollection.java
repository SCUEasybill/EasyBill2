package util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by anquan on 2016/3/12.
 */
public class DataCollection {

    //user表的数据
    public List<String> getUserDates() {
        List<String> userDatas = new ArrayList<>();
        userDatas.add("insert into user values('666666', '安埋', 'anmai.png', '男', '1994.12.23', '123456', '18030402604', '41573432@qq.com', '1000');");
        return userDatas;
    }

    //account表的数据
    public List<String> getAccountDates() {
        List<String> accountDatas = new ArrayList<>();
        accountDatas.add("insert into account values(null, '666666', '支付宝', '18030402604', 999);");
        accountDatas.add("insert into account values(null, '666666', '工行卡', '18030402604', 888);");
        accountDatas.add("insert into account values(null, '666666', '建行卡', '18030402604', 777);");
        accountDatas.add("insert into account values(null, '666666', 'VISA', '18030402604', 666);");
        accountDatas.add("insert into account values(null, '666666', '其他', '18030402604', 555);");
        return accountDatas;
    }

    //label表的数据
    public List<String> getLabelDatas() {
        List<String> labelDatas = new ArrayList<>();
        labelDatas.add("insert into label values(null, '666666', 1, 1, 'cate_book', '书籍')");
        labelDatas.add("insert into label values(null, '666666', 1, 2, 'cate_clothes', '衣服')");
        labelDatas.add("insert into label values(null, '666666', 1, 3, 'cate_commodity', '日用')");
        labelDatas.add("insert into label values(null, '666666', 1, 4, 'cate_communication', '住宿')");
        labelDatas.add("insert into label values(null, '666666', 1, 5, 'cate_food', '食物')");
        labelDatas.add("insert into label values(null, '666666', 1, 6, 'cate_gift', '礼物')");
        labelDatas.add("insert into label values(null, '666666', 1, 7, 'cate_health', '健康')");
        labelDatas.add("insert into label values(null, '666666', 1, 8, 'cate_milk', '牛奶')");
        labelDatas.add("insert into label values(null, '666666', 1, 9, 'cate_play', '玩耍')");
        labelDatas.add("insert into label values(null, '666666', 1, 10, 'cate_recreation', '饮品')");
        labelDatas.add("insert into label values(null, '666666', 1, 11, 'cate_shopping', '购物')");
        labelDatas.add("insert into label values(null, '666666', 1, 12, 'cate_sport', '运动')");
        labelDatas.add("insert into label values(null, '666666', 1, 0, 'cate_stationery', '文具')");
        labelDatas.add("insert into label values(null, '666666', 0, 0, 'cate_traffic', '交通')");
        labelDatas.add("insert into label values(null, '666666', 1, 0, 'cate_travel', '旅游')");
        return labelDatas;
    }

    //label的图标编号集
    public HashMap<Integer, String> getCategory() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "cate_book");
        map.put(2, "cate_clothes");
        map.put(3, "cate_commodity");
        map.put(4, "cate_communication");
        map.put(5, "cate_food");
        map.put(6, "cate_gift");
        map.put(7, "cate_health");
        map.put(8, "cate_milk");
        map.put(9, "cate_play");
        map.put(10, "cate_recreation");
        map.put(11, "cate_shopping");
        map.put(12, "cate_sport");
        map.put(13, "cate_stationery");
        map.put(14, "cate_traffic");
        map.put(15, "cate_travel");
        return map;
    }

    //bugget表数据
    public List<String> getBuggetDatas() {
        List<String> list = new ArrayList<>();
        list.add("insert into bugget values(null, '666666', '2016-01-01', '2016-03-15', 1000, 900, 0)");
        list.add("insert into bugget values(null, '666666', '2016-01-02', '2016-03-01', 999, 888, 0)");
        return list;
    }

    //debt表数据
    public List<String> getDebtDatas() {
        List<String> list = new ArrayList<>();
        list.add("insert into debt values(null, '666666', 0, 150, '张三', '2016-01-03', '2016-02-03', '2016-01-26', 0, '张三修自行车')");
        list.add("insert into debt values(null, '666666', 0, 60, '李四', '2015-11-11', '2015-12-14', '2016-11-30', 0, '李四买东西')");
        return list;
    }

    //dream表数据
    public List<String> getDreamDatas() {
        List<String> list = new ArrayList<>();
        list.add("insert into dream values(null, '666666', '青城山之旅', '和王五一起去青城山玩', 600, 400, '2016-02-04', '2016-04-03', 0)");
        list.add("insert into dream values(null, '666666', '峨眉山一日游', '和吴六一起去峨眉爬山', 800, 900, '2015-10-05', '2015-11-18', 1)");
        return list;
    }

    //project表数据
    public List<String> getProjectDatas() {
        List<String> list = new ArrayList<>();
        list.add("insert into dream values(null, '666666', '大创项目', 4000, 1500, '2016-02-04', '2016-04-03', 1, '基于安卓的项目资金')");
        return list;
    }
}
