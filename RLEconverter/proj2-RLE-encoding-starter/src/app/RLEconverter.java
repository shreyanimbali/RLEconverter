package app;

import java.util.Scanner;
import java.io.*;

public class RLEconverter {
   private final static int DEFAULT_LEN = 100; // used to create arrays.

   /*
    *  This method reads in an uncompressed ascii image file that contains 
    *  2 characters. It stores each line of the file in an array.
    *  It then calls compressAllLines to get an array that stores the compressed
    *  version of each uncompressed line from the file. The compressed array
    *  is then passed to the getCompressedFileStr method which returns a String
    *  of all compressed lines (the two charcaters are written in the first line)
    *  in CSV format. This String is written to a text file with the prefix "RLE_"
    *  added to the original, uncompressed file name.
    *  Note that dataSize keeps track of the number of lines in the file. The array 
    *  that holds the lines of the file is initialized to the DEFAULT_LEN, which 
    *  is assumed to be << the number of lines in the file.
    */   
  public void compressFile(String fileName) throws IOException{
    Scanner scan = new Scanner(new FileReader(fileName));
    String line = null;
    String[] decompressed = new String [DEFAULT_LEN];
    int dataSize = 0;
    while(scan.hasNext()){
      line = scan.next();
      if(line != null && line.length()>0)
        decompressed[dataSize]=line;
        dataSize++;
    }
    scan.close();
    char[] fileChars = discoverAsciiChars(decompressed, dataSize); 
    String[] compressed = compressAllLines(decompressed, dataSize, fileChars);
    writeFile(getCompressedFileStr(compressed, fileChars), "RLE_"+fileName);
  }
  
   
/*
 * This method implements the RLE compression algorithm. It takes a line of uncompressed data
 * from an ascii file and returns the RLE encoding of that line in CSV format.
 * The two characters that make up the image file are passed in as a char array, where
 * the first cell contains the first character that occurred in the file.
*/
public String compressLine(String line, char[] fileChars){
   //TODO: Implement this method
   String ans = "";
   if(line.charAt(0) == fileChars[0]){
     int len = line.length();
     int count = 0;
     for(int i = 0 ; i < (len-1); i++){
       char current = line.charAt(i);
       if(line.charAt(i+1) == current){
         count+=1;
         if ( i == (len-2)){
           count += 1;
           String s1 = Integer.toString(count);
           ans = ans + s1;
           count = 0;
         }
       }
       else{
           String s = Integer.toString(count+1);
           ans = ans + s + "," ;
           count = 0;
           if ( i == (len-2)){
               count += 1;
               ans = ans + "1";
               count = 0;
       }
     }
 }
 }
 else{
  ans = ans + Integer.toString(0)+ ",";
  int start = line.indexOf(fileChars[0]);
  if (start != -1){
    int len = line.length();
      ans = ans + Integer.toString(start)+",";
        int count = 0;
        for(int i = start ; i < (len-1); i++){
          char current = line.charAt(i);
          if(line.charAt(i+1) == current){
            count+=1;
            if ( i == (len-2)){
              count += 1;
              String s1 = Integer.toString(count);
              ans = ans + s1;
              count = 0;
            }
          }
          else{
              String s = Integer.toString(count+1);
              ans = ans + s + "," ;
              count = 0;
              if ( i == (len-2)){
                  count += 1;
                  ans = ans + "1";
                  count = 0;
          }
        }
    }  
  }
  else{
      ans = ans + Integer.toString(line.length());
  }
} 
return ans;
}

