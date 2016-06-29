package com.myresources.tuwenhunpai;

import java.io.Serializable;

public class Content implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String details;
	private boolean img; //�����ж��Ƿ���ͼƬ��ַ
	private boolean video_;//�����ж��Ƿ�����Ƶ��ַ
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public boolean isImg() {
		return img;
	}
	public void setImg(boolean img) {
		this.img = img;
	}
	public boolean isVideo_() {
		return video_;
	}
	public void setVideo_(boolean video_) {
		this.video_ = video_;
	}
	public Content(String details, boolean img, boolean video_) {
		super();
		this.details = details;
		this.img = img;
		this.video_ = video_;
	}
	
	
}
