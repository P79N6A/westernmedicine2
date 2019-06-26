package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.util;

import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueGroup;
import com.xywy.retrofit.demo.BaseRetrofitResponse;

import java.util.List;

import rx.Subscriber;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/4/5 16:42
 */

public class DataParserUtil {
    public static KeyValueGroup find(String id, List<KeyValueGroup> group) {
        for (KeyValueGroup key : group) {
            if (key.getId().equals(id)) {
                return key;
            }
        }
        return new KeyValueGroup();
    }


    public static void parseSex(BaseRetrofitResponse<List<KeyValueGroup>> subscriber) {
        KeyValueParser.parseOneLevel(DataParserUtil.SEX, subscriber);
    }

    public static void parseJobTitle(Subscriber subscriber) {
        KeyValueParser.parseOneLevel(DataParserUtil.JOB_TITLE, subscriber);
    }

    //解析科室数据
    public static void parseMajor(Subscriber subscriber) {
        KeyValueParser.parseTwoLevel(DataParserUtil.Major_DATA, DataParserUtil.Major_Sec_DATA, subscriber);
    }

    //解析地区数据
    public static void parseArea(Subscriber subscriber) {
        KeyValueParser.parseTwoLevel(DataParserUtil.PRO_DATA, DataParserUtil.city, subscriber);
    }

    //解析医生认证数据
    public static void parseState(Subscriber subscriber) {
        KeyValueParser.parseOneLevel(DataParserUtil.CHECK_STATE, subscriber);
    }

