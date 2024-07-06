# epub-repair-tool
This tool can repair ePub files that contain entries with a corrupted filename in Central Directory File Header (CDFH) and a correct filename in the Local File Header (LFH).
Usually the filename field in both these headers (CDFH and LFH) should be equal, but sometimes it happens that for few entries of the ePub file the filename in the CDFH is corrupted and differs from the filename contained in the LFH.
This tool simply copies the filename field of LFH in the filename field of CDFH where they are not consistent. The tool always assumes the LFH is correct.

# Typical usage
When a DRM protect ePub contains invalid filename field in one or more entries the decrypting tool INEPT gives error when it tries to decode the string of the filename.
![alt text](https://github.com/marcocod/epub-repair-tool/blob/master/img/inept.png?raw=true)
If you open the ePub file with a archive manager program as 7zip you can see that some filename contains invalid characters.
![alt text](https://github.com/marcocod/epub-repair-tool/blob/master/img/7zip.png?raw=true)
Note that since the filename contains the entire relative path, archive manager programs can show the corrupted entry in the wrong subdirectory.
This tool analyzes a folder with ePub files and allows to fix the corrupeted entries.
![alt text](https://github.com/marcocod/epub-repair-tool/blob/master/img/jar.png?raw=true)

# How to build
The repository contains:
- the java source code in the src folder
- the NetBeans project files in the nbproject folder
The code can be built with NetBeans version 8.2 or above opening the repository folder as project folder.

# How to launch
The release is a Java Archive (JAR) that can be executed with a Java Runtime Environment (JRE) version 8 or above with the following command:

java -jar epub-repair-tool.jar
