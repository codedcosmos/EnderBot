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

import codedcosmos.enderbot.core.ConfigManager;
import codedcosmos.enderbot.discord.GuildContext;
import codedcosmos.enderbot.discord.Guilds;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleDrive {
	public static final String EXPORT_LOG_PATH = "plugins/EnderBot/";
	public static final String EXPORT_LOG = EXPORT_LOG_PATH+"archive_log.txt";

	public static final String DATE_FORMAT = "yyyy-MM-dd--HH:mm:ss";

	public static int archiveIfNeeded() {
		if (shouldArchive()) {
			Console.print("Running Server World Archive.");
			Console.print("This will create lag for a short while");
			int time = archive();
			Console.print("Completed archival process in " + time + "ms");
		}
		return -1;
	}

	public static boolean shouldArchive() {
		// If last know archive data is unknown, archive
		if (createIfMissing()) return true;

		// Get last archive message
		BufferedReader br = null;
		String lastLine = "";
		int linesRead = 0;
		try {
			String sCurrentLine;

			br = new BufferedReader(new FileReader(EXPORT_LOG));

			while ((sCurrentLine = br.readLine()) != null) {
				lastLine = sCurrentLine;
				linesRead++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
		}

		// Last Line is unreadable
		if (linesRead == 0) return true;

		try {
			// Parse date
			Date last = new SimpleDateFormat(DATE_FORMAT).parse(lastLine);
			Date now = new Date();

			int daysSinceLast = (int) TimeUnit.MILLISECONDS.toDays(now.getTime() - last.getTime());

			Log.print(daysSinceLast + " days since last backup!");
			if (daysSinceLast > ConfigManager.world_backups_frequency_in_days) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public static int archive() {
		long start = System.currentTimeMillis();

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		LocalDateTime now = LocalDateTime.now();
		String filename = "world-"+dtf.format(now)+".zip";

		try {
			// Create zip
			FileUtils.mkdirIfMissing("enderbotbackups");
			FileUtils.clearDirectory("enderbotbackups");
			FileUtils.zip("world", "enderbotbackups/"+filename);

			// Append to archive log
			Writer output = new BufferedWriter(new FileWriter(EXPORT_LOG, true));
			output.append("\n"+dtf.format(now));
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Upload it
		upload();

		int timeMS = (int) (System.currentTimeMillis()-start);
		return timeMS;
	}

	private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_FILE);
	private static final String CREDENTIALS_FILE_PATH = EXPORT_LOG_PATH+"credentials.json";
	private static final String TOKENS_DIRECTORY_PATH = EXPORT_LOG_PATH+"tokens";

	public static void upload() {
		try {
			// Get HTTP_TRANSPORT
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

			// Get credentials
			// Load client secrets.
			InputStream in = new FileInputStream(new java.io.File(CREDENTIALS_FILE_PATH));
			if (in == null) {
				throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
			}
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
					HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
					.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
					.setAccessType("offline")
					.build();
			LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

			Credential credentials = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

			// Get Service
			Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
					.setApplicationName(APPLICATION_NAME)
					.build();

			// Upload
			String filenameFull = FileUtils.getFirstZip("enderbotbackups");
			String filename = Paths.get(filenameFull).getFileName().toString();
			java.io.File filePath = new java.io.File(filenameFull);

			File fileMetadata = new File();
			fileMetadata.setName(filename);

			FileContent mediaContent = new FileContent("application/zip", filePath);
			File file = service.files().create(fileMetadata, mediaContent)
					.setFields("id")
					.execute();
			Log.print("Uploaded with File ID: " + file.getId());

			// Create Sharable link
			JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
				@Override
				public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
					// Handle error
					Log.printErr(e.getMessage());
				}

				@Override
				public void onSuccess(Permission permission, HttpHeaders responseHeaders) throws IOException {
				}
			};

			File fileb = service.files().get(file.getId()).setFields("webViewLink").execute();
			BatchRequest batch = service.batch();

			Permission permission = new Permission()
					.setType("anyone")
					.setRole("reader");
			service.permissions().create(file.getId(), permission).setFields("id").queue(batch, callback);

			batch.execute();

			String link = fileb.getWebViewLink();
			Log.print("Sharable Link :" + link);

			for (GuildContext context : Guilds.getContexts()) {
				context.getBackupsChannel().sendMessage("Zip: " + link);
			}

		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	// Returns true if new file was made
	public static boolean createIfMissing() {
		if (FileUtils.doesFileExist(EXPORT_LOG)) {
			return false;
		}
		Log.print("Archive Log file missing, recreating");

		try {
			java.io.File file = new java.io.File(EXPORT_LOG);
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
}
