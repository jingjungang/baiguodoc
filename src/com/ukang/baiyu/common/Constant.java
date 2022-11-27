package com.ukang.baiyu.common;

/**
 * 静态变量对比表
 * 景俊钢
 * 2016年5月20日 17:16:26
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ukang.baiyu.entity.NewsDetail;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.UserInfo;
import com.ukang.baiyu.entity.Users;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;

@SuppressLint("UseSparseArrays")
public class Constant {

	// 对比版本号
	public static final String VERSION_CODE = "1.1.0";
	public static final int CODE = 110;
	public static Boolean isNewVersion = false;
	public static boolean ISDEBUG = true;
	// 游客
	public static final String GUEST_MOBILE = "13311036301";
	public static final String GUEST_PASSWORD = "123456";
	public static String sessionId = "";
	public static String token = null;// 登录验证秘药
	// 第一次进入app
	public static Boolean firstLoad = false;
	public static String NULL = "";
	public static String UTF = "utf-8";
	public static Bitmap bitmap;
	public static DisplayMetrics metrics;
	public static Boolean pushFlag;

	// 保存用户名密码的用户实体类
	public static Users users;
	// 用户基本信息
	public static UserInfo userinfo = null;
	// 当前设备id
	public static String deviceId;
	// 扫码设备id
	public static String scanDevId;
	// 当前设备运行时间
	public static String runDay;
	// 用来保存列表总条数的map
	public static HashMap<Integer, Integer> totalMap = new HashMap<Integer, Integer>();
	public static final int PAGE_SIZE = 15;
	public static Map<Integer, Map<String, String>> storeMap = new HashMap<Integer, Map<String, String>>();

	public static final String USER_AGENT = "Android;Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.2.2000 Chrome/30.0.1599.101 Safari/537.36";
	public static final String AUTH_ADDR = "https://passport-test.wiwide.com/auth/mobile";// 获取auth的url地址

	/*
	 * 
	 * 下面是接口地址*********
	 */

	public static final String HOST = "http://yd.baiyu.cn/";// host
	public static final String HOST_ADDR = "http://yd.baiyu.cn/api.php";// host_ADDR
	// public static final String HOST_ADDR =
	// "http://192.168.88.121/baiyu/api.php";
	public static final String APKurl = "http://yd.baiyu.cn/api.php/version/index"; // 版本更新请求
	public static String APK_URL = "http://yd.baiyu.cn/index.php/apkdown/doctor/1"; // apk地址

	// public static final String HOST_ADDR =
	// "http://wom.sys.wiwide.com/mobile/v2";//release host
	public static String REGISTER_URL = HOST_ADDR + "/user/newregister"; // 注册
	public static String LOGIN_URL = HOST_ADDR + "/user/newlogin";// 登陆
	public static final String LOGOUT_URL = HOST_ADDR + "/main/logout"; // 登出
	public static final String DOCTOR_NEWS_URL = HOST_ADDR + "/pubmed/";
	public static final String DOCTOR_NEWS_DETAIL_URL = HOST_ADDR
			+ "/pubmed/newsshow";
	public static final String UPLOAD_IMG_URL = HOST_ADDR + "/user/newavatar";// 上传头像
	public static final String LUNBO_URL = HOST_ADDR + "/consultation/lists";// 轮播
	public static List<Map<String, String>> LunBoList;// 轮播数据集合

	public static List<Map<String, String>> getLunBoList() {
		return LunBoList;
	}

	public static void setLunBoList(List<Map<String, String>> lunBoList) {
		LunBoList = lunBoList;
	}

	public static final String UPDATE_PWD_URL = HOST_ADDR // 更新密码
			+ "/user/newupdatePass";
	public static final String SAVE_BASEINFOS_AFTER_REGISET = HOST_ADDR// 注册完善信息
			+ "/user/perfectinfo";
	public static final String REGISTER_YZM_URL = HOST_ADDR // 注册验证码
			+ "/password/newsendcode";
	public static final String YZM_URL = HOST_ADDR + "/password/newindex"; // 找回密码-验证码
	public static final String YZM_PWD_URL = HOST_ADDR + "/password/newupdates"; // 找回密码

	public static final String REPORT_NEWS_URL = HOST_ADDR + "/pubmed/reports";// 资讯
	public static final String REPORT_DEPARTMENTS_URL = HOST_ADDR// 资讯学科分类
			+ "/pubmed/departments";
	public static final String NEWS_CMT_LIST_URL = HOST_ADDR// 资讯评论列表
			+ "/pubmed/comment";
	public static final String MEDICAL_LIST_URL = HOST_ADDR + "/pubmed/medical";// 科室列表
	public static final String JOURAL_LIST_URL = HOST_ADDR// 科室下对应的期刊信息
			+ "/pubmed/journallist";
	public static final String NEWS_SEARCH_URL = HOST_ADDR + "/pubmed/search";// 文献搜索
	public static final String DATABASE_SEARCH_URL = HOST_ADDR// 数据库搜索
			+ "/pubmed/searchdo";
	public static final String CONFERENCE_LIST_URL = HOST_ADDR + "/conference";// 会议列表
	public static final String CONFERENCE_CATEGORY_URL = HOST_ADDR// 会议分类列表
			+ "/conference/category";
	public static final String CONFERENCE_CMT_LIST_URL = HOST_ADDR// 会议评论列表
			+ "/conference/meetingreport";
	public static final String APP_LIST = HOST_ADDR + "/doctorApp";// 应用列表
	public static final String SEARCH_APP_LIST = HOST_ADDR + "/doctorApp";// 应用列表
	public static final String USER_INFO_URL_1 = HOST_ADDR + "/user/newindex";// 用户基本信息
	public static final String UPDATE_USER_URL = HOST_ADDR + "/user/update";// 用户基本信息-更新
	public static final String MEDCHART_LIST_URL = HOST_ADDR// 病例列表
			+ "/medicalphoto/index";
	public static final String ADD_MEDCHART_URL = HOST_ADDR// 添加病例
			+ "/medicalphoto/add";
	public static final String EDIT_MEDCHART_URL = HOST_ADDR// 修改病例
			+ "/medicalphoto/edit";
	public static final String MEDCHART_DETAIL_URL = HOST_ADDR// 病例详情
			+ "/medicalphoto/show";
	public static final String MEDCHART_DELETE_URL = HOST_ADDR// 病例删除
			+ "/medicalphoto/delet";
	public static final String COMMENT_LIST_URL = HOST_ADDR + "/pubmed/comment";// 列表评论
	public static final String MY_COMMENT_LIST_URL = HOST_ADDR// 我的列表评论
			+ "/pubmed/mycomment";
	public static final String ADD_COMMENT_URL = HOST_ADDR// 添加评论
			+ "/pubmed/addcomment";
	public static final String READ_PIC_LIST_URL = HOST_ADDR// 读片-列表
			+ "/filmreading/index";
	public static final String READ_PIC_DETAIL_URL = HOST_ADDR// 读片-详情
			+ "/filmreading/show";
	public static final String ADD_READ_PIC_URL = HOST_ADDR// 读片-添加
			+ "/filmreading/add";
	public static final String EDIT_READ_PIC_URL = HOST_ADDR // 读片-修改
			+ "/filmreading/edit";
	public static final String READ_PIC_CMT_LIST_URL = HOST_ADDR // 读片评论列表
			+ "/filmreading/index";
	public static final String PATH_WAY_LIST_URL = HOST_ADDR + "/pathway/index";// 临床路径列表
	public static final String ADD_FEED_BACK_URL = HOST_ADDR // 意见反馈
			+ "/feedback/index";
	public static final String ADD_STORE_URL = HOST_ADDR + "/collection"; // 收藏-添加
	public static final String DEL_STORE_URL = HOST_ADDR + "/collection/delete";// 收藏-取消
	public static final String STORE_LIST_URL = HOST_ADDR + "/collection/lists"; // 收藏-列表
	public static final String DIAN_ZAN_URL = HOST_ADDR + "/conference/zambia";// 点赞
	public static final String ZAN_TOTAL_URL = HOST_ADDR// 获取点赞数量
			+ "/conference/getzambia";
	public static final String MY_PATIENT_URL = HOST_ADDR + "/doctor_mypatient";// 患者列表
	public static final String USER_INFOS_URL = HOST_ADDR// 患者基本信息
			+ "/doctor_mypatient/show";
	public static final String ADD_PATIENT_URL = HOST_ADDR// 患者信息-添加
			+ "/doctor_mypatient/addcase";
	public static final String UPDATE_PATIENT_URL = HOST_ADDR// 患者信息-修改
			+ "/doctor_mypatient/edit";
	public static final String SCIENCE_NOTICE = HOST_ADDR + "/doctor/getnotice";// 学术科研-公告
	// 我的患者提问列表 未读咨询
	public static final String messagenoread = HOST_ADDR
			+ "/doctor_mypatient/messagenoread";

	/*
	 * 
	 * 下面是实体变量缓存**********************
	 */

	/** 当前查看的新闻 */
	public static NewsDetail curtNews;
	/** 业内新闻RESP */
	public static Response docNewsResp;
	/** 最新资讯RESP */
	public static Response reportNewsResp;
	/** 科室列表RESP */
	public static Response medicalResp;
	/** 期刊列表RESP */
	public static Response journalListResp;
	/** 学科分类列表 */
	public static Response depListResp;
	/** 保存学科新闻列表添加的关键字 */
	public static Map<String, String> newsAddKey = new HashMap<String, String>();
	/** 保存会议学科里诶包添加的关键字 */
	public static Map<String, String> conferenceAddKey = new HashMap<String, String>();
	/** 保存学科新闻列表删除的关键字 */
	public static Map<String, String> newsDelKey = new HashMap<String, String>();
	/** 保存学科新闻列表删除的关键字 */
	public static Map<String, String> conferenceDelKey = new HashMap<String, String>();
	/** 保存新闻学科列表<学科英文名, 新闻列表数据> */
	public static Map<String, Response> newsMap = new HashMap<String, Response>();
	/** 保存会议列表<学科id, 列表数据> */
	public static Map<String, Response> conferenceMap = new HashMap<String, Response>();
	/** 保存会议学科列表 */
	public static Response conferenceCategoryResp;
	/** 数据库搜索，all的数据 */
	public static Response searchAllResp;
	/** 文献搜索的数据 */
	public static Response searchArticleResp;
	/** 推荐应用RESP */
	public static Response appsResp;
	/** 病例列表RESP */
	public static Response medChartListResp;
	/** 病例详情RESP */
	public static Response medchartResp;
	/** 收藏列表RESP */
	public static Response storeListResp;
	/** 评论列表RESP */
	public static Response cmtListResp;
	/** 读片RESP */
	public static Response readPicListResp;
	/** 读片详情RESP */
	public static Response readpicResp;

	public static String getImagePath() {
		return Environment.getExternalStorageDirectory().toString() + "/mwd/";
	}

}
