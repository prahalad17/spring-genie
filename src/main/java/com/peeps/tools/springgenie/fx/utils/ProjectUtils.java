package com.peeps.tools.springgenie.fx.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ProjectUtils {

	public static boolean isSpringBootProject(File folder) {
		if (folder == null || !folder.isDirectory())
			return false;

		File pom = new File(folder, "pom.xml");
		File gradle = new File(folder, "build.gradle");

		try {
			if (pom.exists()) {
				String content = Files.readString(pom.toPath());
				return content.contains("spring-boot-starter");
			} else if (gradle.exists()) {
				String content = Files.readString(gradle.toPath());
				return content.contains("spring-boot-starter");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static void listFilesRecursively(File folder) {
		if (folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				System.out.println(file.getAbsolutePath());
				listFilesRecursively(file);
			}
		}
	}

}
