/*
 * EnderBot by codedcosmos
 *
 * EnderBot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * EnderBot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License 3 for more details.
 * You should have received a copy of the GNU General Public License 3
 * along with EnderBot.  If not, see <https://www.gnu.org/licenses/>.
 */
package codedcosmos.enderbot.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
	
	public static void zip(String source, String zipname) throws IOException {
		// Helpful console message
		Log.print("Creating zip file '" + zipname + "' for folder '" + source + "'");

		// Verify path exists
		File pathAsFile = new File(source);
		if (!pathAsFile.exists() || pathAsFile.isFile()) {
			throw new IOException("Source path '" + source + "' does not exist or is a file!");
		}

		// Zip
		Path p = Files.createFile(Paths.get(zipname));

		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
			Path pp = Paths.get(source);
			Files.walk(pp)
				.filter(path -> !Files.isDirectory(path))
				.forEach(path -> {
					ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
					try {
						zs.putNextEntry(zipEntry);
						Files.copy(path, zs);
						zs.closeEntry();
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
		}
	}

	public static void clearDirectory(String dir) throws IOException {
		// Helpful console message
		Log.print("Deleting files in directory '" + dir + "'");

		// Verify path exists
		File pathAsFile = new File(dir);
		if (!pathAsFile.exists() || pathAsFile.isFile()) {
			throw new IOException("Source path '" + dir + "' does not exist or is a file!");
		}

		// Delete files
		for(File file: pathAsFile.listFiles()) {
			Log.print("Deleting '" + file.getAbsoluteFile() + "'");
			file.delete();
		}
	}

	public static boolean doesFileExist(String file) {
		File check = new File(file);
		return (check.exists() && !check.isDirectory());
	}

	public static void mkdir(String path) {
		File file = new File(path);
		file.mkdir();
	}

	public static void mkdirIfMissing(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) return;

		file.mkdir();
	}

	public static String getFirstZip(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".zip")) {
				return listOfFiles[i].getAbsoluteFile().toString();
			}
		}
		return "";
	}
}