  /*
   *  This method discovers the two ascii characters that make up the image. 
   *  It iterates through all of the lines and writes each compressed line
   *  to a String array which is returned. The method compressLine is called on 
   *  each line.
   *  The dataSize is the number of lines in the file, which is likely to be << the length of lines.
   */
  public String[] compressAllLines(String[] lines, int dataSize, char[] fileChars){
      //TODO: Implement this method
      String[] ans = new String[dataSize];
      for(int i = 0 ; i < lines.length ; i++){
        ans[i] = compressLine(lines[i] , fileChars);
      }
      return ans ;
}

/*
 *  This method assembles the lines of compressed data for
 *  writing to a file. The first line must be the 2 ascii characters
 *  in comma-separated format. 
 */
public String getCompressedFileStr(String[] compressed, char[] fileChars) {
    //TODO: Implement this method
      String ans = "";
      ans = ans + fileChars[0] + "," + fileChars[1] + "\n";
      for (String element : compressed){
        ans += element + "\n";
      }
      return ans;
}
   /*
    *  This method reads in an RLE compressed ascii image file that contains 
    *  2 characters. It stores each line of the file in an array.
    *  It then calls decompressAllLines to get an array that stores the decompressed
    *  version of each compressed line from the file. The first row contains the two 
    *  ascii charcaters used in the original image file. The decompressed array
    *  is then passed to the getDecompressedFileStr method which returns a String
    *  of all decompressed lines, thus restoring the original, uncompressed image.
    *  This String is written to a text file with the prefix "DECOMP_"
    *  added to the original, compressed file name.
    *  Note that dataSize keeps track of the number of lines in the file. The array 
    *  that holds the lines of the file is initialized to the DEFAULT_LEN, which 
    *  is assumed to be << the number of lines in the file.
    */   
  public void decompressFile(String fileName) throws IOException{
    Scanner scan = new Scanner(new FileReader(fileName));
    String line = null;
    String[] compressed = new String [DEFAULT_LEN];
    int dataSize =0;
    while(scan.hasNext()){
      line = scan.next();
      if(line != null && line.length()>0){
        compressed[dataSize] = line;
        dataSize++;
      }
    }
    scan.close();
    String[] decompressed = decompressAllLines(compressed, dataSize);
    writeFile(getDecompressedFileStr(decompressed), "DECOMP_"+fileName);
  }
 
   /*
   * This method decodes lines that were encoded by the RLE compression algorithm. 
   * It takes a line of compressed data and returns the decompressed, or original version
   * of that line. The two characters that make up the image file are passed in as a char array, 
   * where the first cell contains the first character that occurred in the file.
   */
   public String decompressLine(String line, char[] fileChars){
      //TODO: Implement this method
      String ans = "";
      String[] arrOfStr = line.split(",");
      if (arrOfStr[0] != "0"){
        for (int i = 0 ; i < arrOfStr.length ; i++){
            if (i%2 == 0){
                for(int x = 1; x <= Integer.parseInt(arrOfStr[i]); x++){
                  ans += fileChars[0];  
                }
            }
            else{
                for(int x = 1; x <= Integer.parseInt(arrOfStr[i]); x++){
                  ans += fileChars[1];  
                }
        }
       } 
      }
      else{
        for (int i = 1 ; i < arrOfStr.length ; i++){
            if (i%2 == 0){
                for(int x = 1; x <= Integer.parseInt(arrOfStr[i]); x++){
                  ans += fileChars[1];  
                }
            }
            else{
                for(int x = 1; x <= Integer.parseInt(arrOfStr[i]); x++){
                  ans += fileChars[0];  
                }
        }
       }  
      }
      
      return ans;
   }
    /*
   *  This method iterates through all of the compressed lines and writes 
   *  each decompressed line to a String array which is returned. 
   *  The method decompressLine is called on each line. The first line in
   *  the compressed array passed in are the 2 ascii characters used to make
   *  up the image. 
   *  The dataSize is the number of lines in the file, which is likely to be << the length of lines.
   *  The array returned contains only the decompressed lines to be written to the decompressed file.
   */
  public String[] decompressAllLines(String[] lines, int dataSize){
     //TODO: Implement this method
      String[] ans = new String[dataSize];
      char[] fileChars =  new char[2];
      fileChars[0] = lines[0].charAt(0);
      fileChars[1] = lines[0].charAt(2);
      for (int i = 1; i < dataSize; i++){
        ans[i] = decompressLine(lines[i], fileChars);
      }
      return ans;
  }
  
  /*
   *  This method assembles the lines of decompressed data for
   *  writing to a file. 
   */
  public String getDecompressedFileStr(String[] decompressed){
   //TODO: Implement this method
      String ans = "";
      for(int i = 0 ; i < decompressed.length ; i++){
        ans = ans + decompressed[i] + "\n";
      }
      return ans;
  }

  // assume the file contains only 2 different ascii characters.
  public char[] discoverAsciiChars(String[] decompressed, int dataSize){
//TODO: Implement this method
    String temp = "";
    for(String element : decompressed){
        for(int i = 0; i < element.length(); i++){
            char check = element.charAt(i);
            if(!(temp.contains(String.valueOf(check)))){
                temp+=element.charAt(i);
        }
        }
    }
    char[] fileChars =  new char[2];
    fileChars[0] = temp.charAt(0);
    fileChars[1] = temp.charAt(1);
    return fileChars;
  }
 
   public void writeFile(String data, String fileName) throws IOException{
		PrintWriter pw = new PrintWriter(fileName);
      pw.print(data);
      pw.close();
   }
}