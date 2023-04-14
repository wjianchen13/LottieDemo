package com.example.lottiedemo.rich.ems;

import android.text.TextUtils;

import com.example.lottiedemo.rich.other.ObjectUtils;

import org.json.JSONObject;

public class ExpressionBean {

	/**
	 * 表情id
	 */
	private int id;

	/**
	 * 0:普通表情 1:DVIP表情
	 */
	private int type;

	/**
	 * 资源图片id，只有删除类型有
	 */
    private int mResId;

	/**
	 * 标识，[em:12]
	 */
	private String mContent = "";

	/**
	 * 名称，比如，开心，颓废
	 */
	private String name = "";

	/**
	 * 图片地址
	 */
	private String image = "";

	/**
	 * json路径，zip格式，https:\/\/tapi.{domain}\/upload\/down\/emo\/7\/7.zip
	 */
	private String json = "";

	/**
	 * svga路径，https:\/\/tapi.{domain}\/upload\/down\/emo\/7\/emo_7.svga
	 */
	private String svga = "";

	/**
	 * 不展示，仅仅用来解析，比如骰子的2,3,4,5,6几个点数
	 */
	private boolean isHide;

	public ExpressionBean(JSONObject obj, boolean isVip){
		if (ObjectUtils.isNotEmpty(obj)){
			id = obj.optInt("id", 0);
			mContent = "[em:" + id + "]";
			this.type = isVip ? 1 : 0;

			name = obj.optString("name", "");
			image = obj.optString("image", "");
			json = obj.optString("json", "");
			svga = obj.optString("svga", "");
			isHide = obj.optInt("isHide", 0) > 0;
		}
	}

	public ExpressionBean(){

	}

	public static ExpressionBean empty(){
		ExpressionBean bean = new ExpressionBean();
		bean.setId(-1);
		bean.setName("??");
		bean.setHide(true);
		return bean;
	}

	public int getResId() {
		return mResId;
	}

	public void setResId(int resId) {
		mResId = resId;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		mContent = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isVip(){
		return getType() == 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean hasImage(){
		return !TextUtils.isEmpty(getImage());
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public boolean hasJson(){
		return !TextUtils.isEmpty(getJson());
	}

	public String getSvga() {
		return svga;
	}

	public void setSvga(String svga) {
		this.svga = svga;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean hide) {
		isHide = hide;
	}

	@Override
	public String toString() {
		return "ExpressionBean{" +
				"id=" + id +
				", type=" + type +
				", mResId=" + mResId +
				", mContent='" + mContent + '\'' +
				", name='" + name + '\'' +
				", image='" + image + '\'' +
				", json='" + json + '\'' +
				", svga='" + svga + '\'' +
				", isHide=" + isHide +
				'}';
	}
}