    public static final String CHECK_STATE = "{\n" +
            "\"-2\": \"未通过\",\n" +
//            "\"0\": \"待审核\",\n" +
//            "\"1\": \"已开通\",\n" +
            "\"0\": \"认证中\",\n" +
            "\"1\": \"已认证\",\n" +
            "\"2\": \"关闭\",\n" +
            "\"3\": \"暂时关闭\",\n" +
            "\"-1\": \"未认证\"\n" +
            "}";
    public static final String PRO_DATA = "{\n" +
            "    \"11\": \"北京市\",\n" +
            "    \"12\": \"天津市\",\n" +
            "    \"13\": \"河北省\",\n" +
            "    \"14\": \"山西省\",\n" +
            "    \"15\": \"内蒙古自治区\",\n" +
            "    \"21\": \"辽宁省\",\n" +
            "    \"22\": \"吉林省\",\n" +
            "    \"23\": \"黑龙江省\",\n" +
            "    \"31\": \"上海市\",\n" +
            "    \"32\": \"江苏省\",\n" +
            "    \"33\": \"浙江省\",\n" +
            "    \"34\": \"安徽省\",\n" +
            "    \"35\": \"福建省\",\n" +
            "    \"36\": \"江西省\",\n" +
            "    \"37\": \"山东省\",\n" +
            "    \"41\": \"河南省\",\n" +
            "    \"42\": \"湖北省\",\n" +
            "    \"43\": \"湖南省\",\n" +
            "    \"44\": \"广东省\",\n" +
            "    \"45\": \"广西壮族自治区\",\n" +
            "    \"46\": \"海南省\",\n" +
            "    \"50\": \"重庆市\",\n" +
            "    \"51\": \"四川省\",\n" +
            "    \"52\": \"贵州省\",\n" +
            "    \"53\": \"云南省\",\n" +
            "    \"54\": \"西藏自治区\",\n" +
            "    \"61\": \"陕西省\",\n" +
            "    \"62\": \"甘肃省\",\n" +
            "    \"63\": \"青海省\",\n" +
            "    \"64\": \"宁夏回族自治区\",\n" +
            "    \"65\": \"新疆维吾尔自治区\",\n" +
            "    \"71\": \"台湾省\",\n" +
            "    \"81\": \"香港\",\n" +
            "    \"82\": \"澳门\"\n" +
            "}";
    public static final String city = "{\n" +
            "    \"11\": {\n" +
            "        \"1101\": \"东城区\",\n" +
            "        \"1102\": \"西城区\",\n" +
            "        \"1103\": \"崇文区\",\n" +
            "        \"1104\": \"宣武区\",\n" +
            "        \"1105\": \"朝阳区\",\n" +
            "        \"1106\": \"丰台区\",\n" +
            "        \"1107\": \"石景山区\",\n" +
            "        \"1108\": \"海淀区\",\n" +
            "        \"1109\": \"门头沟区\",\n" +
            "        \"1111\": \"房山区\",\n" +
            "        \"1112\": \"通州区\",\n" +
            "        \"1113\": \"顺义区\",\n" +
            "        \"1114\": \"昌平区\",\n" +
            "        \"1115\": \"大兴区\",\n" +
            "        \"1116\": \"怀柔区\",\n" +
            "        \"1117\": \"平谷区\",\n" +
            "        \"1128\": \"密云县\",\n" +
            "        \"1129\": \"延庆县\"\n" +
            "    },\n" +
            "    \"12\": {\n" +
            "        \"1201\": \"和平区\",\n" +
            "        \"1202\": \"河东区\",\n" +
            "        \"1203\": \"河西区\",\n" +
            "        \"1204\": \"南开区\",\n" +
            "        \"1205\": \"河北区\",\n" +
            "        \"1206\": \"红桥区\",\n" +
            "        \"1207\": \"塘沽区\",\n" +
            "        \"1208\": \"汉沽区\",\n" +
            "        \"1209\": \"大港区\",\n" +
            "        \"1210\": \"东丽区\",\n" +
            "        \"1211\": \"西青区\",\n" +
            "        \"1212\": \"津南区\",\n" +
            "        \"1213\": \"北辰区\",\n" +
            "        \"1214\": \"武清区\",\n" +
            "        \"1215\": \"宝坻区\",\n" +
            "        \"1221\": \"宁河县\",\n" +
            "        \"1223\": \"静海县\",\n" +
            "        \"1225\": \"蓟县\"\n" +
            "    },\n" +
            "    \"13\": {\n" +
            "        \"1301\": \"石家庄市\",\n" +
            "        \"1302\": \"唐山市\",\n" +
            "        \"1303\": \"秦皇岛市\",\n" +
            "        \"1304\": \"邯郸市\",\n" +
            "        \"1305\": \"邢台市\",\n" +
            "        \"1306\": \"保定市\",\n" +
            "        \"1307\": \"张家口市\",\n" +
            "        \"1308\": \"承德市\",\n" +
            "        \"1309\": \"沧州市\",\n" +
            "        \"1310\": \"廊坊市\",\n" +
            "        \"1311\": \"衡水市\"\n" +
            "    },\n" +
            "    \"14\": {\n" +
            "        \"1401\": \"太原市\",\n" +
            "        \"1402\": \"大同市\",\n" +
            "        \"1403\": \"阳泉市\",\n" +
            "        \"1404\": \"长治市\",\n" +
            "        \"1405\": \"晋城市\",\n" +
            "        \"1406\": \"朔州市\",\n" +
            "        \"1407\": \"晋中市\",\n" +
            "        \"1408\": \"运城市\",\n" +
            "        \"1409\": \"忻州市\",\n" +
            "        \"1410\": \"临汾市\",\n" +
            "        \"1411\": \"吕梁市\"\n" +
            "    },\n" +
            "    \"15\": {\n" +
            "        \"1501\": \"呼和浩特市\",\n" +
            "        \"1502\": \"包头市\",\n" +
            "        \"1503\": \"乌海市\",\n" +
            "        \"1504\": \"赤峰市\",\n" +
            "        \"1505\": \"通辽市\",\n" +
            "        \"1506\": \"鄂尔多斯市\",\n" +
            "        \"1507\": \"呼伦贝尔市\",\n" +
            "        \"1508\": \"巴彦淖尔市\",\n" +
            "        \"1509\": \"乌兰察布市\",\n" +
            "        \"1522\": \"兴安盟\",\n" +
            "        \"1525\": \"锡林郭勒盟\",\n" +
            "        \"1529\": \"阿拉善盟\"\n" +
            "    },\n" +
            "    \"21\": {\n" +
            "        \"2101\": \"沈阳市\",\n" +
            "        \"2102\": \"大连市\",\n" +
            "        \"2103\": \"鞍山市\",\n" +
            "        \"2104\": \"抚顺市\",\n" +
            "        \"2105\": \"本溪市\",\n" +
            "        \"2106\": \"丹东市\",\n" +
            "        \"2107\": \"锦州市\",\n" +
            "        \"2108\": \"营口市\",\n" +
            "        \"2109\": \"阜新市\",\n" +
            "        \"2110\": \"辽阳市\",\n" +
            "        \"2111\": \"盘锦市\",\n" +
            "        \"2112\": \"铁岭市\",\n" +
            "        \"2113\": \"朝阳市\",\n" +
            "        \"2114\": \"葫芦岛市\"\n" +
            "    },\n" +
            "    \"22\": {\n" +
            "        \"2201\": \"长春市\",\n" +
            "        \"2202\": \"吉林市\",\n" +
            "        \"2203\": \"四平市\",\n" +
            "        \"2204\": \"辽源市\",\n" +
            "        \"2205\": \"通化市\",\n" +
            "        \"2206\": \"白山市\",\n" +
            "        \"2207\": \"松原市\",\n" +
            "        \"2208\": \"白城市\",\n" +
            "        \"2224\": \"延边州\"\n" +
            "    },\n" +
            "    \"23\": {\n" +
            "        \"2301\": \"哈尔滨市\",\n" +
            "        \"2302\": \"齐齐哈尔市\",\n" +
            "        \"2303\": \"鸡西市\",\n" +
            "        \"2304\": \"鹤岗市\",\n" +
            "        \"2305\": \"双鸭山市\",\n" +
            "        \"2306\": \"大庆市\",\n" +
            "        \"2307\": \"伊春市\",\n" +
            "        \"2308\": \"佳木斯市\",\n" +
            "        \"2309\": \"七台河市\",\n" +
            "        \"2310\": \"牡丹江市\",\n" +
            "        \"2311\": \"黑河市\",\n" +
            "        \"2312\": \"绥化市\",\n" +
            "        \"2327\": \"大兴安岭\"\n" +
            "    },\n" +
            "    \"31\": {\n" +
            "        \"3101\": \"黄浦区\",\n" +
            "        \"3104\": \"徐汇区\",\n" +
            "        \"3105\": \"长宁区\",\n" +
            "        \"3106\": \"静安区\",\n" +
            "        \"3107\": \"普陀区\",\n" +
            "        \"3108\": \"闸北区\",\n" +
            "        \"3109\": \"虹口区\",\n" +
            "        \"3110\": \"杨浦区\",\n" +
            "        \"3112\": \"闵行区\",\n" +
            "        \"3113\": \"宝山区\",\n" +
            "        \"3114\": \"嘉定区\",\n" +
            "        \"3115\": \"浦东新区\",\n" +
            "        \"3116\": \"金山区\",\n" +
            "        \"3117\": \"松江区\",\n" +
            "        \"3118\": \"青浦区\",\n" +
            "        \"3120\": \"奉贤区\",\n" +
            "        \"3130\": \"崇明县\"\n" +
            "    },\n" +
            "    \"32\": {\n" +
            "        \"3201\": \"南京市\",\n" +
            "        \"3202\": \"无锡市\",\n" +
            "        \"3203\": \"徐州市\",\n" +
            "        \"3204\": \"常州市\",\n" +
            "        \"3205\": \"苏州市\",\n" +
            "        \"3206\": \"南通市\",\n" +
            "        \"3207\": \"连云港市\",\n" +
            "        \"3208\": \"淮安市\",\n" +
            "        \"3209\": \"盐城市\",\n" +
            "        \"3210\": \"扬州市\",\n" +
            "        \"3211\": \"镇江市\",\n" +
            "        \"3212\": \"泰州市\",\n" +
            "        \"3213\": \"宿迁市\"\n" +
            "    },\n" +
            "    \"33\": {\n" +
            "        \"3301\": \"杭州市\",\n" +
            "        \"3302\": \"宁波市\",\n" +
            "        \"3303\": \"温州市\",\n" +
            "        \"3304\": \"嘉兴市\",\n" +
            "        \"3305\": \"湖州市\",\n" +
            "        \"3306\": \"绍兴市\",\n" +
            "        \"3307\": \"金华市\",\n" +
            "        \"3308\": \"衢州市\",\n" +
            "        \"3309\": \"舟山市\",\n" +
            "        \"3310\": \"台州市\",\n" +
            "        \"3311\": \"丽水市\"\n" +
            "    },\n" +
            "    \"34\": {\n" +
            "        \"3401\": \"合肥市\",\n" +
            "        \"3402\": \"芜湖市\",\n" +
            "        \"3403\": \"蚌埠市\",\n" +
            "        \"3404\": \"淮南市\",\n" +
            "        \"3405\": \"马鞍山市\",\n" +
            "        \"3406\": \"淮北市\",\n" +
            "        \"3407\": \"铜陵市\",\n" +
            "        \"3408\": \"安庆市\",\n" +
            "        \"3410\": \"黄山市\",\n" +
            "        \"3411\": \"滁州市\",\n" +
            "        \"3412\": \"阜阳市\",\n" +
            "        \"3413\": \"宿州市\",\n" +
            "        \"3414\": \"巢湖市\",\n" +
            "        \"3415\": \"六安市\",\n" +
            "        \"3416\": \"亳州市\",\n" +
            "        \"3417\": \"池州市\",\n" +
            "        \"3418\": \"宣城市\"\n" +
            "    },\n" +
            "    \"35\": {\n" +
            "        \"3501\": \"福州市\",\n" +
            "        \"3502\": \"厦门市\",\n" +
            "        \"3503\": \"莆田市\",\n" +
            "        \"3504\": \"三明市\",\n" +
            "        \"3505\": \"泉州市\",\n" +
            "        \"3506\": \"漳州市\",\n" +
            "        \"3507\": \"南平市\",\n" +
            "        \"3508\": \"龙岩市\",\n" +
            "        \"3509\": \"宁德市\"\n" +
            "    },\n" +
            "    \"36\": {\n" +
            "        \"3601\": \"南昌市\",\n" +
            "        \"3602\": \"景德镇市\",\n" +
            "        \"3603\": \"萍乡市\",\n" +
            "        \"3604\": \"九江市\",\n" +
            "        \"3605\": \"新余市\",\n" +
            "        \"3606\": \"鹰潭市\",\n" +
            "        \"3607\": \"赣州市\",\n" +
            "        \"3608\": \"吉安市\",\n" +
            "        \"3609\": \"宜春市\",\n" +
            "        \"3610\": \"抚州市\",\n" +
            "        \"3611\": \"上饶市\"\n" +
            "    },\n" +
            "    \"37\": {\n" +
            "        \"3701\": \"济南市\",\n" +
            "        \"3702\": \"青岛市\",\n" +
            "        \"3703\": \"淄博市\",\n" +
            "        \"3704\": \"枣庄市\",\n" +
            "        \"3705\": \"东营市\",\n" +
            "        \"3706\": \"烟台市\",\n" +
            "        \"3707\": \"潍坊市\",\n" +
            "        \"3708\": \"济宁市\",\n" +
            "        \"3709\": \"泰安市\",\n" +
            "        \"3710\": \"威海市\",\n" +
            "        \"3711\": \"日照市\",\n" +
            "        \"3712\": \"莱芜市\",\n" +
            "        \"3713\": \"临沂市\",\n" +
            "        \"3714\": \"德州市\",\n" +
            "        \"3715\": \"聊城市\",\n" +
            "        \"3716\": \"滨州市\",\n" +
            "        \"3717\": \"菏泽市\"\n" +
            "    },\n" +
            "    \"41\": {\n" +
            "        \"4101\": \"郑州市\",\n" +
            "        \"4102\": \"开封市\",\n" +
            "        \"4103\": \"洛阳市\",\n" +
            "        \"4104\": \"平顶山市\",\n" +
            "        \"4105\": \"安阳市\",\n" +
            "        \"4106\": \"鹤壁市\",\n" +
            "        \"4107\": \"新乡市\",\n" +
            "        \"4108\": \"焦作市\",\n" +
            "        \"4109\": \"濮阳市\",\n" +
            "        \"4110\": \"许昌市\",\n" +
            "        \"4111\": \"漯河市\",\n" +
            "        \"4112\": \"三门峡市\",\n" +
            "        \"4113\": \"南阳市\",\n" +
            "        \"4114\": \"商丘市\",\n" +
            "        \"4115\": \"信阳市\",\n" +
            "        \"4116\": \"周口市\",\n" +
            "        \"4117\": \"驻马店市\",\n" +
            "        \"4118\": \"济源市\"\n" +
            "    },\n" +
            "    \"42\": {\n" +
            "        \"4201\": \"武汉市\",\n" +
            "        \"4202\": \"黄石市\",\n" +
            "        \"4203\": \"十堰市\",\n" +
            "        \"4205\": \"宜昌市\",\n" +
            "        \"4206\": \"襄阳市\",\n" +
            "        \"4207\": \"鄂州市\",\n" +
            "        \"4208\": \"荆门市\",\n" +
            "        \"4209\": \"孝感市\",\n" +
            "        \"4210\": \"荆州市\",\n" +
            "        \"4211\": \"黄冈市\",\n" +
            "        \"4212\": \"咸宁市\",\n" +
            "        \"4213\": \"随州市\",\n" +
            "        \"4228\": \"恩施州\",\n" +
            "        \"4229\": \"仙桃市\",\n" +
            "        \"4230\": \"潜江市\",\n" +
            "        \"4231\": \"天门市\",\n" +
            "        \"4232\": \"神农架\",\n" +
            "        \"4233\": \"江汉油田\"\n" +
            "    },\n" +
            "    \"43\": {\n" +
            "        \"4301\": \"长沙市\",\n" +
            "        \"4302\": \"株洲市\",\n" +
            "        \"4303\": \"湘潭市\",\n" +
            "        \"4304\": \"衡阳市\",\n" +
            "        \"4305\": \"邵阳市\",\n" +
            "        \"4306\": \"岳阳市\",\n" +
            "        \"4307\": \"常德市\",\n" +
            "        \"4308\": \"张家界市\",\n" +
            "        \"4309\": \"益阳市\",\n" +
            "        \"4310\": \"郴州市\",\n" +
            "        \"4311\": \"永州市\",\n" +
            "        \"4312\": \"怀化市\",\n" +
            "        \"4313\": \"娄底市\",\n" +
            "        \"4331\": \"湘西\"\n" +
            "    },\n" +
            "    \"44\": {\n" +
            "        \"4401\": \"广州市\",\n" +
            "        \"4402\": \"韶关市\",\n" +
            "        \"4403\": \"深圳市\",\n" +
            "        \"4404\": \"珠海市\",\n" +
            "        \"4405\": \"汕头市\",\n" +
            "        \"4406\": \"佛山市\",\n" +
            "        \"4407\": \"江门市\",\n" +
            "        \"4408\": \"湛江市\",\n" +
            "        \"4409\": \"茂名市\",\n" +
            "        \"4412\": \"肇庆市\",\n" +
            "        \"4413\": \"惠州市\",\n" +
            "        \"4414\": \"梅州市\",\n" +
            "        \"4415\": \"汕尾市\",\n" +
            "        \"4416\": \"河源市\",\n" +
            "        \"4417\": \"阳江市\",\n" +
            "        \"4418\": \"清远市\",\n" +
            "        \"4419\": \"东莞市\",\n" +
            "        \"4420\": \"中山市\",\n" +
            "        \"4451\": \"潮州市\",\n" +
            "        \"4452\": \"揭阳市\",\n" +
            "        \"4453\": \"云浮市\"\n" +
            "    },\n" +
            "    \"45\": {\n" +
            "        \"4501\": \"南宁市\",\n" +
            "        \"4502\": \"柳州市\",\n" +
            "        \"4503\": \"桂林市\",\n" +
            "        \"4504\": \"梧州市\",\n" +
            "        \"4505\": \"北海市\",\n" +
            "        \"4506\": \"防城港市\",\n" +
            "        \"4507\": \"钦州市\",\n" +
            "        \"4508\": \"贵港市\",\n" +
            "        \"4509\": \"玉林市\",\n" +
            "        \"4510\": \"百色市\",\n" +
            "        \"4511\": \"贺州市\",\n" +
            "        \"4512\": \"河池市\",\n" +
            "        \"4513\": \"来宾市\",\n" +
            "        \"4514\": \"崇左市\"\n" +
            "    },\n" +
            "    \"46\": {\n" +
            "        \"4601\": \"海口市\",\n" +
            "        \"4602\": \"三亚市\",\n" +
            "        \"4603\": \"五指山市\",\n" +
            "        \"4604\": \"琼海市\",\n" +
            "        \"4605\": \"儋州市\",\n" +
            "        \"4607\": \"文昌市\",\n" +
            "        \"4608\": \"万宁市\",\n" +
            "        \"4609\": \"东方市\",\n" +
            "        \"4623\": \"定安县\",\n" +
            "        \"4624\": \"屯昌县\",\n" +
            "        \"4625\": \"澄迈县\",\n" +
            "        \"4626\": \"临高县\",\n" +
            "        \"4627\": \"白沙\",\n" +
            "        \"4628\": \"昌江\",\n" +
            "        \"4629\": \"乐东\",\n" +
            "        \"4630\": \"陵水\",\n" +
            "        \"4631\": \"保亭\",\n" +
            "        \"4632\": \"琼中\",\n" +
            "        \"4690\": \"洋浦经济开发区\"\n" +
            "    },\n" +
            "    \"50\": {\n" +
            "        \"5001\": \"万州区\",\n" +
            "        \"5002\": \"涪陵区\",\n" +
            "        \"5003\": \"渝中区\",\n" +
            "        \"5004\": \"大渡口区\",\n" +
            "        \"5005\": \"江北区\",\n" +
            "        \"5006\": \"沙坪坝区\",\n" +
            "        \"5007\": \"九龙坡区\",\n" +
            "        \"5008\": \"南岸区\",\n" +
            "        \"5009\": \"北碚区\",\n" +
            "        \"5010\": \"万盛区\",\n" +
            "        \"5011\": \"双桥区\",\n" +
            "        \"5012\": \"渝北区\",\n" +
            "        \"5013\": \"巴南区\",\n" +
            "        \"5014\": \"黔江区\",\n" +
            "        \"5015\": \"长寿区\",\n" +
            "        \"5016\": \"江津区\",\n" +
            "        \"5017\": \"合川区\",\n" +
            "        \"5019\": \"南川区\",\n" +
            "        \"5022\": \"綦江县\",\n" +
            "        \"5023\": \"潼南县\",\n" +
            "        \"5024\": \"铜梁县\",\n" +
            "        \"5025\": \"大足县\",\n" +
            "        \"5026\": \"荣昌县\",\n" +
            "        \"5027\": \"璧山县\",\n" +
            "        \"5028\": \"梁平县\",\n" +
            "        \"5029\": \"城口县\",\n" +
            "        \"5030\": \"丰都县\",\n" +
            "        \"5031\": \"垫江县\",\n" +
            "        \"5032\": \"武隆县\",\n" +
            "        \"5033\": \"忠县\",\n" +
            "        \"5034\": \"开县\",\n" +
            "        \"5035\": \"云阳县\",\n" +
            "        \"5036\": \"奉节县\",\n" +
            "        \"5037\": \"巫山县\",\n" +
            "        \"5038\": \"巫溪县\",\n" +
            "        \"5040\": \"石柱县\",\n" +
            "        \"5041\": \"秀山县\",\n" +
            "        \"5042\": \"酉阳县\",\n" +
            "        \"5043\": \"彭水县\",\n" +
            "        \"5083\": \"永川区\",\n" +
            "        \"5084\": \"北部新区\"\n" +
            "    },\n" +
            "    \"51\": {\n" +
            "        \"5101\": \"成都市\",\n" +
            "        \"5103\": \"自贡市\",\n" +
            "        \"5104\": \"攀枝花市\",\n" +
            "        \"5105\": \"泸州市\",\n" +
            "        \"5106\": \"德阳市\",\n" +
            "        \"5107\": \"绵阳市\",\n" +
            "        \"5108\": \"广元市\",\n" +
            "        \"5109\": \"遂宁市\",\n" +
            "        \"5110\": \"内江市\",\n" +
            "        \"5111\": \"乐山市\",\n" +
            "        \"5113\": \"南充市\",\n" +
            "        \"5114\": \"眉山市\",\n" +
            "        \"5115\": \"宜宾市\",\n" +
            "        \"5116\": \"广安市\",\n" +
            "        \"5117\": \"达州市\",\n" +
            "        \"5118\": \"雅安市\",\n" +
            "        \"5119\": \"巴中市\",\n" +
            "        \"5120\": \"资阳市\",\n" +
            "        \"5132\": \"阿坝州\",\n" +
            "        \"5133\": \"甘孜\",\n" +
            "        \"5134\": \"凉山\"\n" +
            "    },\n" +
            "    \"52\": {\n" +
            "        \"5201\": \"贵阳市\",\n" +
            "        \"5202\": \"六盘水市\",\n" +
            "        \"5203\": \"遵义市\",\n" +
            "        \"5204\": \"安顺市\",\n" +
            "        \"5205\": \"毕节市\",\n" +
            "        \"5206\": \"铜仁市\",\n" +
            "        \"5223\": \"黔西南\",\n" +
            "        \"5226\": \"黔东南\",\n" +
            "        \"5227\": \"黔南布\"\n" +
            "    },\n" +
            "    \"53\": {\n" +
            "        \"5301\": \"昆明市\",\n" +
            "        \"5303\": \"曲靖市\",\n" +
            "        \"5304\": \"玉溪市\",\n" +
            "        \"5305\": \"保山市\",\n" +
            "        \"5306\": \"昭通市\",\n" +
            "        \"5307\": \"丽江市\",\n" +
            "        \"5308\": \"普洱市\",\n" +
            "        \"5309\": \"临沧市\",\n" +
            "        \"5323\": \"楚雄\",\n" +
            "        \"5325\": \"红河\",\n" +
            "        \"5326\": \"文山\",\n" +
            "        \"5328\": \"西双版纳\",\n" +
            "        \"5329\": \"大理\",\n" +
            "        \"5331\": \"德宏\",\n" +
            "        \"5333\": \"怒江\",\n" +
            "        \"5334\": \"迪庆\"\n" +
            "    },\n" +
            "    \"54\": {\n" +
            "        \"5401\": \"拉萨市\",\n" +
            "        \"5421\": \"昌都地区\",\n" +
            "        \"5422\": \"山南地区\",\n" +
            "        \"5423\": \"日喀则地区\",\n" +
            "        \"5424\": \"那曲地区\",\n" +
            "        \"5425\": \"阿里地区\",\n" +
            "        \"5426\": \"林芝地区\"\n" +
            "    },\n" +
            "    \"61\": {\n" +
            "        \"6101\": \"西安市\",\n" +
            "        \"6102\": \"铜川市\",\n" +
            "        \"6103\": \"宝鸡市\",\n" +
            "        \"6104\": \"咸阳市\",\n" +
            "        \"6105\": \"渭南市\",\n" +
            "        \"6106\": \"延安市\",\n" +
            "        \"6107\": \"汉中市\",\n" +
            "        \"6108\": \"榆林市\",\n" +
            "        \"6109\": \"安康市\",\n" +
            "        \"6110\": \"商洛市\",\n" +
            "        \"6151\": \"杨凌示范区\"\n" +
            "    },\n" +
            "    \"62\": {\n" +
            "        \"6201\": \"兰州市\",\n" +
            "        \"6202\": \"嘉峪关市\",\n" +
            "        \"6203\": \"金昌市\",\n" +
            "        \"6204\": \"白银市\",\n" +
            "        \"6205\": \"天水市\",\n" +
            "        \"6206\": \"武威市\",\n" +
            "        \"6207\": \"张掖市\",\n" +
            "        \"6208\": \"平凉市\",\n" +
            "        \"6209\": \"酒泉市\",\n" +
            "        \"6210\": \"庆阳市\",\n" +
            "        \"6211\": \"定西市\",\n" +
            "        \"6212\": \"陇南市\",\n" +
            "        \"6229\": \"临夏\",\n" +
            "        \"6230\": \"甘南\"\n" +
            "    },\n" +
            "    \"63\": {\n" +
            "        \"6301\": \"西宁市\",\n" +
            "        \"6321\": \"海东\",\n" +
            "        \"6322\": \"海北\",\n" +
            "        \"6323\": \"黄南\",\n" +
            "        \"6325\": \"海南\",\n" +
            "        \"6326\": \"果洛\",\n" +
            "        \"6327\": \"玉树\",\n" +
            "        \"6328\": \"海西\"\n" +
            "    },\n" +
            "    \"64\": {\n" +
            "        \"6401\": \"银川市\",\n" +
            "        \"6402\": \"石嘴山市\",\n" +
            "        \"6403\": \"吴忠市\",\n" +
            "        \"6404\": \"固原市\",\n" +
            "        \"6405\": \"中卫市\"\n" +
            "    },\n" +
            "    \"65\": {\n" +
            "        \"6501\": \"乌鲁木齐市\",\n" +
            "        \"6502\": \"克拉玛依市\",\n" +
            "        \"6521\": \"吐鲁番地区\",\n" +
            "        \"6522\": \"哈密地区\",\n" +
            "        \"6523\": \"昌吉\",\n" +
            "        \"6527\": \"博尔塔拉\",\n" +
            "        \"6528\": \"巴音郭楞\",\n" +
            "        \"6529\": \"阿克苏地区\",\n" +
            "        \"6530\": \"克孜勒\",\n" +
            "        \"6531\": \"喀什地区\",\n" +
            "        \"6532\": \"和田地区\",\n" +
            "        \"6540\": \"伊犁\",\n" +
            "        \"6542\": \"塔城地区\",\n" +
            "        \"6543\": \"阿勒泰地区\",\n" +
            "        \"6590\": \"自治区直辖县级行政单位\"\n" +
            "    },\n" +
            "    \"71\": {\n" +
            "        \"7101\": \"台北市\",\n" +
            "        \"7102\": \"高雄市\",\n" +
            "        \"7103\": \"基隆市\",\n" +
            "        \"7104\": \"台中市\",\n" +
            "        \"7105\": \"台南市\",\n" +
            "        \"7106\": \"新竹市\",\n" +
            "        \"7107\": \"嘉义市\",\n" +
            "        \"7108\": \"台北县\",\n" +
            "        \"7109\": \"宜兰县\",\n" +
            "        \"7110\": \"新竹县\",\n" +
            "        \"7111\": \"桃园县\",\n" +
            "        \"7112\": \"苗栗县\",\n" +
            "        \"7113\": \"台中县\",\n" +
            "        \"7114\": \"彰化县\",\n" +
            "        \"7115\": \"南投县\",\n" +
            "        \"7116\": \"嘉义县\",\n" +
            "        \"7117\": \"云林县\",\n" +
            "        \"7118\": \"台南县\",\n" +
            "        \"7119\": \"高雄县\",\n" +
            "        \"7120\": \"屏东县\",\n" +
            "        \"7121\": \"台东县\",\n" +
            "        \"7122\": \"花莲县\",\n" +
            "        \"7123\": \"澎湖县\"\n" +
            "    },\n" +
            "    \"81\": {\n" +
            "        \"8101\": \"中西区\",\n" +
            "        \"8102\": \"东区\",\n" +
            "        \"8103\": \"九龙城区\",\n" +
            "        \"8104\": \"观塘区\",\n" +
            "        \"8105\": \"南区\",\n" +
            "        \"8106\": \"深水区\",\n" +
            "        \"8107\": \"湾仔区\",\n" +
            "        \"8108\": \"黄大仙区\",\n" +
            "        \"8109\": \"油尖旺区\",\n" +
            "        \"8110\": \"离岛区\",\n" +
            "        \"8111\": \"葵青区\",\n" +
            "        \"8112\": \"北区\",\n" +
            "        \"8113\": \"西贡区\",\n" +
            "        \"8114\": \"沙田区\",\n" +
            "        \"8115\": \"屯门区\",\n" +
            "        \"8116\": \"大埔区\",\n" +
            "        \"8117\": \"荃湾区\",\n" +
            "        \"8118\": \"元朗区\"\n" +
            "    },\n" +
            "    \"82\": {\n" +
            "        \"8201\": \"花地玛堂区\",\n" +
            "        \"8202\": \"圣安多尼堂区\",\n" +
            "        \"8203\": \"大堂区\",\n" +
            "        \"8204\": \"望德堂区\",\n" +
            "        \"8205\": \"风顺堂区\",\n" +
            "        \"8206\": \"嘉模堂区\",\n" +
            "        \"8207\": \"圣方济各堂区\"\n" +
            "    }\n" +
            "}";
    public static final String Major_DATA = "{\n" +
            "    \"1\": \"外科\",\n" +
            "    \"2\": \"内科\",\n" +
            "    \"3\": \"妇产科学\",\n" +
            "    \"4\": \"生殖中心\",\n" +
            "    \"5\": \"骨外科\",\n" +
            "    \"6\": \"眼科学\",\n" +
            "    \"7\": \"五官科\",\n" +
            "    \"8\": \"肿瘤科\",\n" +
            "    \"9\": \"口腔科学\",\n" +
            "    \"10\": \"皮肤性病科\",\n" +
            "    \"11\": \"男科\",\n" +
            "    \"12\": \"皮肤美容\",\n" +
            "    \"13\": \"烧伤科\",\n" +
            "    \"14\": \"精神心理科\",\n" +
            "    \"15\": \"中医学\",\n" +
            "    \"16\": \"中西医结合科\",\n" +
            "    \"17\": \"介入医学科\",\n" +
            "    \"18\": \"康复医学科\",\n" +
            "    \"19\": \"运动医学科\",\n" +
            "    \"20\": \"麻醉医学科\",\n" +
            "    \"22\": \"职业病科\",\n" +
            "    \"23\": \"营养科\",\n" +
            "    \"24\": \"病理科\",\n" +
            "    \"25\": \"医学影像科\",\n" +
            "    \"26\": \"感染中心\",\n" +
            "    \"27\": \"其他科室\",\n" +
            "    \"28\": \"儿科学\"\n" +
            "}";
    public static final String Major_Sec_DATA = "{\n" +
            "    \"1\": {\n" +
            "        \"29\": \"神经外科\",\n" +
            "        \"30\": \"功能神经外科\",\n" +
            "        \"31\": \"心血管外科\",\n" +
            "        \"32\": \"胸外科\",\n" +
            "        \"34\": \"整形科\",\n" +
            "        \"38\": \"乳腺外科\",\n" +
            "        \"39\": \"泌尿外科\",\n" +
            "        \"40\": \"肝胆外科\",\n" +
            "        \"41\": \"肛肠科\",\n" +
            "        \"42\": \"血管外科\",\n" +
            "        \"43\": \"微创外科\",\n" +
            "        \"44\": \"普外科\",\n" +
            "        \"201\": \"器官移植\",\n" +
            "        \"210\": \"综合外科\"\n" +
            "    },\n" +
            "    \"2\": {\n" +
            "        \"45\": \"普通内科\",\n" +
            "        \"46\": \"心血管内科\",\n" +
            "        \"47\": \"神经内科\",\n" +
            "        \"48\": \"消化内科\",\n" +
            "        \"49\": \"内分泌科\",\n" +
            "        \"50\": \"免疫科\",\n" +
            "        \"51\": \"呼吸内科\",\n" +
            "        \"52\": \"肾病内科\",\n" +
            "        \"53\": \"血液科\",\n" +
            "        \"54\": \"变态反应科\",\n" +
            "        \"55\": \"老年病科\",\n" +
            "        \"57\": \"高压氧科\"\n" +
            "    },\n" +
            "    \"3\": {\n" +
            "        \"59\": \"产科\",\n" +
            "        \"60\": \"妇科\",\n" +
            "        \"63\": \"妇科内分泌\",\n" +
            "        \"64\": \"妇泌尿科\",\n" +
            "        \"65\": \"产前检查科\",\n" +
            "        \"67\": \"妇产科\",\n" +
            "        \"69\": \"遗传咨询科\",\n" +
            "        \"70\": \"计划生育科\"\n" +
            "    },\n" +
            "    \"4\": {\n" +
            "        \"71\": \"生殖中心\"\n" +
            "    },\n" +
            "    \"5\": {\n" +
            "        \"74\": \"骨科\",\n" +
            "        \"76\": \"创伤骨科\",\n" +
            "        \"77\": \"手外科\",\n" +
            "        \"79\": \"脊柱外科\",\n" +
            "        \"80\": \"骨关节科\",\n" +
            "        \"82\": \"骨质疏松科\",\n" +
            "        \"84\": \"矫形骨科\"\n" +
            "    },\n" +
            "    \"6\": {\n" +
            "        \"86\": \"眼科\",\n" +
            "        \"87\": \"小儿眼科\",\n" +
            "        \"89\": \"眼底\",\n" +
            "        \"91\": \"角膜科\",\n" +
            "        \"92\": \"青光眼\",\n" +
            "        \"93\": \"白内障\",\n" +
            "        \"94\": \"眼外伤\",\n" +
            "        \"95\": \"眼眶及肿瘤\",\n" +
            "        \"96\": \"屈光\",\n" +
            "        \"97\": \"眼整形\"\n" +
            "    },\n" +
            "    \"7\": {\n" +
            "        \"109\": \"耳鼻喉科\",\n" +
            "        \"110\": \"头颈外科\"\n" +
            "    },\n" +
            "    \"8\": {\n" +
            "        \"113\": \"肿瘤外科\",\n" +
            "        \"114\": \"肿瘤妇科\",\n" +
            "        \"115\": \"放疗科\",\n" +
            "        \"116\": \"肿瘤康复科\",\n" +
            "        \"117\": \"肿瘤综合科\",\n" +
            "        \"118\": \"肿瘤内科\",\n" +
            "        \"119\": \"骨肿瘤科\"\n" +
            "    },\n" +
            "    \"9\": {\n" +
            "        \"98\": \"口腔科\",\n" +
            "        \"99\": \"颌面外科\",\n" +
            "        \"100\": \"正畸科\",\n" +
            "        \"101\": \"牙体牙髓科\",\n" +
            "        \"102\": \"牙周科\",\n" +
            "        \"103\": \"口腔粘膜科\",\n" +
            "        \"104\": \"儿童口腔科\",\n" +
            "        \"105\": \"口腔修复科\",\n" +
            "        \"106\": \"种植科\",\n" +
            "        \"107\": \"口腔预防科\",\n" +
            "        \"108\": \"口腔特诊科\"\n" +
            "    },\n" +
            "    \"10\": {\n" +
            "        \"120\": \"皮肤科\",\n" +
            "        \"121\": \"性病科\"\n" +
            "    },\n" +
            "    \"11\": {\n" +
            "        \"33\": \"男科\"\n" +
            "    },\n" +
            "    \"12\": {\n" +
            "        \"35\": \"皮肤美容\"\n" +
            "    },\n" +
            "    \"13\": {\n" +
            "        \"122\": \"烧伤科\"\n" +
            "    },\n" +
            "    \"14\": {\n" +
            "        \"36\": \"精神科\",\n" +
            "        \"37\": \"药物依赖科\",\n" +
            "        \"123\": \"心理咨询科\",\n" +
            "        \"124\": \"双相障碍科\",\n" +
            "        \"200\": \"司法鉴定科\"\n" +
            "    },\n" +
            "    \"15\": {\n" +
            "        \"125\": \"中医妇产科\",\n" +
            "        \"126\": \"中医儿科\",\n" +
            "        \"127\": \"中医骨科\",\n" +
            "        \"128\": \"中医皮肤科\",\n" +
            "        \"129\": \"中医内分泌\",\n" +
            "        \"130\": \"中医消化科\",\n" +
            "        \"131\": \"中医呼吸科\",\n" +
            "        \"132\": \"中医肾病内科\",\n" +
            "        \"133\": \"中医免疫内科\",\n" +
            "        \"134\": \"中医心内科\",\n" +
            "        \"135\": \"中医神经内科\",\n" +
            "        \"136\": \"中医肿瘤科\",\n" +
            "        \"137\": \"中医血液科\",\n" +
            "        \"138\": \"中医感染内科\",\n" +
            "        \"139\": \"中医肝病科\",\n" +
            "        \"140\": \"中医五官科\",\n" +
            "        \"141\": \"中医泌尿、男科\",\n" +
            "        \"142\": \"针灸科\",\n" +
            "        \"143\": \"中医按摩科\",\n" +
            "        \"144\": \"中医乳腺外科\",\n" +
            "        \"145\": \"中医外科\",\n" +
            "        \"146\": \"中医肛肠科\",\n" +
            "        \"147\": \"中医老年病科\",\n" +
            "        \"148\": \"中医科\",\n" +
            "        \"149\": \"中医眼科\",\n" +
            "        \"204\": \"中医内科\"\n" +
            "    },\n" +
            "    \"16\": {\n" +
            "        \"150\": \"中西医结合科\"\n" +
            "    },\n" +
            "    \"17\": {\n" +
            "        \"151\": \"介入医学科\"\n" +
            "    },\n" +
            "    \"18\": {\n" +
            "        \"152\": \"康复科\",\n" +
            "        \"153\": \"理疗科\"\n" +
            "    },\n" +
            "    \"19\": {\n" +
            "        \"154\": \"运动医学科\"\n" +
            "    },\n" +
            "    \"20\": {\n" +
            "        \"155\": \"疼痛科\",\n" +
            "        \"156\": \"麻醉科\"\n" +
            "    },\n" +
            "    \"22\": {\n" +
            "        \"158\": \"职业病科\"\n" +
            "    },\n" +
            "    \"23\": {\n" +
            "        \"159\": \"营养科\"\n" +
            "    },\n" +
            "    \"24\": {\n" +
            "        \"164\": \"病理科\"\n" +
            "    },\n" +
            "    \"25\": {\n" +
            "        \"160\": \"核医学科\",\n" +
            "        \"161\": \"放射科\",\n" +
            "        \"162\": \"超声科\",\n" +
            "        \"163\": \"医学影像科\"\n" +
            "    },\n" +
            "    \"26\": {\n" +
            "        \"165\": \"发热门诊\",\n" +
            "        \"166\": \"传染病科\",\n" +
            "        \"167\": \"艾滋病科\",\n" +
            "        \"168\": \"结核病科\",\n" +
            "        \"169\": \"肝病科\",\n" +
            "        \"170\": \"寄生虫\"\n" +
            "    },\n" +
            "    \"27\": {\n" +
            "        \"171\": \"急诊科\",\n" +
            "        \"172\": \"特色医疗科\",\n" +
            "        \"173\": \"干部诊疗科\",\n" +
            "        \"174\": \"重症监护室\",\n" +
            "        \"175\": \"特诊科\",\n" +
            "        \"176\": \"检验科\",\n" +
            "        \"177\": \"预防保健科\",\n" +
            "        \"178\": \"功能检查科\",\n" +
            "        \"179\": \"全科\",\n" +
            "        \"180\": \"药剂科\",\n" +
            "        \"181\": \"体检科\",\n" +
            "        \"182\": \"血透中心\",\n" +
            "        \"183\": \"实验中心\",\n" +
            "        \"184\": \"碎石中心\",\n" +
            "        \"185\": \"IMCC\",\n" +
            "        \"186\": \"护理咨询\",\n" +
            "        \"187\": \"ICU\",\n" +
            "        \"213\": \"PICC静脉导管门诊\",\n" +
            "        \"227\": \"热门疾病\"\n" +
            "    },\n" +
            "    \"28\": {\n" +
            "        \"58\": \"新生儿科\",\n" +
            "        \"61\": \"小儿消化内科\",\n" +
            "        \"66\": \"小儿心内科\",\n" +
            "        \"68\": \"小儿呼吸内科\",\n" +
            "        \"72\": \"小儿肾内科\",\n" +
            "        \"73\": \"小儿皮肤科\",\n" +
            "        \"75\": \"小儿营养保健科\",\n" +
            "        \"78\": \"小儿妇科\",\n" +
            "        \"81\": \"小儿神经内科\",\n" +
            "        \"83\": \"小儿感染科\",\n" +
            "        \"85\": \"小儿内分泌科\",\n" +
            "        \"88\": \"小儿心外科\",\n" +
            "        \"90\": \"小儿胸外科\",\n" +
            "        \"188\": \"儿科\",\n" +
            "        \"189\": \"小儿免疫科\",\n" +
            "        \"190\": \"小儿耳鼻喉\",\n" +
            "        \"191\": \"小儿血液科\",\n" +
            "        \"192\": \"小儿精神科\",\n" +
            "        \"193\": \"小儿外科\",\n" +
            "        \"194\": \"小儿骨科\",\n" +
            "        \"195\": \"小儿泌尿科\",\n" +
            "        \"196\": \"小儿神经外科\",\n" +
            "        \"197\": \"小儿整形科\",\n" +
            "        \"198\": \"小儿康复科\"\n" +
            "    }\n" +
            "}";

