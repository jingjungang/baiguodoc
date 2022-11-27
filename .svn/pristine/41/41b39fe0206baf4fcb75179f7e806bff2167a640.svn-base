package com.ukang.baiyu.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.ukang.baiyu.entity.AppLink;
import com.ukang.baiyu.entity.Comment;
import com.ukang.baiyu.entity.CommonEntity;
import com.ukang.baiyu.entity.Conference;
import com.ukang.baiyu.entity.Department;
import com.ukang.baiyu.entity.DocNews;
import com.ukang.baiyu.entity.ImgInfo;
import com.ukang.baiyu.entity.Journal;
import com.ukang.baiyu.entity.MedChart;
import com.ukang.baiyu.entity.Medical;
import com.ukang.baiyu.entity.NewsDetail;
import com.ukang.baiyu.entity.PageInfo;
import com.ukang.baiyu.entity.ReadPic;
import com.ukang.baiyu.entity.Response;
import com.ukang.baiyu.entity.SearchDB;
import com.ukang.baiyu.entity.SearchNews;
import com.ukang.baiyu.entity.UserInfo;

/**
 * 数据解析器，解析返回的Json数据并将其转化为实体对象
 * @author SAN
 *
 */
public class DataParser {
	public static Response parseUserInfo(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj;
		try {
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONObject info = obj.getJSONObject("into");
				UserInfo user = new UserInfo();
				String token = info.getString("token");
				String nickName = info.getString("nickname");
				Constant.token = token;
				user.setMobile(info.getString("mobile"));
				user.setEmail(MWDUtils.replaceNull(info.getString("email")));
				user.setBirthday(MWDUtils.replaceNull(info.getString("birthday")));
				String sex = MWDUtils.replaceNull(info.getString("sex"));
				user.setSex(sex==null ? "1" : sex);
				user.setAvatar(MWDUtils.replaceNull(info.getString("avatar")));
				user.setNickname(MWDUtils.replaceNull(nickName));
				user.setHospital(MWDUtils.replaceNull(info.getString("hospital")));
				user.setSubject(MWDUtils.replaceNull(info.getString("subject")));
				user.setJob(MWDUtils.replaceNull(info.getString("job")));
				user.setEdu(MWDUtils.replaceNull(info.getString("edu")));
				Constant.userinfo = user;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析业内新闻
	 * @param jsonStr
	 * @return
	 */
	public static Response parseDocNews(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONObject infoObj = obj.getJSONObject("info");
				PageInfo pageInfo = new PageInfo();
				pageInfo.setPage(infoObj.getInt("page"));
				pageInfo.setTotalNum(infoObj.getInt("total_num"));
				response.setObj(pageInfo);
				JSONArray arr = infoObj.getJSONArray("info");
				List<DocNews> newsList = new ArrayList<DocNews>();
				for(int i = 0; i < arr.length(); i++){
					DocNews news = new DocNews();
					JSONObject item = arr.getJSONObject(i);
					news.setId(item.getInt("id"));
					news.setTitle(item.getString("title"));
					news.setDescription(item.getString("description"));
					news.setInputtime(item.getString("inputtime"));
					news.setThumb(item.getString("thumb"));
					news.setCopyfrom(item.getString("copyfrom"));
					newsList.add(news);
				}
				response.setList(newsList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析业内新闻详情
	 * @param jsonStr
	 * @return
	 */
	public static Response parseDocNewsDetail(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONObject infoObj = obj.getJSONObject("info");
				NewsDetail news = new NewsDetail();
				news.setId(infoObj.getString("id"));
				news.setTitle(infoObj.getString("title"));
				news.setDescription(infoObj.getString("description"));
				news.setInputtime(infoObj.getString("inputtime"));
				news.setContent(infoObj.getString("content"));
				news.setCopyfrom(infoObj.getString("copyfrom"));
				response.setObj(news);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析科室列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseMedicalList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONArray arr = obj.getJSONArray("info");
				List<Medical> mList = new ArrayList<Medical>();
				for(int i = 0; i < arr.length(); i++){
					Medical m = new Medical();
					JSONObject item = arr.getJSONObject(i);
					m.setId(item.getString("id"));
					m.setSubject(item.getString("subject"));
					mList.add(m);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析科室下的期刊列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseJournallList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONArray arr = obj.getJSONArray("info");
				List<Journal> mList = new ArrayList<Journal>();
				for(int i = 0; i < arr.length(); i++){
					Journal j = new Journal();
					JSONObject item = arr.getJSONObject(i);
					j.setTitle(item.getString("title"));
					j.setIssn(item.getString("issn"));
					j.setNlmid(item.getString("nlmid"));
					j.setCountry(item.getString("country"));
					j.setPic(item.getString("pic"));
					mList.add(j);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析最新咨讯学科分类
	 * @param jsonStr
	 * @return
	 */
	public static Response parseDeplList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONArray arr = obj.getJSONArray("info");
				List<List<Department>> mList = new ArrayList<List<Department>>();
				for(int i = 0; i < arr.length(); i++){
					List<Department> depList = new ArrayList<Department>();
					JSONObject item = arr.getJSONObject(i);
					@SuppressWarnings("unchecked")
					Iterator<String> it = item.keys();
					while(it.hasNext()){
						Department dep = new Department();
						String key = it.next();
						dep.setDepNameEn(key);
						dep.setDepNameCn(item.getString(key));
						depList.add(dep);
					}
					mList.add(depList);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析搜索列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseSearchDBList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				Integer total = obj.getInt("total");
				response.setObj(total);
				JSONArray arr = obj.getJSONArray("info");
				List<SearchDB> mList = new ArrayList<SearchDB>();
				for(int i = 0; i < arr.length(); i++){
					SearchDB s = new SearchDB();
					JSONObject item = arr.getJSONObject(i);
					s.setType(item.getInt("type"));
					s.setId(item.getString("id"));
					s.setTitle(item.getString("title"));
					try{
						s.setDescription(item.getString("description"));
					}catch(Exception e){
					}
					mList.add(s);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析搜索列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseSearchNewsList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONObject infoObj = obj.getJSONObject("info");
				Integer page = infoObj.getInt("page");
				Integer total = infoObj.getInt("total_num");
				JSONArray arr = infoObj.getJSONArray("infos");
				List<SearchNews> mList = new ArrayList<SearchNews>();
				for(int i = 0; i < arr.length(); i++){
					SearchNews s = new SearchNews();
					JSONObject item = arr.getJSONObject(i);
					s.setPmid(item.getString("pmid"));
					s.setPubdate(item.getString("pubdate"));
					s.setAuthorstr(item.getString("authorstr"));
					s.setTitle(item.getString("title"));
					s.setFulljournalname(item.getString("fulljournalname"));
					s.setIssn(item.getString("issn"));
					s.setEssn(item.getString("essn"));
					s.setNlmid(item.getString("nlmid"));
					mList.add(s);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析搜索列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseAppLink(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				Integer total = obj.getInt("total");
				response.setObj(total);
				JSONArray arr = obj.getJSONArray("info");
				List<AppLink> mList = new ArrayList<AppLink>();
				for(int i = 0; i < arr.length(); i++){
					AppLink s = new AppLink();
					JSONObject item = arr.getJSONObject(i);
					s.setDiylink_id(item.getString("diylink_id"));
					s.setDiylink_name(item.getString("diylink_name"));
					s.setDiylink_img(item.getString("diylink_img"));
					s.setDiylink_content(item.getString("diylink_content"));
					s.setAdmin_name(item.getString("admin_name"));
					s.setAdd_time(item.getString("add_time"));
					s.setDiylink_android(item.getString("add_time"));
					mList.add(s);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析会议列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseConferenceList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONObject infoObj = obj.getJSONObject("info");
				Integer total = infoObj.getInt("total");
				response.setObj(total);
				JSONArray arr = infoObj.getJSONArray("noticeList");
				List<Conference> mList = new ArrayList<Conference>();
				for(int i = 0; i < arr.length(); i++){
					Conference c = new Conference();
					JSONObject item = arr.getJSONObject(i);
					c.setID(item.getString("ID"));
					c.setNAME(item.getString("NAME"));
					c.setADDRESS(item.getString("ADDRESS"));
					c.setDATE_STR(item.getString("DATE_STR"));
					c.setURL(item.getString("URL"));
					c.setTYPE(item.getInt("TYPE"));
					mList.add(c);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析会议学科分类列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseConferenceCategory(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				JSONObject infoObj = obj.getJSONObject("info");
				JSONObject pinfoObj = infoObj.getJSONObject("pinfo");
				Iterator<String> it = pinfoObj.keys();
				//父元素集合
				List<CommonEntity> cList = new ArrayList<CommonEntity>();
				while(it.hasNext()){
					CommonEntity entity = new CommonEntity();
					String key = it.next();
					String value = pinfoObj.getString(key);
					entity.setStr1(key);
					entity.setStr2(value);
					cList.add(entity);
				}
				response.setList(cList);
				//解析子元素
				JSONObject cinfoObj = infoObj.getJSONObject("cinfo");
				Iterator<String> it2 = cinfoObj.keys();
				HashMap<String, List<CommonEntity>> cmap = new HashMap<String,List<CommonEntity>>();
				while(it2.hasNext()){
					String key = it2.next();
					JSONObject item = cinfoObj.getJSONObject(key);
					Iterator<String> it3 = item.keys();
					List<CommonEntity> childList = new ArrayList<CommonEntity>();
					while(it3.hasNext()){
						CommonEntity cc = new CommonEntity();
						String key2 = it3.next();
//						System.out.println(key2);
						try{
							String value2 = item.getString(key2);
							cc.setStr1(key2);
							cc.setStr2(value2);
							childList.add(cc);
						}catch(Exception ee){
							System.out.println(key2);
							ee.printStackTrace();
						}
					}
					cmap.put(key, childList);
				}
				response.setObj(cmap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析用户基本信息
	 * @param jsonStr
	 * @return
	 */
	public static Response parseAccountInfo(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj;
		try {
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			JSONObject info = obj.getJSONObject("info");
			if(response.getRet() == 1){
				UserInfo u = new UserInfo();
				u.setMobile(MWDUtils.replaceNull(info.getString("mobile")));
				u.setNickname(MWDUtils.replaceNull(info.getString("nickname")));
				u.setEmail(MWDUtils.replaceNull(info.getString("email")));
				u.setSex(MWDUtils.replaceNull(info.getString("sex")));
				u.setBirthday(MWDUtils.replaceNull(info.getString("birthday")));
				u.setHospital(MWDUtils.replaceNull(info.getString("hospital")));
				u.setSubject(MWDUtils.replaceNull(info.getString("subject")));
				u.setJob(MWDUtils.replaceNull(info.getString("job")));
				u.setEdu(MWDUtils.replaceNull(info.getString("edu")));
				try{
					u.setAvatar(info.getString("avatar"));
				}catch(Exception ee){}
				Constant.userinfo = u;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析病例列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseMedChartList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				Integer total = obj.getInt("total");
				response.setObj(total);
				JSONArray arr = obj.getJSONArray("info");
				List<MedChart> mList = new ArrayList<MedChart>();
				for(int i = 0; i < arr.length(); i++){
					MedChart s = new MedChart();
					JSONObject item = arr.getJSONObject(i);
					s.setId(item.getString("id"));
					s.setHcardnum(item.getString("hcardnum"));
					s.setBnum(MWDUtils.replaceNull(item.getString("bnum")));
					s.setPname(item.getString("pname"));
					s.setSnum(MWDUtils.replaceNull(item.getString("snum")));
					s.setZnum(MWDUtils.replaceNull(item.getString("znum")));
					s.setSex(MWDUtils.replaceNull(item.getString("sex")));
					s.setAge(MWDUtils.replaceNull(item.getString("age")));
					s.setFtime(MWDUtils.replaceNull(item.getString("ftime")));
					s.setBtime(MWDUtils.replaceNull(item.getString("btime")));
					s.setZs(MWDUtils.replaceNull(item.getString("zs")));
					s.setXdiagnosis(MWDUtils.replaceNull(item.getString("xdiagnosis")));
					s.setZdiagnosis(MWDUtils.replaceNull(item.getString("zdiagnosis")));
					s.setAddtime(MWDUtils.replaceNull(item.getString("addtime")));
					s.setUserid(MWDUtils.replaceNull(item.getString("userid")));
					s.setStatus(MWDUtils.replaceNull(item.getString("status")));
					if(s.getStatus().equals("0")){//1：显示；0：不显示
						continue;
					}
					try{
						JSONArray imgs = item.getJSONArray("imgs");
						List<ImgInfo> imgList = new ArrayList<ImgInfo>();
						for(int j = 0; j < imgs.length(); j++){
							String imgUrl = imgs.get(j).toString();
							ImgInfo img = new ImgInfo();
							img.setImgPath(imgUrl);
							imgList.add(img);
						}
						s.setImgs(imgList);
					}catch(Exception ee){}
					mList.add(s);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}

	public static Response parseAddMechart(String jsonStr) {
		// TODO Auto-generated method stub
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				Integer total = obj.getInt("total");
				response.setObj(total);
				JSONArray arr = obj.getJSONArray("info");
				List<AppLink> mList = new ArrayList<AppLink>();
				for(int i = 0; i < arr.length(); i++){
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析病例详情
	 * @param jsonStr
	 * @return
	 */
	public static Response parseMedChartDetail(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
//				Integer total = obj.getInt("total");
//				response.setObj(total);
//				JSONArray arr = obj.getJSONArray("info");
				MedChart s = new MedChart();
//				JSONObject item = arr.getJSONObject(i);
				s.setId(obj.getString("id"));
				s.setHcardnum(MWDUtils.replaceNull(obj.getString("hcardnum")));
				s.setBnum(MWDUtils.replaceNull(obj.getString("bnum")));
				s.setPname(obj.getString("pname"));
				s.setSnum(MWDUtils.replaceNull(obj.getString("snum")));
				s.setZnum(MWDUtils.replaceNull(obj.getString("znum")));
				s.setSex(MWDUtils.replaceNull(obj.getString("sex")));
				s.setAge(MWDUtils.replaceNull(obj.getString("age")));
				s.setFtime(MWDUtils.replaceNull(obj.getString("ftime")));
				s.setBtime(MWDUtils.replaceNull(obj.getString("btime")));
				s.setZs(MWDUtils.replaceNull(obj.getString("zs")));
				s.setXdiagnosis(MWDUtils.replaceNull(obj.getString("xdiagnosis")));
				s.setZdiagnosis(MWDUtils.replaceNull(obj.getString("zdiagnosis")));
				s.setAddtime(MWDUtils.replaceNull(obj.getString("addtime")));
				s.setUserid(MWDUtils.replaceNull(obj.getString("userid")));
				try{
					List<ImgInfo> imgList = new ArrayList<ImgInfo>();
					JSONObject imgs = obj.getJSONObject("imgs");
		            Iterator it = imgs.keys();  
		            while (it.hasNext()){
		            	ImgInfo img = new ImgInfo();
		                String key = (String) it.next();  
		                String value = imgs.getString(key);
		                img.setId(key);
		                img.setImgPath(value);
		                img.setNetImg(true);
		                imgList.add(img);
		            }  
					s.setImgs(imgList);
				}catch(Exception ee){
					ee.printStackTrace();
				}
				response.setObj(s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 添加评论
	 * @param jsonStr
	 * @return
	 */
	public static Response parseAddComment(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析评论列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseCommentList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				List<Comment> cmtList = new ArrayList<Comment>();
				JSONObject jo = obj.getJSONObject("info");
				JSONArray info = jo.getJSONArray("data");
				for(int i = 0; i < info.length(); i++){
					JSONObject item = info.getJSONObject(i);
					Comment cmt = new Comment();
//					cmt.setId(item.getString("id"));
//					cmt.setLinkid(item.getString("linkid"));
//					cmt.setContent(item.getString("content"));
//					cmt.setAdd_time(item.getString("add_time"));
//					cmt.setType(item.getString("type"));
//					cmt.setTitle(item.getString("title"));
//					cmt.setUsername(MWDUtils.replaceNull(item.getString("username")));
//					cmt.setAvatar(item.getString("avatar"));

					cmt.setUsername(MWDUtils.replaceNull(item.getString("username")));
					cmt.setAdd_time(item.getString("creat_at"));
					cmt.setContent(item.getString("content"));
					cmtList.add(cmt);
				}
				response.setList(cmtList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 解析评论列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseStoreList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				List<Comment> cmtList = new ArrayList<Comment>();
				JSONArray info = obj.getJSONObject("info").getJSONArray("info");
				for(int i = 0; i < info.length(); i++){
					JSONObject item = info.getJSONObject(i);
					Comment cmt = new Comment();
					cmt.setLinkid(item.getString("linkid"));
					cmt.setAdd_time(item.getString("add_time"));
					cmt.setType(item.getString("type"));
					cmt.setTitle(item.getString("title"));
					try{
						int type = Integer.parseInt(cmt.getType());
						switch (type) {
							case 1:
								type = 3;
								break;
							case 2:
								break;
							case 3:
								type = 1;
								break;
							default:
								break;
						}
						if(Constant.storeMap.containsKey(type)){
							Map<String, String> map = Constant.storeMap.get(type);
							map.put(cmt.getLinkid(), cmt.getLinkid());
						}else{
							Map<String, String> map = new HashMap<String, String>();
							map.put(cmt.getLinkid(), cmt.getLinkid());
							Constant.storeMap.put(type, map);
						}

					} catch (Exception e1){}
					cmtList.add(cmt);
				}
				response.setList(cmtList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析病例读片列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parseReadPicList(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				Integer total = obj.getInt("total");
				response.setObj(total);
				JSONArray arr = obj.getJSONArray("info");
				List<ReadPic> mList = new ArrayList<ReadPic>();
				for(int i = 0; i < arr.length(); i++){
					ReadPic s = new ReadPic();
					JSONObject item = arr.getJSONObject(i);
					s.setId(item.getString("id"));
					s.setAddtime(MWDUtils.replaceNull(item.getString("addtime")));
					s.setUserid(item.getString("userid"));
					s.setPresentation(MWDUtils.replaceNull(item.getString("presentation")));
					s.setTitle(MWDUtils.replaceNull(item.getString("title")));
					s.setUpdatetime(item.getString("updatetime"));
					try{
						JSONArray imgs = item.getJSONArray("imgs");
						List<ImgInfo> imgList = new ArrayList<ImgInfo>();
						for(int j = 0; j < imgs.length(); j++){
							String imgUrl = imgs.get(j).toString();
							ImgInfo img = new ImgInfo();
							img.setImgPath(imgUrl);
							imgList.add(img);
						}
						s.setImgs(imgList);
					}catch(Exception ee){}
					mList.add(s);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析临床路径列表
	 * @param jsonStr
	 * @return
	 */
	public static Response parsePathWay(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
				Integer total = obj.getInt("total");
				response.setObj(total);
				JSONArray arr = obj.getJSONArray("info");
				List<MedChart> mList = new ArrayList<MedChart>();
				for(int i = 0; i < arr.length(); i++){
//					MedChart s = new MedChart();
//					JSONObject item = arr.getJSONObject(i);
//					s.setId(item.getString("id"));
//					s.setHcardnum(item.getString("hcardnum"));
//					s.setBnum(MWDUtils.replaceNull(item.getString("bnum")));
//					s.setPname(item.getString("pname"));
//					s.setSnum(MWDUtils.replaceNull(item.getString("snum")));
//					s.setZnum(MWDUtils.replaceNull(item.getString("znum")));
//					s.setSex(MWDUtils.replaceNull(item.getString("sex")));
//					s.setAge(MWDUtils.replaceNull(item.getString("age")));
//					s.setFtime(MWDUtils.replaceNull(item.getString("ftime")));
//					s.setBtime(MWDUtils.replaceNull(item.getString("btime")));
//					s.setZs(MWDUtils.replaceNull(item.getString("zs")));
//					s.setXdiagnosis(MWDUtils.replaceNull(item.getString("xdiagnosis")));
//					s.setZdiagnosis(MWDUtils.replaceNull(item.getString("zdiagnosis")));
//					s.setAddtime(MWDUtils.replaceNull(item.getString("addtime")));
//					s.setUserid(MWDUtils.replaceNull(item.getString("userid")));
//					try{
//						JSONArray imgs = item.getJSONArray("imgs");
//						List<ImgInfo> imgList = new ArrayList<ImgInfo>();
//						for(int j = 0; j < imgs.length(); j++){
//							String imgUrl = imgs.get(j).toString();
//							ImgInfo img = new ImgInfo();
//							img.setImgPath(imgUrl);
//							imgList.add(img);
//						}
//						s.setImgs(imgList);
//					}catch(Exception ee){}
//					mList.add(s);
				}
				response.setList(mList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 解析读片详情
	 * @param jsonStr
	 * @return
	 */
	public static Response parseReadPicDetail(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
//				Integer total = obj.getInt("total");
//				response.setObj(total);
//				JSONArray arr = obj.getJSONArray("info");
				ReadPic s = new ReadPic();
//				JSONObject item = arr.getJSONObject(i);
				s.setId(obj.getString("id"));
				s.setTitle(obj.getString("title"));
				s.setPresentation(MWDUtils.replaceNull(obj.getString("presentation")));
				s.setAddtime(MWDUtils.replaceNull(obj.getString("addtime")));
				s.setUpdatetime(obj.getString("updatetime"));
				s.setUserid(MWDUtils.replaceNull(obj.getString("userid")));
				try{
					List<ImgInfo> imgList = new ArrayList<ImgInfo>();
					JSONObject imgs = obj.getJSONObject("imgs");
		            Iterator it = imgs.keys();  
		            while (it.hasNext()){
		            	ImgInfo img = new ImgInfo();
		                String key = (String) it.next();  
		                String value = imgs.getString(key);
		                img.setId(key);
		                img.setImgPath(value);
		                img.setNetImg(true);
		                imgList.add(img);
		            }  
					s.setImgs(imgList);
				}catch(Exception ee){
					ee.printStackTrace();
				}
				response.setObj(s);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 添加评论
	 * @param jsonStr
	 * @return
	 */
	public static Response parseFeedBack(String jsonStr){
		Response response = new Response();
		response.setRet(-1);
		JSONObject obj = null;
		try{
			obj = new JSONObject(jsonStr);
			response.setRet(obj.getInt("status"));
			if(response.getRet() == 1){
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}
}
