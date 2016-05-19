package com.softech.ls360.lcms.contentbuilder.utils;

public interface WlcmsConstants {

	// Validation messages codes
	String VALIDATION_LINK_SUCCESS_CODE = "0001";
	String VALIDATION_LINK_SUCCESS_MESSAGE_KEY = "msgId";

	String[] INDUSTRY_LIST = new String[] { "All", "Business Skills", "Cosmetology Education", "Environmental Health & Safety", "Financial Services Education", "Food and Beverage Programs",
			"Healthcare Education","HR, Ethics, & Compliance", "Industrial Skills", "Insurance Education", "IT Certification", "IT and Software Skills" , "Power and Utilities", "Quality Management Education",
			"Real Estate Education", "Trades and Engineering", "Other"
	};

	//course types
	String PARAMETER_COURSE_TYPE = "cType";

	//publish statuses
	String PUBLISH_STATUS_NOT_STARTED = "Not Started";
	String PUBLISH_STATUS_PUBLISHED = "Published";
	String PUBLISH_STATUS_NOT_PUBLISHED = "Not Published";
	String PUBLISH_STATUS_CHANGES_NOT_PUBLISHED = "Changes Not Published";

	//course status 
	String COURSE_STATUS_ACTIVE = "Active";
	String COURSE_STATUS_RETIRED = "Retired";

	String COURSE_GROUP_CLASSROOM = "Classroom Courses";
	String COURSE_GROUP_WEBINAR = "Webinar Courses";

	//course rating
	String COURSE_RATING_PENDING = "Pending";

	//email
	String SUPPORT_360TRAINING_EMAIL_ADDRESS = "support@360training.com";
	String WEBINAR_SERVICE_PROVIDER_360TRAINING = "360training";

	public enum DeliveryMethod {
		SelfPaced(1), Classroom(7), Webinar(8);
		private Integer value;

		private DeliveryMethod(Integer value) {
			this.value = value;
		}

		public Integer getDeliveryMethod(){
			return value;
		}
	};
	int VISUAL_LEFT_CONSTATNT = 1;
	int VISUAL_RIGHT_CONSTATNT = 2;
	int VISUAL_TOP_CONSTATNT = 3;
	int VISUAL_BOTTOM_CONSTATNT = 4;
	int VIDEO_STRAMING_CONSTATNT = 1190;//On Prod Video Streaming Center = 1190 this will be fixed fully in sprint-2

}