    public static final String JOB_TITLE = "{\n" +
            "    \"1\": \"主任医师\",\n" +
            "    \"2\": \"副主任医师\",\n" +
            "    \"3\": \"主治医师\",\n" +
            "    \"4\": \"总住院医师\",\n" +
            "    \"5\": \"住院医师\",\n" +
            "    \"28\": \"医师\"\n" +
            "}";

    //    public static final String JOB_TITLE = "{\n" +
//            "    \"1\": \"主任医师\",\n" +
//            "    \"2\": \"副主任医师\",\n" +
//            "    \"3\": \"主治医师\",\n" +
//            "    \"4\": \"总住院医师\",\n" +
//            "    \"5\": \"住院医师\",\n" +
//            "    \"6\": \"助产师\",\n" +
//            "    \"7\": \"助产士\",\n" +
//            "    \"8\": \"主任技师\",\n" +
//            "    \"9\": \"副主任技师\",\n" +
//            "    \"10\": \"主管技师\",\n" +
//            "    \"11\": \"技师\",\n" +
//            "    \"12\": \"主任药师\",\n" +
//            "    \"13\": \"副主任药师\",\n" +
//            "    \"14\": \"主管药师\",\n" +
//            "    \"15\": \"药师\",\n" +
//            "    \"16\": \"主任检验师\",\n" +
//            "    \"17\": \"副主任检验师\",\n" +
//            "    \"18\": \"主管检验师\",\n" +
//            "    \"19\": \"无\",\n" +
//            "    \"20\": \"检验师\",\n" +
//            "    \"21\": \"护士\",\n" +
//            "    \"22\": \"护师\",\n" +
//            "    \"23\": \"主管护师\",\n" +
//            "    \"24\": \"副主任护师\",\n" +
//            "    \"25\": \"主任护师\",\n" +
//            "    \"26\": \"其他\",\n" +
//            "    \"27\": \"助理医师\",\n" +
//            "    \"28\": \"医师\",\n" +
//            "    \"29\": \"药士\",\n" +
//            "    \"30\": \"技士\",\n" +
//            "    \"31\": \"检验士\"\n" +
//            "}";
    public static final String SEX = "{\n" +
            "    \"0\": \"男\",\n" +
            "    \"1\": \"女\"\n" +
            "}";
}