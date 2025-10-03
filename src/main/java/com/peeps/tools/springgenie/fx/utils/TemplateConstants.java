package com.peeps.tools.springgenie.fx.utils;

public class TemplateConstants {

	public static final String authorName = "Prahalad";

	public static final String dbCommonFields = "@Column(name = \"status\")\r\n" + "	private String status;\r\n"
			+ "\r\n" + "	@Column(name = \"remarks\")\r\n" + "	private String remarks;\r\n" + "\r\n"
			+ "	@Column(name = \"created_by\")\r\n" + "	private String createdBy;\r\n" + "\r\n"
			+ "	@Column(name = \"created_on\")\r\n" + "	private LocalDateTime createdOn;\r\n" + "\r\n"
			+ "	@Column(name = \"updated_by\")\r\n" + "	private String updatedBy;\r\n" + "\r\n"
			+ "	@Column(name = \"updated_on\")\r\n" + "	private LocalDateTime updatedOn;";

}
