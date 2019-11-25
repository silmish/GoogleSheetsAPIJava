/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package googletest.googletest;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.DeleteDimensionRequest;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author patrhenr
 */
public class SheetsAndJava {

    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "Google Sheets Example";
    private static String SPREADSHEET_ID = "1unsZDU8xOI8WzlafIW9i5NZmBeta4U19rcVJobJkvGU";

    private static Credential authorize() throws IOException, GeneralSecurityException, Exception {
        InputStream in = SheetsAndJava.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in)
        );

        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("user");

        return credential;
    }
    
    public static Sheets getSheetsService() throws IOException, GeneralSecurityException, Exception {
        Credential credential = authorize();
        
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    public static void main(String[] args) throws IOException, GeneralSecurityException, Exception {
        sheetsService = getSheetsService();
        String range = "A1:F8";
        
        
        // Read data
        /*ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        
        List<List<Object>> values = response.getValues();
        
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (List row : values) {
                System.out.printf("%s %s %s\n", row.get(5), row.get(4), row.get(1));
            }
        }*/
        
        //Insert data
        ValueRange appendBody = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("Kokeillaan", "Miten", "Tää", "toimii")
                ));
        
        AppendValuesResponse appendresult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, range, appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(Boolean.TRUE)
                .execute();
        
        //Update data
        /*ValueRange body = new ValueRange()
                 .setValues(Arrays.asList(
                        Arrays.asList("Päivitys")
                ));
        
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, "B16", body)
                .setValueInputOption("RAW")
                .execute();*/
        
        //Delete data
        /*DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
                .setRange(
                        new DimensionRange()
                        .setSheetId(0)
                        .setDimension("ROWS")
                        .setStartIndex(15)           
                );
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setDeleteDimension(deleteRequest));
        
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, body).execute();*/
    }
}
